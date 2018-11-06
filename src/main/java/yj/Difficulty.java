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

public class Difficulty
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

		double score = Double.parseDouble(JpaUtils.createNativeQuery("SELECT Score  FROM s_se where ID=" + seID).getSingleResult().toString());

		Map<Long, Double> studentScoreMap = getSeStudentIdScoreBySeForOrder(seID);// 学生总分
		Map<Double, List<Long>> scoreStudentIDs = getSegments4ComputeDifficulty(studentScoreMap, score, true,seID);

		List<Map<String, Object>> resultAll = new ArrayList<>();
		for (Map<String, Object> subject : subjects)
		{
			if ((float) subject.get("Score") == 0)
			{
				continue;
			}
			Map<Long, Double> subjectStudentScoreMap = getSeStudentIdScoreBySeSubjectForOrder(seID, (long) subject.get("SubjectID"));// 科目学生得分

			//计算 结果
			List<Map<String, Object>> computeResult = computeDifficulty(scoreStudentIDs, subjectStudentScoreMap, (float) subject.get("Score"));

			//查询阅卷结果
			List<Map<String, Object>> results = JpaUtils
					.createNativeQuery("SELECT * FROM s_sesubjectdifficulty where seID=" + seID + " and SubjectID=" + (long) subject.get("SubjectID"), HashMap.class).getResultList();

			
			Map<Float,Map<String, Object>> resultsMap=LambdaUtils.list2map2(results, x->(float)x.get("Score"));
			
			
			for(Map<String, Object> cr:computeResult){
				Double sc = (Double)cr.get("Score");
				Double d =  round((Double)cr.get("DifficultyIndex"));
				
				
				double cd = round((float) resultsMap.get(sc.floatValue()).get("DIndex"));
				
				if(sc!=10&&d!=cd){
					System.out.println(seID + "!=" + subject.get("SubjectID")+"="+sc);
					System.out.println(d+"!="+cd);
					System.exit(0);
				}
			}
			
			
			if (computeResult.size() != results.size())
			{
				System.out.println(seID + "!=" + subject.get("SubjectID"));
				System.out.println(computeResult.size() + "!=" + results.size());
				System.exit(0);
			}

		}
		// insert(computeService, Tool.getInsertSqls("s_sesubjectdifficulty", resultAll));
	}
	
	public static double round(Object v)
	{
		BigDecimal b = new BigDecimal(v.toString());
		return b.divide(new BigDecimal("1"), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static Map<Long, Double> getSeStudentIdScoreBySeSubjectForOrder(long seID, long subjectID) throws RuntimeException
	{
		Map<Long, Double> map = new HashMap<>();

		String sql = "SELECT sss.SeStudentID,sss.Score FROM s_sestudentsubject sss " + "JOIN s_sestudent est ON sss.SeStudentID=est.ID "
				+ "JOIN s_seschool sc ON sc.SchoolID=est.SchoolID AND sc.SeID=est.SeID " + "WHERE est.SeID=?1 AND sss.SubjectID=?2 AND sc.StatisticFlag=?3 AND sss.StudentFlag=?4";
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

	public static Map<Long, Double> getSeStudentIdScoreBySeForOrder(long seID) throws RuntimeException
	{
		Map<Long, Double> mapStudentIDScore = new HashMap<>();

		StringBuffer sql = new StringBuffer("select us.ID,us.Score from s_sestudent us join s_seschool s");
		sql.append(" on us.SeID = s.SeID and us.SchoolID = s.SchoolID and us.SeID=?1");
		sql.append(" and s.StatisticFlag=?2");

		Query query = JpaUtils.createNativeQuery(sql.toString());
		query.setParameter(1, seID);
		query.setParameter(2, 1);

		List<Object[]> list = query.getResultList();
		for (Object[] objects : list)
		{
			Long seStudentID = Long.parseLong(objects[0].toString());
			Double score = Double.parseDouble(objects[1].toString());
			mapStudentIDScore.put(seStudentID, score);
		}
		return mapStudentIDScore;
	}

	private static <T> Map<Double, List<Long>> getSegments4ComputeDifficulty(Map<Long, Double> seScoreMap, double full, boolean subject,long seID)
	{
		List<Map<String, Object>> list = new ArrayList<>();
		seScoreMap.forEach((x, y) -> {
			Map<String, Object> map = new HashMap<>();
			map.put("seStudentID", x);
			map.put("score", y);
			list.add(map);
		});
		
		return ComputeUtils.getSegments4ComputeDifficulty(list, x -> (double) x.get("score"), x -> (long) x.get("seStudentID"), full,seID, subject);
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
