package ycj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;

public class Test
{
	public static void main(String[] args)
	{
		Query query = JpaUtils.createNativeQuery("select * from t_user limit 1", HashMap.class, JpaUtils.dataSources_yj);
		List<Map<String, Object>> list = query.getResultList();
		for (Map<String, Object> map : list)
		{
			map.forEach((x, y) -> {
				System.out.println(x + "===" + y);
				if (y == null)
				{
				}
			});
		}

	}

}
