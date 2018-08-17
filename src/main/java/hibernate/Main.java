package hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main
{
	public static void main(String[] args)
	{
		Configuration configuration =new Configuration();
		
		configuration.addClass(hibernate.Model.class);
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.setProperty("hibernate.connection.username", "root");
		configuration.setProperty("hibernate.connection.password", "mysql");
		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://127.0.0.1:3306/jpa?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf8");
		configuration.setProperty("hibernate.max_fetch_depth", "3");
		configuration.setProperty("hibernate.hbm2ddl.auto", "update");
		configuration.setProperty("hibernate.show_sql", "true");
		
		
		SessionFactory sessions = configuration.buildSessionFactory();
		Session session = sessions.openSession(); // open a new Session
		
		
		session.getTransaction().begin();
		Model model =new Model();
		model.setModelID("mymodel");
		model.setModelName("good");
		
		session.persist(model);
		session.getTransaction().commit();
	}
}
