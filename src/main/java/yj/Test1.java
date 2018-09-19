package yj;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;

public class Test1
{
	public static void main(String[] args)
	{
		long s = System.currentTimeMillis();

		// 全部
		String sql1 = "SELECT sss.SeStudentID,sss.Score FROM s_sestudentsubject sss " + "JOIN s_sestudent est ON sss.SeStudentID=est.ID "
				+ "JOIN s_seschool sc ON sc.SchoolID=est.SchoolID AND sc.SeID=est.SeID " + "WHERE est.SeID=?1 AND sss.SubjectID=?2 AND sc.StatisticFlag=?3 AND sss.StudentFlag=?4";

		Query query1 = JpaUtils.createNativeQuery(sql1.toString());
		query1.setParameter(1, 3664);
		query1.setParameter(2, 45);
		query1.setParameter(3, 1);
		query1.setParameter(4, 1);

		List<Object[]> list1 = query1.getResultList();
		System.out.println(list1.size());

		// 统计人数
		StringBuilder sql = new StringBuilder(
				"SELECT uss.SeStudentID,uss.Score FROM s_sestudentsubject uss " + "JOIN s_sestudent us ON uss.SeStudentID=us.ID AND us.SeID=?1 AND uss.SubjectID=?2 AND uss.StudentFlag=?3 "
						+ "JOIN s_seschool s ON s.SeID=us.SeID AND s.SchoolID=us.SchoolID AND s.StatisticFlag=?4 ");
		sql.append(" and uss.`Except`=?5");

		Query query = JpaUtils.createNativeQuery(sql.toString());
		query.setParameter(1, 3664);
		query.setParameter(2, 45);
		query.setParameter(3, 1);
		query.setParameter(4, 1);
		query.setParameter(5, 0);

		List<Object[]> list = query.getResultList();
		
		Map<Long, Float> map = new HashMap<>();
		for (Object[] objects : list) {
			Long seStudentID = Long.parseLong(objects[0].toString());
			Float score = Float.parseFloat(objects[1].toString());
			map.put(seStudentID, score);
		}
		System.out.println(list.size());
		
		
		System.out.println(System.currentTimeMillis() - s);
		System.exit(0);
	}
}
