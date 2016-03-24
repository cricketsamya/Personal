/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.datebook.user.view;

import de.fhhof.datebook.common.Utility;
import de.fhhof.datebook.event.service.EventService;
import de.fhhof.datebook.user.domain.Eventdetails;
import de.fhhof.datebook.user.domain.Eventfrequency;
import de.fhhof.datebook.user.domain.Eventtype;
import de.fhhof.datebook.user.domain.Userdetails;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sumedh
 */
@Component("neweventhandler")
@Scope("request")
public class UserEventHandler {

    private Eventdetails eventdetails = new Eventdetails();
    private EventService eventservice;
    private List lstEventList = null;
    private List lstmyRadioPossibleOptions = null;
    FacesContext context = FacesContext.getCurrentInstance();
    private String strEventFrequeny = new String("5");
    private String strEventType = new String();
    private Date dtSelectedDate = new Date();
    private String SUCCESS = "addsucceed";
    private String FAIL = "addfailed";
    //Function to add new event

    public String Addevent() {
        String isAddedSuccessfully = "";
        try {
            if (dtSelectedDate == null || eventdetails.getStrEventdescription().equals("")
                    || strEventFrequeny.equals("") || strEventType.equals("") || eventdetails.getStrComments().equals("")) {
                isAddedSuccessfully = FAIL;
                eventdetails.setStrErrorMessage(Utility.getMessageResourceString(context.getApplication().getMessageBundle(), "Added.Failed", null, Locale.ENGLISH));
            } else {
                HttpSession session = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
                Userdetails oUserdetails = (Userdetails) session.getAttribute("UObject");
                Eventfrequency oEventfrequency = new Eventfrequency(new Integer(strEventFrequeny));
                Eventtype oEventtype = new Eventtype(new Integer(strEventType));
                eventdetails.setIntEventTypeID(oEventtype);
                eventdetails.setIntFrequencyID(oEventfrequency);
                eventdetails.setIntUserID(oUserdetails);
                eventdetails.setDtEventdate(dtSelectedDate);
                this.eventservice.Addevent(eventdetails);
                eventdetails.setStrErrorMessage(Utility.getMessageResourceString(context.getApplication().getMessageBundle(), "Added.Successfully", null, Locale.ENGLISH));
                UserLoginHandler.loadTodaysList(oUserdetails);
                isAddedSuccessfully = SUCCESS;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            isAddedSuccessfully = FAIL;
        }
        return isAddedSuccessfully;

    }

//Function to get the list of event types
    public List getLstEventList() {
        try {
            lstEventList = eventservice.getListOfAllEventTypes();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lstEventList;
    }

    //Function to get the list of event frequencies
    public List getLstmyRadioPossibleOptions() {
        try {
            lstmyRadioPossibleOptions = eventservice.getListOfAllEventFrequencies();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lstmyRadioPossibleOptions;
    }

    public void setLstmyRadioPossibleOptions(List lstmyRadioPossibleOptions) {
        this.lstmyRadioPossibleOptions = lstmyRadioPossibleOptions;
    }

    @Autowired
    public void setEventservice(EventService eventservice) {
        this.eventservice = eventservice;
    }

    public EventService getEventservice() {
        return eventservice;
    }

    public void setEventdateils(Eventdetails eventdateils) {
        this.eventdetails = eventdateils;
    }

    public Eventdetails getEventdetails() {
        return eventdetails;
    }

    public String getStrEventFrequeny() {
        return strEventFrequeny;
    }

    public void setStrEventFrequeny(String strEventFrequeny) {
        this.strEventFrequeny = strEventFrequeny;
    }

    public String getStrEventType() {
        return strEventType;
    }

    public void setStrEventType(String strEventType) {
        this.strEventType = strEventType;
    }

    public Date getDtSelectedDate() {
        return dtSelectedDate;
    }

    public void setDtSelectedDate(Date dtSelectedDate) {
        this.dtSelectedDate = dtSelectedDate;
    }
}


