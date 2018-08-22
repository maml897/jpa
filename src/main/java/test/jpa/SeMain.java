package test.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;

public class SeMain {
	public static void main(String[] args) {
		computeNS(45);
	}

	public static void computeNS(long nsID) {

		Query query = JpaUtils.createNativeQuery(
				"select ns.SubjectID from n_nsrsubject ns where ns.nsID=" + nsID + " and ns.nsID=" + nsID);
		List<Long> nsSubjects = query.getResultList();

		// 1.计算ns
		// 2.计算NsSubject
		for (long subjectID : nsSubjects) {
			computeNsSubject(nsID, subjectID, null);
		}

		// 3.计算NsDistrict
		// 4.计算NsDistrictSubject

		// 5.计算NsSchool
		// 6.计算NsSchoolSubject

		// 7.计算NsClass
		// 8.计算NsClassSubject
	}

	public static void computeNs(long nsID, List<Map<String, Object>> nsStudentSubjects) {
		
	}
	
	public static void computeNsSubject(long nsID, long subjectID, List<Map<String, Object>> nsStudentSubjects) {
		ComputeScore.computeSubject(nsID, subjectID, nsStudentSubjects);
	}
	
	public static void computeNsDistrict(long nsID, long districtID, List<Map<String, Object>> nsStudentSubjects) {
		
	}
	
	public static void computeNsDistrictSubject(long nsID, long districtID,long subjectID, List<Map<String, Object>> nsStudentSubjects) {
		ComputeScore.computeDistrictSubject(nsID, subjectID, districtID, nsStudentSubjects);
	}
	
	public static void computeNsSchool(long nsID, long schoolID, List<Map<String, Object>> nsStudentSubjects) {
		
	}
	
	public static void computeNsSchoolSubject(long nsID, long schoolID,long subjectID, List<Map<String, Object>> nsStudentSubjects) {
		ComputeScore.computeSchoolSubject(nsID, subjectID, schoolID, nsStudentSubjects);
	}
	
	public static void computeNsClass(long nsID, long schoolID,String className, List<Map<String, Object>> nsStudentSubjects) {
		
	}
	
	public static void computeNsClassSubject(long nsID, long schoolID,String className,long subjectID, List<Map<String, Object>> nsStudentSubjects) {
		
	}
}
