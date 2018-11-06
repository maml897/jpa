package uitls;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LambdaUtils
{
	// list 转 map，指定一个属性当key，value默认U
	public static <T, U> Map<T, U> list2map(List<U> list, Function<U, T> key)
	{
		return list2map(list, key, x -> x);
	}

	// list 转 map，指定一个属性当key，执行一个属性当value
	public static <U, T, K> Map<T, K> list2map(List<U> list, Function<U, T> key, Function<U, K> value)
	{
		return list.stream().collect(Collectors.toMap(key, value, (key1, key2) -> key2, LinkedHashMap::new));
	}

	// list抽取属性
	public static <U, T> List<T> list2list(Collection<U> list, Function<U, T> fun)
	{
		return list.stream().map(fun).collect(Collectors.toList());
	}

	// 过滤
	public static <U> List<U> filter(Collection<U> list, Predicate<U> keyExtractor)
	{
		return list.stream().filter(keyExtractor).collect(Collectors.toList());
	}

	// groupby
	public static <T, U> Map<U, List<T>> groupby(List<T> list, Function<T, U> keyExtractor)
	{
		return list.stream().collect(Collectors.groupingBy(keyExtractor, LinkedHashMap::new, Collectors.toList()));
	}

	public static <T, U, K> Map<U, K> groupby(Collection<T> list, Function<T, U> groupExtractor, Collector<T, ?, K> c)
	{
		return list.stream().collect(Collectors.groupingBy(groupExtractor, LinkedHashMap::new,c));
	}
	
	public static <T, U, K> Map<U, Map<K, T>> groupby(List<T> list, Function<T, U> groupExtractor, Function<T, K> keyExtractor)
	{
		return list.stream().collect(Collectors.groupingBy(groupExtractor, LinkedHashMap::new, Collectors.toMap(keyExtractor, x -> x, (key1, key2) -> key2, LinkedHashMap::new)));
	}

	public static <T, U, K> Map<U, Map<K, List<T>>> groupby2(List<T> list, Function<T, U> groupExtractor, Function<T, K> keyExtractor)
	{
		return list.stream().collect(Collectors.groupingBy(groupExtractor, LinkedHashMap::new, Collectors.groupingBy(keyExtractor, LinkedHashMap::new, Collectors.toList())));
	}
	
	public static <T> Map<Boolean, List<T>> groupbyboolean(List<T> list, Predicate<T> keyExtractor)
	{
		return list.stream().collect(Collectors.partitioningBy(keyExtractor));
	}
	
	//计算平均分
	public static double average(List<Double> list) {
		return list.stream().mapToDouble(x -> x).average().orElse(0);
	}
	
	//最大值
	
	//最小值
	
	//count
	
	//sum
}