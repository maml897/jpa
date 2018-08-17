package junit.test;

import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import n21.Forum;

import org.junit.Test;

public class Main4
{
	@Test
	public void testSave() throws Exception
	{
		Map<String,String> map =new LinkedHashMap<String, String>();
		
		//以下设置覆盖persistence.xml的内容
//		map.put("hibernate.format_sql","true");
//		map.put("hibernate.show_sql","false");
		
		
		EntityManagerFactory emf = MyPersistence.createEntityManagerFactory("myjpa",map);
		EntityManager em = emf.createEntityManager();

//		Query query = em.createQuery("select p from Person p where not(p.personID=?1) order by p.personID");
//		Query query = em.createQuery("select p from Person p where p.personID between 1 and 10");
//		Query query = em.createQuery("select p from Person p where p.personID in(1,10,11)");
//		Query query = em.createQuery("select p from Person p where p.personName like 'maml%'");
//		Query query = em.createQuery("select f from Forum f where f.user is not null order by f.forumID");
//		Query query = em.createQuery("select u from ForumGroup u where u.forums is not empty");
//		Query query = em.createQuery("select f from Forum f where not exists (select fg from ForumGroup fg where fg.forumGroupID=3)");
//		Query query = em.createQuery("select concat(p.personName, '_foshan') from Person p");
		Query query = em.createQuery("select f from Forum f where f.group not in(select fg from ForumGroup fg where fg.forumGroupID =3)");
		
		List<Forum> list =query.getResultList();
		
		for(Forum person:list)
		{
			System.out.println(person.getForumID());
		}
		
		
		
		Enumeration<URL> xmls = Thread.currentThread().getContextClassLoader().getResources( "META-INF/persistence.xml" );
		
		System.out.println(xmls.nextElement());
		
		
	}
}
