 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import storagecontrol.utility.FofUtility;
import storagecontrol.utility.Utility;

/**
 *
 * @author Sameer
 */
public class Message implements MessageIf {

    public static int msgCount = 0;
    private BigInteger number;
    private String Receiver;
    private String Sender ;
    private String Command = null;
    private String Payload = null;
    private String MsgId = null;
    private static String strOldMsgId = "";
    private Connection connectionDetails = null;
    public Message() {
        connectionDetails = new Connection();
    }

    public Message(BigInteger number) {
        this();
        this.number = number;
    }

    public Message(String Command, String Payload) {
        this();
        this.Command = Command;
        this.Payload = Payload;
    }

    public BigInteger getNumber() {
        return number;
    }

    public byte[] getRawMessage() {
        String asciiNumber = number.toString();
//ASCII representation
        String asciiLength = Utility.pad4(asciiNumber.length()); //padded length
        return (asciiLength + asciiNumber).getBytes();
    }


    public static MessageIf receive(DataInputStream in) throws IOException {
        String strPayload = "";
        String strCommand = "";
        String strHeader = "";
        Message msg = new Message();
        try {

            byte[] byteHeader = new byte[25];
            in.readFully(byteHeader);
            strHeader = new String(byteHeader);
            String strPayloadLen = strHeader.substring(21, 25);

            byte[] bytePayload = new byte[Integer.parseInt(strPayloadLen)];
            in.readFully(bytePayload);
            strPayload = new String(bytePayload);

            strCommand = strHeader.substring(4, 8);
            msg.setCommand(strCommand);
            msg.setPayload(strPayload);
            msg.setMsgId(strHeader.substring(8, 21));
            msg.setReceiver(strHeader.substring(2, 4));
            msg.setSender(strHeader.substring(0, 2));
        } catch (IOException e) {
            System.out.println(e);
            throw e;
        }
        return msg;
    }

    public void ack(String strCommand, String strPayload) {
        Message message = new Message();
        message.setCommand(strCommand);
        message.setPayload(strPayload);
        message.setMsgId(MsgId);
        message.setSender(connectionDetails.getStrHost());
        message.setReceiver(connectionDetails.getStrGuest());        
        connectionDetails.send(message);
        FofUtility.printMessage(message);
    }

    public String getCommand() {
        return Command;
    }

    public void setCommand(String Command) {
        this.Command = Command;
    }

    public String getPayload() {
        return Payload;
    }

    public void setPayload(String Payload) {
        this.Payload = Payload;
    }

    public byte[] getFofRawMessage(String strHost, String strGuest) {
        String strTimestamp = "";
        if (strOldMsgId.equals("")) {
            strOldMsgId = System.currentTimeMillis() / 1000 + "";
            strTimestamp = strOldMsgId;
        } else {
            strTimestamp = System.currentTimeMillis() / 1000 + "";
            if (!strOldMsgId.equals(strTimestamp)) {
                msgCount = 0;
            }
        }
        if (MsgId == null) {
            MsgId = strTimestamp + ":" + Utility.pad2(msgCount++);
        } else if (MsgId.equals("")) {
            MsgId = strTimestamp + ":" + Utility.pad2(msgCount++);
        }
        String asciiLength = Utility.pad4(getPayload().length());
        String str = strHost + strGuest + getCommand()
                + MsgId + asciiLength + getPayload();
        //ASCII representation
        return (str).getBytes();
    }
    public String getMsdId() {
        return MsgId;
    }

    public void setMsgId(String MsgId) {
        this.MsgId = MsgId;
    }

    public void setReceiver(String Receiver) {
        this.Receiver = Receiver;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setSender(String Sender) {
        this.Sender = Sender;
    }

    public String getSender() {
        return Sender;
    }

    public void setConnectionDetails(Connection connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public Connection getConnectionDetails() {
        return connectionDetails;
    }
}
