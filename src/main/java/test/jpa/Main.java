package test.jpa;

import java.util.List;

import javax.persistence.Query;

import config.jpa.JpaUtils;
import vo.VoModel;

public class Main {
	public static void main(String[] args) {

		Query query = JpaUtils.createNativeQuery("select * from model", VoModel.class);
		List<VoModel> list = query.getResultList();

		for (VoModel vm : list) {
			System.out.println(vm.getModelID() + "==" + vm.getModelName());
		}

	}
}
