package yj;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;

public class Test
{
	public static void main(String[] args)
	{
		Query query2 = JpaUtils.createNativeQuery("select ID,Number from s_se",HashMap.class);
		List<Map<String, Object>> list = query2.getResultList();
		
		for(Map<String, Object> map:list){
			long id=(long)map.get("ID");
			int number=(int)map.get("Number");
			
			
			Query query = JpaUtils.createNativeQuery("select count(0) from s_sestudent where seID="+id);
			BigInteger count = (BigInteger)query.getSingleResult();
			if(count.intValue()!=number){
				System.out.println(id+"=="+number+"=="+count.intValue());
			}
		}
		
		System.exit(0);
	}
}
