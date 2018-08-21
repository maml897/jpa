package uitls;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collectors;

public class StatisticsUtils {
	/**
	 * 标准差
	 * 
	 * @param list
	 * @param averages
	 *            平均分，可以不传，不传的话就自动计算
	 * @return
	 */
	public static double stand(List<Float> list, float... averages) {
		double average = (averages == null || averages.length == 0) ? average(list) : averages[0];
		double variance = list.stream().mapToDouble(x -> Math.pow(x - average, 2)).average().getAsDouble();
		return variance > 0 ? Math.sqrt(variance) : 0;
	}

	// 区分度
	public static double discrimination(List<Float> list, float... averages) {
		return 0;
	}

	// 信度
	public static double reliability(List<Float> list, float... averages) {
		return 0;
	}

	/**
	 * 中位数
	 * 
	 * @param list
	 * @param orderd
	 *            列表是否已经进行了排序，如果没有需要false
	 * @return
	 */
	public static double median(List<Float> list, boolean... orderd) {
		if (orderd != null && orderd.length > 0 && !orderd[0]) {// averaged传false，需要排序
			list = list.stream().sorted().collect(Collectors.toList());
		}

		int studentNumber = list.size();
		float median = 0f;
		int x = studentNumber / 2;
		if (studentNumber % 2 == 0) {
			return (list.get(x - 1) + list.get(x)) / 2;
		} else {
			median = list.get(x).floatValue();
		}
		return median;
	}

	/**
	 * 中位数
	 * 
	 * @param list
	 * @param orderd
	 *            列表是否已经进行了排序，如果没有需要false
	 * @return
	 */
	public static double median1(List<Float> list, boolean... orderd) {

		if (orderd == null || orderd.length == 0 || orderd[0]) {
			int number = list.size();
			float median = 0f;
			int x = number / 2;
			if (number % 2 == 0) {
				return (list.get(x - 1) + list.get(x)) / 2;
			} else {
				median = list.get(x).floatValue();
			}
			return median;
		}

		// 无序列表快速得到中位数
		int heapSize = list.size() / 2 + 1;
		PriorityQueue<Float> heap = new PriorityQueue<>(heapSize);
		for (int i = 0; i < heapSize; i++) {
			heap.add(list.get(i));
		}

		for (int i = heapSize; i < list.size(); i++) {
			if (heap.peek() < list.get(i)) {
				heap.poll();
				heap.add(list.get(i));
			}
		}
		if (list.size() % 2 == 1) {
			return (double) heap.peek();
		} else {
			return (double) (heap.poll() + heap.peek()) / 2.0;
		}
	}

	/**
	 * 平均分
	 * 
	 * @param list
	 * @return
	 */
	public static double average(List<Float> list) {
		return list.stream().mapToDouble(x -> x).average().getAsDouble();
	}

	public static void main(String[] args) {
		List<Float> list = new ArrayList<>();

		for (int i = 50000; i > 0; i--)
			list.add(new Random().nextFloat());

		long s = System.currentTimeMillis();
		System.out.println(median(list, false));
		System.out.println(System.currentTimeMillis() - s);

		s = System.currentTimeMillis();
		System.out.println(median1(list, false));
		System.out.println(System.currentTimeMillis() - s);
	}
}
