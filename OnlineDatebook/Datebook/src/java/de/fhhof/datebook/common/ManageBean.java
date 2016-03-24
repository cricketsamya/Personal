/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.datebook.common;

import de.fhhof.datebook.user.domain.Userdetails;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sameer
 */

@Component("ManageBean")
@Scope("session")
public class ManageBean implements Serializable{

    private String name;
    private int ID;

    public String getName() {
        name = ((Userdetails) getSession().getAttribute("UObject")).getStrFirstname();
        return name;
    }

    public Integer getUserID() {
        ID = ((Userdetails) getSession().getAttribute("UObject")).getIntUserID();
        return ID;
    }


    public void setName(String name) {
        this.name = name;
    }


    public HttpSession getSession() {

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return req.getSession();
    }
}
