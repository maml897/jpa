package test.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.LambdaUtils;
import uitls.StatisticsUtils;

public class SubjectTopAndBottomAndFullTest {
	public static void main(String[] args) throws Exception {
		test(45, 2);
		System.exit(0);
	}

	public static void test(long nsID, long subjectID) throws Exception {

		

		Query query2 = JpaUtils.createNativeQuery("select nss.YsScore,nss.EvenScore, nss.OddScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID=" + nsID +" and nss.subjectID="+subjectID +" and nss.JoinOption=1 and nss.YsScore>0", HashMap.class);
		List<Map<String,Object>> nsStudentSubjects = query2.getResultList();
		
		List<Float> list = LambdaUtils.list2list(nsStudentSubjects, x->(float)x.get("YsScore"));
		
		
		long s = System.currentTimeMillis();
		System.out.println(StatisticsUtils.top(list));
		System.out.println(StatisticsUtils.bottom(list));
		System.out.println(StatisticsUtils.full(list,136));
		
		System.out.println(System.currentTimeMillis()-s);
		
	}
}
