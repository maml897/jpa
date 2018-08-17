package junit.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import n21.Forum;
import n21.ForumGroup;

import org.junit.Test;

public class Main3
{
	@Test
	public void testSave()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("myjpa");
		EntityManager em = emf.createEntityManager();

//		em.getTransaction().begin();
//		ForumGroup forumGroup =new ForumGroup();
//		forumGroup.setForumGroupID("1");
//		Forum forum =new Forum();
//		forum.setForumID("1");
//		forum.setGroup(forumGroup);
//		em.persist(forum);
//		em.getTransaction().commit();
		
		
		ForumGroup forumGroup =em.find(ForumGroup.class, "1");
		System.out.println(forumGroup.getForumGroupID());
		
		for(Forum f:forumGroup.getForums())
		{
			System.out.println(f.getForumID()+"--"+f.getUser().getName());
		}
		
//		Query query =em.createQuery("select fg from ForumGroup fg join fetch fg.forums f join fetch f.user where fg.forumGroupID=?1");
//		query.setParameter(1, "1");
//		ForumGroup forumGroup = (ForumGroup)query.getResultList().get(0);
//		for(Forum f:forumGroup.getForums())
//		{
//			System.out.println(f.getForumID() + "--" + f.getUser().getName());
//		}
		
	}
}
