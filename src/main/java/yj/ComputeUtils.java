package yj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import uitls.LambdaUtils;
import uitls.Tool;


public class ComputeUtils
{

	/**
	 * 计算分数的排名，数量，sum（例如 130对应的是 (130,满分]的人数）
	 * @param objects
	 * @param scorefun
	 * @return
	 */
	@SafeVarargs
	public static <T> List<Map<String, Object>> computeScore(List<T> objects, Function<T, Double> scorefun, Collector<T, ?, Long>... cs)
	{
		Collector<T, ?, Long> c = Collectors.counting();
		if (cs != null && cs.length > 0)
		{
			c = cs[0];
		}

		Map<Double, Long> group = LambdaUtils.groupby(objects, scorefun, c);
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

			map.put("Score", score);
			map.put("COrder", order);
			map.put("CCount", count);
			map.put("CSum", sum);
			result.add(map);

			lastOrder = order;
			lastCount = count;
			lastSum = sum;
		}

		return result;
	}

	
	/**
	 * 获取分段
	 * @param from
	 *            开始值
	 * @param to
	 *            结束值
	 * @param step
	 *            步长
	 * @param addZero
	 *            是否加0，默认不加
	 * @return
	 */
	public static List<Double> getSegments(double from, double to, int step, boolean... addZeros)
	{

		boolean addzreo = false;
		if (addZeros != null && addZeros.length > 0)
		{
			addzreo = addZeros[0];
		}

		if (from == to)
		{
			if (addzreo && from != 0)
			{
				return Arrays.asList(from, 0d);
			}
			return Arrays.asList(from);
		}

		List<Double> list = new ArrayList<>();
		if (from > to)
		{
			while (from > to)
			{
				list.add(from);
				from = from - step;
			}
			if (!list.contains(to))
			{
				list.add(to);
			}

			if (addzreo && !list.contains(0d))
			{
				list.add(0d);
			}

		}
		else
		{
			while (from < to)
			{
				list.add(from);
				from = from + step;
			}
			if (!list.contains(to))
			{
				list.add(to);
			}

			if (addzreo && !list.contains(0d))
			{
				list.add(0, 0d);
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

	

	/**
	 * 计算难度
	 * @param segments
	 *            分段人员表
	 * @param objects
	 *            待计算的对象
	 * @param studentIDfunction
	 *            连接桥，studentID
	 * @param scorefunction
	 *            分数
	 * @param fullScore
	 *            满分
	 * @return
	 */
	public static <T> List<Map<String, Object>> computeDifficulty(Map<Double, List<Long>> segments, List<T> objects, Function<T, Long> studentIDfunction, ToDoubleFunction<T> scorefunction,
			double fullScore)
	{

		Map<Long, T> mapt = LambdaUtils.list2map(objects, studentIDfunction);

		List<Map<String, Object>> result = new ArrayList<>();
		for (double score : segments.keySet())
		{
			Map<String, Object> map = new LinkedHashMap<>();
			List<Long> studentIDs = segments.get(score);
			List<Long> newstudentIDs = new ArrayList<>();

			if (studentIDs.size() > 0)
			{
				for (long studentID : studentIDs)
				{
					T t = mapt.get(studentID);
					if (t != null)
					{
						newstudentIDs.add(studentID);
					}
				}
			}

			map.put("Score", score);
			if (newstudentIDs.size() > 0)
			{
				double average = newstudentIDs.stream().mapToDouble(x -> {
					return scorefunction.applyAsDouble(mapt.get(x));
				}).average().orElse(0d);
				map.put("DifficultyIndex", average / fullScore);
			}
			else
			{
				map.put("DifficultyIndex", 0d);
			}
			result.add(map);
		}
		return result;
	}

	

	/**
	 * 向上靠 返回一个分数对应的学生ID列表
	 * @param scores
	 *            对象列表
	 * @param scorefunction
	 *            分数
	 * @param studentIDfunction
	 *            学生ID列表
	 * @param segments
	 * @return 分数-学生IDs
	 */
	public static <T> Map<Double, List<Long>> getSegments4ComputeDifficulty(List<T> scores, Function<T, Double> scorefunction, Function<T, Long> studentIDExtractor, double full, long seID,boolean... subjects)
	{
		boolean subject = false;
		if (subjects != null && subjects.length > 0)
		{
			subject = subjects[0];
		}

		List<Double> segments = new ArrayList<>();
		if (subject)
		{
			// from jy seOff.setSeDifficulty
			segments.add(0d);
			for (double i = 10; i <= full; i += 20)
			{
				segments.add(i);
			}
		}
		else
		{
			// from jy SeOffQuestionDifficulty.getMapSegmentStudentId
			segments.clear();
			segments.addAll(getSegments(full, 0, 5));
			Collections.reverse(segments);
		}

		if(seID==9){
			List<Double> segments1=segments;
//			System.out.println(segments1);
		}
		
		
		Map<Double, List<Long>> map = LambdaUtils.groupby(scores, x -> {
			double d = Tool.key(segments, scorefunction.apply(x));
			return d;
		}, Collectors.mapping(studentIDExtractor, Collectors.toList()));

		Map<Double, List<Long>> result = new LinkedHashMap<>();
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
		
//		System.out.println(result.get(30d));
		return result;
	}
}
