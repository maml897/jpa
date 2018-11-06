package test.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import test.jpa.ComputeScore;
import uitls.LambdaUtils;

public class Test2 {

	//段次验证
	public static void main(String[] args) {
		
		Query query = JpaUtils.createNativeQuery(
				"select nss.ID,nss.Score from n_nsstudent nss where nsID=2655 order by Score desc",
				LinkedHashMap.class);
		List<Map<String, Object>> students = query.getResultList();
		
		
		Map<Long,Integer> map= LambdaUtils.list2map(students, x->(Long)x.get("ID"), x->{
			float score =(float)x.get("Score");
			if(score>=572){
				return 1;
			}
			if(score>=450){
				return 2;
			}
			if(score>=253){
				return 3;
			}
			return 0;
		});
		
		
		for(Map<String, Object> student:students){
			long id = (long)student.get("ID");
			float score = (float)student.get("Score");
			
			int part=map.get(id);
			
			
			long userID=getUserID(id);
			int fpart=getStudentLine(2655, userID);
			
			
			System.out.println(id+"=="+score+"==="+part+"=="+fpart);
			if(fpart!=0 && part!=fpart){
				break;
			}
		}
		
		System.out.println("okok");
	}
	
	
	public static long getUserID(long nsStudentID) throws RuntimeException
	{
		StringBuffer sql = new StringBuffer("SELECT stu.StudentID FROM n_stunsstudent stu");
		sql.append(" where stu.NsStudentID="+nsStudentID);
		Query query = JpaUtils.createNativeQuery(sql.toString());
		
		List<Object> list = query.setMaxResults(1).getResultList();
		for (Object object : list)
		{
			return Long.parseLong(object.toString());
		}
		return 0;
	}
	
	
	public static int getStudentLine(long nsID, long userID) throws RuntimeException
	{
		float nsStudentScore =getNsStudentByStudent(nsID, userID);
		
		if (nsStudentScore > (572 - 0.005f))
		{
			return 1;
		}
		else if (nsStudentScore > (450 - 0.005f))
		{
			return 2;
		}
		else if (nsStudentScore > (253 - 0.005f))
		{
			return 3;
		}
		return 0;
	}
	
	
	public static float getNsStudentByStudent(long nsID, long userID) throws RuntimeException
	{
		StringBuffer sql = new StringBuffer("SELECT ns.Score FROM n_stunsstudent stu");
		sql.append(" JOIN n_nsstudent ns ON stu.NsStudentID=ns.ID and stu.StudentID=?1");
		sql.append(" and ns.NsID=?2");
		Query query = JpaUtils.createNativeQuery(sql.toString());
		query.setParameter(1, userID);
		query.setParameter(2, nsID);
		
		List<Object> list = query.setMaxResults(1).getResultList();
		for (Object object : list)
		{
			return Float.parseFloat(object.toString());
		}
		return 0;
	}
	
	
}
