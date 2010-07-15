package es.uniovi.innova.toolkits.jpa.easydao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

/**
 * @author miguelff
 * 
 *         Modelles a persistence transaction, i.e. a set of operations that are
 *         executed in a row, and commited altogether.
 * 
 */
public abstract class Transaction {

	private Logger logger = Logger.getLogger(getClass());

	private EntityManager entityManager;

	public Transaction(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

	/**
	 * Executes the transaction
	 */
	public void execute() {
		EntityTransaction tx = entityManager.getTransaction();
		invokeAnnotatedMethods(BeforeTransaction.class);
		tx.begin();
		try {
			transactionalCode();
			tx.commit();
			invokeAnnotatedMethods(AfterCommit.class);
		} catch (Throwable t) {
			logger.warn("An exception occurred while executing the transaction");
			tx.rollback();
			invokeAnnotatedMethods(AfterRollback.class);
		}
	}

	/**
	 * Template method which code will be executed in the scope of a transaction
	 */
	protected abstract void transactionalCode();

	/**
	 * Invokes the methods with the given annotation
	 * 
	 * @param annotation
	 */
	private void invokeAnnotatedMethods(Class<? extends Annotation> annotation) {
		for (Method m : getClass().getMethods()) {
			if (m.isAnnotationPresent(annotation)) {
				try {
					m.invoke(this);
				} catch (Throwable ex) {
					logger.error(String
							.format("Error executing %s method with %s annotation. Error was: %s",
									m.getName(), 
									annotation.getSimpleName(),
									ex.getMessage()));
					throw new RuntimeException(ex);
				}
			}
		}
	}

	/**
	 * sets the entity manager if it's not null and it's open.
	 * 
	 * @throws IllegalArgumentException
	 *             if the entity manager is null
	 * @throws IllegalStateException
	 *             if the entity manager is closed
	 */
	private void setEntityManager(EntityManager entityManager) {
		if (entityManager == null) {
			throw new IllegalArgumentException("Argument cannot be null");
		}
		if (!entityManager.isOpen()) {
			throw new IllegalStateException("Entity manager is closed: "
					+ entityManager);
		}
		this.entityManager = entityManager;
	}
}
