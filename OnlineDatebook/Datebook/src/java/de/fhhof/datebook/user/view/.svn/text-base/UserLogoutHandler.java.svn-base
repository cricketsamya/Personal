/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.user.view;

import de.fhhof.datebook.common.Utility;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sameer
 */
@Component("UserLogoutHandler")
@Scope("session")
public class UserLogoutHandler {

    private String strLogoutMessage = "";
    public String logout(){
        String strRetValue = "logoutfailed";
        strLogoutMessage = Utility.getMessageResourceString(FacesContext.getCurrentInstance().getApplication().getMessageBundle(), "Logout.Failed", null, Locale.ENGLISH);
        try{
            HttpSession session = getSession();
            session.invalidate();
            //UserLoginHandler.setFlagLogged(false);
            strRetValue = "logoutsuccess";
            strLogoutMessage = Utility.getMessageResourceString(FacesContext.getCurrentInstance().getApplication().getMessageBundle(), "Logout.Success", null, Locale.ENGLISH);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return strRetValue;
    }

    public String getStrLogoutMessage() {
        return strLogoutMessage;
    }

    
    public HttpSession getSession() {

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return req.getSession();
    }
}
