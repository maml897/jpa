package test.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.LambdaUtils;
import uitls.Utils;

/**
 * 小题难度
 * 
 * @author maml
 *
 */
public class QuestionDifficult {
	public static void main(String[] args) {
		questionDifficult(45, 2);
	}

	private static void questionDifficult(long nsID, long subjectID) {
		Query query2 = JpaUtils.createNativeQuery(
				"select nss.NsStudentID,nss.YsScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID="
						+ nsID + " and nss.subjectID=" + subjectID + " and nss.JoinOption=1 and nss.YsScore>0",
				HashMap.class);
		List<Map<String, Object>> nsStudentSubjects = query2.getResultList();
		
		Query query = JpaUtils.createNativeQuery(
				"select ID,Score from n_nsrquestion nq where length(CLevel)>4 and nsID="
						+ nsID + " and SubjectID=" + subjectID,
				HashMap.class);
		List<Map<String, Object>> questions = query.getResultList();
		compute(nsStudentSubjects, questions, 150, 5);
	}

	public static Map<Float, Double> compute(List<Map<String, Object>> nsStudents, List<Map<String, Object>> questions,
			float full, float step) {
		Map<Float, Double> result = new HashMap<>();
		Map<Float, List<Long>> map = score2students(nsStudents, full, step);// 分数--人员列表

		for (Map<String, Object> question : questions) {
			
			long questionID=(long)question.get("ID");
			float questionscore=(float)question.get("Score");
			
			if(15083!=questionID)
			{
				continue;
			}
			Query query = JpaUtils.createNativeQuery(
					"select NsrQuestionID,NsStudentID,Score from n_nsrquestionstudent nq where nq.NsrQuestionID="+questionID,HashMap.class);
			List<Map<String, Object>> questionStudents = query.getResultList();// 本道题学生的答题情况,sudentID,questionID,Score

			Map<Long, Map<Long, Map<String, Object>>> questionStudent = LambdaUtils.groupby(questionStudents, x -> {
				return (Long) x.get("NsrQuestionID");
			}, x -> {
				return (Long) x.get("NsStudentID");
			});

			for (Float score : map.keySet()) {
				List<Long> studentIDs = map.get(score);
				
				float sum=0;
				for (Long studentID : studentIDs) {
					float questionstudentscore = (Float) questionStudent.get(questionID).get(studentID)
							.get("Score");
					sum=sum+questionstudentscore;
				}
				
				float diff=0;
				if(studentIDs.size()==0){
					diff=0;
				}
				else{
					diff=sum/studentIDs.size()/questionscore;
				}
				
				System.out.println(diff);
			}
		}
		return result;
	}

	// 待计算学生
	private static Map<Float, List<Long>> score2students(List<Map<String, Object>> nsStudents, float full,
			float step) {
		Map<Float, List<Long>> mapSegmentStudentId = initMap(full, step);

		for (Map<String, Object> map : nsStudents) {
			Long nsStudentID = (long) map.get("NsStudentID");// 学生
			Float score = (Float) map.get("YsScore");// 得分

			// 5分一段区间
			Float segmentScore = Utils.key(mapSegmentStudentId.keySet(), score);
			if (segmentScore > full) {
				segmentScore = full;
			}

			List<Long> studentIds = mapSegmentStudentId.get(segmentScore);
			if (studentIds == null) {
				studentIds = new ArrayList<>();
			}
			studentIds.add(nsStudentID);
			mapSegmentStudentId.put(segmentScore, studentIds);
		}
		return mapSegmentStudentId;
	}

	private static Map<Float, List<Long>> initMap(float full, float step) {
		Map<Float, List<Long>> map = new LinkedHashMap<Float, List<Long>>();
		float lowerScore = 0;
		float upperScore = full;
		while (lowerScore <= upperScore) {
			map.put(lowerScore, new ArrayList<>());
			lowerScore = lowerScore + step;
		}
		return map;
	}
}
