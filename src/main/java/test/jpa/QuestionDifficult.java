package test.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小题难度
 * 
 * @author maml
 *
 */
public class QuestionDifficult {
	public static void main(String[] args) {

	}
	
	
	public static Map<Float,Double> subjectDifficult(List<Map<String,Object>> nsStudents, float scorePonit, float subjectFull)
	{
		Map<Float,Double> map =new HashMap<>();
		map.put(scorePonit, 0d);
		try
		{
			double average = nsStudents.stream().filter(x->{
				float studentScore = (float)x.get("Score");//学生总分
				return (studentScore<=scorePonit&&studentScore>(scorePonit-20));
				
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
