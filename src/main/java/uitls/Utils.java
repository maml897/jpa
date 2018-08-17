package uitls;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Utils {

	/**
	 * 返回values 中距离oldValue 最近的值，可以上取，也可以下取
	 * 
	 * @param value：
	 * @param values：排好序的value，从小到大
	 * @return
	 */
	public static float key(Collection<Float> values, float value, boolean... flag) {

		if (flag != null && flag.length > 0 && flag[0]) {// 取高
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
		List<Float> list = Arrays.asList(0f, 5f, 10f, 15f, 20f);

		System.out.println(key(list, 5.2f, true));
	}
}
