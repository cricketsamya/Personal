/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fofprotocol;

import net.Connection;
import net.Message;
import net.MessageIf;
import storagecontrol.utility.FofUtility;

/**
 * This class is used to connect to the Storage Simulator.
 * For the connection from Storage module the Storage-Control will act as a Server.
 * @author Sameer
 */
public class ServerStorageControl extends Thread {

    private static final String ACK_SENDER = "la";
    private static final String ACK_RECEIVER = "Sl";
    private static final String MSG_SENDER = "Sl";
    private static final String MSG_RECEIVER = "la";
    private static final int PUT_PALETTE = 1;
    private static final int GET_PALETTE = 2;
    private ClientStorageControl clientStorageControl;
    private Connection connectionToStorage = null;
    private String strLastInstuction = "";
    private MessageIf objStoredMessage;

    @Override
    public void run() {
        begin();
    }

    public void begin() {
        boolean bConnectionEstablished = false;
        while (true) {
            try {
                if (connectionToStorage == null) {
                    connectionToStorage = new Connection(9999, "Sl", "la"); //Server side
                    bConnectionEstablished = true;
                }
            } catch (Exception ex) {
                System.out.println(ex);
                connectionToStorage = null;
            }
            while (connectionToStorage != null && bConnectionEstablished) {
                try {
                    Thread.sleep(5000);
                } catch (Exception ex) {
                    System.out.println(ex);
                    bConnectionEstablished = false;
                }
            }
        }
    }

    public String storagePalletOperation(String strPalletId, int nOperation) {
        if(connectionToStorage == null){
            System.out.println("Waiting for connection from Function Simulator..");
        }
        while (connectionToStorage == null) {
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        String strSuccess = "";
        MessageIf msgAckOneFromStorage = null;
        MessageIf msgAckTwoFromStorage = null;
        MessageIf msgCommandToStorage = null;
        if (FofUtility.isRecovery()) {
            String strFullInstruction = FofUtility.readLastInstructionFromFile(FofUtility.INSTRUCTION_STORAGE_SEQUENCE_FILENAME);
            String[] strArrTmp = strFullInstruction.split(":");
            try {
                strLastInstuction = strArrTmp[1];
                String strInstruction = strArrTmp[3];
                strInstruction = strInstruction + ":" + strArrTmp[4];
                objStoredMessage = FofUtility.messageSplitter(strInstruction);
                FofUtility.setRecovery(false);
            } catch (Exception ex) {
            }
        } else {
            strLastInstuction = "";
        }

        String strOperation = "";
        if (nOperation == PUT_PALETTE) {
            msgCommandToStorage = new Message("K001", strPalletId);
            strOperation = "Put";
        } else if (nOperation == GET_PALETTE) {
            msgCommandToStorage = new Message("K002", strPalletId);
            strOperation = "Get";
        }
        ((Message) msgCommandToStorage).setSender(MSG_SENDER);
        ((Message) msgCommandToStorage).setReceiver(MSG_RECEIVER);
        Message msgAck1 = null;
        Message msgAck2 = null;
        if (strLastInstuction.equals("")) {
            FofUtility.writeInstructionToFile("Slla:" + strOperation + ":S1:" + strPalletId + ":" + FofUtility.messageGenerator((Message) msgCommandToStorage), FofUtility.INSTRUCTION_STORAGE_SEQUENCE_FILENAME);
        }
        if (strLastInstuction.equals("S1") || strLastInstuction.equals("")) {
            System.out.println("Sending an instrunction");
            connectionToStorage.send(msgCommandToStorage);
            FofUtility.printMessage(msgCommandToStorage);
            FofUtility.writeInstructionToFile("Slla:" + strOperation + ":S2:" + strPalletId + ":" + FofUtility.messageGenerator((Message) msgCommandToStorage), FofUtility.INSTRUCTION_STORAGE_SEQUENCE_FILENAME);
            strLastInstuction = "";
        }
        if (strLastInstuction.equals("S2") || strLastInstuction.equals("")) {
            System.out.println("Waiting for acknowledgement 1");
            msgAckOneFromStorage = connectionToStorage.receive();
            System.out.println("Acknowledgement 1 received");
            FofUtility.printMessage(msgAckOneFromStorage);
            FofUtility.writeInstructionToFile("Slla:" + strOperation + ":A1:" + strPalletId + ":" + FofUtility.messageGenerator((Message) msgAckOneFromStorage), FofUtility.INSTRUCTION_STORAGE_SEQUENCE_FILENAME);
            msgAck1 = ((Message) msgAckOneFromStorage);
            FofUtility.saveConnectionDetails(msgAck1, connectionToStorage);
            strLastInstuction = "";
        } else {
            msgAck1 = (Message) objStoredMessage;
        }
        if (isValidCommunication(msgAck1)) {
            String strCommand1 = msgAck1.getCommand();
            String strPayload1 = msgAck1.getPayload();
            if ((strCommand1.equals("A001") && strPayload1.equals(""))
                    || (strCommand1.equals("A002") && strPayload1.equals("") && !strLastInstuction.equals(""))) {
                if (strLastInstuction.equals("A1") || strLastInstuction.equals("")) {
                    System.out.println("Waiting for acknowledgement 2");
                    msgAckTwoFromStorage = connectionToStorage.receive();
                    System.out.println("Acknowledgement 2 received");
                    FofUtility.printMessage(msgAckTwoFromStorage);
                    FofUtility.writeInstructionToFile("Slla:" + strOperation + ":A2:" + strPalletId + ":" + FofUtility.messageGenerator((Message) msgAckTwoFromStorage), FofUtility.INSTRUCTION_STORAGE_SEQUENCE_FILENAME);
                    msgAck2 = ((Message) msgAckTwoFromStorage);
                    strLastInstuction = "";
                } else {
                    msgAck2 = ((Message) objStoredMessage);
                }
                if (msgAck2 == null) {
                    msgAck2 = ((Message) msgAckTwoFromStorage);
                }
                String strCommand2 = msgAck2.getCommand();
                String strPayload2 = msgAck2.getPayload();
                if (strCommand2.equals("A002")) {
                    if (strPayload2.equals("")) {
                        strSuccess = "A002";
                        System.out.println("Pallet operation successful.!");
                    }
                } else if (strCommand2.equals("F000")) {
                    if (strPayload2.equals("")) {
                        strSuccess = "F000";
                        System.out.println("Error in command!");
                    }
                } else if (strCommand2.equals("F001")) {
                    if (strPayload2.equals("")) {
                        strSuccess = "F001";
                        System.out.println("Error in Pallet operation.!");
                    }
                } else if (strCommand2.equals("F002")) {
                    if (strPayload2.equals("")) {
                        strSuccess = "F002";
                        System.out.println("Error in Pallet operation.!");
                    }
                }
            } else {
                strSuccess = strCommand1;
            }
        } else {
            System.out.println("Not a valid communication");
            strSuccess = "F000";
        }
        return strSuccess;
    }

    private boolean isValidCommunication(Message objMessage) {
        boolean isValid = false;
        if (objMessage.getSender().equals(ACK_SENDER) && objMessage.getReceiver().equals(ACK_RECEIVER)) {
            isValid = true;
        }
        return isValid;
    }

    public static void main(String[] args) {
        ServerStorageControl c = new ServerStorageControl();
        c.start();
    }

    public ClientStorageControl getClientStorageControl() {
        return clientStorageControl;
    }

    public void setClientStorageControl(ClientStorageControl clientStorageControl) {
        this.clientStorageControl = clientStorageControl;
    }
}
