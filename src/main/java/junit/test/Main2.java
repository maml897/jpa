package junit.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Order;

import org.junit.Test;

public class Main2
{
	@Test
	public void testSave()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("myjpa");
		EntityManager em = emf.createEntityManager();

//		em.getTransaction().begin();
//		Order order =em.find(Order.class, "2");
//		Person person =new Person();
//		person.setPersonID(1);
//		order.setPerson(person);
//		em.merge(order);
//		em.getTransaction().commit();
		
		
		Order order=em.find(Order.class, "1");
		System.out.println(order.getOrderName());
//		System.out.println(order.getPerson().getPersonName());
		
		
		
		
	}
}
