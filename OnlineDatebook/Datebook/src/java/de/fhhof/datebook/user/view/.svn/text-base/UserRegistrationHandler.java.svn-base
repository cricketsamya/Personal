/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.datebook.user.view;

import de.fhhof.datebook.common.CryptographicUtility;
import de.fhhof.datebook.common.Utility;
import de.fhhof.datebook.user.domain.Userdetails;
import de.fhhof.datebook.user.service.UserService;
import java.util.Locale;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 *
 * @author Sumedh
 */
@Component("userRegistrationHandler")
@Scope("request")
public class UserRegistrationHandler {

    private UserService userServcie;
    private Userdetails userdetails = new Userdetails();
    FacesContext context = FacesContext.getCurrentInstance();
    public static String REGISTRATION_SUCCESS = "regsuccess";
    public static String REGISTRATION_FAILED = "regfailed";

    public String register() {
        String strReturnString = "";
        try {
            if (userdetails.getStrFirstname().equals("") || userdetails.getStrLastname().equals("")
                    || userdetails.getStrEmailID().equals("") || userdetails.getStrPassword().equals("")) {
                userdetails.setStrErrorMessage(Utility.getMessageResourceString(context.getApplication().getMessageBundle(), "Registration.InvalidData", null, Locale.ENGLISH));
                strReturnString = REGISTRATION_FAILED;
            } else {
            	CryptographicUtility crpUtil = new CryptographicUtility();
            	this.userdetails.setStrPassword(crpUtil.getEncryptedText(this.userdetails.getStrPassword()));
                this.userServcie.registerUser(this.userdetails);
                strReturnString = REGISTRATION_SUCCESS;
                userdetails.setStrErrorMessage(Utility.getMessageResourceString(context.getApplication().getMessageBundle(), "Registration.Success", null, Locale.ENGLISH));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strReturnString;
    }

    public Userdetails getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(Userdetails userdetails) {
        this.userdetails = userdetails;
    }

    /**
     * @param userServcie the userServcie to set
     */
    @Autowired
    public void setUserServcie(UserService userServcie) {
        this.userServcie = userServcie;
    }
}
