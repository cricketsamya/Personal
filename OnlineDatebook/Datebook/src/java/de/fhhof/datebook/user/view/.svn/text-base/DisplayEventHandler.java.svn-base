
package de.fhhof.datebook.user.view;

import de.fhhof.datebook.common.BasePage;
import de.fhhof.datebook.common.CommonConstant;
import de.fhhof.datebook.dashboard.CalendarBean;
import de.fhhof.datebook.event.service.EventService;
import de.fhhof.datebook.user.domain.Eventdetails;
import de.fhhof.datebook.user.domain.Eventfrequency;
import de.fhhof.datebook.user.domain.Eventtype;
import de.fhhof.datebook.user.domain.Userdetails;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * @author Sameer
 */
@Component("displayeventhandler")
@Scope("session")
public class DisplayEventHandler extends BasePage {

    private static Eventdetails eventdetails = new Eventdetails();
    private EventService eventservice;
    private Date dtEventDate = new Date();
    private static Date dtSelectedDate = new Date();
    private String strEventFrequeny = new String();
    private String strEventType = new String();
    private String strEventDate = "";
    private List lsteventdetailsbyid = null;
    private List<Eventdetails> lstSortedListByDate = null;
    private List lstEventList = null;
    private List lstmyRadioPossibleOptions = null;
    private boolean listfilled = false;
    private boolean listeventfilled = false;
    private static final String DISPLAY_BY_DATE_SUCCESS = "displaybydatesuccess";
    private static final String DISPLAY_BY_DATE_FAILURE = "displaybydatefailed";
    private String SUCCESS = "editsucceed";
    private String FAIL = "editeventfailed";


    public String loadAllEvent() {
        try {

            String prm = this.getParameter(CommonConstant.DATE_PRM);
            if (prm == null || prm.equals("")) {
                return null;
            }
            DateFormat df = new SimpleDateFormat(CommonConstant.SHORT_DATE_FORMAT);
            dtEventDate = df.parse(prm);

            if (dtEventDate != null) {
                load();
                return CommonConstant.DASHBOARD_TO_EVENT;
            }
        } catch (Exception ex) {
        }
        return null;


    }

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
        lstSortedListByDate = eventservice.getUserEventsByMonth(this.getUser().getIntUserID(), dtEventStartDate, dtEventEndDate);
    }

    public String getEventDetailsByDate() {
        Date dtEventStartDate = new Date();
        Date dtEventEndDate = new Date();
        if (dtEventDate != null) {
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
            if (!lstSortedListByDate.isEmpty()) {
                listfilled = true;
            } else {
                listfilled = false;
            }
            return DISPLAY_BY_DATE_SUCCESS;
        }
        return DISPLAY_BY_DATE_FAILURE;

    }

    public String deleteEvent(ActionEvent event) {
        try {

            Integer id = (Integer) event.getComponent().getAttributes().get("intEventID");
            eventdetails.setIntEventID(id);
            this.eventservice.deleteEvent(eventdetails);
            this.load();
            HttpSession session = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
            Userdetails oUserdetails = (Userdetails) session.getAttribute("UObject");
            UserLoginHandler.loadTodaysList(oUserdetails);
            return SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FAIL;
    }

    public void getEventDetailsByID(ActionEvent event) {
        Integer eventid = (Integer) event.getComponent().getAttributes().get("eventid");
        eventdetails = new Eventdetails(eventid);
        lsteventdetailsbyid = this.eventservice.getEventDetailsByID(eventdetails);
        if (lsteventdetailsbyid.size() > 0) {
            listeventfilled = true;
            eventdetails = (Eventdetails) lsteventdetailsbyid.get(0);
            strEventFrequeny = eventdetails.getIntFrequencyID().getIntFrequencyID().toString();
            strEventType = eventdetails.getIntEventTypeID().getIntEventtypeID().toString();
            dtEventDate = eventdetails.getDtEventdate();
        } else {
            listeventfilled = false;
        }
    }

    public String updateEvent() {
        try {
//            Integer id = (Integer) event.getComponent().getAttributes().get("editeventid");
//            eventdetails=new Eventdetails(id);
            HttpSession session = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
            Userdetails oUserdetails = (Userdetails) session.getAttribute("UObject");
            Eventfrequency oEventfrequency = new Eventfrequency(new Integer(strEventFrequeny));
            Eventtype oEventtype = new Eventtype(new Integer(strEventType));
            eventdetails.setIntEventTypeID(oEventtype);
            eventdetails.setIntFrequencyID(oEventfrequency);
            eventdetails.setIntUserID(oUserdetails);
            eventdetails.setDtEventdate(dtEventDate);
            this.eventservice.updateEvent(eventdetails);
            //fetchEvents();
            listeventfilled = false;
            getEventDetailsByDate();
            UserLoginHandler.loadTodaysList(oUserdetails);
            return SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FAIL;
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

    public Date getDtEventDate() {
        return dtEventDate;
    }

    public void setDtEventDate(Date dtEventDate) {
        this.dtEventDate = dtEventDate;
    }

    public List getLstSortedListByDate() {
    	try{
    		load();
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    	}
        return lstSortedListByDate;
    }

    public void setLstSortedListByDate(List lstSortedListByDate) {
        this.lstSortedListByDate = lstSortedListByDate;
    }

    public boolean isListfilled() {
        return listfilled;
    }

    public void setListfilled(boolean listfilled) {
        this.listfilled = listfilled;
    }

    public String getStrEventDate() {
        return strEventDate;
    }

    public void setStrEventDate(String strEventDate) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        strEventDate = df.format(dtEventDate);
        this.strEventDate = strEventDate;
    }

    public void setListeventfilled(boolean listeventfilled) {
        this.listeventfilled = listeventfilled;
    }

    public boolean isListeventfilled() {
        return listeventfilled;
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

    public Eventdetails getEventdetails() {
        return eventdetails;
    }

    public void setEventdetails(Eventdetails eventdetails) {
        this.eventdetails = eventdetails;
    }

}
