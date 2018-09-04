package uitls;

import java.util.ArrayList;
import java.util.Arrays;
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
	 * @param steps 分数段
	 * @param fullscore 满分
	 * @param objects 待计算实体
	 * @param scoreExtractor 分数属性
	 * @param countExtractor 数量属性
	 * @return
	 */
	public static <T> Map<Double,?> computeSegments(List<Double> steps, List<T> objects, Function<T, Double> scoreExtractor, ToIntFunction<T> countExtractor,boolean... withSums) {
		
		Map<Double,Integer> map=LambdaUtils.groupby(objects, x->Utils.key(steps, scoreExtractor.apply(x),false,false), Collectors.summingInt(countExtractor));
		Map<Double, Integer> result =new LinkedHashMap<>();
		for(double key:steps){
			if(map.containsKey(key)){
				result.put(key,map.get(key));
			}
			else{
				result.put(key, 0);
			}
		}
		
		if(withSums!=null && withSums.length>0 && withSums[0]){
			Map<Double, List<Integer>> resultWithSum =new LinkedHashMap<>();
			int sum = 0;//累计
			for (double key : result.keySet())
			{
				int number=result.get(key);
				sum += number;
				resultWithSum.put(key, Arrays.asList(number,sum));
			}
			return resultWithSum;
		}
		return result;
	}
	
	/**
	 * 获取分段
	 * @param up 上限
	 * @param down 下限
	 * @param step 步长
	 * @param fullscore 满分
	 * @param toup 从低到高，还是从高到低
	 * @return
	 */
	public static List<Double> getSegments(double down,double up, int step, double fullscore,boolean... toups) {
		boolean toup=false;
		if(toups!=null &&toups.length>0&&toups[0]){
			toup=true;
		}
		
		List<Double> list =Arrays.asList(down);
		if(toup){
			while(down<=up){
				list.add(down);
				down=down+step;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		//System.out.println(getSegments(down, up, step, fullscore, toups));
	}
}
