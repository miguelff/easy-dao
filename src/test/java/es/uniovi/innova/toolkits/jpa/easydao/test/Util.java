package es.uniovi.innova.toolkits.jpa.easydao.test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Util {

	/**
	 * @return  a new entity manager from the Entity Manager Factory configured
	 * in "test" persistence unit.
	 * 
	 */
	public static EntityManager createTestEntityManager() {
		return Persistence.createEntityManagerFactory("test")
				.createEntityManager();
	}

}