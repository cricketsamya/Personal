package de.fhhof.datebook.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.fhhof.datebook.common.BasePage;
import de.fhhof.datebook.event.service.EventService;
import de.fhhof.datebook.user.domain.Eventdetails;
import de.fhhof.datebook.user.domain.Eventfrequency;
import de.fhhof.datebook.user.domain.Eventtype;

@Component("weeklyViewHandler")
@Scope("session")
public class WeeklyViewHandler extends BasePage{

    EventService eventservice;
    
    private List<Eventdetails> weeklyEvents;
    
    private Date currentDate;
    
    private Eventdetails currEvent;
    
    private Boolean auto;
    
    public void loadEvent(ActionEvent event) {
    	Integer eventId = (Integer) event.getComponent().getAttributes().get("eventid");
    	this.currEvent = this.eventservice.getEvent(eventId);
    	this.auto = false;
    	if(this.currEvent == null){
    		this.auto = true;
    		return;
    	}
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	cal.set(Calendar.HOUR, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	
    	if(this.currEvent.getDtEventdate().before(cal.getTime())){
    		this.currEvent.setReadOnly(true);
    	}else{
    		this.currEvent.setReadOnly(false);
    	}
    }
    public void updateEvent(){
    	try{
    		
    		this.eventservice.updateEvent(currEvent);
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    @Autowired
    public void setEventservice(EventService eventservice) {
        this.eventservice = eventservice;
    }
    
    public void goNextWeek(){
    	this.changeDate(7);
    	this.loadEvents();
    	
    }
    
    public void goPreviousWeek(){
    	
    	this.changeDate(-7);
    	this.loadEvents();
    }

	public List<Eventdetails> getWeeklyEvents() {
		try{
		
			if(weeklyEvents == null){
				if(currentDate == null) currentDate = new Date();
			}
			this.loadEvents();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return weeklyEvents;
	}

	private void loadEvents(){
		try{
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);


			int weekDay = cal.get(Calendar.DAY_OF_WEEK);
			int weekEnd = 7 - weekDay;
			
			Calendar start = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			start.setTime(currentDate);
			end.setTime(currentDate);
			
			start.add(Calendar.DATE, -weekDay);
			end.add(Calendar.DATE, weekEnd);
			
			weeklyEvents = eventservice.getUserEventsByMonth(this.getUser().getIntUserID(), start.getTime(), end.getTime());
			System.out.println(" "+ start.getTime().toString() + "  --  "+ end.getTime().toString());

		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void setWeeklyEvents(List<Eventdetails> weeklyEvents) {
		this.weeklyEvents = weeklyEvents;
	}
	
	private void changeDate(int dayOffSet){
		if(currentDate == null) currentDate = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);

		cal.add(Calendar.DATE, dayOffSet);
		this.currentDate = cal.getTime();
	}

	public Date getCurrentDate() {
		if(currentDate == null) currentDate = new Date();
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public Eventdetails getCurrEvent() {
		if(this.currEvent == null){
			this.currEvent = new Eventdetails();
			Eventfrequency fr = new Eventfrequency();
			fr.setIntFrequencyID(1);
			this.currEvent.setIntFrequencyID(fr);
			Eventtype type = new Eventtype();
			type.setIntEventtypeID(1);
			this.currEvent.setIntEventTypeID(type);
		}
		
		return currEvent;
	}

	public void setCurrEvent(Eventdetails currEvent) {

		this.currEvent = currEvent;
	}
	public Boolean getAuto() {
		return auto;
	}
	public void setAuto(Boolean auto) {
		this.auto = auto;
	}
    
    
}
