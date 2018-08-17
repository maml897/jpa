package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		Connection con = MyConnection.getConnectionC3P01(); //此处替换成MyConnection里面的各种获取Connection的方法测试
		Statement s;
		try
		{
			s = con.createStatement();
			ResultSet rs =s.executeQuery("select * from model");
			while (rs.next()) {        
                System.out.print(rs.getString(1) + "\t");  
                System.out.println(rs.getString(2) + "\t");  
          }   
			
			rs.close();
			s.close();
			con.close(); 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		
	}
}
