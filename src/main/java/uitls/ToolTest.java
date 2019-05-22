package uitls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ToolTest
{
	public static void main(String[] args)
	{
		
		List<Double> list = new ArrayList<>();
		for (double i = 10; i <= 50; i = i + 20)
		{
			list.add(i);
		}

		
		System.out.println(list);
		
		System.out.println(Tool.binaryKey(list, 60, false));
		System.out.println(Tool.key(list, 60, false,true));
		
		List<Double> list1 = new ArrayList<>();
		for (double i = 0; i < 100000; i++)
		{
			list1.add(1000 * Math.random());
		}
		
		
//		long s=System.currentTimeMillis();
//		Map<Double, List<Double>> map1 = LambdaUtils.groupby(list1, x -> {
//			Double r = Tool.key(list, x,false,true);
//			return r;
//		});
//		System.out.println(System.currentTimeMillis()-s);
//		System.out.println(map1);
//		
//		long s1=System.currentTimeMillis();
//		Map<Double, List<Double>> map = LambdaUtils.groupby(list1, x -> {
//			Double r = Tool.binaryKey(list, x,false);
//			return r;
//		});
//		System.out.println(System.currentTimeMillis()-s1);
//		System.out.println(map);
	}
}
