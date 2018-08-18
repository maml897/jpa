package test.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;

public class DisdrictQuestionDifficult {

	
	public static void main(String[] args) {
		questionDifficult(45, 2);
	}

	//SELECT * FROM `n_nsrdistrictquestiondifficulty` WHERE NsrQuestionID=15083 AND NsDistrictID=862 ORDER BY Score
	private static void questionDifficult(long nsID, long subjectID) {
		Query query2 = JpaUtils.createNativeQuery(
				"SELECT nss.NsStudentID,nss.YsScore FROM n_nsstudentsubject nss JOIN n_nsstudent ns ON nss. NsStudentID=ns.ID AND ns.nsID=45 AND nss.SubjectID=2"+
" JOIN  n_nsdistrictschool nds ON nds.NsDistrictID=862 AND nds.SchoolID=ns.SchoolID AND nss.JoinOption=1 AND nss.YsScore>0",
				HashMap.class);
		List<Map<String, Object>> nsStudentSubjects = query2.getResultList();
		
		Query query = JpaUtils.createNativeQuery(
				"select ID,Score from n_nsrquestion nq where length(CLevel)>4 and nsID="
						+ nsID + " and SubjectID=" + subjectID,
				HashMap.class);
		List<Map<String, Object>> questions = query.getResultList();
		QuestionDifficult.compute(nsStudentSubjects, questions, 150, 5);
	}

}
