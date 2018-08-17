package config.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

/**
 * 公共的Query包装器，统一使用这个进行dao数据库的操作
 * @author lifw
 *
 */
@Component
public class QueryWish {

	@PersistenceContext
	private EntityManager em;
	
	public EntityManager getEntityManager()
	{
		return em;
	}
	
	public Query createQuery(String qlString){
		return em.createQuery(qlString);
	}
	
	public Query createNativeQuery(String sqlString){
		return em.createNativeQuery(sqlString);
	}
	
	public Query createNativeQuery(String sqlString,Class<?> cls){
		Query query = em.createNativeQuery(sqlString);
		
		SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
		WishAliasToBeanResultTransformer transformer = new WishAliasToBeanResultTransformer(cls);
		sqlQuery.setResultTransformer(transformer);
		
		return query;
	}
	
	/**
	 * em.find()，如果返回null 自动new出来
	 * @param classType
	 * @param primaryKey
	 * @return
	 */
	public <T>T find(Class<T> classType, Object primaryKey)
	{
		T find = em.find(classType, primaryKey);
		try
		{
			find = find==null?classType.newInstance():find;
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			
		}
		return find;
	}
}
