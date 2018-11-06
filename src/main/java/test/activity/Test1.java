package test.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import test.jpa.ComputeScore;
import uitls.LambdaUtils;

public class Test1 {

	//排名验证
	public static void main(String[] args) {
		
		Query query = JpaUtils.createNativeQuery(
				"select nss.ID,nss.Score from n_nsstudent nss where nsID=2655 order by Score desc",
				LinkedHashMap.class);
		List<Map<String, Object>> students = query.getResultList();
		
		
		List<Map<String,Object>> orders = ComputeScore.compute(students, x->(float)x.get("Score"));
		Map<Float,Map<String,Object>> ordersMap= LambdaUtils.list2map(orders, x->(float)x.get("score"));
		
		for(Map<String, Object> student:students){
			long id = (long)student.get("ID");
			float score = (float)student.get("Score");
			
			int order = (int)ordersMap.get(score).get("order");
			int forder = getTotalOrderByScore(2655, score);
			if(order==forder){
				System.out.println(id+"=="+score+"==="+order+"=="+forder);
			}
			else{
				System.out.println("--------------");
			}
		}
		
		System.out.println("okok");
	}

	
	public static int getTotalOrderByScore(long nsID, float score) throws RuntimeException
	{
		Query query = JpaUtils.createNativeQuery("select count(0) from n_nsstudent where NsID=?1 and Score>?2");
		query.setParameter(1, nsID);
		query.setParameter(2, score+0.005f);
		return Integer.parseInt(query.getSingleResult().toString()) + 1;
	}
	
}
