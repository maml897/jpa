package test.jpa;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Query;

import config.jpa.JpaUtils;

public class SubjectScore {
	public static void main(String[] args) {
		test(45, 2);
		System.exit(0);
	}
	
	public static void test(long nsID, long subjectID) {
		
		long s =System.currentTimeMillis();
		
		Query query2 = JpaUtils.createNativeQuery("select ns.ID,ns.Score,nss.YsScore from n_nsstudentsubject nss join n_nsstudent ns on nss.NsStudentID=ns.ID and ns.nsID=" + nsID +" and nss.subjectID="+subjectID +" and nss.JoinOption=1 and nss.YsScore>0 order by nss.YsScore desc", HashMap.class);
		List<Map<String,Object>> nsStudentSubjects = query2.getResultList();
		
		System.out.println(nsStudentSubjects.size());
		Map<Float, Long> group2 = nsStudentSubjects.stream().collect(Collectors.groupingBy(t -> (float)t.get("YsScore"), LinkedHashMap::new, Collectors.counting()));
		
//		System.out.println(group2);
//		System.out.println(group2.get(1.5f));
		
		long lastOrder =1;
		long lastCount = 0;
		
		for(float ysScore:group2.keySet()){
			
			long order = lastOrder+lastCount;
			System.out.println(ysScore+"=="+group2.get(ysScore)+"==="+order);
			lastOrder = order;
			lastCount = group2.get(ysScore);
		}
		
		System.out.println(System.currentTimeMillis()-s);
	}
}
