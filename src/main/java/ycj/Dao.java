package ycj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class Dao
{

	private EntityManager em;

	public Dao(EntityManager em)
	{
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	public Map<Long, Double> getSeStudentIdScoreBySeForOrder(long seID) throws RuntimeException
	{
		Map<Long, Double> mapStudentIDScore = new HashMap<>();

		StringBuffer sql = new StringBuffer("select us.ID,us.Score from s_sestudent us join s_seschool s");
		sql.append(" on us.SeID = s.SeID and us.SchoolID = s.SchoolID and us.SeID=?1");
		sql.append(" and s.StatisticFlag=?2");

		Query query = em.createNativeQuery(sql.toString());
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
	
	@SuppressWarnings("unchecked")
	public List<Double> getSeStudentScoresForOrder(long seID) throws RuntimeException
	{

		StringBuffer sql = new StringBuffer("select us.Score from s_sestudent us join s_seschool s");
		sql.append(" on us.SeID = s.SeID and us.SchoolID = s.SchoolID and us.SeID=?1");
		sql.append(" and s.StatisticFlag=?2");

		Query query = em.createNativeQuery(sql.toString());
		query.setParameter(1, seID);
		query.setParameter(2, 1);

		List<Object> list = query.getResultList();
		List<Double> result=new ArrayList<>();
		for (Object object : list)
		{
			Double score = Double.parseDouble(object.toString());
			result.add( score);
		}
		return result;
	}
}
