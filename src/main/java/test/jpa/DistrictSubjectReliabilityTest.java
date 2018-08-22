package test.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.LambdaUtils;
import uitls.StatisticsUtils;

public class DistrictSubjectReliabilityTest {
	public static void main(String[] args) throws Exception {
		test(45, 2,862);
		System.exit(0);
	}

	public static void test(long nsID, long subjectID,long nsDistrictID) throws Exception {

		long s = System.currentTimeMillis();

		Query query2 = JpaUtils.createNativeQuery(
				"SELECT nss.YsScore,nss.EvenScore, nss.OddScore FROM n_nsstudentsubject nss JOIN n_nsstudent ns ON nss. NsStudentID=ns.ID AND ns.nsID="+nsID+" AND nss.SubjectID="+subjectID+
" JOIN  n_nsdistrictschool nds ON nds.NsDistrictID="+nsDistrictID+" AND nds.SchoolID=ns.SchoolID AND nss.JoinOption=1 AND nss.YsScore>0  order by nss.YsScore desc",
				HashMap.class);
		
		List<Map<String,Object>> nsStudentSubjects = query2.getResultList();
		
		List<Float> list = LambdaUtils.list2list(nsStudentSubjects, x->(float)x.get("YsScore"));
		List<Float> list_even = LambdaUtils.list2list(nsStudentSubjects, x->(float)x.get("EvenScore"));
		List<Float> list_odd = LambdaUtils.list2list(nsStudentSubjects, x->(float)x.get("OddScore"));
		
		double variance=StatisticsUtils.variance(list);
		System.out.println(variance);
		
		System.out.println(StatisticsUtils.reliability(20, list, list_odd, list_even, variance));
		System.out.println(System.currentTimeMillis()-s);
		
	}
}
