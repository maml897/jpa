package test.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
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
public class ComputeDifficultQuestion {
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

		List<Double> list = new ArrayList<>();
		for (double i = 0; i <= 150; i = i + 5) {
			list.add(i);
		}

		long s = System.currentTimeMillis();
		compute1(nsStudentSubjects, questions, fun, 150,list);
		compute2(nsStudentSubjects, questions, fun, 150, list);
		System.out.println("时间：" + (System.currentTimeMillis() - s));
	}

	public static Map<Double, Double> compute1(List<Map<String, Object>> nsStudents, List<Map<String, Object>> questions,
			Function<Long, List<Map<String, Object>>> fun, float full, List<Double> steps) {

		Map<Double, List<Long>> map = scoreStudents(nsStudents,x->(double)x.get("YsScore"),x->(long)x.get("NsStudentID"), steps);// 分数--人员列表

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

			Map<Double, Double> questionresult = new LinkedHashMap<>();
			for (double score : map.keySet()) {
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
	 *  
	 * @param objects 待处理
	 * @param segments 分段表
	 * @return
	 */
	// 分数（相近）-学生IDs compute1使用
	private static <T> Map<Double, List<Long>> scoreStudents(List<T> objects,Function<T,Double> keyfunction,Function<T,Long> studentIDfunction,List<Double> segments) {
		Map<Double, List<Long>> map = LambdaUtils.groupby(objects, x -> Utils.key(segments, keyfunction.apply(x)), 
				Collectors.mapping(studentIDfunction, Collectors.toList()));
		
		Map<Double, List<Long>> result = new LinkedHashMap<>();
		segments.forEach(x -> {
			if (map.containsKey(x)) {
				result.put(x, map.get(x));
			} else {
				result.put(x, new ArrayList<>());
			}
		});
		return result;
	}
	

	//***********************************************************************************************************************************************************
	
	/**
	 * @param nsStudents
	 * @param questions
	 * @param fun
	 * @param full
	 * @param step
	 * @return
	 */
	public static void compute2(List<Map<String, Object>> nsStudents, List<Map<String, Object>> questions,
			Function<Long, List<Map<String, Object>>> fun, float full, List<Double> segments) {

		Map<Long, Double> studentScore = studentScore(nsStudents,x -> (Long) x.get("NsStudentID"),x->(double) x.get("YsScore"), segments);

		for (Map<String, Object> question : questions) {
			long questionID = (long) question.get("ID");
			float questionscore = (float) question.get("Score");
			System.out.println("questionID:" + questionID);
			List<Map<String, Object>> questionStudents = fun.apply(questionID);
			
			Map<Double, Double> map = computeStudentScore(studentScore, questionStudents,x->(long)x.get("NsStudentID"),x->(double)x.get("Score"), questionscore, segments);
			System.out.println(map);
		}
	}
	
	/**
	 * 
	 * @param objects
	 * @param keyFunction 学生ID
	 * @param valueFunction 学生分值
	 * @param segments
	 * @return
	 */
	// 学生-分数（相近） compute2使用x -> (Long) x.get("NsStudentID");
	public static <T> Map<Long, Double> studentScore(List<T> objects,Function<T,Long> keyFunction,Function<T,Double> valueFunction, List<Double> segments) {
		Map<Long, Double> map = LambdaUtils.list2map2(objects,keyFunction ,
				x -> Utils.key(segments,valueFunction.apply(x)));
		return map;
	}

	/**
	 * 
	 * @param studentScore
	 * @param questionStudents
	 * @param studentIDFunction 学生ID
	 * @param studentScoreFunction 学生分值
	 * @param questionScore
	 * @param segments
	 * @return
	 */
	// compute2使用
	public static <T> Map<Double, Double> computeStudentScore(Map<Long, Double> studentScore, List<T> questionStudents,Function<T,Long> studentIDFunction,ToDoubleFunction<T> studentScoreFunction,double questionScore, List<Double> segments) {
		
		Map<Double, Double> map = LambdaUtils.groupby(questionStudents, x -> {
			long studentID = studentIDFunction.apply(x);
			return studentScore.get(studentID) == null ? -1 : studentScore.get(studentID);
		}, Collectors.averagingDouble(studentScoreFunction));

		Map<Double, Double> result = new LinkedHashMap<>();
		segments.forEach(x -> {
			double d = 0;
			if (map.containsKey(x)) {
				d = map.get(x) / questionScore;
			}
			result.put(x, d);
		});
		return result;
	}
	
	/**
	 * 
	 * @param studentScore 学生-分数（相近，总分或者科目等）
	 * @param studentScores 学生-分数（小题或者科目等）
	 * @param fullScore
	 * @param segments
	 * @return
	 */
	public static Map<Double, Double> computeStudentScore(Map<Long, Double> studentScore, Map<Long,Double> studentScores,double fullScore, List<Double> segments) {
		
		Map<Double, Double> map = LambdaUtils.groupby(studentScores.keySet(), x -> {
			return studentScore.get(x) == null ? -1 : studentScore.get(x);
		}, Collectors.averagingDouble(x->studentScores.get(x)));

		Map<Double, Double> result = new LinkedHashMap<>();
		segments.forEach(x -> {
			double d = 0;
			if (map.containsKey(x)) {
				d = map.get(x) / fullScore;
			}
			result.put(x, d);
		});
		return result;
	}
	
}
