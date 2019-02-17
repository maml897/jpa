package config.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.jpa.HibernatePersistenceProvider;

public class JpaUtils
{

	private static EntityManager em;

	public static Query createNativeQuery(String sqlString, Class<?> cls, Map<String,String>... datasource)
	{
		EntityManager em = getEm(datasource);
		Query query = em.createNativeQuery(sqlString);
		SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
		WishAliasToBeanResultTransformer transformer = new WishAliasToBeanResultTransformer(cls);
		sqlQuery.setResultTransformer(transformer);

		return query;
	}

	public static Query createNativeQuery(String sqlString, Map<String,String>... datasource)
	{
		EntityManager em = getEm(datasource);
		Query query = em.createNativeQuery(sqlString);
		return query;
	}

	public static EntityManager getEm(Map<String,String>... datasource)
	{
		if (em == null)
		{
			EntityManagerFactory emf = null;
			if (datasource.length > 0)
			{
				emf =getEntityManagerFactory(datasource[0]);
			}
			else
			{
				emf = Persistence.createEntityManagerFactory("myjpa");
			}
			em = emf.createEntityManager();
		}
		return em;
	}

	public static EntityManagerFactory getEntityManagerFactory(Map<String, String> map)
	{
		map.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		map.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		map.put("hibernate.hbm2ddl.auto", "validate");
		if(!map.containsKey("hibernate.connection.username")){
			map.put("hibernate.connection.username", "root");
		}
		if(!map.containsKey("hibernate.connection.password")){
			map.put("hibernate.connection.password", "mysql");
		}
		HibernatePersistenceProvider pp = new HibernatePersistenceProvider();
		return pp.createEntityManagerFactory("myjpa", map);
	}
	
	
	public static Map<String,String> dataSources_ycj =new HashMap<>();
	
	static{
		dataSources_ycj.put("hibernate.connection.url", "jdbc:mysql://172.16.0.129:3306/ytw?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf8");
	}
	
	public static Map<String,String> dataSources_yj =new HashMap<>();
	
	static{
		dataSources_yj.put("hibernate.connection.url", "jdbc:mysql://172.16.0.129:3306/pingjuan?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf8");
	}
}
