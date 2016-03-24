/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.datebook.user.view;

import de.fhhof.datebook.event.service.EventService;
import de.fhhof.datebook.user.domain.Eventdetails;
import org.springframework.stereotype.Component;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author admin
 */
@Component("editeventhandler")
@Scope("request")
public class EditEventHandler {

    private EventService eventservice;
    private Eventdetails eventdetails = new Eventdetails();
    private Eventdetails fetchedEventdetails = new Eventdetails();
    private List lstEventList = null;
    private List lstmyRadioPossibleOptions = null;
    //private List lsteventdetailsbyid = null;
    private boolean listfilled = false;
    FacesContext context = FacesContext.getCurrentInstance();
    private String strEventFrequeny = new String();
    private String strEventType = new String();
    private String strEventTimeHr = new String();
    private String strEventTimeMin = new String();
    //private String SUCCESS_FETCH_EDITEVENT = "successfullyfetched";
    //private String FAIL_FETCH_EDITEVENT = "failedtofetch";


//    public String getEventDetailsByID(ActionEvent event) {
//        Integer eventid = (Integer) event.getComponent().getAttributes().get("eventid");
//        eventdetails = new Eventdetails(eventid);
//        lsteventdetailsbyid = this.eventservice.getEventDetailsByID(eventdetails);
//        if (lsteventdetailsbyid.size() > 0) {
//            listfilled = true;
//            fetchedEventdetails=(Eventdetails)lsteventdetailsbyid.get(0);
//            return SUCCESS_FETCH_EDITEVENT;
//        }
//        else {
//            listfilled = false;
//        }
//        return FAIL_FETCH_EDITEVENT;
//    }

    //Function to get the list of event types

    private void fillEventTypeList() {
        try {
            lstEventList = eventservice.getListOfAllEventTypes();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List getLstEventList() {
        fillEventTypeList();
        return lstEventList;
    }

    //Function to get the list of event frequencies
    private void fillFrqList() {
        try {
            lstmyRadioPossibleOptions = eventservice.getListOfAllEventFrequencies();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setLstmyRadioPossibleOptions(List lstmyRadioPossibleOptions) {
        this.lstmyRadioPossibleOptions = lstmyRadioPossibleOptions;
    }

    public List getLstmyRadioPossibleOptions() {
        fillFrqList();
        return lstmyRadioPossibleOptions;
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

    public void setStrEventTimeHr(String strEventTimeHr) {
        this.strEventTimeHr = strEventTimeHr;
    }

    public void setStrEventTimeMin(String strEventTimeMin) {
        this.strEventTimeMin = strEventTimeMin;
    }

    public String getStrEventTimeHr() {
        return strEventTimeHr;
    }

    public String getStrEventTimeMin() {
        return strEventTimeMin;
    }

    public boolean isListfilled() {
        return listfilled;
    }

    public void setListfilled(boolean listfilled) {
        this.listfilled = listfilled;
    }

    public Eventdetails getFetchedEventdetails() {
        return fetchedEventdetails;
    }

    public void setFetchedEventdetails(Eventdetails fetchedEventdetails) {
        this.fetchedEventdetails = fetchedEventdetails;
    }
}
