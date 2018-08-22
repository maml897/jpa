package uitls;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import common.excel.PoiExcelReader;

public class StatisticsUtils {

	/**
	 * 方差
	 * 
	 * @param list
	 * @param averages
	 *            平均分，可以不传，不传的话就自动计算
	 * @return
	 */
	public static double variance(List<Float> list, double... averages) {
		double average = (averages == null || averages.length == 0) ? average(list) : averages[0];
		double variance = list.stream().mapToDouble(x -> Math.pow(x - average, 2)).average().getAsDouble();
		return variance;
	}

	/**
	 * 标准差
	 * 
	 * @param list
	 * @param averages
	 *            平均分，可以不传，不传的话就自动计算
	 * @return
	 */
	public static double stand(List<Float> list, double... averages) {
		double variance = variance(list, averages);
		return variance > 0 ? Math.sqrt(variance) : 0;
	}

	/**
	 * 区分度
	 * 
	 * @param list
	 *            排好序的,按照单科分数排序，分数相同按照examCode
	 * @param fullScore
	 * @return
	 */
	public static double discrimination(List<Float> list, float fullScore) {
		int statisticNum = list.size();

		int hnum = statisticNum * 27 / 100;
		int lnum = statisticNum - statisticNum * 73 / 100;

		List<Float> hlist = list.subList(0, hnum);
		List<Float> llist = list.subList(statisticNum - lnum, statisticNum);

		double haverage = average(hlist);
		double laverage = average(llist);
		return (haverage - laverage) / fullScore;
	}

	public static void main(String[] args) throws Exception {
		PoiExcelReader excelReader = new PoiExcelReader(
				new File(StatisticsUtils.class.getResource("f_value.xls").getPath()));
		excelReader.selSheet(0);

		System.out.println(excelReader.getOneCell(1, 1, false));
	}

	/**
	 * 信度
	 * 
	 * @param questionCount
	 *            问题数量
	 * @param scores
	 *            学生的分数集合
	 * @param oddScores
	 *            学生的奇数题分数集合
	 * @param evenScores
	 *            学生的偶数题分数集合
	 * @param variance
	 *            学生分数方差
	 * @return
	 * @throws Exception
	 */
	public static double reliability(int questionCount, List<Float> scores, List<Float> oddScores,
			List<Float> evenScores, double variance) throws Exception {

		double oddAvg = average(oddScores);
		double oddVariance = variance(oddScores, oddAvg);

		double evenAvg = average(evenScores);
		double evenVariance = variance(evenScores, evenAvg);

		int y = questionCount / 2;
		int x = questionCount - y;

		if (x <= 50 && y <= 50) {
			PoiExcelReader excelReader = new PoiExcelReader(
					new File(StatisticsUtils.class.getResource("f_value.xls").getPath()));
			excelReader.selSheet(0);

			double f = 0;
			double f_s = 0;

			if (oddVariance > evenVariance) {
				f = oddVariance / evenVariance;
				f_s = Double.parseDouble(excelReader.getOneCell(x - 1, y - 1, false));
			} else {
				f = evenVariance / oddVariance;
				f_s = Double.parseDouble(excelReader.getOneCell(y - 1, x - 1, false));
			}

			if (f <= f_s) {
				double rAvg = 0;
				if (scores.size() > 0) {
					double total = 0;
					for (int i = 0; i < oddScores.size(); i++) {
						total = total + oddScores.get(i) * evenScores.get(i);
					}
					rAvg = total / scores.size();
				}
				double r = (rAvg - oddAvg * evenAvg) / (Math.sqrt(oddVariance) * Math.sqrt(evenVariance));
				return 2 * r / (1 + r);
			}
		}

		double dvariance = 0d;
		List<Float> list = new ArrayList<>();
		if (scores.size() > 0) {
			for (int i = 0; i < oddScores.size(); i++) {
				list.add(Math.abs(oddScores.get(i) - evenScores.get(i)));
			}
			dvariance = variance(list);
		}
		return 1 - dvariance / variance;
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
		if (list.size() == 0) {
			return 0;
		}
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

		// 无序列表快速得到中位数https://www.cnblogs.com/shizhh/p/5746151.html
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

	/**
	 * 满分人数
	 * 
	 * @param list
	 * @return
	 */
	public static int full(List<Float> list,float score) {
		int full = (int)list.stream().filter(x -> x ==score).count();
		return full;
	}

	/**
	 * 最高分
	 * 
	 * @param list
	 * @return
	 */
	public static double top(List<Float> list) {
		double result = list.stream().mapToDouble(x -> x).max().getAsDouble();
		return result;
	}

	/**
	 * 最低分
	 * 
	 * @param list
	 * @return
	 */
	public static double bottom(List<Float> list) {
		double result = list.stream().mapToDouble(x -> x).min().getAsDouble();
		return result;
	}
}
