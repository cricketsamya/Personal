package de.fhhof.datebook.user.view;

import de.fhhof.datebook.common.BasePage;
import de.fhhof.datebook.common.CryptographicUtility;
import de.fhhof.datebook.common.Utility;
import de.fhhof.datebook.event.service.EventService;
import de.fhhof.datebook.user.domain.Userdetails;
import de.fhhof.datebook.user.service.UserService;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sameer
 */
@Component("userloginhandler")
@Scope("session")
public class UserLoginHandler extends BasePage {

    private UserService userservices;
    private static EventService eventservice;
    private Userdetails loggedinUserDetails;
    private String SUCCESS = "loginsuccess";
    private String ADMIN = "loginadmin";
    private String ADMIN_MAIN = "adminmain";
    private String FAIL = "loginfail";
    private static Boolean listTodaysfilled = false;
    private static List lstTodaysEvents;
    FacesContext context = FacesContext.getCurrentInstance();
    private String strErrorMsg = "";
    private String strInformationMsg = "";
    private boolean flagFrequencyListEmpty = false;
    private boolean flagTypeListEmpty = false;

    public UserLoginHandler() {
        loggedinUserDetails = new Userdetails();
    }

    public String login() {
        String strOriginalPassword = "";
        try {
            CryptographicUtility crpUtil = new CryptographicUtility();
            strOriginalPassword = loggedinUserDetails.getStrPassword();
            this.loggedinUserDetails.setStrPassword(crpUtil.getEncryptedText(this.loggedinUserDetails.getStrPassword()));
            Userdetails tmpUser = userservices.login(loggedinUserDetails);
            if (tmpUser != null) {
                HttpSession session = this.getRequest().getSession(true);
                session.setAttribute("UObject", tmpUser);

                this.loadTodayEvent();

                if (lstTodaysEvents != null && lstTodaysEvents.isEmpty()) {
                    listTodaysfilled = false;
                } else {
                    listTodaysfilled = true;
                }
                return SUCCESS;
            } else {
                String strUsername = loggedinUserDetails.getStrEmailID();
//                String strPassword = loggedinUserDetails.getStrPassword();
                String strUsernameAdmin = Utility.getMessageResourceString(FacesContext.getCurrentInstance().getApplication().getMessageBundle(), "Admin.Username", null, Locale.ENGLISH);
                String strPasswordAdmin = Utility.getMessageResourceString(FacesContext.getCurrentInstance().getApplication().getMessageBundle(), "Admin.Password", null, Locale.ENGLISH);
                if ((strUsername != null && strUsername.equals(strUsernameAdmin)) && (strOriginalPassword != null && strOriginalPassword.equals(strPasswordAdmin))) {
                    List lstEventType = eventservice.getListOfAllEventTypes();
                    List lstEventFrequencies = eventservice.getListOfAllEventFrequencies();
                    if (lstEventType.size() == 0) {
                        flagTypeListEmpty = true;
                    }
                    if (lstEventFrequencies.size() == 0) {
                        flagFrequencyListEmpty = true;
                    }
                    HttpSession session = this.getRequest().getSession(true);
                    session.setAttribute("UObject", loggedinUserDetails);
                    return ADMIN;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        loggedinUserDetails.setStrErrorMessage(Utility.getMessageResourceString(FacesContext.getCurrentInstance().getApplication().getMessageBundle(), "Login.Failed", null, Locale.ENGLISH));
        return FAIL;
    }

    private void loadTodayEvent() {
        try {
            Date dtEventStartDate = new Date();
            Date dtEventEndDate = new Date();

            Calendar tmpCal = Calendar.getInstance();
            tmpCal.setTimeInMillis(dtEventStartDate.getTime());

            tmpCal.set(Calendar.HOUR_OF_DAY, 0);
            tmpCal.set(Calendar.MINUTE, 0);
            tmpCal.set(Calendar.SECOND, 0);
            dtEventStartDate = tmpCal.getTime();

            tmpCal.set(Calendar.HOUR_OF_DAY, 23);
            tmpCal.set(Calendar.MINUTE, 59);
            tmpCal.set(Calendar.SECOND, 59);

            dtEventEndDate = tmpCal.getTime(); //2010
            lstTodaysEvents = eventservice.getUserEventsByMonth(this.getUser().getIntUserID(), dtEventStartDate, dtEventEndDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Userdetails getLoggedinUserDetails() {
        return loggedinUserDetails;
    }

    public void setLoggedinUserDetails(Userdetails loggedinUserDetails) {
        this.loggedinUserDetails = loggedinUserDetails;
    }

    public UserService getUserservices() {
        return userservices;
    }

    public boolean isLogged() {
        Userdetails tmpUserdetails = ((Userdetails) this.getSession().getAttribute("UObject"));
        if (tmpUserdetails != null) {
            return true;
        }
        return false;
    }

    @Autowired
    public void setUserservices(UserService userservices) {
        this.userservices = userservices;
    }

    public EventService getEventservice() {
        return eventservice;
    }

    @Autowired
    public void setEventservice(EventService eventservice) {
        this.eventservice = eventservice;
    }

    public Boolean getListTodaysfilled() {
        if (lstTodaysEvents != null && lstTodaysEvents.size() > 0) {
            return true;
        }
        return false;
    }

    public void setListTodaysfilled(Boolean listTodaysfilled) {
        this.listTodaysfilled = listTodaysfilled;
    }

    public List getLstTodaysEvents() {
        this.loadTodayEvent();
        return lstTodaysEvents;
    }

    public void setLstTodaysEvents(List lstTodaysEvents) {
        this.lstTodaysEvents = lstTodaysEvents;
    }

    public static void loadTodaysList(Userdetails tmpUser) {
        Date dtEventStartDate = new Date();
        Date dtEventEndDate = new Date();

        Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTimeInMillis(dtEventStartDate.getTime());

        tmpCal.set(Calendar.HOUR_OF_DAY, 0);
        tmpCal.set(Calendar.MINUTE, 0);
        tmpCal.set(Calendar.SECOND, 0);
        dtEventStartDate = tmpCal.getTime();

        tmpCal.set(Calendar.HOUR_OF_DAY, 23);
        tmpCal.set(Calendar.MINUTE, 59);
        tmpCal.set(Calendar.SECOND, 59);

        dtEventEndDate = tmpCal.getTime(); //2010
        if (tmpUser != null) {
            lstTodaysEvents = eventservice.getEventDetailsByDate(tmpUser, dtEventStartDate, dtEventEndDate);
            if (lstTodaysEvents != null && lstTodaysEvents.isEmpty()) {
                listTodaysfilled = false;
            } else {
                listTodaysfilled = true;
            }
        } else {
            lstTodaysEvents.clear();
            listTodaysfilled = true;
        }
    }

    public String generateEventTypeTable() {
        try {
            String strEventTypeValues = Utility.getMessageResourceString(FacesContext.getCurrentInstance().getApplication().getMessageBundle(), "EventTypes.Values", null, Locale.ENGLISH);
            String []arrValue = strEventTypeValues.split(":");
            eventservice.generateEventTypeTable(arrValue);
            flagTypeListEmpty = false;
            strInformationMsg = Utility.getMessageResourceString(FacesContext.getCurrentInstance().getApplication().getMessageBundle(), "Admin.EventType", null, Locale.ENGLISH);
            return ADMIN_MAIN;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ADMIN;
    }

    public String generateEventFrequencyTable() {
        try {
            String strEventFreqValues = Utility.getMessageResourceString(FacesContext.getCurrentInstance().getApplication().getMessageBundle(), "EventFrequency.Values", null, Locale.ENGLISH);
            String []arrValue = strEventFreqValues.split(":");
            eventservice.generateEventFrequencyTable(arrValue);
            flagFrequencyListEmpty = false;
            strInformationMsg = Utility.getMessageResourceString(FacesContext.getCurrentInstance().getApplication().getMessageBundle(), "Admin.EventFrequency", null, Locale.ENGLISH);
            return ADMIN_MAIN;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ADMIN;
    }

    public String back() {
        List lstEventType = eventservice.getListOfAllEventTypes();
        List lstEventFrequencies = eventservice.getListOfAllEventFrequencies();
        if (lstEventType.size() == 0) {
            flagTypeListEmpty = true;
        } else {
            flagTypeListEmpty = false;
        }
        if (lstEventFrequencies.size() == 0) {
            flagFrequencyListEmpty = true;
        } else {
            flagFrequencyListEmpty = false;
        }
        return ADMIN;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }

    public boolean isFlagFrequencyListEmpty() {
        return flagFrequencyListEmpty;
    }

    public void setFlagFrequencyListEmpty(boolean flagFrequencyListEmpty) {
        this.flagFrequencyListEmpty = flagFrequencyListEmpty;
    }

    public boolean isFlagTypeListEmpty() {
        return flagTypeListEmpty;
    }

    public void setFlagTypeListEmpty(boolean flagTypeListEmpty) {
        this.flagTypeListEmpty = flagTypeListEmpty;
    }

    public String getStrInformationMsg() {
        return strInformationMsg;
    }

    public void setStrInformationMsg(String strInformationMsg) {
        this.strInformationMsg = strInformationMsg;
    }

}
