package test.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.LambdaUtils;
import uitls.StatisticsUtils;

public class QuestionDiscriminationTest {
	public static void main(String[] args) {
		test(45, 2);
		System.exit(0);
	}

	public static void test(long nsID, long subjectID) {

		long s = System.currentTimeMillis();

		Query query2 = JpaUtils.createNativeQuery("select ID,Score from n_nsrquestion where nsID=" + nsID +" and subjectID="+subjectID +" and length(CLevel)=8 order by ID", HashMap.class);
		List<Map<String,Object>> questions = query2.getResultList();
		
		//注意排序
		Query query1 = JpaUtils.createNativeQuery("select ns.ID,nss.YsScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID=" + nsID +" and nss.subjectID="+subjectID +" and nss.JoinOption=1 and nss.YsScore>0 order by nss.YsScore desc,ns.ExamCode", LinkedHashMap.class);
		List<Map<String,Object>> students = query1.getResultList();
		
		
		Map<Long,Float> studentmap = LambdaUtils.list2map2(students, x->(long)x.get("ID"),  x->(float)x.get("YsScore"));
		
		Map<Long,Double> results= new LinkedHashMap<>();
		
		for(Map<String,Object> map:questions){
			long ID=(long)map.get("ID");
			float full=(float)map.get("Score");
//			if(ID!=15083){
//				continue;
//			}
			Query query = JpaUtils.createNativeQuery("select qs.NsStudentID,qs.Score from n_nsrquestionstudent qs where qs.NsrQuestionID=" + ID , HashMap.class);
			List<Map<String,Object>> result = query.getResultList();
			
			Map<Long,Float> tmp = LambdaUtils.list2map2(result, x->(long)x.get("NsStudentID"),  x->(float)x.get("Score"));
			
			List<Float> list = new ArrayList<>();
			for(Map map0:students){
				long studentID=(long)map0.get("ID");
				if(tmp.containsKey(studentID)){
					list.add(tmp.get(studentID));
				}
			}
			
			double d=StatisticsUtils.discrimination(list, full);
			results.put(ID, d*full);
		}
		
		double total=0;
		
		for(Long ID:results.keySet()){
			total=total+ results.get(ID);
			
		}
		
		System.out.println("科目的区分度由小题计算而来："+total/150);
		
	}
}
