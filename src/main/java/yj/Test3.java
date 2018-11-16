package yj;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;

public class Test3
{
	public static void main(String[] args)
	{
		Query query2 = JpaUtils.createNativeQuery("select ID,Number from s_se",HashMap.class);
		List<Map<String, Object>> list = query2.getResultList();
		
		System.out.println(list.size());
		System.exit(0);
	}
}
