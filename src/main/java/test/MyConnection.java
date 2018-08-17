package test;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 以下涉及到jndi的 都得在servlet环境下运行。
 * @author maml
 *
 */
public class MyConnection
{
	
	//c3p0 连接池：不用jndi，普通连接
	public static Connection getConnectionC3P01() throws Exception
	{
		ComboPooledDataSource comboPooledDataSource =new ComboPooledDataSource();
		comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
		comboPooledDataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/jpa?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf8");
		comboPooledDataSource.setUser("root");
		comboPooledDataSource.setPassword("mysql");
		comboPooledDataSource.setMinPoolSize(1);
		comboPooledDataSource.setMaxPoolSize(10);
		comboPooledDataSource.setInitialPoolSize(1);
		comboPooledDataSource.setMaxIdleTime(21600);
		Connection connection=comboPooledDataSource.getConnection();
		return connection;
	}
	
	
	//普通数据库链接
	public static Connection getConnection1()
	{
		Connection con;
		try
		{
			//自动检测驱动包，进而知道使用何种数据库
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jpa?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf8", "root", "mysql");
			return con;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
