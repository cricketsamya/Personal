/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.datebook.user.view;

import de.fhhof.datebook.common.BasePage;
import de.fhhof.datebook.event.service.EventService;
import de.fhhof.datebook.user.domain.Eventdetails;
import de.fhhof.datebook.user.domain.Eventtype;
import de.fhhof.datebook.user.domain.Userdetails;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Sumedh
 */
@Component("deleteeventhandler")
@Scope("session")
public class DeleteEventHandler extends BasePage {

    Eventdetails eventdetails = new Eventdetails();
    EventService eventservice;
    private List lstEventList = null;
    private List lstFetchedEventDetails = null;
    private Date dtEventDate = new Date();
    private String strEventType = new String();
    private String strEventDate = "";
    private List<Eventdetails> lstSortedListByDate = null;
    private String strEventInfo = "";
    private boolean listfilled = false;
    private String SUCCESS = "delsuccess";
    private String FAIL = "delfail";
    private Date dateStart;
    private Date dateEnd;
    

    private void load() {
        Date dtEventStartDate = new Date();
        Date dtEventEndDate = new Date();
        setStrEventDate("");

        Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTimeInMillis(dtEventDate.getTime());

        tmpCal.set(Calendar.HOUR_OF_DAY, 0);
        tmpCal.set(Calendar.MINUTE, 0);
        tmpCal.set(Calendar.SECOND, 0);
        dtEventStartDate = tmpCal.getTime();

        tmpCal.set(Calendar.HOUR_OF_DAY, 23);
        tmpCal.set(Calendar.MINUTE, 59);
        tmpCal.set(Calendar.SECOND, 59);

        dtEventEndDate = tmpCal.getTime(); //2010-05-17 01:05:00.0
        lstSortedListByDate = eventservice.getEventDetailsByDate(this.getUser(), dtEventStartDate, dtEventEndDate);
    }

    public String deleteEvent(ActionEvent event) {
        try {
            Integer id = (Integer) event.getComponent().getAttributes().get("intEventID");
            eventdetails.setIntEventID(id);
            this.eventservice.deleteEvent(eventdetails);
            this.fetchEvents();
            this.load();
            HttpSession session = this.getSession();
            Userdetails oUserdetails = (Userdetails) session.getAttribute("UObject");
            UserLoginHandler.loadTodaysList(oUserdetails);
            return SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FAIL;
    }

    public void fetchEvents() {

    	HttpSession session = this.getSession();
        Userdetails oUserdetails = (Userdetails) session.getAttribute("UObject");

        Eventtype oEventtype = new Eventtype(new Integer(this.strEventType));

        eventdetails.setIntEventTypeID(oEventtype);
        eventdetails.setIntUserID(oUserdetails);
        lstFetchedEventDetails = eventservice.fetchEvents(eventdetails);
        if (lstFetchedEventDetails.size() > 0) {
            listfilled = true;
        } else {
            //Condition Added by Sameer Kulkarni
            listfilled = false;
        }

    }

    //Function to get the list of event types
    public void fillList() {
        try {
            lstEventList = eventservice.getListOfAllEventTypes();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Eventdetails getEventdetails() {
        return eventdetails;
    }

    public void setEventdetails(Eventdetails eventdetails) {
        this.eventdetails = eventdetails;
    }

    public List getLstEventList() {
        fillList();
        return lstEventList;
    }

    public EventService getEventservice() {
        return eventservice;
    }

    @Autowired
    public void setEventservice(EventService eventservice) {
        this.eventservice = eventservice;
    }

    public String getStrEventType() {
        return strEventType;
    }

    public void setStrEventType(String strEventType) {
        this.strEventType = strEventType;
    }

    public void setStrEventInfo(String strEventInfo) {
        this.strEventInfo = strEventInfo;
    }

    public String getStrEventInfo() {
        return strEventInfo;
    }

    public void setListfilled(boolean listfilled) {
        this.listfilled = listfilled;
    }

    public boolean isListfilled() {
        return listfilled;
    }

    public List getLstFetchedEventDetails() {
        return lstFetchedEventDetails;
    }

    public void setLstFetchedEventDetails(List lstFetchedEventDetails) {
        this.lstFetchedEventDetails = lstFetchedEventDetails;
    }

    public Date getDtEventDate() {
        return dtEventDate;
    }

    public void setDtEventDate(Date dtEventDate) {
        this.dtEventDate = dtEventDate;
    }

    public List<Eventdetails> getLstSortedListByDate() {
        return lstSortedListByDate;
    }

    public void setLstSortedListByDate(List<Eventdetails> lstSortedListByDate) {
        this.lstSortedListByDate = lstSortedListByDate;
    }

    public String getStrEventDate() {
        return strEventDate;
    }

    public void setStrEventDate(String strEventDate) {
        this.strEventDate = strEventDate;
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
}
