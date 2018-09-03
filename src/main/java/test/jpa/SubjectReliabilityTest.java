package test.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.LambdaUtils;
import uitls.StatisticsUtils;

public class SubjectReliabilityTest {
	public static void main(String[] args) throws Exception {
		test(45, 2);
		System.exit(0);
	}

	public static void test(long nsID, long subjectID) throws Exception {

		long s = System.currentTimeMillis();

		Query query2 = JpaUtils.createNativeQuery("select nss.YsScore,nss.EvenScore, nss.OddScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID=" + nsID +" and nss.subjectID="+subjectID +" and nss.JoinOption=1 and nss.YsScore>0", HashMap.class);
		List<Map<String,Object>> nsStudentSubjects = query2.getResultList();
		
		List<Double> list = LambdaUtils.list2list(nsStudentSubjects, x->(double)x.get("YsScore"));
		List<Double> list_even = LambdaUtils.list2list(nsStudentSubjects, x->(double)x.get("EvenScore"));
		List<Double> list_odd = LambdaUtils.list2list(nsStudentSubjects, x->(double)x.get("OddScore"));
		
		double variance=StatisticsUtils.variance(list);
		System.out.println(variance);
		
		
		System.out.println(StatisticsUtils.reliability(20, list, list_odd, list_even, variance));
		
		System.out.println(System.currentTimeMillis()-s);
		
	}
}
