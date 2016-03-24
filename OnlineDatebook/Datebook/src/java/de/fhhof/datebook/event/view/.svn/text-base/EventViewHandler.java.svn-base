package de.fhhof.datebook.event.view;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.fhhof.datebook.common.BasePage;
import de.fhhof.datebook.event.service.EventService;
import de.fhhof.datebook.user.domain.Eventdetails;

@Component("eventViewHandler")
@Scope("session")
public class EventViewHandler extends BasePage{
    private Date dateStart;
    private Date dateEnd;
    private String eventType = new String();
    private List<Eventdetails> eventList;
    private EventService eventservice;
    private Boolean noResult = false;
    
    public void loadEvents(){
    	try{
    		this.noResult = false;
    		this.eventList = this.eventservice.getUserAllEvents(this.getUser().getIntUserID(), new Integer(this.eventType), this.dateStart, this.dateEnd);
    		
    		if(this.eventList != null && this.eventList.size() > 0){
    			this.noResult = true;
    		}
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public List<Eventdetails> getEventList() {
		this.loadEvents();
		return eventList;
	}
	public void setEventList(List<Eventdetails> eventList) {
		this.eventList = eventList;
	}
	public EventService getEventservice() {
		return eventservice;
	}
	@Autowired
	public void setEventservice(EventService eventservice) {
		this.eventservice = eventservice;
	}
	public Boolean getNoResult() {
		return noResult;
	}
	public void setNoResult(Boolean noResult) {
		this.noResult = noResult;
	}
    
    
    
}
