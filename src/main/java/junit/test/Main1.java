package junit.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQueries;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Person;

import org.junit.Test;

public class Main1
{
	@Test
	public void testSave()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("myjpa");
		EntityManager em = emf.createEntityManager();

		// em.getTransaction().begin();
		//		
		// Person person =new Person();
		// person.setPersonID(2);
		// person.setPersonName("maml");
		// em.persist(person);
		//		
		// em.getTransaction().commit();

		// @NamedQueries用法
		// Query query =
		// em.createNamedQuery("getPersonList");//createNamedQuery的用法
		// Object o= query.getResultList().get(0);
		// Person p =(Person)o;
		// System.out.println(p.getPersonName());

		// 排序操作
		// Query query
		// =em.createQuery("select p from Person p order by p.personID desc");
		// List<Person> list=query.getResultList();
		// for(Person person:list)
		// {
		// System.out.println(person.getPersonID()+"----"+person.getPersonName());
		// }

		// 查询部分属性,返回的对象是以Objet[]存在的
		// Query
		// query=em.createQuery("select p.personID, p.personName from Person p order by p.personID desc");
		// List<Object[]> list = query.getResultList();
		// for(Object[] os:list)
		// {
		// System.out.println(os[0].toString()+"----"+os[1].toString());
		// }

		// 查询中使用构造器,由于没有查询name所以 name为null
		// Query query =
		// em.createQuery("select new model.Person(p.personID) from Person p order by p.personID desc");
		// List<Person> list=query.getResultList();
		// for(Person person:list)
		// {
		// System.out.println(person.getPersonID()+"----"+person.getPersonName());
		// }

		// 一下还是聚合查询

		// Query query =em.createQuery("select max(p.personID) from Person p");
		// System.out.println(query.getSingleResult().toString());

		// Query query =em.createQuery("select min(p.personID) from Person p");
		// System.out.println(query.getSingleResult().toString());

		// Query query =em.createQuery("select avg(p.personID) from Person p");
		// System.out.println(query.getSingleResult().toString());

		// Query query =em.createQuery("select sum(p.personID) from Person p");
		// System.out.println(query.getSingleResult().toString());

		// Query query =em.createQuery("select count(p) from Person p");
		// System.out.println(query.getSingleResult().toString());

		Query query = em.createQuery("select c.name,sum(c.catID) from Cat c group by c.name having sum(c.catID)>?1");
		query.setParameter(1, "4");
		List<Object[]> list = query.getResultList();
		for (Object[] os : list)
		{
			System.out.println(os[0].toString()+"==="+os[1].toString());
		}
	}
}
