/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Shamim
 */
public abstract class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

	protected final Log log = LogFactory.getLog(getClass());
	private Class<T> persistentClass;

	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	public T loadById(ID id) {
            return entityManager.find(persistentClass, id);
	}

        public void persist(T entity) {
            entityManager.persist(entity);
        }

        public void update(T entity) {
            entityManager.merge(entity);

        }

	public void delete(T entity) {
		entityManager.remove(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> loadAll() {
		return entityManager.createQuery(
				"Select t from " + persistentClass.getName() + " t")
				.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<T> loadByClause(String clause, Object[] params) {
		String hsql = "Select t from " + persistentClass.getName()
				+ " t where " + clause;
		log.debug(hsql);
		Query query = entityManager.createQuery(hsql);
		/** bind parameters */
		for (int i = 0; params != null && i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> loadByQuery(String sql, Object[] params) {
		Query query = entityManager.createQuery(sql);
		/** bind parameters */
		for (int i = 0; params != null && i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}
}
