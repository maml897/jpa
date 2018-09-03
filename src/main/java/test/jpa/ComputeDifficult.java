package test.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.LambdaUtils;
import uitls.Utils;

/**
 * 学科难度
 * 
 * @author maml
 *
 */
public class ComputeDifficult {

	public static void main(String[] args) {
		test(45, 2, 20);
		System.exit(0);
	}

	@SuppressWarnings("unchecked")
	public static void test(long nsID, long subjectID, float step) {

		long s = System.currentTimeMillis();

		Query query2 = JpaUtils.createNativeQuery(
				"select ns.ID,ns.Score,nss.YsScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID="
						+ nsID + " and nss.subjectID=" + subjectID + " and nss.JoinOption=1 and nss.Score>0",
				HashMap.class);
		List<Map<String, Object>> nsStudentSubjects = query2.getResultList();
		
		
		List<Double> list=new ArrayList<>();
		list.add(0d);
		list.add(10d);
		for(double i=30;i<=750;i=i+20){
			list.add(i);
		}
		
		Map<Double, Double> map=compute(nsStudentSubjects,x->(double)x.get("Score"),x->(double)x.get("YsScore"), list, 150d);
		map.forEach((x,y)->{
			System.out.println(x+"=="+y);
		});
		System.out.println("用时：" + (System.currentTimeMillis() - s));
	}


	/**
	 * 
	 * @param objects 待计算的对象列表
	 * @param keyfunction key的字段 
	 * @param averagefunction 计算平均分的字段
	 * @param segments 分数段
	 * @param fullScore 满分
	 * @return
	 */
	public static <T> Map<Double, Double> compute(List<T> objects,Function<T,Double> keyfunction,ToDoubleFunction<T> averagefunction, List<Double> segments,
			double fullScore) {
		
		Map<Double, Double> map=LambdaUtils.groupby(objects, x->{
			double result=Utils.key(segments, keyfunction.apply(x));
			return result;
		},Collectors.averagingDouble(averagefunction));
		
		Map<Double, Double> result = new LinkedHashMap<>();
		segments.forEach(x->{
			double d =0;
			if(map.containsKey(x)){
				d=map.get(x)/fullScore;
			}
			result.put(x, d);
		});
		return result;
	}
}
