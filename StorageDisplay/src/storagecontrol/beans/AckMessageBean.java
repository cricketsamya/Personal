/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storagecontrol.beans;

/**
 *
 * @author Vaishnavi
 */
public class AckMessageBean {

    public AckMessageBean() {
    }

    
    public AckMessageBean(String strCommand, String strPayload) {
        this.strCommand = strCommand;
        this.strPayload = strPayload;
    }


    public String getStrCommand() {
        return strCommand;
    }

    public void setStrCommand(String strCommand) {
        this.strCommand = strCommand;
    }

    public String getStrPayload() {
        return strPayload;
    }

    public void setStrPayload(String strPayload) {
        this.strPayload = strPayload;
    }

    private String strCommand;
    private String strPayload;
}
