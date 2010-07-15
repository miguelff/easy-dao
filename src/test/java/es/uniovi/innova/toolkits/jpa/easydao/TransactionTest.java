package es.uniovi.innova.toolkits.jpa.easydao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;

import es.uniovi.innova.toolkits.jpa.easydao.test.Util;

public class TransactionTest {

	/**
	 * Passes when creating a new instance of Transaction passing a not null,
	 * open entityManager.
	 */
	public void itCreatesANewInstanceForPass() {
		EntityManager em = Util.createTestEntityManager();
		new Transaction(em) {
			@Override
			protected void transactionalCode() {
				;
			}
		};
	}

	/**
	 * Passes when an exception is raised because the given entityManager is
	 * null
	 */
	@Test(expected = IllegalArgumentException.class)
	public void itFailsCreatingAnInstanceDueToNullEntityManager() {
		new Transaction(null) {
			@Override
			protected void transactionalCode() {
				;
			}
		};
	}

	/**
	 * Passes when an exception is raised because the given entityManager is not
	 * open
	 */
	@Test(expected = IllegalStateException.class)
	public void itFailsCreaingAnInstanceDueToWrongEntityManager() {
		EntityManager em = Util.createTestEntityManager();
		em.close();
		new Transaction(em) {
			@Override
			protected void transactionalCode() {
				;
			}
		};
	}

	/**
	 * Passes if methods annotated with {@link BeforeTransaction} execute before
	 * the transactional code.
	 */
	@Test
	public void itExecutesBeforeTransactionHookCorrectly() {
		final List<String> methodsExecuted = new ArrayList<String>();
		Transaction t = new Transaction(Util.createTestEntityManager()) {

			@SuppressWarnings("unused")
			@BeforeTransaction
			public void beforeTransactionHook() {
				methodsExecuted.add("beforeTransactionHook");
			}

			@Override
			protected void transactionalCode() {
				methodsExecuted.add("transactionalCode");

			}
		};
		t.execute();
		assertEquals(
				Arrays.asList("beforeTransactionHook", "transactionalCode"),
				methodsExecuted);

	}

	/**
	 * Passes if methods annotated with {@link AfterCommit} execute after the
	 * transactional code.
	 */
	@Test
	public void itExecutesAfterCommitHookCorrectly() {
		final List<String> methodsExecuted = new ArrayList<String>();
		Transaction t = new Transaction(Util.createTestEntityManager()) {
			
			@SuppressWarnings("unused")
			@AfterCommit
			public void afterCommitHook() {
				methodsExecuted.add("afterCommitHook");
			}

			@Override
			protected void transactionalCode() {
				methodsExecuted.add("transactionalCode");

			}
		};
		t.execute();
		assertEquals(Arrays.asList("transactionalCode", "afterCommitHook"),
				methodsExecuted);

	}

	/**
	 * Passes if methods annotated with {@link AfterRollback} execute after the
	 * transactional code.
	 */
	@Test
	public void itExecutesAfterRollbackHookCorrectly() {
		final List<String> methodsExecuted = new ArrayList<String>();
		Transaction t = new Transaction(Util.createTestEntityManager()) {

			@SuppressWarnings("unused")
			@AfterRollback
			public void afterRollbackHook() {
				methodsExecuted.add("afterRollbackHook");
			}

			@Override
			protected void transactionalCode() {
				throw new RuntimeException(
						"this causes transaction to be rolled back");
			}
		};
		t.execute();
		assertEquals(Arrays.asList("afterRollbackHook"), methodsExecuted);

	}

	/**
	 * Passes if methods annotated with {@link AfterCommit}, and those annotated
	 * with with {@link BeforeTransaction} execute in the correct order
	 */
	@Test
	public void itExecutesBeforeAndAfterHooksCorrectly() {
		final List<String> methodsExecuted = new ArrayList<String>();
		Transaction t = new Transaction(Util.createTestEntityManager()) {
			
			@SuppressWarnings("unused")
			@BeforeTransaction
			public void beforeTransactionHook() {
				methodsExecuted.add("beforeTransactionHook");
			}

			@Override
			protected void transactionalCode() {
				methodsExecuted.add("transactionalCode");

			}
			@SuppressWarnings("unused")
			@AfterCommit
			public void afterCommitHook() {
				methodsExecuted.add("afterCommitHook");
			}
		};
		t.execute();
		assertEquals(
				Arrays.asList("beforeTransactionHook", "transactionalCode","afterCommitHook"),
				methodsExecuted);
	}

}
