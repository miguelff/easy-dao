package es.uniovi.innova.toolkits.jpa.easydao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.queries.QueryByExamplePolicy;
import org.eclipse.persistence.queries.ReadAllQuery;

/**
 * Concrete DAO for JPA 2 reference implementation vendor
 * (Eclipselink 2.0)
 * 
 * Supports {@link IDAO#queryByExample(Object)} operation
 * 
 * @author miguelff
 *
 * @param <T>  the type of the entities this Data Access Object works with.
 */
public class EclipselinkDAO<T> extends DAO<T> {

	public EclipselinkDAO(Class<T> cls) {
		super(cls);
	}
	
	public EclipselinkDAO(Class<T> cls,EntityManager em) {
		super(cls,em);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryByExample(T example) {
		QueryByExamplePolicy policy = new QueryByExamplePolicy();
		policy.excludeDefaultPrimitiveValues();
		ReadAllQuery q = new ReadAllQuery(example, policy);
		Query query = JpaHelper.createQuery(q, getEntityManager()); 
		return  query.getResultList();
	}

}
