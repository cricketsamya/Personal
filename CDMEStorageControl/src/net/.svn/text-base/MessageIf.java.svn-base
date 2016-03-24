/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.math.BigInteger;
import java.net.Socket;

/**
 *
 * @author sameer
 */
public interface MessageIf {

    Socket objClient = null;

    BigInteger getNumber();

    void ack(String command, String userMessage);

    String getPayload();

    void setPayload(String strPayload);

    void setCommand(String strCommand);

    String getCommand();

    String getMsdId();

    String getReceiver();

    String getSender();
}
