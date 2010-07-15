package es.uniovi.innova.toolkits.jpa.easydao;

import java.util.List;

import javax.persistence.EntityManager;

/**
 * Data access object Contract
 * 
 * @author miguelff
 * 
 * @param <T>
 *            the type of the entities this Data Access Object works with.
 */
public interface IDAO<T> {

	/**
	 * @return this DAO entity Manager
	 */
	EntityManager getEntityManager();

	/**
	 * @return the class of the entity which will be the same as the actual
	 *         parameter type.
	 */
	Class<T> getEntityClass();

	/**
	 * sets the entity manager if it's not null and it's open.
	 * 
	 * @throws IllegalArgumentException
	 *             if the entity manager is null
	 * @throws IllegalStateException
	 *             if the entity manager is closed
	 */
	void setEntityManager(EntityManager manager);

	/**
	 * Persists or updates the entity, attaching it to the persistence context.
	 * 
	 * @param entity
	 *            entity to persist
	 * @return the persisted entity
	 */
	T persist(T entity);

	/**
	 * Removes the entity from the persistence context
	 * 
	 * @param entity
	 *            entity to remove
	 */
	void remove(T entity);

	/**
	 * Fetches an entity from the persistence context
	 * 
	 * @param id
	 *            unique identifier (primary key) for the entity
	 * @return The persistence entity with the given id.
	 */
	T findById(Object id);

	/**
	 * Fetches the list of persistent objects that match the criteria denoted by
	 * those fields that have a not null value in the example
	 * 
	 * provided with the following criteria template
	 * 
	 * <pre>
	 * @=================@
	 * ||    Person     ||
	 * ===================
	 * ||name="Paul"	||
	 * ||age=Integer(2) ||
	 * ||surname=null	||
	 * @=================@
	 * </pre>
	 * 
	 * The method will create a criteria query, with the following WHERE
	 * clauses: name="Paul" AND age=2
	 * 
	 * if also surname wouldn't be null, the WHERE clause would also contain an
	 * additional AND atom for the surname:
	 * 
	 * <pre>
	 * @=====================@
	 * ||      Person       ||
	 * =======================
	 * ||name="Paul"		||
	 * ||age=Integer(2)		||
	 * ||surname="octopus"	||
	 * @=====================@
	 * </pre>
	 * 
	 * name='Paul' AND age=2 and surname='octopus'
	 * 
	 * @param example
	 *            the entity that will serve as a criteria
	 * @return a list of objects matching the criteria.
	 */
	List<T> queryByExample(T example);
}
