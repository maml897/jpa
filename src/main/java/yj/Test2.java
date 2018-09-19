package yj;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.LambdaUtils;

public class Test2
{
	public static void main(String[] args)
	{
		long s = System.currentTimeMillis();

		// 全部
		String sql1 = "SELECT sss.SeStudentID,sss.Score,sss.Except FROM s_sestudentsubject sss " + "JOIN s_sestudent est ON sss.SeStudentID=est.ID "
				+ "JOIN s_seschool sc ON sc.SchoolID=est.SchoolID AND sc.SeID=est.SeID " + "WHERE est.SeID=?1 AND sss.SubjectID=?2 AND sc.StatisticFlag=?3 AND sss.StudentFlag=?4";

		Query query1 = JpaUtils.createNativeQuery(sql1.toString(),HashMap.class);
		query1.setParameter(1, 3664);
		query1.setParameter(2, 45);
		query1.setParameter(3, 1);
		query1.setParameter(4, 1);

		List<Map<String,Object>> list1 = query1.getResultList();
		System.out.println(list1.size());

		
		List<Map<String,Object>> list2=LambdaUtils.filter(list1, x-> {
			float score = (float)x.get("Score");
			int except = (int)x.get("Except");
			return score>0 &&except==0;
		});
		System.out.println(list2.size());
		
		System.out.println(System.currentTimeMillis() - s);
		System.exit(0);
	}
}
