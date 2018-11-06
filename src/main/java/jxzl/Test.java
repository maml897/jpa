package jxzl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import config.jpa.JpaUtils;

public class Test
{
	public static void main(String[] args)
	{
		
		EntityManager queryWish=JpaUtils.getEm();
//		
//		
		Query query = queryWish.createNativeQuery("SELECT score FROM s_sestudent WHERE seID=2 AND SchoolID=1 AND score>0 AND CExcept=0");
		
		List<Double> list= query.getResultList();
		
		double d=list.stream().mapToDouble(x->x).average().orElse(0);
		System.out.println(d);
//		
//		queryWish.getTransaction().begin();
//		Query query1 = queryWish.createNativeQuery("update s_sedistrict set Average= 211.625 where ID=64");
//		query1.executeUpdate();
//		queryWish.getTransaction().commit();
		System.out.println(round(d));
	}
	
	public static double round(double v)
	{
		BigDecimal b = new BigDecimal(Double.toString(v));
		return b.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
