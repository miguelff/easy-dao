package es.uniovi.innova.toolkits.jpa.easydao;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

/**
 * Abstract implementation of IDAO<T> interface
 * 
 * @author miguelff
 * 
 * @param <T>
 *            the type of the entities this Data Access Object works with.
 *            
 * As Query By Example is not an standard operation in JPA, this DAO implementation will not support
 * {@link DAO#queryByExample(Object)} operation.
 */
public class DAO<T> implements IDAO<T> {

	private final Class<T> entityClass;

	private EntityManager entityManager;

	/**
	 * creates a new instance of this DAO.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             in case <tt>cls</tt> is null, or is not annotated as an
	 *             entity.
	 */
	public DAO(Class<T> cls) {
		if (cls == null) {
			throw new IllegalArgumentException(
					"Argument cannot be null. It must be a POJO Class annotated as an entity.");
		}
		if (cls.getAnnotation(Entity.class) == null) {
			throw new IllegalArgumentException(String.format(
					"%s is not annotated as an entity", cls.getName()));
		}
		this.entityClass = (Class<T>) cls;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Initializes an instance of this DAO by calling {@link DAO#DAO()}
	 * constructor and setting the entityManager.
	 */
	public DAO(Class<T> cls, EntityManager entityManager) {
		this(cls);
		setEntityManager(entityManager);
	}

	/**
	 * sets the entity manager if it's not null and it's open.
	 * 
	 * @throws IllegalArgumentException
	 *             if the entity manager is null
	 * @throws IllegalStateException
	 *             if the entity manager is closed
	 */
	public void setEntityManager(EntityManager entityManager) {
		if (entityManager == null) {
			throw new IllegalArgumentException("Argument cannot be null");
		}
		if (!entityManager.isOpen()) {
			throw new IllegalStateException("Entity manager is closed: "
					+ entityManager);
		}
		this.entityManager = entityManager;
	}

	/**
	 * @returns the class type of the entities this DAO works with.
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	@Override
	public T persist(T entity) {
		return getEntityManager().merge(entity);
	}

	@Override
	public void remove(T entity) {
		getEntityManager().remove(entity);
	}

	@Override
	public T findById(Object id) {
		return getEntityManager().find(entityClass, id);

	}

	@Override
	public List<T> queryByExample(T example) {
		throw new UnsupportedOperationException(
				"Query by example is not vendor independent, Use a sublcass of this DAO," +
				" such as EclipselinkDAO if you want to have support for QBE queries.");
	}

}
