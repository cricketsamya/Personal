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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sameer
 */
@Component("userProfileHandler")
@Scope("request")
public class UserProfileHandler {

    private UserService userService;
    private Userdetails userdetails = new Userdetails();
    private Userdetails oldUserDetails = new Userdetails();
    FacesContext context = FacesContext.getCurrentInstance();
    public static String EDIT_SUCCESS = "editsuccess";
    public static String EDIT_FAILED = "editfailed";

    public UserProfileHandler() {
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = req.getSession();
        userdetails = (Userdetails) httpSession.getAttribute("UObject");
        if (userdetails == null) {
            userdetails = new Userdetails();
        }
        if (oldUserDetails == null) {
            oldUserDetails = new Userdetails();
        }
        userdetails.setStrErrorMessage("");
        oldUserDetails.setIntUserID(userdetails.getIntUserID());
        oldUserDetails.setStrConfirmPassword(new String(userdetails.getStrConfirmPassword()));
        oldUserDetails.setStrPassword(new String(userdetails.getStrPassword()));
        oldUserDetails.setStrEmailID(new String(userdetails.getStrEmailID()));
        oldUserDetails.setStrFirstname(new String(userdetails.getStrFirstname()));
        oldUserDetails.setStrLastname(new String(userdetails.getStrLastname()));
    }

    public String change() {
        String strReturnString = "";
        try {
            if (userdetails.getStrFirstname().equals("") || userdetails.getStrLastname().equals("")
                    || userdetails.getStrEmailID().equals("")) {
                userdetails.setStrErrorMessage(Utility.getMessageResourceString(context.getApplication().getMessageBundle(), "Edit.InvalidData", null, Locale.ENGLISH));
                strReturnString = EDIT_FAILED;
            } else {
                CryptographicUtility crpUtil = new CryptographicUtility();
                if (userdetails.getStrConfirmPassword().trim().equals(userdetails.getStrPassword().trim())) {
                    if (userdetails.getStrPassword().trim().equals("")) {
                        userdetails.setStrPassword(crpUtil.getEncryptedText(oldUserDetails.getStrPassword()));
                    } else {
                        userdetails.setStrPassword(crpUtil.getEncryptedText(userdetails.getStrPassword()));
                    }
                    userService.updateUserProfile(userdetails);
                    strReturnString = EDIT_SUCCESS;
                    userdetails.setStrErrorMessage(Utility.getMessageResourceString(context.getApplication().getMessageBundle(), "Edit.Success", null, Locale.ENGLISH));
                } else {
                    strReturnString = EDIT_FAILED;
                    userdetails.setStrErrorMessage(Utility.getMessageResourceString(context.getApplication().getMessageBundle(), "Edit.PasswordError", null, Locale.ENGLISH));
                }
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

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
