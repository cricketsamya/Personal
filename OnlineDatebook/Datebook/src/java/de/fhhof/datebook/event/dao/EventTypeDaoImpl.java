/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.event.dao;

import de.fhhof.datebook.common.dao.GenericDaoImpl;
import de.fhhof.datebook.user.domain.Eventtype;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sameer
 */

@Repository
public class EventTypeDaoImpl extends  GenericDaoImpl<Eventtype, Long>implements EventTypeDao{

    public void addEventType(Eventtype oEventtype) {
        entityManager.merge(oEventtype);
    }

}
