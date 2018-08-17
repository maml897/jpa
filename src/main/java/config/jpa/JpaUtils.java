package config.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.SQLQuery;

public class JpaUtils {

	public static Query createNativeQuery(String sqlString, Class<?> cls) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("myjpa");
		EntityManager em = emf.createEntityManager();

		Query query = em.createNativeQuery(sqlString);

		SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
		WishAliasToBeanResultTransformer transformer = new WishAliasToBeanResultTransformer(cls);
		sqlQuery.setResultTransformer(transformer);

		return query;
	}
	
	public static Query createNativeQuery(String sqlString) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("myjpa");
		EntityManager em = emf.createEntityManager();
		Query query = em.createNativeQuery(sqlString);
		return query;
	}
}
