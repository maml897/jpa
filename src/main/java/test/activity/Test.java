package test.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import config.jpa.JpaUtils;

public class Test {

	public static void main(String[] args) {
		long s=System.currentTimeMillis();
		exe(2017,4876, 2, new String[] {"政治","历史","地理","物理","化学","生物","技术"});
		System.out.println(System.currentTimeMillis()-s);
		System.exit(0);
	}

	/**
	 * 排名
	 * 
	 * @param ranking
	 * @param part
	 * @param sub
	 */
	public static void exe(int year,int ranking, int part, String[] sub) {
		List<Object> s_list_1_1 = null;// 同位次、第1段、第1部分
		List<Object> s_list_1_2 = null;// 同位次、第1段、第2部分
		List<Object> s_list_2_1 = null;// 同位次、第2段、第1部分
		List<Object> s_list_2_2 = null;// 同位次、第2段、第2部分
		List<Object> s_list_3_1 = null;// 同位次、第3段、第1部分
		List<Object> s_list_3_2 = null;// 同位次、第3段、第2部分

		List<Object> u_list_1 = null;// 高位次、第1段
		List<Object> u_list_2 = null;// 高位次、第2段
		List<Object> u_list_3 = null;// 高位次、第3段

		List<Object> d_list_1 = null;// 低位次、第1段
		List<Object> d_list_2 = null;// 低位次、第2段
		List<Object> d_list_3 = null;// 低位次、第3段

		int list_size = 30;// 每个列表最多30条

		int s_list_1_1_size = 0;
		int s_list_1_2_size = 0;
		int s_list_2_1_size = 0;
		int s_list_2_2_size = 0;
		int s_list_3_1_size = 0;
		int s_list_3_2_size = 0;

		int u_list_1_size = 0;
		int u_list_2_size = 0;
		int u_list_3_size = 0;

		int d_list_1_size = 0;
		int d_list_2_size = 0;
		int d_list_3_size = 0;

		int line_2018_1 = 588;// 2018年高考1段分数线
		int line_2018_2 = 490;// 2018年高考2段分数线
		int line_2018_3 = 344;// 2018年高考3段分数线
		
		if(year==2017){//修改
			line_2018_1 = 577;// 2017年高考1段分数线
			line_2018_2 = 480;// 2017年高考2段分数线
			line_2018_3 = 359;// 2017年高考3段分数线
		}

		int m_type = 0;// 同位次标识
		int u_type = 1;// 高位次标识
		int d_type = -1;// 低位次标识
		
		int part_1 = 1;// 第1段标识
		int part_2 = 2;// 第2段标识
		int part_3 = 3;// 第3段标识
		
		int diff = 10;// 查询分数步长

		int score = 483;
		
		if (part == part_1) {// 第一段考生
			/******************** 同位次一段 *******************/
			if (score < line_2018_1) {
				s_list_1_1 = new ArrayList<>(0);
				s_list_1_2 = new ArrayList<>(0);
			} else {
				s_list_1_1 = getLuquList(year, part_1, score, sub, m_type, diff, 1, list_size);
				s_list_1_1_size = s_list_1_1.size();
				if (s_list_1_1_size < list_size) {
					s_list_1_2 = getLuquList(year, part_1, score, sub, d_type, diff, 1, list_size - s_list_1_1_size);
					s_list_1_2_size = s_list_1_2.size();
				} else {
					s_list_1_2 = new ArrayList<>(0);
				}
			}

			/******************** 同位次二段 *******************/
			s_list_2_1 = getLuquList(year, part_2, score, sub, m_type, diff, 1, list_size);
			s_list_2_1_size = s_list_2_1.size();
			s_list_2_2 = new ArrayList<>();

			/******************** 同位次三段 *******************/
			s_list_3_1 = getLuquList(year, part_3, score, sub, m_type, diff, 1, list_size);
			s_list_3_2 = new ArrayList<>();

			
			/******************** 高位次一段 *******************/
			u_list_1 = getLuquList(year, part_1, score, sub, u_type, diff, 1, list_size);
			u_list_1_size = u_list_1.size();

			/******************** 高位次二段 *******************/
			u_list_2 = getLuquList(year, part_2, score, sub, u_type, diff, 1, list_size);
			u_list_2_size = u_list_2.size();

			/******************** 高位次三段 *******************/
			u_list_3 = getLuquList(year, part_3, score, sub, u_type, diff, 1, list_size);
			u_list_3_size = u_list_3.size();

			
			/******************** 低位次一段 *******************/
			d_list_1 = getLuquList(year, part_1, score, sub, d_type, diff, s_list_1_2_size + 1, list_size);
			d_list_1_size = d_list_1.size();

			/******************** 低位次二段 *******************/
			d_list_2 = getLuquList(year, part_2, score, sub, d_type, diff, 1, list_size);
			d_list_2_size = d_list_2.size();

			/******************** 低位次三段 *******************/
			d_list_3 = getLuquList(year, part_3, score, sub, d_type, diff, 1, list_size);
			d_list_3_size = d_list_3.size();
			
		} else if (part == part_2) {// 第二段考生
			s_list_1_1 = new ArrayList<>(0);
			s_list_1_2 = new ArrayList<>(0);
			u_list_1 = new ArrayList<>(0);
			d_list_1 = new ArrayList<>(0);

			/******************** 同位次二段 *******************/
			if (score < line_2018_2) {
				s_list_2_1 = new ArrayList<>(0);
				s_list_2_2 = new ArrayList<>(0);
			} else {
				s_list_2_1 = getLuquList(year, part_2, score, sub, m_type, diff, 1, list_size);
				s_list_2_1_size = s_list_2_1.size();
				if (s_list_2_1_size < list_size) {
					s_list_2_2 = getLuquList(year, part_2, score, sub, d_type, diff, 1, list_size - s_list_2_1_size);
					s_list_2_2_size = s_list_2_2.size();
				} else {
					s_list_2_2 = new ArrayList<>(0);
				}
			}
			/******************** 同位次三段 *******************/
			s_list_3_1 = getLuquList(year, part_3, score, sub, m_type, diff, 1, list_size);
			s_list_3_1_size = s_list_3_1.size();
			s_list_3_2 = new ArrayList<>();

			/******************** 高位次二段 *******************/
			u_list_2 = getLuquList(year, part_2, score, sub, u_type, diff, 1, list_size);
			u_list_2_size = u_list_2.size();

			/******************** 高位次三段 *******************/
			u_list_3 = getLuquList(year, part_3, score, sub, u_type, diff, 1, list_size);
			u_list_3_size = u_list_3.size();

			/******************** 低位次二段 *******************/
			d_list_2 = getLuquList(year, part_2, score, sub, d_type, diff, s_list_2_2_size + 1, list_size);
			d_list_2_size = d_list_2.size();

			/******************** 低位次三段 *******************/
			d_list_3 = getLuquList(year, part_3, score, sub, d_type, diff, 1, list_size);
			d_list_3_size = d_list_3.size();
		} else if (part == part_3) {// 第三段考生
			s_list_1_1 = new ArrayList<>(0);
			s_list_1_2 = new ArrayList<>(0);
			s_list_2_1 = new ArrayList<>(0);
			s_list_2_2 = new ArrayList<>(0);
			u_list_1 = new ArrayList<>(0);
			u_list_2 = new ArrayList<>(0);
			d_list_1 = new ArrayList<>(0);
			d_list_2 = new ArrayList<>(0);

			/******************** 同位次三段 *******************/
			if (score < line_2018_3) {
				s_list_3_1 = new ArrayList<>(0);
				s_list_3_2 = new ArrayList<>(0);
			} else {
				s_list_3_1 = getLuquList(year, part_3, score, sub, m_type, diff, 1, list_size);
				s_list_3_1_size = s_list_3_1.size();
				if (s_list_3_1_size < list_size) {
					s_list_3_2 = getLuquList(year, part_3, score, sub, d_type, diff, 1, list_size - s_list_3_1_size);
					s_list_3_2_size = s_list_3_2.size();
				} else {
					s_list_2_2 = new ArrayList<>(0);
				}
			}

			/******************** 高位次三段 *******************/
			u_list_3 = getLuquList(year, part_3, score, sub, u_type, diff, 1, list_size);
			u_list_3_size = u_list_3.size();

			/******************** 低位次三段 *******************/
			d_list_3 = getLuquList(year, part_3, score, sub, d_type, diff, s_list_3_2_size + 1, list_size);
			d_list_3_size = d_list_3.size();
		} else {
			s_list_1_1 = new ArrayList<>(0);
			s_list_1_2 = new ArrayList<>(0);
			s_list_2_1 = new ArrayList<>(0);
			s_list_2_2 = new ArrayList<>(0);
			s_list_3_1 = new ArrayList<>(0);
			s_list_3_2 = new ArrayList<>(0);
			
			u_list_1 = new ArrayList<>(0);
			u_list_2 = new ArrayList<>(0);
			u_list_3 = new ArrayList<>(0);
			
			d_list_1 = new ArrayList<>(0);
			d_list_2 = new ArrayList<>(0);
			d_list_3 = new ArrayList<>(0);
		}

		System.out.println(s_list_1_1.size());
		System.out.println(s_list_1_2.size());
		System.out.println(s_list_2_1.size());
		System.out.println(s_list_2_2.size());
		System.out.println(s_list_3_1.size());
		System.out.println(s_list_3_2.size());
		
		System.out.println(u_list_1.size());
		System.out.println(u_list_2.size());
		System.out.println(u_list_3.size());
		
		
		System.out.println(d_list_1.size());
		System.out.println(d_list_2.size());
		System.out.println(d_list_3.size());
		
		// model.addAttribute("s_list_1_1", s_list_1_1);
		// model.addAttribute("s_list_1_2", s_list_1_2);
		// model.addAttribute("s_list_2_1", s_list_2_1);
		// model.addAttribute("s_list_2_2", s_list_2_2);
		// model.addAttribute("s_list_3_1", s_list_3_1);
		// model.addAttribute("s_list_3_2", s_list_3_2);
		// model.addAttribute("s_list_1_1_size", s_list_1_1_size);
		// model.addAttribute("s_list_1_2_size", s_list_1_2_size);
		// model.addAttribute("s_list_2_1_size", s_list_2_1_size);
		// model.addAttribute("s_list_2_2_size", s_list_2_2_size);
		// model.addAttribute("s_list_3_1_size", s_list_3_1_size);
		// model.addAttribute("s_list_3_2_size", s_list_3_2_size);
		// model.addAttribute("u_list_1", u_list_1);
		// model.addAttribute("u_list_2", u_list_2);
		// model.addAttribute("u_list_3", u_list_3);
		// model.addAttribute("d_list_1", d_list_1);
		// model.addAttribute("d_list_2", d_list_2);
		// model.addAttribute("d_list_3", d_list_3);
		// model.addAttribute("u_list_1_size", u_list_1_size);
		// model.addAttribute("u_list_2_size", u_list_2_size);
		// model.addAttribute("u_list_3_size", u_list_3_size);
		// model.addAttribute("d_list_1_size", d_list_1_size);
		// model.addAttribute("d_list_2_size", d_list_2_size);
		// model.addAttribute("d_list_3_size", d_list_3_size);
	}
	
	/**
	 * 获取对应年份的高考分
	 * 
	 * @param year
	 *            年份
	 * @param ranking
	 *            市排名
	 * @return
	 */
	public static int getGaokaoScore(int year, int ranking) {
		int score = 0;
		StringBuffer sql = new StringBuffer(
				"select Score from t_sgk where Ranking>=:ranking and CYear=:year order by Ranking");
		Query query = JpaUtils.createNativeQuery(sql.toString());
		query.setParameter("ranking", ranking);
		query.setParameter("year", year);
		
		List<Integer> scoreList = query.setFirstResult(0).setMaxResults(1).getResultList();
		if(scoreList.size()>0){
			score = Integer.parseInt(scoreList.get(0).toString());
		}
		return score;
	}

	public static List<Object> getLuquList(int year, int part, int score, String[] sub, int type, int diff, int start, int size)
	{
		
		String sql_order = null;
		StringBuffer sql = new StringBuffer("select * from t_slq where CYear=:year and Part=:part");
		Map<String, Object> params = new HashMap<>();
		params.put("year", year);
		params.put("part", part);
		if (type == 0)//同位次
		{
			sql.append(" and Score=:score");
			params.put("score", score);
			sql_order = " order by Score desc, SubjectState desc, SchoolCode, MajorCode";
		}
		else if (type > 0)//高位次
		{
			sql.append(" and Score>:score and Score<:diffScore");
			params.put("score", score);
			params.put("diffScore", score + diff);
			sql_order = " order by Score, SubjectState desc, SchoolCode, MajorCode";
		}
		else//低位次
		{
			sql.append(" and Score<:score and Score>:diffScore");
			params.put("score", score);
			params.put("diffScore", score - diff);
			sql_order = " order by Score desc, SubjectState desc, SchoolCode, MajorCode";
		}
		sql.append(" and (SubjectNumber=:subjectNumber");
		sql.append(getSubSql(sub));
		sql.append(")");
		params.put("subjectNumber", 0);
		
		if (sub != null)
		{
			for (int i = 0; i < sub.length; i++)
			{
				params.put("sub" + i, 1);
			}
		}
		sql.append(sql_order);
		
		System.out.println(sql);
		System.out.println(params);
		Query query = JpaUtils.createNativeQuery(sql.toString(),HashMap.class);
		params.forEach((x,y)->{
			query.setParameter(x, y);
		});
		return query.setFirstResult(start).setMaxResults(size).getResultList();
	}

	private static StringBuffer getSubSql(String[] sub)
	{
		StringBuffer sql = new StringBuffer();
		int i = 0;
		if (sub != null)
		{
			for (String s : sub)
			{
				if ("物理".equals(s))
				{
					sql.append(" or Sub04=:sub").append(i++);
				}
				else if ("化学".equals(s))
				{
					sql.append(" or Sub05=:sub").append(i++);
				}
				else if ("生物".equals(s))
				{
					sql.append(" or Sub06=:sub").append(i++);
				}
				else if ("政治".equals(s))
				{
					sql.append(" or Sub07=:sub").append(i++);
				}
				else if ("历史".equals(s))
				{
					sql.append(" or Sub08=:sub").append(i++);
				}
				else if ("地理".equals(s))
				{
					sql.append(" or Sub09=:sub").append(i++);
				}
				else if ("技术".equals(s))
				{
					sql.append(" or Sub15=:sub").append(i++);
				}
			}
		}
		return sql;
	}
}
