package uitls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
		scores.sort(Comparator.comparingDouble(x->function.apply(x)));//需要一个排序
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
	 * 计算分数段，可以是学生列表也可以是score列表
	 * @param top
	 * @param bottom
	 * @param step
	 * @param objects
	 * @param scoreExtractor
	 * @param c
	 * @param withSums
	 * @return
	 */
	public static <T, U> List<Map<String, Object>> computeSegments(double top, double bottom, int step, List<T> objects, Function<T, Double> scoreExtractor, Collector<T, ?, ?> c, boolean... withSums)
	{
		List<Double> temp = getSegments(top, bottom, step);
		List<Double> segments = new ArrayList<>(temp);
		if (!segments.contains(0d))
		{
			segments.add(0d);
		}
		Map<Double, ?> map = LambdaUtils.groupby(objects, x -> Tool.key(segments, scoreExtractor.apply(x), false, false), c);
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
	public static List<Double> getSegments(double from, double to, int step)
	{

		if (from == to)
		{
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
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<Map<String, Object>> difficulty(List<T> nsrQuestions,Function<T,Double> difficultyfun,ToDoubleFunction<T> scorefun,ToIntFunction<T> typefun)
	{
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("name", "难题");
		map1.put("info", "p≤0.4");
		
		Predicate<T> predicate1 = x->difficultyfun.apply(x)<=0.4;
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
			//map.put("map", LambdaUtils.groupby(questions, x -> x.getType() == BaseConstants.QUESTION_TYPE_OBJECTIVE));
			map.put("map", LambdaUtils.groupbyboolean(questions, x -> typefun.applyAsInt(x) == 1));
		}
		return list;
	}
	
	// 小题区分度分析
	@SuppressWarnings("unchecked")
	public static <T> List<Map<String, Object>> discrimination(List<T> nsrQuestions,Function<T,Double> fun,ToDoubleFunction<T> scorefun,ToIntFunction<T> typefun)
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

}
