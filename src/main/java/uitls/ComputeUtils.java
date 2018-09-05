package uitls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class ComputeUtils {

	/**
	 * 计算分数的排名，数量，sum（例如 130对应的是 (130,满分]的人数）
	 * 
	 * @param scores
	 * @param function
	 * @return
	 */
	public static <T> List<Map<String, Object>> computeScore(List<T> scores, Function<T, Double> function) {
		Map<Double, Long> group = LambdaUtils.groupby(scores, function, Collectors.counting());
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		int lastOrder = 1;
		int lastCount = 0;
		int lastSum = 0;

		for (double score : group.keySet()) {

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
	 * 
	 * @param steps
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
	public static <T> Map<Double, ?> computeSegments(List<Double> steps, List<T> objects,
			Function<T, Double> scoreExtractor, ToIntFunction<T> countExtractor, boolean... withSums) {

		Map<Double, Integer> map = LambdaUtils.groupby(objects,
				x -> Utils.key(steps, scoreExtractor.apply(x), false, false), Collectors.summingInt(countExtractor));
		Map<Double, Integer> result = new LinkedHashMap<>();
		for (double key : steps) {
			if (map.containsKey(key)) {
				result.put(key, map.get(key));
			} else {
				result.put(key, 0);
			}
		}

		if (withSums != null && withSums.length > 0 && withSums[0]) {
			Map<Double, List<Integer>> resultWithSum = new LinkedHashMap<>();
			int sum = 0;// 累计
			for (double key : result.keySet()) {
				int number = result.get(key);
				sum += number;
				resultWithSum.put(key, Arrays.asList(number, sum));
			}
			return resultWithSum;
		}
		return result;
	}

	/**
	 * 获取分段
	 * 
	 * @param to
	 *            上限
	 * @param from
	 *            下限
	 * @param step
	 *            步长
	 * @param fullscore
	 *            满分
	 * @param descs 排序方式，默认是从高到低
	 *            从低到高，还是从高到低
	 * @return
	 */
	public static List<Double> getSegments(double from, double to, int step) {
		if(from==to){
			return Arrays.asList(from);
		}
		
		List<Double> list = new ArrayList<>();
		if (from>to) {
			while (from > to) {
				list.add(from);
				from = from - step;
			}
			if (!list.contains(to)) {
				list.add(to);
			}
		} 
		else 
		{
			while (from < to) {
				list.add(from);
				from = from + step;
			}
			if (!list.contains(to)) {
				list.add(to);
			}
		}

		return list;
	}

	/**
	 * 获取segement的名字，例如[10,30)等
	 * 
	 * @param segments
	 * @param fullscore
	 * descs 默认segments从高到低排序，如果是从低到高排序需要传false
	 * @return
	 */
	public static List<String> getSegmentNames(List<Double> segments, boolean... descs) {
		boolean desc = true;// 默认从高到底排序
		if (descs != null && descs.length > 0 && !descs[0]) {
			desc = false;
		}

		long size=segments.size();
		List<String> segmentNames = new ArrayList<>();
		
		if (desc) {
			double last=-1d;
			for (int i = 0; i < segments.size(); i++) {
				double value = segments.get(i);
				
				//第一个元素
				if (i == 0) {
					segmentNames.add(value + "及以上");
					last=value;
					continue;
				}
				segmentNames.add("["+value+","+last+")");
				
				//如果最后一个元素不等于0，还需要新增加一个
				if(i==size-1 && value!=0){
					segmentNames.add(value + "以下");
				}
				last=value;
			}
		}
		else{
			Collections.reverse(segments);
			segmentNames =getSegmentNames(segments);
			Collections.reverse(segmentNames);
		}
		return segmentNames;
	}

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		
		List<Double> list1=getSegments(0, 110, 30);
		System.out.println(list1);
		System.out.println(System.currentTimeMillis() - s);

		s = System.currentTimeMillis();
		System.out.println(getSegmentNames(list1,false));
		System.out.println(System.currentTimeMillis() - s);
		
		System.out.println("==================================================================================");
		s = System.currentTimeMillis();
		List<Double> list =getSegments(150, 10, 20);
		System.out.println(list);
		System.out.println(System.currentTimeMillis() - s);
		
		
		s = System.currentTimeMillis();
		System.out.println(getSegmentNames(list));
		System.out.println(System.currentTimeMillis() - s);
	}
}
