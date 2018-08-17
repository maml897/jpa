package config.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.SQLQuery;

public class JpaUtils {

	private static EntityManager em;

	public static Query createNativeQuery(String sqlString, Class<?> cls) {
		EntityManager em = getEm();
		Query query = em.createNativeQuery(sqlString);
		SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
		WishAliasToBeanResultTransformer transformer = new WishAliasToBeanResultTransformer(cls);
		sqlQuery.setResultTransformer(transformer);

		return query;
	}

	public static Query createNativeQuery(String sqlString) {
		EntityManager em = getEm();
		Query query = em.createNativeQuery(sqlString);
		return query;
	}

	public static EntityManager getEm() {
		if (em == null) {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("myjpa");
			em = emf.createEntityManager();
		}
		return em;
	}
}
