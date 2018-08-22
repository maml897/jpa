package test.jpa;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Query;

import config.jpa.JpaUtils;

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
		ComputeScore.compute(nsStudentSubjects);
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
		ComputeScore.compute(nsStudentSubjects);
		System.out.println(System.currentTimeMillis() - s);
	}

	public static void computeSchoolSubject(long nsID, long subjectID, long schoolID,
			List<Map<String, Object>> nsStudentSubjects) {
	}

	public static void compute(List<Map<String, Object>> nsStudentSubjects) {
		Map<Float, Long> group2 = nsStudentSubjects.stream().collect(
				Collectors.groupingBy(t -> (float) t.get("YsScore"), LinkedHashMap::new, Collectors.counting()));

		long lastOrder = 1;
		long lastCount = 0;
		long lastSum = 0;

		System.out.println("ysScore==order===ount===sum");
		for (float ysScore : group2.keySet()) {

			long order = lastOrder + lastCount;//
			long sum = lastCount + lastSum;//
			long count = group2.get(ysScore);

			System.out.println(ysScore + "==" + order + "===" + count + "===" + sum);

			lastOrder = order;
			lastCount = count;
			lastSum = sum;
		}
	}
}
