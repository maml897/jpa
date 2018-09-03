package test.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.LambdaUtils;
import uitls.StatisticsUtils;

public class NsrSubjectTest {
	public static void main(String[] args) {
		test(45, 2);
	}

	public static void test(long nsID, long subjectID) {

		long s = System.currentTimeMillis();

		Query query2 = JpaUtils.createNativeQuery("select nss.YsScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID=" + nsID +" and nss.subjectID="+subjectID +" and nss.JoinOption=1 and nss.YsScore>0", HashMap.class);
		List<Map<String,Object>> nsStudentSubjects = query2.getResultList();

		System.out.println(nsStudentSubjects.size());
		
		List<Double> list = LambdaUtils.list2list(nsStudentSubjects, x->(double)x.get("YsScore"));
		System.out.println(StatisticsUtils.stand(list));
		System.out.println(StatisticsUtils.median(list,false));
		
		
		
	}
}
