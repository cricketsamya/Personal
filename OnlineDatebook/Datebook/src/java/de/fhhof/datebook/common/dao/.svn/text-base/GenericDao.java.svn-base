/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.common.dao;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Shamim
 */
public interface  GenericDao<T, ID extends Serializable>{
    
	T loadById(ID id);

	void persist(T entity);

	void update(T entity);

	void delete(T entity);

	List<T> loadAll();
        
	List<T> loadByQuery(String sql, Object[] params);

	List<T> loadByClause(String clause, Object[] params);
}
