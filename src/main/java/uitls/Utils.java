package uitls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

	/**
	 * 返回values 中距离value 最近的值，可以上取，也可以下取
	 * 取高：低于最低分的都算作最低分；取低：高于最高分的都算作最高分
	 * @param value：
	 * @param values：排好序的value，从小到大
	 * @return
	 */
	public static double key(Collection<Double> values, double value, boolean... flag) {

		if (flag != null && flag.length > 0 && flag[0]) {// 取低
			double result = -1;
			for (Double f : values) {
				if (f <= value) {
					result = f;
				}
			}
			return result;

		} else {//取高
			for (Double f : values) {
				if (f >= value) {
					return f;
				}
			}
			return -1;
		}
	}

	public static void main(String[] args) {
		List<Double> list = new ArrayList<>();
		for(double i=0;i<1000;i++){
			list.add(i);
		}
		
		long s=System.currentTimeMillis();
		List<Double> list1 = new ArrayList<>();
		for(double i=0;i<1000;i++){
			list1.add(i-0.5f);
		}
		
		Map<Double, List<Double>> map=LambdaUtils.groupby(list1, x->{
			double result=Utils.key(list, x);
			return result;
		});

		
		System.out.println(map);
		System.out.println(System.currentTimeMillis()-s);
		
	}
}
