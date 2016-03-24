/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.datebook.event.service;

import de.fhhof.datebook.user.domain.Eventdetails;
import de.fhhof.datebook.user.domain.Userdetails;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Sameer
 */
public interface EventService {

    List getListOfAllEventTypes();

    List getListOfAllEventFrequencies();
    /**
     *
     * @author Sumedh
     */
    void Addevent(Eventdetails eventdetails);

    void deleteEvent(Eventdetails eventdetails);

    List<Eventdetails> fetchEvents(Eventdetails eventdetails);

    public List<Eventdetails> getUserEventsByMonth(Integer userId,Date start, Date end);

    List<Eventdetails> getEventDetailsByDate(final Userdetails objUserdetails,Date dtEventStartDate, Date dtEventEndDate);

    List<Eventdetails> getEventDetailsByID(Eventdetails eventdetails);

    public void updateEvent(Eventdetails eventdetails);

    public Eventdetails getEvent(Integer eventId);
    
    public List<Eventdetails> getUserAllEvents(Integer userId,Integer type,Date start, Date end);

    public void generateEventTypeTable(String []arrValue);

    public void generateEventFrequencyTable(String []arrValue);
}
