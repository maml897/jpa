package test.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.LambdaUtils;

public class ComputeScore {
	public static void main(String[] args) {
		computeSubject(45, 2, null);
		System.exit(0);
	}

	public static void computeSubject(long nsID, long subjectID, List<Map<String, Object>> nsStudentSubjects) {
		long s = System.currentTimeMillis();
		if(nsStudentSubjects==null){
			Query query2 = JpaUtils.createNativeQuery(
					"select ns.ID,ns.Score,nss.YsScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID="
							+ nsID + " and nss.subjectID=" + subjectID
							+ " and nss.JoinOption=1 and nss.YsScore>0 order by nss.YsScore desc",
					HashMap.class);
			nsStudentSubjects = query2.getResultList();
		}

		System.out.println(nsStudentSubjects.size());
		ComputeScore.compute(nsStudentSubjects,t -> (float) t.get("YsScore"));
		System.out.println(System.currentTimeMillis() - s);
	}

	public static void computeDistrictSubject(long nsID, long subjectID, long districtID,
			List<Map<String, Object>> nsStudentSubjects) {
		long s = System.currentTimeMillis();

		if (nsStudentSubjects == null) {
			Query query2 = JpaUtils.createNativeQuery(
					"SELECT nss.NsStudentID,nss.YsScore FROM n_nsstudentsubject nss JOIN n_nsstudent ns ON nss. NsStudentID=ns.ID AND ns.nsID="
							+ nsID + " AND nss.SubjectID=" + subjectID
							+ " JOIN  n_nsdistrictschool nds ON nds.NsDistrictID=" + districtID
							+ " AND nds.SchoolID=ns.SchoolID AND nss.JoinOption=1 AND nss.YsScore>0  order by nss.YsScore desc",
					HashMap.class);
			nsStudentSubjects = query2.getResultList();
		}

		System.out.println(nsStudentSubjects.size());
		ComputeScore.compute(nsStudentSubjects,t -> (float) t.get("YsScore"));
		System.out.println(System.currentTimeMillis() - s);
	}

	public static void computeSchoolSubject(long nsID, long subjectID, long schoolID,
			List<Map<String, Object>> nsStudentSubjects) {
	}

	/**
	 * 计算每个分数的排名
	 * @param scores
	 * @param function
	 */
	public static <T> List<Map<String,Object>> compute(List<T> scores,Function<T,Float> function) {
		Map<Float, Long> group=LambdaUtils.groupby(scores, function, Collectors.counting());
//		Map<Float, Long> group = scores.stream().collect(Collectors.groupingBy(function, LinkedHashMap::new, Collectors.counting()));
		
		List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
		
		long lastOrder = 1;
		long lastCount = 0;
		long lastSum = 0;

		for (float score : group.keySet()) {

			Map<String,Object> map =new HashMap<>();
			
			long order = lastOrder + lastCount;//
			long sum = lastCount + lastSum;//
			long count = group.get(score);

			map.put("score", score);
			map.put("order", order);
			map.put("count", count);
			map.put("sum", sum);
			result.add(map);
			
			lastOrder = order;
			lastCount = count;
			lastSum = sum;
		}
		
		return result;
	}
}
