package uitls;

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
			map.put("order", order);
			map.put("count", count);
			map.put("sum", sum);
			result.add(map);

			lastOrder = order;
			lastCount = count;
			lastSum = sum;
		}

		return result;
	}

	/**
	 * 分数段人数统计 ** 此处要求step 从大到小排列**
	 * @param segments
	 *            分数段
	 * @param fullscore
	 *            满分
	 * @param objects
	 *            待计算实体
	 * @param scoreExtractor
	 *            分数属性
	 * @param countExtractor
	 *            数量属性
	 * @return
	 */
	public static <T> Map<Double, ?> computeSegments(double top,double bottom,int step,List<T> objects, Function<T, Double> scoreExtractor, ToIntFunction<T> countExtractor, boolean... withSums)
	{
		List<Double> segments = getSegments(top, bottom, step);
		if (!segments.contains(0d))
		{
			segments.add(0d);
		}
		
		Map<Double, Integer> map = LambdaUtils.groupby(objects, x -> Utils.key(segments, scoreExtractor.apply(x), false, false), Collectors.summingInt(countExtractor));
		Map<Double, Integer> result = new LinkedHashMap<>();
		for (double key : segments)
		{
			if (map.containsKey(key))
			{
				result.put(key, map.get(key));
			}
			else
			{
				result.put(key, 0);
			}
		}

		if (withSums != null && withSums.length > 0 && withSums[0])
		{
			Map<Double, List<Integer>> resultWithSum = new LinkedHashMap<>();
			int sum = 0;// 累计
			for (double key : result.keySet())
			{
				int number = result.get(key);
				sum += number;
				resultWithSum.put(key, Arrays.asList(number, sum));
			}
			return resultWithSum;
		}
		return result;
	}

	public static <T, U> List<Map<String, Object>> computeSegmentss(double top,double bottom,int step,List<T> objects, Function<T, Double> scoreExtractor, ToIntFunction<T> countExtractor, boolean... withSums)
	{
		List<Double> segments = getSegments(top, bottom, step);
		if (!segments.contains(0d))
		{
			segments.add(0d);
		}

		Map<Double, Integer> map = LambdaUtils.groupby(objects, x -> Utils.key(segments, scoreExtractor.apply(x), false, false), Collectors.summingInt(countExtractor));
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < segments.size(); i++)
		{
			double key = segments.get(i);
			Integer count = map.get(key);
			if (count == null)
			{
				count = 0;
			}

			Map<String, Object> onemap = new HashMap<>();
			onemap.put("id", key);
			onemap.put("count", count);

			if (i == 0)
			{
				onemap.put("desc", "[" + key + ",)");
			}
			else
			{
				onemap.put("desc", "[" + key + "," + segments.get(i - 1) + ")");
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

	/**
	 * 获取segement的名字，例如[10,30)等 如果只有一个元素，返回 以上，以及下 两个
	 * @param segments
	 * @descs 默认segments从高到低排序，如果是从低到高排序需要传false
	 * @return
	 */
	public static List<String> getSegmentNames(List<Double> segments, double down, boolean... descs)
	{
		// segments如果包含0，需要判断这个0是自动生成的，还是用户输入的

		boolean desc = true;// 默认从高到底排序
		if (descs != null && descs.length > 0 && !descs[0])
		{
			desc = false;
		}

		long size = segments.size();
		List<String> segmentNames = new ArrayList<>();

		if (desc)
		{
			double prev = -1d;
			for (int i = 0; i < segments.size(); i++)
			{
				double value = segments.get(i);

				// 第一个元素
				if (i == 0)
				{
					segmentNames.add(value + "及以上");
					prev = value;
					continue;
				}

				if (i != size - 1)
				{
					segmentNames.add("[" + value + "," + prev + ")");
					prev = value;
					continue;
				}

				// 最后一个元素
				if (i == size - 1)
				{
					if (value != 0)
					{
						segmentNames.add("[" + value + "," + prev + ")");
						segmentNames.add(value + "以下");
					}
					else
					{
						if (down == 0d)
						{
							segmentNames.add("[" + value + "," + prev + ")");
						}
						else
						{
							segmentNames.add(prev + "以下");
						}
					}
				}
			}
		}
		else
		{
			Collections.reverse(segments);
			segmentNames = getSegmentNames(segments, down);
			Collections.reverse(segmentNames);
		}
		return segmentNames;
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

}
