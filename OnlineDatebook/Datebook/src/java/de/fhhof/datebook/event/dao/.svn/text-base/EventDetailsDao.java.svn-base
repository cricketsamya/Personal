/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.datebook.event.dao;

import de.fhhof.datebook.common.dao.GenericDao;
import de.fhhof.datebook.user.domain.Eventdetails;
import de.fhhof.datebook.user.domain.Userdetails;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Sumedh
 */
public interface EventDetailsDao extends GenericDao<Eventdetails, Integer> {

    public void Addevent(Eventdetails oEventdetails);

    public List<Eventdetails> fetchEvents(Eventdetails oEventdetails);

    public void deleteEvent(Eventdetails oEventdetails);

    public List<Eventdetails> getUserEventsByMonth(Integer userId, Date start, Date end);

    public List<Eventdetails> getEventDetailsByDate(final Userdetails objUserdetails, Date dtStartDate, Date dtEndDate);

    public void updateEvent(Eventdetails oEventdetails);

    public List<Eventdetails> getEventDetailsByID(Eventdetails oEventdetails);

    public Eventdetails getEvent(Integer eventId);
    
    public List<Eventdetails> getUserEventsByType(Integer userId, Integer type);
    
    public List<Eventdetails> getUserAllEvents(Integer userId,Integer type,Date start, Date end);
}