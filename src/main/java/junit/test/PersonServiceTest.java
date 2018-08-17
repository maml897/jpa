package junit.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Test;

public class PersonServiceTest
{
	@Test
	public void testSave()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("myjpa");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		// Person person = new Person();
		// person.setPersonID(1);
		// person.setPersonName("马明亮");
		// em.persist(person);

		// Order order = new Order();
		// order.setOrderID("1");
		// order.setOrderName("mml's order");
		// Person person = new Person();
		// person.setPersonID(1);
		// order.setPerson(person);
		// em.persist(order);

//		Pig pig = new Pig();
//		pig.setPigId("l");
//		pig.setName("f");
//		em.persist(pig);
		
//		Dog dog =new Dog();
//		dog.setDogID("1");
//		dog.setName("lll");
//		em.persist(dog);
		
//		Cat cat =new Cat();
//		cat.setCatID("4");
//		cat.setName("女");
//		em.persist(cat);
		
		Query query = em.createQuery("select c.name, count(c) from Cat c group by c.name having count(*)>?1");
		query.setParameter(1, new Long(1));//直接写1还不行
		List<?> list=query.getResultList();
		for(Object o:list)
		{
			Object[] row = (Object[])o;
			System.out.println(row[0].toString()+"==="+row[1].toString());
		}
		
		
//		Cow cow =new Cow();
//		cow.setCowID("111");
//		cow.setName("mycow");
//		em.persist(cow);

		
//		Cow cow =new Cow();
//		cow.setCowID("111");
//		cow.setName("lllfffcowwwww");
//		em.merge(cow);
		
//		Cow c1 =em.find(Cow.class, "111");
//		System.out.println(em.contains(c1));;//判定该对象是否受em管理，如果受em管理的话，那么将会修改的什么的 将不用merge操作
//		c1.setName("ruabis");
		
		
		em.getTransaction().commit();
	}

}
