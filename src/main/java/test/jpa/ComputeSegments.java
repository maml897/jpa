package test.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import uitls.ComputeUtils;

public class ComputeSegments {

	public static void main(String[] args) {
		compute(45, 6);
		System.exit(0);
	}
	
	
	public static void compute(long nsID, long subjectID) {
		long s = System.currentTimeMillis();
		Query query2 = JpaUtils.createNativeQuery(
				"select Score,CCount from n_nsrsubjectscore nss where NsID="
						+ nsID + " and SubjectID=" + subjectID,HashMap.class);
		
		List<Map<String,Object>> result = query2.getResultList();

		List<Double> steps =new ArrayList<>();
		for(double d=100;d>=0;d--){
			steps.add(d);
		}
		
		Map<Double,?> map=ComputeUtils.computeSegments(steps, result, x->Double.parseDouble(x.get("Score").toString()), x->(Integer)x.get("CCount"), true);

		map.forEach((x,y)->{
			System.out.println(x+"=="+y);
		});
		
		System.out.println(System.currentTimeMillis()-s);
	}
}
