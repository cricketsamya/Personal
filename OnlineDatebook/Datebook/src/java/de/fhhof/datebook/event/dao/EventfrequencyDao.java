/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.event.dao;

import de.fhhof.datebook.common.dao.GenericDao;
import de.fhhof.datebook.user.domain.Eventfrequency;

/**
 *
 * @author Sumedh
 */
public interface  EventfrequencyDao extends GenericDao<Eventfrequency,Long>{

    void addEventFrequency(Eventfrequency oEventfrequency);
}
