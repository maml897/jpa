package test.jpa;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Query;

import config.jpa.JpaUtils;

public class DistrictSubjectScore {
	public static void main(String[] args) {
		test(45, 2,862);
		System.exit(0);
	}
	
	public static void test(long nsID, long subjectID,long nsDistrictID) {
		
		long s =System.currentTimeMillis();
		
		Query query2 = JpaUtils.createNativeQuery(
				"SELECT nss.NsStudentID,nss.YsScore FROM n_nsstudentsubject nss JOIN n_nsstudent ns ON nss. NsStudentID=ns.ID AND ns.nsID="+nsID+" AND nss.SubjectID="+subjectID+
" JOIN  n_nsdistrictschool nds ON nds.NsDistrictID="+nsDistrictID+" AND nds.SchoolID=ns.SchoolID AND nss.JoinOption=1 AND nss.YsScore>0  order by nss.YsScore desc",
				HashMap.class);
		List<Map<String, Object>> nsStudentSubjects = query2.getResultList();
		
		System.out.println(nsStudentSubjects.size());
		SubjectScore.compute(nsStudentSubjects);
		System.out.println(System.currentTimeMillis()-s);
	}
	
}
