package uitls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

	/**
	 * 返回values 中距离value 最近的值，可以上取，也可以下取
	 * 
	 * @param value：
	 * @param values：排好序的value，从小到大
	 * @return
	 */
	public static float key(Collection<Float> values, float value, boolean... flag) {

		if (flag != null && flag.length > 0 && flag[0]) {// 取低
			float result = -1;
			for (Float f : values) {
				if (f <= value) {
					result = f;
				}
			}
			return result;

		} else {
			for (Float f : values) {
				if (f >= value) {
					return f;
				}
			}
			return -1;
		}
	}

	public static void main(String[] args) {
		List<Float> list = new ArrayList<Float>();
		for(float i=0;i<100000;i++){
			list.add(i);
		}

		long s=System.currentTimeMillis();
		System.out.println(key(list, 8965.2f));
		System.out.println(System.currentTimeMillis()-s);
		
	}
}
