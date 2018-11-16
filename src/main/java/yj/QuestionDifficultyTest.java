package yj;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.LambdaUtils;

public class QuestionDifficultyTest
{
	public static void main(String[] args)
	{
		StringBuffer sql = new StringBuffer("SELECT DISTINCT(seID) FROM s_sesubjectdifficulty");
		Query query = JpaUtils.createNativeQuery(sql.toString());

		List<BigInteger> list = query.getResultList();
		for (BigInteger seID : list)
		{
			if (seID.longValue() < 1)
			{
				continue;
			}
			System.out.println(seID);
			test(seID.longValue());
		}
	}

	public static void test(long seID)
	{
		StringBuffer sql = new StringBuffer("SELECT Score,SubjectID FROM s_sesubject where seID=" + seID);
		Query query = JpaUtils.createNativeQuery(sql.toString(), HashMap.class);
		List<Map<String, Object>> subjects = query.getResultList();

		List<Map<String, Object>> resultAll = new ArrayList<>();
		for (Map<String, Object> subject : subjects)
		{
			if ((float) subject.get("Score") == 0)
			{
				continue;
			}
			
			Map<Long, Double> subjectStudentScoreMap = getSeStudentsForSeQuestionDifficulty(seID, (long)subject.get("SubjectID"));// 科目学生得分
			Map<Double, List<Long>> subjectScoreStudentIDs = getSegments4ComputeDifficulty(subjectStudentScoreMap, (double)subject.get("Score"),false);

			List<Map<String,Object>> questions=null;
			
			List<Map<String, Object>> resultQuestionAll = new ArrayList<>();
			for (Map<String,Object> question: questions)
			{
				Map<Long, Double> questionStudentScore=getQuestionStudentScore((long)question.get("ID"));
				List<Map<String, Object>> computeResult = computeDifficulty(subjectScoreStudentIDs, questionStudentScore,(double)question.get("Score"));
				for (Map<String, Object> map : computeResult)
				{
					map.put("SeQuestionID", question.get("ID"));
				}
				resultQuestionAll.addAll(computeResult);
			}
		}
	}
	
	public static double round(Object v)
	{
		BigDecimal b = new BigDecimal(v.toString());
		return b.divide(new BigDecimal("1"), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	
	public static Map<Long,Double> getQuestionStudentScore(long seQuestionID) throws RuntimeException{
		String sql = "SELECT SeStudentID,Score FROM s_sequestionstudent WHERE SeQuestionID=?1";
		Query query = JpaUtils.createNativeQuery(sql);
		query.setParameter(1, seQuestionID);
		
		List<Object[]> list = query.getResultList();
		Map<Long, Double> mapStudentIDScore = new HashMap<>();
		
		list.forEach(objs -> {
			Long seStudentID = Long.parseLong(objs[0].toString());
			Double score = Double.parseDouble(objs[1].toString());
			mapStudentIDScore.put(seStudentID, score);
		});
		
		return mapStudentIDScore;
	}
	
	public static Map<Long, Double> getSeStudentsForSeQuestionDifficulty(long seID, long subjectID) throws RuntimeException
	{
		Map<Long, Double> map = new HashMap<>();

		String sql = "SELECT sss.SeStudentID,sss.Score FROM s_sestudentsubject sss " + "JOIN s_sestudent est ON sss.SeStudentID=est.ID "
				+ "JOIN s_seschool sc ON sc.SchoolID=est.SchoolID AND sc.SeID=est.SeID " + "WHERE est.SeID=?1 AND sss.SubjectID=?2 AND sc.StatisticFlag=?3 AND sss.HaveAnswer=?4";
		Query query = JpaUtils.createNativeQuery(sql.toString());
		query.setParameter(1, seID);
		query.setParameter(2, subjectID);
		query.setParameter(3, 1);
		query.setParameter(4, 1);
		List<Object[]> list = query.getResultList();
		for (Object[] objects : list)
		{
			Long seStudentID = Long.parseLong(objects[0].toString());
			Double score = Double.parseDouble(objects[1].toString());
			map.put(seStudentID, score);
		}

		return map;
	}
	
	private static <T> Map<Double, List<Long>> getSegments4ComputeDifficulty(Map<Long, Double> seScoreMap, double full, boolean subject)
	{
		List<Map<String, Object>> list = new ArrayList<>();
		seScoreMap.forEach((x, y) -> {
			Map<String, Object> map = new HashMap<>();
			map.put("seStudentID", x);
			map.put("score", y);
			list.add(map);
		});
		
		return ComputeUtils.getSegments4ComputeDifficulty(list, x -> (double) x.get("score"), x -> (long) x.get("seStudentID"), full,0, subject);
	}

	private static <T> List<Map<String, Object>> computeDifficulty(Map<Double, List<Long>> segments, Map<Long, Double> studentScoreMap, double fullScore)
	{
		List<Map<String, Object>> list = new ArrayList<>();
		studentScoreMap.forEach((x, y) -> {
			Map<String, Object> map = new HashMap<>();
			map.put("seStudentID", x);
			map.put("score", y);
			list.add(map);
		});
		return ComputeUtils.computeDifficulty(segments, list, x -> (long) x.get("seStudentID"), x -> (double) x.get("score"), fullScore);
	}
}
