package test.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
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
		Query query = JpaUtils.createNativeQuery("select * from n_ns where ID=" + nsID, HashMap.class);
		Map<String, Object> ns = (Map<String, Object>) query.getSingleResult();

		long s = System.currentTimeMillis();

		s = System.currentTimeMillis();
		Query query2 = JpaUtils.createNativeQuery(
				"select ns.ID,ns.Score,nss.YsScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID="
						+ nsID + " and nss.subjectID=" + subjectID + " and nss.JoinOption=1 and nss.Score>0",
				HashMap.class);
		List<Map<String, Object>> nsStudentSubjects = query2.getResultList();
		// System.out.println(nsStudentSubjects);

		s = System.currentTimeMillis();
		float score = (float) ns.get("Score");
		while (score > 0) {
			Map<Float, Double> msp = subjectDifficult(nsStudentSubjects, score, step, 150);// 150是学科满分
			System.out.println(msp);
			score = score - step;
		}
		System.out.println("用时：" + (System.currentTimeMillis() - s));
		
		
		s = System.currentTimeMillis();
		List<Float> list=new ArrayList<>();
		
		list.add(0f);
		list.add(10f);
		for(float i=10;i<=750;i=i+20){
			list.add(i);
		}
		
		
		Map<Float, Double> map=compute(nsStudentSubjects, list, 150);
		map.forEach((x,y)->{
			System.out.println(x+"=="+y);
		});
		System.out.println("用时：" + (System.currentTimeMillis() - s));
	}

	// nsStudents：统计学生
	public static Map<Float, Double> subjectDifficult(List<Map<String, Object>> nsStudents, float scorePonit,
			float step, float subjectFull) {
		Map<Float, Double> map = new HashMap<>();
		map.put(scorePonit, 0d);
		try {
			double average = nsStudents.stream().filter(x -> {
				float studentScore = (float) x.get("Score");// 学生总分
				return (studentScore <= scorePonit && studentScore > (scorePonit - step));

			}).mapToDouble(x -> (float) x.get("YsScore")).average().getAsDouble();// 学生单科成绩
			double result = average / subjectFull;
			map.put(scorePonit, result);
		} catch (Exception e) {

		}
		return map;
	}

	public static Map<Float, Double> compute(List<Map<String, Object>> nsStudents, List<Float> list,
			float subjectFull) {
		Map<Float, Double> map=LambdaUtils.groupby3(nsStudents, x->{
			float result=Utils.key(list, (float)x.get("Score"));
			return result;
		},Collectors.averagingDouble(x->(float)x.get("YsScore")));
		
		
		
		/*
		long l=nsStudents.stream().filter(x -> {
			float studentScore = (float) x.get("Score");// 学生总分
			return (studentScore <= 70 && studentScore > 50);

		}).collect(Collectors.counting());
		
		
		
		System.out.println(l);
		
		Map<Float, Long> map1=LambdaUtils.groupby3(nsStudents, x->{
			float result=Utils.key(list, (float)x.get("Score"));
			return result;
		},Collectors.counting());
		*/
		
		for(float f:map.keySet()){
			map.put(f, map.get(f)/subjectFull);
		}
		
		return map;
	}
}
