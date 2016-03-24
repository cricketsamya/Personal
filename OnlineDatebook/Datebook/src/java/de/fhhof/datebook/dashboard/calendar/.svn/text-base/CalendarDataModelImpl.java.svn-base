/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.dashboard.calendar;


import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.fhhof.datebook.common.BasePage;
import de.fhhof.datebook.common.CommonConstant;
import de.fhhof.datebook.event.service.EventService;
import de.fhhof.datebook.user.domain.Eventdetails;
import de.fhhof.datebook.user.domain.Userdetails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Shamim
 */
@Component("calendarDataModel")
@Scope("session")
public class CalendarDataModelImpl extends BasePage implements CalendarDataModel  {
/* (non-Javadoc) 
	 * @see org.richfaces.component.CalendarDataModel#getData(java.util.Date[])
	 */
	private EventService eventService;
	
	private CalendarDataModelItem[] items;

	private String currentDescription;
	private String currentShortDescription;
	private Date currentDate;
	private boolean currentDisabled;


	/* (non-Javadoc)
	 * @see org.richfaces.model.CalendarDataModel#getData(java.util.Date[])
	 */
	
	public CalendarDataModelItem[] getData(Date[] dateArray) {
		if (dateArray == null) {
			return null;
		}
		Date start = dateArray[0];
		Date end = dateArray[dateArray.length -1];
		List<Eventdetails> eventDetails = null;
		try{
			Userdetails user = this.getUser();
			eventDetails = this.eventService.getUserEventsByMonth(user.getIntUserID(), start, end);

		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		items = new CalendarDataModelItem[dateArray.length];
		for (int i = 0; i < dateArray.length; i++) {
			Eventdetails detail = this.getEventDetail(dateArray[i],eventDetails);
			if(detail != null) {
				System.out.println(dateArray[i] +" -- "+ detail.getStrEventdescription());
			}
			items[i] = createDataModelItem(dateArray[i],detail);
		}

		return items;
	}

	/**
	 * @param date
	 * @return CalendarDataModelItem for date
	 */
	private Eventdetails getEventDetail(Date date, List<Eventdetails> details){
		
		Calendar first = Calendar.getInstance();
		first.setTime(date);
		
		Calendar end = Calendar.getInstance();
		
		for(Eventdetails detail: details){
			end.setTime(detail.getDtEventdate());
			
			if(first.get(Calendar.YEAR) == end.get(Calendar.YEAR) 
					&& first.get(Calendar.MONTH) == end.get(Calendar.MONTH) 
					&& first.get(Calendar.DAY_OF_MONTH) == end.get(Calendar.DAY_OF_MONTH) ){
				return  detail;
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	protected CalendarDataModelItem createDataModelItem(Date date,Eventdetails detail) {
		CalendarDataModelItemImpl item = new CalendarDataModelItemImpl();
		Map data = new HashMap();
		//data.put("shortDescription", "Not planed");
		item.setIsEmpty(false);
		if (detail != null){
			data.put("shortDescription", detail.getStrEventdescription());
			data.put("more", " More...");
			item.setIsEmpty(true);
			
		}
		DateFormat df = new SimpleDateFormat(CommonConstant.SHORT_DATE_FORMAT);
		
		data.put("description", "");
		String val = df.format(date);
		data.put("date", val);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		item.setDay(c.get(Calendar.DAY_OF_MONTH));
		item.setEnabled(true);
		item.setStyleClass("rel-hol");
		item.setData(data);
		return item;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.model.CalendarDataModel#getToolTip(java.util.Date)
	 */
	public Object getToolTip(Date date) {

		// TODO Auto-generated method stub
		return "Test";
	}

	/**
	 * @return items
	 */
	public CalendarDataModelItem[] getItems() {
		return items;
	}

	/**
	 * @param setter for items
	 */
	public void setItems(CalendarDataModelItem[] items) {
		this.items = items;
	}

	/**
	 * @param valueChangeEvent handling
	 */
	public void valueChanged(ValueChangeEvent event) {
/*		try {
			//Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Date date = (Date)event.getNewValue();
		setCurrentDate(date);
		System.out.println(date);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
		
		setCurrentDescription((String)((HashMap)items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData()).get("description"));
		setCurrentShortDescription((String)((HashMap)items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData()).get("shortDescription"));
	}



	/**
	 * @return currentDescription
	 */
	public String getCurrentDescription() {
		return currentDescription;
	}

	/**
	 * @param currentDescription
	 */
	public void setCurrentDescription(String currentDescription) {
		this.currentDescription = currentDescription;
	}

	/**
	 * @return currentDisabled
	 */
	public boolean isCurrentDisabled() {
		return currentDisabled;
	}

	/**
	 * @param currentDisabled
	 */
	public void setCurrentDisabled(boolean currentDisabled) {
		this.currentDisabled = currentDisabled;
	}

	/**
	 * @return currentShortDescription
	 */
	public String getCurrentShortDescription() {
		return currentShortDescription;
	}

	/**
	 * @param currentShortDescription
	 */
	public void setCurrentShortDescription(String currentShortDescription) {
		this.currentShortDescription = currentShortDescription;
	}

	/**
	 * @return currentDate
	 */
	public Date getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate
	 */
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	@Autowired
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

}
