package test.jpa;

import java.util.List;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import vo.School;
import vo.VoModel;

public class Main {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		Query query = JpaUtils.createNativeQuery("select ID,CName from t_school", School.class);
		List<School> list = query.getResultList();

		for (School school : list) {
			System.out.println(school.getID() + "==" + school.getName());
		}

	}
}
