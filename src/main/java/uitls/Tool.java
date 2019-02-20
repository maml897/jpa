package uitls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tool
{

	/**
	 * 查找最接近目标值的数，并返回
	 * @param array
	 * @param targetNum
	 * @return
	 */
	public static Double binarysearchKey(List<Double> values, double targetNum)
	{
		// Arrays.sort(array);
		int targetindex = 0;
		int left = 0, right = 0;
		for (right = values.size() - 1; left != right;)
		{
			int midIndex = (right + left) / 2;
			int mid = (right - left);

			double midValue = values.get(midIndex);
			if (targetNum == midValue)
			{
				return midValue;
			}

			if (targetNum > midValue)
			{
				left = midIndex;
			}
			else
			{
				right = midIndex;
			}

			if (mid <= 2)
			{
				break;
			}
		}
		// System.out.println("和要查找的数：" + targetNum + "最接近的数：" + array[targetindex]);
		return (values.get(right) - values.get(left)) / 2 > targetNum ? values.get(right) : values.get(left);
	}

	/**
	 * 返回values 中距离value 最近的值，可以上取，也可以下取 取高：低于最低分的都算作最低分；取低：高于最高分的都算作最高分
	 * @param value：
	 * @param values：排好序的value，从小到大
	 * @return
	 */
	public static double key(Collection<Double> values, double value, boolean... flags)
	{

		// 1.从小到大
		// 2.从大到小
		// 3.取低
		// 4.取高

		boolean up = true;// 默认取高,也就是5.5 会认为是6
		boolean toup = true;// 默认列表排序从小到大，也就是 1，2，3

		if (flags != null)
		{
			if (flags.length > 0)
			{
				up = flags[0];
				if (flags.length > 1)
				{
					toup = flags[1];
				}
			}
		}
		if (toup)
		{// 从小到大

			// 取低
			if (!up)
			{
				double result = -1;
				for (Double f : values)
				{
					if (f <= value)
					{
						result = f;
					}
					else
					{
						break;
					}
				}
				return result;
			}

			// 取高
			for (Double f : values)
			{
				if (f >= value)
				{
					return f;
				}
			}
			return -1;
		}
		else // 从大到小
		{
			// 取低
			if (!up)
			{
				for (Double f : values)
				{
					if (f <= value)
					{
						return f;
					}
				}
				return -1;
			}
			else// 取高
			{
				double result = -1;
				for (Double f : values)
				{
					if (f >= value)
					{
						result = f;
					}
					else
					{
						break;
					}
				}
				return result;
			}
		}
	}

	/**
	 * 二分折半查找，较比上面的顺序查找速度快一点，但是由于待查数据少，所以效果不明显
	 * @param list
	 * @param findElem
	 * @return
	 */
	public static double binaryKey(List<Double> list, double findElem)
	{
		int size = list.size();

		int low = 0;
		int high = size - 1;
		int mid;
		while (low <= high)
		{
			mid = (low + high) / 2;
			double midValue = list.get(mid);
			if (findElem < midValue)
			{
				if (mid > 0 && findElem > list.get(mid - 1))
				{
					return list.get(mid);
				}
				high = mid - 1;
			}
			if (findElem > midValue)
			{
				if (mid + 1 < size && findElem < list.get(mid + 1))
				{
					return list.get(mid + 1);
				}

				low = mid + 1;
			}
			if (midValue == findElem)
			{
				return list.get(mid);
			}
		}
		return -1;
	}

	public static List<Double> keys(Collection<Double> values, double value)
	{
		if (values.contains(value))
		{
			return Arrays.asList(value, value);
		}

		List<Double> result = new ArrayList<>();
		result.add(values.stream().filter(x -> x < value).mapToDouble(x -> x).max().orElse(0d));
		result.add(values.stream().filter(x -> x > value).mapToDouble(x -> x).min().orElse(0d));

		if (result.size() == 1)
		{
			result.add(result.get(0));
		}
		return result;
	}

	public static String formatDouble(double f)
	{
		int i = (int) f;
		if (i == f)
		{
			return i + "";
		}
		else
		{
			return f + "";
		}
	}

	public static void main(String[] args)
	{
		List<Double> list = new ArrayList<>();
		for (double i = 10; i <= 750; i = i + 20)
		{
			list.add(i);
		}

		List<Double> list1 = new ArrayList<>();
		for (double i = 0; i < 50000; i++)
		{
			list1.add(750 * Math.random());
		}

		long s = System.currentTimeMillis();
		Map<Double, List<Double>> map = LambdaUtils.groupby(list1, x -> {
			Double r = Tool.key(list, x, false);
			return r;
		});
		System.out.println(System.currentTimeMillis() - s);

		Map<Double, List<Double>> result = new LinkedHashMap<>();
		for (double key : list)
		{
			if (map.containsKey(key))
			{
				result.put(key, map.get(key));
			}
			else
			{
				result.put(key, new ArrayList<>());
			}
		}

		// result.forEach((x,y)->{
		// System.out.println(x+"=="+y);
		// });

	}
}
