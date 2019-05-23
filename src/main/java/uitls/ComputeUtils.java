package uitls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ComputeUtils
{

	/**
	 * 计算分数的排名，数量，sum（例如 130对应的是 (130,满分]的人数）
	 * @param scores
	 * @param function
	 * @return
	 */
	public static <T> List<Map<String, Object>> computeScore(List<T> scores, Function<T, Double> function)
	{
		scores.sort(Comparator.comparingDouble(x -> function.apply(x)));// 需要一个排序
		Map<Double, Long> group = LambdaUtils.groupby(scores, function, Collectors.counting());
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		int lastOrder = 1;
		int lastCount = 0;
		int lastSum = 0;

		for (double score : group.keySet())
		{

			Map<String, Object> map = new HashMap<>();

			int order = lastOrder + lastCount;//
			int sum = lastCount + lastSum;//
			int count = Integer.parseInt(String.valueOf(group.get(score)));

			map.put("score", score);
			map.put("count", count);
			map.put("order", order);
			map.put("sum", sum);
			result.add(map);

			lastOrder = order;
			lastCount = count;
			lastSum = sum;
		}

		return result;
	}

	/**
	 * @param studentIDScore
	 *            学生ID，分数
	 * @return 分数-人s
	 */
	public static Map<Double, List<Long>> scoreStudents(Map<Long, Double> studentIDScore)
	{
		//这种方法方便但是慢
		//Map<Double, List<Long>> map=LambdaUtils.groupby(studentIDScore.entrySet(), x->x.getValue(),Collectors.mapping(x->x.getKey(), Collectors.toList()));
		
		//下面的这种速度要快很多
		Map<Double, List<Long>> result = new LinkedHashMap<>();
		for (Map.Entry<Long, Double> entry : studentIDScore.entrySet())
		{
			List<Long> list = result.get(entry.getValue());
			if (list == null)
			{
				list = new ArrayList<>();
				result.put(entry.getValue(), list);
			}
			list.add(entry.getKey());
		}
		return result;
	}

	/**
	 * 同下，参数只不过是map，获取每个分段的人数
	 * @param segments
	 * @param studentIDScore
	 * @return 分数段-人s
	 */
	public static Map<Double, List<Long>> segmentStudents(List<Double> segments, Map<Long, Double> studentIDScore)
	{
		return segmentStudents(segments, studentIDScore.entrySet(), x->x.getValue(), x->x.getKey());
	}

	/**
	 * 获取每个分数段有多少人
	 * @param segments
	 *            分数段，从低到高排序
	 * @param objects
	 *            要操作的对象
	 * @param scoreExtractor
	 *            操作对象的分数
	 * @param scoreExtractor
	 *            resultMapper 结果收集器
	 * @return 分数段-人s
	 */
	public static <T, U> Map<Double, List<U>> segmentStudents(List<Double> segments, Collection<T> objects, Function<T, Double> scoreExtractor, Function<T, U> resultMapper)
	{
		Map<Double, List<U>> map = Tool.groupBySegment(segments, objects, scoreExtractor, Collectors.mapping(resultMapper, Collectors.toList()), true);
		Map<Double, List<U>> result = new LinkedHashMap<>();
		segments.forEach(x -> {
			if (map.containsKey(x))
			{
				result.put(x, map.get(x));
			}
			else
			{
				result.put(x, new ArrayList<>());
			}
		});
		return result;
	}

	/**
	 * 同上，只是可以自定义返回
	 */
	public static <T, U> Map<Double, U> segment(List<Double> segments, List<T> objects, Function<T, Double> scoreExtractor, Collector<T, ?, U> collector)
	{
		Map<Double, U> map = Tool.groupBySegment(segments, objects, scoreExtractor, collector, false);
		return map;
	}

	/**
	 * 计算分数段，可以是学生列表也可以是score列表
	 * @param top
	 * @param bottom
	 * @param step
	 * @param objects
	 * @param scoreExtractor
	 * @param collector
	 *            ,如果是学生就是Collectors.counting()，如果是score表，就是Collectors.summingInt(Score::getCount)
	 * @param withSums
	 * @return
	 */
	public static <T> List<Map<String, Object>> computeSegments(double bottom, double top, int step, List<T> objects, Function<T, Double> scoreExtractor, Collector<T, ?, Long> collector,
			boolean... withSums)
	{
		List<Double> temp = Tool.getSegments(top, bottom, step);
		List<Double> segments = new ArrayList<>(temp);
		if (!segments.contains(0d))
		{
			segments.add(0d);
		}

		Map<Double, Long> map = LambdaUtils.groupby(objects, x -> Tool.key(segments, scoreExtractor.apply(x), false, false), collector);
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < segments.size(); i++)
		{
			double key = segments.get(i);

			Integer count = 0;
			Object o = map.get(key);
			if (o != null)
			{
				count = Integer.parseInt(o.toString());
			}

			Map<String, Object> onemap = new HashMap<>();
			onemap.put("id", key);
			onemap.put("count", count);

			if (i == 0)
			{
				onemap.put("desc", Tool.formatDouble(key) + "及以上");
			}
			else
			{
				onemap.put("desc", "[" + Tool.formatDouble(key) + "," + Tool.formatDouble(segments.get(i - 1)) + ")");
			}
			list.add(onemap);
		}

		// 累计
		if (withSums != null && withSums.length > 0 && withSums[0])
		{
			int sum = 0;
			for (Map<String, Object> onemap : list)
			{
				int number = (int) onemap.get("count");
				sum += number;
				onemap.put("sum", sum);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<Map<String, Object>> difficulty(List<T> nsrQuestions, Function<T, Double> difficultyfun, ToDoubleFunction<T> scorefun, ToIntFunction<T> typefun)
	{
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("name", "难题");
		map1.put("info", "p≤0.4");

		Predicate<T> predicate1 = x -> difficultyfun.apply(x) <= 0.4;
		map1.put("predicate", predicate1);

		Map<String, Object> map2 = new HashMap<>();
		map2.put("name", "中等题");
		map2.put("info", "0.7＞p＞0.4");
		Predicate<T> predicate2 = x -> difficultyfun.apply(x) > 0.4 && difficultyfun.apply(x) < 0.7;
		map2.put("predicate", predicate2);

		Map<String, Object> map3 = new HashMap<>();
		map3.put("name", "简单题");
		map3.put("info", "p≥0.7");
		Predicate<T> predicate3 = x -> difficultyfun.apply(x) >= 0.7;
		map3.put("predicate", predicate3);

		list.add(map1);
		list.add(map2);
		list.add(map3);

		for (Map<String, Object> map : list)
		{
			@SuppressWarnings("rawtypes")
			List<T> questions = LambdaUtils.filter(nsrQuestions, (Predicate) map.get("predicate"));
			if (questions == null)
			{
				questions = new ArrayList<>();
			}
			map.put("score", questions.stream().mapToDouble(scorefun).sum());
			map.put("list", questions);
			// map.put("map", LambdaUtils.groupby(questions, x -> x.getType() == BaseConstants.QUESTION_TYPE_OBJECTIVE));
			map.put("map", LambdaUtils.groupbyboolean(questions, x -> typefun.applyAsInt(x) == 1));
		}
		return list;
	}

	// 小题区分度分析
	@SuppressWarnings("unchecked")
	public static <T> List<Map<String, Object>> discrimination(List<T> nsrQuestions, Function<T, Double> fun, ToDoubleFunction<T> scorefun, ToIntFunction<T> typefun)
	{
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("name", "优秀");
		map1.put("info", "D≥0.4");

		Predicate<T> predicate1 = x -> fun.apply(x) >= 0.4;
		map1.put("predicate", predicate1);

		Map<String, Object> map2 = new HashMap<>();
		map2.put("name", "良好");
		map2.put("info", "0.3≤D＜0.4");
		Predicate<T> predicate2 = x -> fun.apply(x) >= 0.3 && fun.apply(x) < 0.4;
		map2.put("predicate", predicate2);

		Map<String, Object> map3 = new HashMap<>();
		map3.put("name", "一般");
		map3.put("info", "0.2≤D＜0.3");
		Predicate<T> predicate3 = x -> fun.apply(x) >= 0.2 && fun.apply(x) < 0.3;
		map3.put("predicate", predicate3);

		Map<String, Object> map4 = new HashMap<>();
		map4.put("name", "较低");
		map4.put("info", "D＜0.2");
		Predicate<T> predicate4 = x -> fun.apply(x) < 0.2;
		map4.put("predicate", predicate4);

		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);

		for (Map<String, Object> map : list)
		{
			@SuppressWarnings("rawtypes")
			List<T> questions = LambdaUtils.filter(nsrQuestions, (Predicate) map.get("predicate"));
			if (questions == null)
			{
				questions = new ArrayList<>();
			}
			map.put("score", questions.stream().mapToDouble(scorefun).sum());
			map.put("list", questions);
			map.put("map", LambdaUtils.groupbyboolean(questions, x -> typefun.applyAsInt(x) == 1));
		}
		return list;
	}

	/**
	 * 同下，计算难度指数，只是返回方式不同
	 * @param scoreUsers,要求覆盖所有段，没有的可以是对应new ArrayList，但不能是null
	 * @param userScore
	 * @param fullScore
	 * @return 分数段-难度
	 */
	public static Map<Double, Double> difficultyIndex(Map<Double, List<Long>> scoreUsers, Map<Long, Double> userScore, double fullScore)
	{
		Map<Double, Double> result = new LinkedHashMap<>();
		for (double score : scoreUsers.keySet())
		{
			if (fullScore != 0)
			{
				result.put(score, 0d);
				continue;
			}

			double value = 0d;
			List<Long> studentIDs = scoreUsers.get(score);
			if (studentIDs.size() > 0)
			{
				double average = studentIDs.stream().mapToDouble(x -> {
					Double d = userScore.get(x);
					if (d == null)
					{
						return 0;
					}
					return d;
				}).average().orElse(0d);
				value = average / fullScore;
			}
			result.put(score, value);
		}
		return result;
	}

	/**
	 * 计算科目和小题的难度指数
	 * @param segments
	 *            分数段->人数IDs （总分或者科目的），这个map的规则是，小题0分开始，5分一段，即使没有该段没有人，也要是空列表；科目0分 10分，之后20分递增，某段没有也要存在空列表
	 * @param scoreUsers
	 *            用户ID-分数（科目或者小题）
	 * @param fullScore
	 *            满分
	 * @return 使用方法，一般先用上面的分段方法把总分分段，之后再计算各个科目的：要确认要总分的人员范围，单科的人员范围
	 */
	public static <T> List<Map<String, Object>> computeDifficulty(Map<Double, List<Long>> scoreUsers, Map<Long, Double> userScore, double fullScore)
	{
		List<Map<String, Object>> result = new ArrayList<>();
		for (double score : scoreUsers.keySet())
		{
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("Score", score);
			if (fullScore == 0)
			{
				map.put("DifficultyIndex", 0);
			}
			else
			{
				List<Long> studentIDs = scoreUsers.get(score);
				if (studentIDs.size() > 0)
				{
					double average = studentIDs.stream().mapToDouble(x -> {
						Double d = userScore.get(x);
						if (d == null)
						{
							return 0;
						}
						return d;
					}).average().orElse(0d);
					map.put("DifficultyIndex", average / fullScore);
				}
				else
				{
					map.put("DifficultyIndex", 0d);
				}
			}
			result.add(map);
		}
		return result;
	}

	/**
	 * 计算排名
	 * @return 值（定义）-排名
	 */
	public static <T, U> Map<U, Long> order(Collection<T> list, Function<T, Double> valueFun, Function<T, U> keyFun)
	{
		List<Double> values = LambdaUtils.list2list(list, valueFun);
		Map<Double, Long> order = order(values);
		Map<U, Long> result = new HashMap<>();
		for (T t : list)
		{
			result.put(keyFun.apply(t), order.get(valueFun.apply(t)));
		}
		return result;
	}

	/***
	 * 计算排名
	 * @param values
	 * @return 分数 - 排名
	 */
	public static Map<Double, Long> order(Collection<Double> values)
	{
		Map<Double, Long> map = LambdaUtils.groupby(values, x -> x, Collectors.counting());

		// map 排序
		map = map.entrySet().stream().sorted(Entry.<Double, Long> comparingByKey().reversed()).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue(), (key1, key2) -> key2, LinkedHashMap::new));
		long order = 1;
		Map<Double, Long> retult = new LinkedHashMap<>();
		for (Double value : map.keySet())
		{
			retult.put(value, order);
			order = order + map.get(value);
		}
		return retult;
	}

	/**
	 * 来自云阅卷：计算排名
	 * @param studentIDScores
	 * @return
	 */
	public static Map<Long, Integer> computeOrder(Map<Long, Double> studentIDScores)
	{
		Map<Double, Integer> scores = new HashMap<>();
		studentIDScores.forEach((key, value) -> {
			Integer count = scores.get(value);
			if (count == null)
				count = 1;
			else
				count = count + 1;
			scores.put(value, count);
		});

		Map<Long, Integer> studentIDOrder = new HashMap<>();

		if (studentIDScores.size() == 0)
			return studentIDOrder;

		studentIDScores.forEach((key, value) -> {
			int sum = 0;
			try
			{
				sum = scores.entrySet().stream().filter(map -> map.getKey() > value).mapToInt(map -> map.getValue()).sum();
			}
			catch (Exception e)
			{
			}

			studentIDOrder.put(key, sum + 1);
		});

		return studentIDOrder;
	}

	public static void main(String[] args)
	{
		List<Double> list = new ArrayList<>();
		Map<Long, Float> map = new HashMap<>();
		for (long i = 0; i < 50000; i++)
		{
			int j = new Random().nextInt(750);
			map.put(i, (float) j);
			list.add((double) j);
		}
		long s = System.currentTimeMillis();
		// Map<Long, Integer> aa = computeOrder(map);
		// System.out.println(aa);
		// System.out.println(System.currentTimeMillis() - s);
		// s = System.currentTimeMillis();
		Map<Double, Long> a = order(list);
		System.out.println(a);
		System.out.println(System.currentTimeMillis() - s);

	}
}
