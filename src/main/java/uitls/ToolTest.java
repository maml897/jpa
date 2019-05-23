package uitls;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class ToolTest
{
	public static void main(String[] args)
	{
		
//		List<Double> list = new ArrayList<>();
//		for (double i = 10; i <= 50; i = i + 20)
//		{
//			list.add(i);
//		}
//
//		
//		System.out.println(list);
//		
//		System.out.println(Tool.binaryKey(list, 60, false));
//		System.out.println(Tool.key(list, 60, false,true));
//		
//		List<Double> list1 = new ArrayList<>();
//		for (double i = 0; i < 100000; i++)
//		{
//			list1.add(1000 * Math.random());
//		}
		
		
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
		
		
		
//		List<Double> list=Tool.getSegments(0, 750, 10);
//		System.out.println(Tool.key(list, 90, false,true));
//		
//		List<String> result =new ArrayList<>();
//		for (int i = 0; i < list.size(); i++)
//		{
//			if (i == list.size()-1)
//			{
//				result.add("["+list.get(list.size()-1) + ",)");
//			}
//			else
//			{
//				result.add("[" + list.get(i) + "," +(list.get(i + 1)) + ")");
//			}
//		}
//		
//		System.out.println(result);
		
		
		Map<Long,Double> map =new LinkedHashMap<>();
		
		for(int i=1;i<=50000;i++){
			map.put((long)i, (double)(new Random().nextInt(100)));
		}
		
		
		long s=System.currentTimeMillis();
		Map<Double, List<Long>> result=ComputeUtils.scoreStudents(map);
//		Map<Double, List<Long>> result=LambdaUtils.groupby(map.entrySet(), x->x.getValue(),Collectors.mapping(x->x.getKey(), Collectors.toList()));
		System.out.println(System.currentTimeMillis()-s);
		System.out.println(result);
	}
}
