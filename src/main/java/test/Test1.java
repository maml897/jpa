package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;

public class Test1 {
	public static void main(String[] args) {
		test1();
		System.out.println("======================");
		test2();
	}
	
	private static void test1(){
		long s=System.currentTimeMillis();
		Query query2 = JpaUtils.createNativeQuery(
				"SELECT NsrQuestionID,NsStudentID,Score FROM n_nsrquestionstudent nq WHERE nq.NsrQuestionID=15098",
				HashMap.class);
		List<Map<String, Object>> nsStudentSubjects = query2.getResultList();
		System.out.println(nsStudentSubjects.size());
		System.out.println(System.currentTimeMillis()-s);
	}
	
	private static void test2(){
		long s=System.currentTimeMillis();
		Query query2 = JpaUtils.createNativeQuery(
				"SELECT qs.NsrQuestionID,qs.NsStudentID,qs.Score FROM n_nsrquestionstudent qs"+
" JOIN n_nsstudent s ON qs.NsStudentID = s.ID "+
" JOIN n_nsdistrictschool dsc ON dsc.SchoolID = s.SchoolID "+
" JOIN n_nsdistrict d ON dsc.NsDistrictID = d.ID AND d.NsID = s.NsID "+
" WHERE qs.NsrQuestionID = 15098 AND s.NsID = 45 AND d.ID = 862",
				HashMap.class);
		List<Map<String, Object>> nsStudentSubjects = query2.getResultList();
		
		System.out.println(nsStudentSubjects.size());
		System.out.println(System.currentTimeMillis()-s);
	}
}
