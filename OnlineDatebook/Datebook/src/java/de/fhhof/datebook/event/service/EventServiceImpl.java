
package de.fhhof.datebook.event.service;

import de.fhhof.datebook.event.dao.EventDetailsDao;
import de.fhhof.datebook.event.dao.EventTypeDao;
import de.fhhof.datebook.event.dao.EventfrequencyDao;
import de.fhhof.datebook.user.domain.Eventdetails;
import de.fhhof.datebook.user.domain.Eventfrequency;
import de.fhhof.datebook.user.domain.Eventtype;
import de.fhhof.datebook.user.domain.Userdetails;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Sameer
 * 
 * Add here all changes history.
 * dont add change history before each method
 */

@Transactional
@Service
public class EventServiceImpl implements EventService {

    private EventTypeDao eventTypeDao;
    private EventDetailsDao eventDetailsDao;
    private EventfrequencyDao eventFreqDao;

    public List getListOfAllEventTypes() {
        List arrList = new ArrayList();
        List tmpDBList = eventTypeDao.loadAll();
        Iterator iteDBList = tmpDBList.iterator();
        while (iteDBList.hasNext()) {
            Eventtype oEventtype = (Eventtype) iteDBList.next();
            arrList.add(new SelectItem(oEventtype.getIntEventtypeID() + "", oEventtype.getStrEventtype()));
        }
        return arrList;
    }

    public List getListOfAllEventFrequencies() {
        List arrList_frq = new ArrayList();
        List tmpDBList_frq = eventFreqDao.loadAll();
        Iterator iteDBList_frq = tmpDBList_frq.iterator();
        while (iteDBList_frq.hasNext()) {
            Eventfrequency oEventfreq = (Eventfrequency) iteDBList_frq.next();
            arrList_frq.add(new SelectItem(oEventfreq.getIntFrequencyID() + "", oEventfreq.getStrFrequencydescription()));
        }
        return arrList_frq;
    }

    public List fetchEvents(Eventdetails oEventdetails) {

        List tmpList = this.eventDetailsDao.fetchEvents(oEventdetails);
        return tmpList;
    }

    public void Addevent(Eventdetails eventdetails) {
        this.eventDetailsDao.Addevent(eventdetails);
    }
    
    public List<Eventdetails> getEventDetailsByDate(final Userdetails objUserdetails,Date dtEventStartDate, Date dtEventEndDate) {
        List<Eventdetails> lstEventDetails = new ArrayList<Eventdetails>();
        List<Eventdetails> lstSortedListOfEvents = new ArrayList<Eventdetails>();

        lstEventDetails = eventDetailsDao.getEventDetailsByDate(objUserdetails,dtEventStartDate,dtEventEndDate);
        if(!lstEventDetails.isEmpty()){
            Iterator<Eventdetails> iteEventList = lstEventDetails.iterator();
            while(iteEventList.hasNext()){
                Eventdetails oEventdetails = (Eventdetails)iteEventList.next();
                try {
                    DateFormat dtFormat = new SimpleDateFormat("HH:mm");
                    String tmpStrDate = dtFormat.format(oEventdetails.getDtEventdate());
                    oEventdetails.setStrTime(tmpStrDate);
                    lstSortedListOfEvents.add(oEventdetails);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
        }
        return lstSortedListOfEvents;

    }


    /**
     *
     * @author Sumedh
     */
     public void deleteEvent(Eventdetails eventdetails) {
        this.eventDetailsDao.deleteEvent(eventdetails);
    }

     public void updateEvent(Eventdetails eventdetails){
         this.eventDetailsDao.updateEvent(eventdetails);
     }

     public List<Eventdetails> getEventDetailsByID(Eventdetails eventdetails){
        List<Eventdetails> tmpList= this.eventDetailsDao.getEventDetailsByID(eventdetails);
        return tmpList;
     }

    @Autowired
    public void setEventTypeDao(EventTypeDao eventTypeDao) {
        this.eventTypeDao = eventTypeDao;
    }

    public EventTypeDao getEventTypeDao() {
        return eventTypeDao;
    }

    public EventDetailsDao getEventDetailsDao() {
        return eventDetailsDao;
    }

    @Autowired
    public void setEventDetailsDao(EventDetailsDao eventDetailsDao) {
        this.eventDetailsDao = eventDetailsDao;
    }

    public EventfrequencyDao getEventFreqDao() {
        return eventFreqDao;
    }

    @Autowired
    public void setEventFreqDao(EventfrequencyDao eventFreqDao) {
        this.eventFreqDao = eventFreqDao;
    }
    
    @Override
    public List<Eventdetails> getUserEventsByMonth(Integer userId, Date start,
                    Date end) {
    	List<Eventdetails> allEvents = new ArrayList<Eventdetails>();
    	
    	List<Eventdetails> dailyEvents = this.eventDetailsDao.getUserEventsByType(userId, 1);

    	for(Eventdetails dt: dailyEvents){
    		List<Eventdetails> events  = generateDailyEvents(dt,start,end);
    		allEvents.addAll(events);
    	}
    	
    	List<Eventdetails> weeklyEvents = this.eventDetailsDao.getUserEventsByType(userId, 2);

    	for(Eventdetails dt: weeklyEvents){
    		List<Eventdetails> events  = generateWeeklyEvents(dt,start,end);
    		allEvents.addAll(events);
    	}
    	
    	List<Eventdetails> monthlyEvents = this.eventDetailsDao.getUserEventsByType(userId, 3);
    	for(Eventdetails dt: monthlyEvents){
    		List<Eventdetails> events  = generateMonthlyEvents(dt,start,end);
    		allEvents.addAll(events);
    	}   	
    	
    	List<Eventdetails> yearlyEvents = this.eventDetailsDao.getUserEventsByType(userId,4);
    	for(Eventdetails dt: yearlyEvents){
    		List<Eventdetails> events  = generateYearlyEvents(dt,start,end);
    		allEvents.addAll(events);
    	}
    	
    	List<Eventdetails> dbEvents = this.eventDetailsDao.getUserEventsByMonth(userId, start, end);
    	allEvents.addAll(dbEvents);
        
    	return allEvents;
            
    }
    private Boolean isDateAreEqual(Date obj1, Date obj2){
    	Calendar cal1 = Calendar.getInstance();
    	cal1.setTime(obj1);
    	
    	Calendar cal2 = Calendar.getInstance();
    	cal2.setTime(obj2);
    	
    	if(cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) &&
    			cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
    			cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
    		return true;
    	}
    	return false;
    }
    private List<Eventdetails> generateDailyEvents(Eventdetails evt,Date start, Date end){
    	List<Eventdetails> events = new ArrayList<Eventdetails>();

    	Calendar cal = Calendar.getInstance();
    	cal.setTime(evt.getDtEventdate());
    	while(cal.getTime().compareTo(start) < 0 ){
    		cal.add(Calendar.DATE, 1);
    	}
    	while(cal.getTime().before(end) || cal.getTime().equals(end)){
    		Eventdetails newEvent = evt.copy();
    		newEvent.setDtEventdate(cal.getTime());
    		newEvent.setAutoGenerated(true);
    		if(!isDateAreEqual(newEvent.getDtEventdate(), evt.getDtEventdate())){
    			events.add(newEvent);
    		}
    		
    		cal.add(Calendar.DATE, 1);
    	}
    	return events;
    }
    
    private List<Eventdetails> generateWeeklyEvents(Eventdetails evt,Date start, Date end){
    	List<Eventdetails> events = new ArrayList<Eventdetails>();

    	Calendar cal = Calendar.getInstance();
    	cal.setTime(evt.getDtEventdate());
    	while(cal.getTime().compareTo(start) < 0){
    		cal.add(Calendar.DATE, 7);
    	}
    	while(cal.getTime().before(end)){
    		Eventdetails newEvent = evt.copy();
    		newEvent.setDtEventdate(cal.getTime());
    		newEvent.setAutoGenerated(true);
    		if(!isDateAreEqual(newEvent.getDtEventdate(), evt.getDtEventdate())){
    			events.add(newEvent);
    		}

    		cal.add(Calendar.DATE, 7);

    		
    	}
    	return events;
    }
    private List<Eventdetails> generateMonthlyEvents(Eventdetails evt,Date start, Date end){
    	List<Eventdetails> events = new ArrayList<Eventdetails>();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(evt.getDtEventdate());
    	while(cal.getTime().compareTo(start) < 0){
    		cal.add(Calendar.MONTH, 1);
    	}
    	while(cal.getTime().before(end)){

    		Eventdetails newEvent = evt.copy();
    		newEvent.setAutoGenerated(true);
    		newEvent.setDtEventdate(cal.getTime());
    		if(!isDateAreEqual(newEvent.getDtEventdate(), evt.getDtEventdate())){
    			events.add(newEvent);
    		}
    		cal.add(Calendar.MONTH, 1);
    	}
    	return events;
    }    
    private List<Eventdetails> generateYearlyEvents(Eventdetails evt,Date start, Date end){
    	List<Eventdetails> events = new ArrayList<Eventdetails>();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(evt.getDtEventdate());
    	while(cal.getTime().compareTo(start) < 0){
    		cal.add(Calendar.YEAR, 1);
    	}
    	while(cal.getTime().before(end)){

    		Eventdetails newEvent = evt.copy();
    		newEvent.setAutoGenerated(true);
    		newEvent.setDtEventdate(cal.getTime());
    		if(!isDateAreEqual(newEvent.getDtEventdate(), evt.getDtEventdate())){
    			events.add(newEvent);
    		}
    		cal.add(Calendar.YEAR, 1);
    	}
    	return events;
    }  
	@Override
	public Eventdetails getEvent(Integer eventId) {
		// TODO Auto-generated method stub		
		return this.eventDetailsDao.getEvent(eventId);
	}

	@Override
	public List<Eventdetails> getUserAllEvents(Integer userId, Integer type,
			Date start, Date end) {
		// TODO Auto-generated method stub
		return this.eventDetailsDao.getUserAllEvents(userId,type,start,end);
	}

    public void generateEventTypeTable(String []arrValue) {
        Eventtype oEventtype = null;
        for (int i = 0; i < arrValue.length; i++) {
            oEventtype = new Eventtype();
            oEventtype.setStrEventtype(arrValue[i]);
            eventTypeDao.addEventType(oEventtype);

        }
    }

    public void generateEventFrequencyTable(String []arrValue) {
        Eventfrequency oEventfrequency = null;
        for (int i = 0; i < arrValue.length; i++) {
            oEventfrequency = new Eventfrequency();
            oEventfrequency.setStrFrequencydescription(arrValue[i]);
            eventFreqDao.addEventFrequency(oEventfrequency);
        }
    }


}
