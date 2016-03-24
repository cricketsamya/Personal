/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.datebook.user.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Sameer
 */
public class UserEmailValidation implements Validator {

    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String enteredEmail = (String) o;
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(enteredEmail);
        boolean matchFound = m.matches();

        if (!matchFound) {
            FacesMessage message = new FacesMessage();
            message.setSummary("Invalid Email ID.");
            throw new ValidatorException(message);
        }
    }
}
