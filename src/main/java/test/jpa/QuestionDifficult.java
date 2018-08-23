package test.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
		System.exit(0);
	}

	@SuppressWarnings("unchecked")
	private static void questionDifficult(long nsID, long subjectID) {
		Query query2 = JpaUtils.createNativeQuery(
				"select nss.NsStudentID,nss.YsScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID="
						+ nsID + " and nss.subjectID=" + subjectID + " and nss.JoinOption=1 and nss.YsScore>0",
				HashMap.class);
		List<Map<String, Object>> nsStudentSubjects = query2.getResultList();

		Query query = JpaUtils
				.createNativeQuery("select ID,Score from n_nsrquestion nq where length(CLevel)>4 and nsID=" + nsID
						+ " and SubjectID=" + subjectID, HashMap.class);
		List<Map<String, Object>> questions = query.getResultList();

		Function<Long, List<Map<String, Object>>> fun = x -> {
			Query query1 = JpaUtils.createNativeQuery(
					"select NsrQuestionID,NsStudentID,Score from n_nsrquestionstudent nq where nq.NsrQuestionID=" + x,
					HashMap.class);
			List<Map<String, Object>> questionStudents = query1.getResultList();
			return questionStudents;
		};

		List<Float> list = new ArrayList<>();
		for (float i = 0; i <= 150; i = i + 5) {
			list.add(i);
		}

		long s = System.currentTimeMillis();
		// compute1(nsStudentSubjects, questions, fun, 150,list);
		compute2(nsStudentSubjects, questions, fun, 150, list);
		System.out.println("时间：" + (System.currentTimeMillis() - s));
	}

	public static Map<Float, Double> compute1(List<Map<String, Object>> nsStudents, List<Map<String, Object>> questions,
			Function<Long, List<Map<String, Object>>> fun, float full, List<Float> steps) {

		Map<Float, List<Long>> map = scoreStudents(nsStudents, steps);// 分数--人员列表

		for (Map<String, Object> question : questions) {
			long questionID = (long) question.get("ID");
			float questionscore = (float) question.get("Score");
			System.out.println("questionID:" + questionID);
			List<Map<String, Object>> questionStudents = fun.apply(questionID);

			Map<Long, Map<Long, Map<String, Object>>> questionStudent = LambdaUtils.groupby(questionStudents, x -> {
				return (Long) x.get("NsrQuestionID");
			}, x -> {
				return (Long) x.get("NsStudentID");
			});

			Map<Float, Double> questionresult = new LinkedHashMap<>();
			for (Float score : map.keySet()) {
				List<Long> studentIDs = map.get(score);

				if (studentIDs.size() > 0) {
					double average = studentIDs.stream()
							.mapToDouble(x -> (Float) questionStudent.get(questionID).get(x).get("Score")).average()
							.getAsDouble();
					questionresult.put(score, average / questionscore);
				} else {
					questionresult.put(score, 0d);
				}
			}
			System.out.println(questionresult);
		}

		return null;
	}

	/**
	 * @param nsStudents
	 * @param questions
	 * @param fun
	 * @param full
	 * @param step
	 * @return
	 */
	public static void compute2(List<Map<String, Object>> nsStudents, List<Map<String, Object>> questions,
			Function<Long, List<Map<String, Object>>> fun, float full, List<Float> steps) {

		Map<Long, Float> studentScore = studentScore(nsStudents, steps);

		for (Map<String, Object> question : questions) {
			long questionID = (long) question.get("ID");
			float questionscore = (float) question.get("Score");
			System.out.println("questionID:" + questionID);
			List<Map<String, Object>> questionStudents = fun.apply(questionID);
			Map<Float, Double> map = compute(studentScore, questionStudents, questionscore, steps);
			System.out.println(map);
		}
	}

	// 分数（相近）-学生IDs compute1使用
	private static Map<Float, List<Long>> scoreStudents(List<Map<String, Object>> studentSubjects, List<Float> list) {
		Map<Float, List<Long>> map = LambdaUtils.groupby3(studentSubjects, x -> {
			float f = Utils.key(list, (float) x.get("YsScore"));
			return f;
		}, Collectors.mapping(x -> (Long) x.get("NsStudentID"), Collectors.toList()));

		Map<Float, List<Long>> result = new LinkedHashMap<>();
		list.forEach(x -> {
			if (map.containsKey(x)) {
				result.put(x, map.get(x));
			} else {
				result.put(x, new ArrayList<>());
			}
		});
		return result;
	}

	// 学生-分数（相近） compute2使用
	private static Map<Long, Float> studentScore(List<Map<String, Object>> studentSubjects, List<Float> list) {
		Map<Long, Float> map = LambdaUtils.list2map2(studentSubjects, x -> (Long) x.get("NsStudentID"),
				x -> Utils.key(list, (float) x.get("YsScore")));
		return map;
	}

	// compute2使用
	private static Map<Float, Double> compute(Map<Long, Float> studentScore, List<Map<String, Object>> questionStudents,
			float questionScore, List<Float> list) {
		Map<Float, Double> map = LambdaUtils.groupby3(questionStudents, x -> {
			long studentID = (long) x.get("NsStudentID");
			return studentScore.get(studentID) == null ? -1 : studentScore.get(studentID);
		}, Collectors.averagingDouble(x -> (float) x.get("Score")));

		Map<Float, Double> result = new LinkedHashMap<>();
		list.forEach(x -> {
			double d = 0;
			if (map.containsKey(x)) {
				d = map.get(x) / questionScore;
			}
			result.put(x, d);
		});
		return result;
	}
}
