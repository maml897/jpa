package ycj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.ComputeUtils;

public class Test
{
	public static void main(String[] args)
	{
		EntityManager em = JpaUtils.getEm(JpaUtils.dataSources_tx);
		Dao dao = new Dao(em);

		Map<Long, Double> map1 = dao.getSeStudentIdScoreBySeForOrder(10);
		long s1 = System.currentTimeMillis();
		Map<Long, Integer> result1=ComputeUtils.computeOrder(map1);
		System.out.println(System.currentTimeMillis() - s1);
		System.out.println(result1);
		
//		List<Double> list = dao.getSeStudentScoresForOrder(10);
		long s = System.currentTimeMillis();
		Map<Double, Long> result=ComputeUtils.order(map1.values());
		
		Map<Long, Long> map2 = new HashMap<>();
		for(Long id:map1.keySet()){
			map2.put(id, result.get(map1.get(id)));
		}
		
		System.out.println(System.currentTimeMillis() - s);
		System.out.println(map2);
	}

}
