/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.event.dao;

import de.fhhof.datebook.common.dao.GenericDaoImpl;
import de.fhhof.datebook.user.domain.Eventfrequency;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sumedh
 */
@Repository
public class EventfrequencyImplDao extends GenericDaoImpl<Eventfrequency,Long> implements EventfrequencyDao{

    public void addEventFrequency(Eventfrequency oEventfrequency) {
       entityManager.merge(oEventfrequency);
    }

}
