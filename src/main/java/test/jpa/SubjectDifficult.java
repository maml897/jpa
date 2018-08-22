package test.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;

/**
 * 学科难度
 * @author maml
 *
 */
public class SubjectDifficult {

	public static void main(String[] args) {
		test(45, 2, 20);
	}

	@SuppressWarnings("unchecked")
	public static void test(long nsID, long subjectID, float step) {
		Query query =JpaUtils.createNativeQuery("select * from n_ns where ID=" + nsID, HashMap.class);
		Map<String,Object> ns =(Map<String,Object>)query.getSingleResult();
		
		long s =System.currentTimeMillis();
		
		s =System.currentTimeMillis();
		Query query2 = JpaUtils.createNativeQuery("select ns.ID,ns.Score,nss.YsScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID=" + nsID +" and nss.subjectID="+subjectID +" and nss.JoinOption=1 and nss.Score>0", HashMap.class);
		List<Map<String,Object>> nsStudentSubjects = query2.getResultList();
//		System.out.println(nsStudentSubjects);
		System.out.println(System.currentTimeMillis()-s);
		
		s =System.currentTimeMillis();
		float score=(float)ns.get("Score");
		while(score>0){
			 Map<Float,Double> msp=subjectDifficult(nsStudentSubjects, score,step, 150);//150是学科满分
			 System.out.println(msp);
			 score=score-step;
		}
		System.out.println(System.currentTimeMillis()-s);
	}
	
	//nsStudents：统计学生
	public static Map<Float,Double> subjectDifficult(List<Map<String,Object>> nsStudents, float scorePonit,float step, float subjectFull)
	{
		Map<Float,Double> map =new HashMap<>();
		map.put(scorePonit, 0d);
		try
		{
			double average = nsStudents.stream().filter(x->{
				float studentScore = (float)x.get("Score");//学生总分
				return (studentScore<=scorePonit&&studentScore>(scorePonit-step));
				
			}).mapToDouble(x->(float)x.get("YsScore")).average().getAsDouble();//学生单科成绩
			double result=(float)average/subjectFull;
			map.put(scorePonit, result);
		}
		catch (Exception e)
		{
			
		}
		return map;
	}
	
	public static Map<Float,Double> compute(List<Map<String,Object>> nsStudents, float scorePonit,float step, float subjectFull)
	{
		Map<Float,Double> map =new HashMap<>();
		map.put(scorePonit, 0d);
		try
		{
			double average = nsStudents.stream().filter(x->{
				float studentScore = (float)x.get("Score");//学生总分
				return (studentScore<=scorePonit&&studentScore>(scorePonit-step));
				
			}).mapToDouble(x->(float)x.get("YsScore")).average().getAsDouble();//学生单科成绩
			double result=(float)average/subjectFull;
			map.put(scorePonit, result);
		}
		catch (Exception e)
		{
			
		}
		return map;
	}
	
	
}
