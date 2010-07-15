package es.uniovi.innova.toolkits.jpa.easydao;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

import org.junit.Test;

import es.uniovi.innova.toolkits.jpa.easydao.DAO;
import es.uniovi.innova.toolkits.jpa.easydao.test.Util;

/**
 * Delegate methods ({@link DAO#findById(Object)}, {@link DAO#persist(Object)},
 * {@link DAO#remove(Object)}) won't be tested.
 */
public class DAOTest {

	@Entity
	class Bar {}
	
	class Foo {}
	
	@Test(expected = IllegalArgumentException.class)
	public void itCreatesNewDAOForFail() {
		new DAO<Foo>(Foo.class);
	}

	@Test
	public void itCreatesNewDAOForPass() {
		new DAO<Bar>(Bar.class);
	}
	/**
	 * Passes when an exception is raised because the given entityManager is
	 * null
	 */
	@Test(expected = IllegalArgumentException.class)
	public void itFailsCreatingAnInstanceDueToNullEntityManager() {
		new DAO<Bar>(Bar.class,null);
	}

	/**
	 * Passes when an exception is raised because the given entityManager is not
	 * open
	 */
	@Test(expected = IllegalStateException.class)
	public void itFailsCreaingAnInstanceDueToWrongEntityManager() {
		EntityManager em = Util.createTestEntityManager();
		em.close();
		new DAO<Bar>(Bar.class,em);
	}

}
