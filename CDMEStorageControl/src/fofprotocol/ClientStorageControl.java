/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fofprotocol;

import java.util.ArrayList;
import net.Connection;
import net.Message;
import net.MessageIf;
import storagecontrol.beans.StorageBean;
import storagecontrol.beans.StoragePalletBean;
import storagecontrol.beans.AckMessageBean;
import storagecontrol.utility.FofUtility;
import storagecontrol.utility.Utility;

/**
 *
 * This class is used to connect to the Control module.
 * For the connection from control module the Storage-Control will act as a Client.
 * @author Sameer
 */
public class ClientStorageControl extends Thread {

    private StorageBean storageBean;
    private ServerStorageControl serverStorageControl;
    private Connection connectionToControl;
    private String strLastInstuction = "";
    private MessageIf objStoredMessage;
    public static boolean bConnectionChanged = false;
    private static final String MSG_SENDER = "ST";
    private static final String MSG_RECEIVER = "Sl";

    @Override
    public void run() {
        begin();
    }

    public void begin() {
        String strFullInstruction = FofUtility.readLastInstructionFromFile(FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
        boolean isCycleComplete = FofUtility.checkCycleIsCompleteFromFile(FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
        AckMessageBean objAckMessage = null;//changed by Naresh to get the multiple return parameters
        Message message = null;
        while (true) {
            try {
                if (connectionToControl == null) {
                    connectionToControl = new Connection("192.168.157.80", 8001, "Sl", "ST");
                    try {
                        Thread.sleep(5000);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }

                    if (!isCycleComplete) {
                        try {
                            String[] strArrTmp = strFullInstruction.split(":");
                            strLastInstuction = strArrTmp[1];
                            String strInstruction = strArrTmp[2];
                            strInstruction = strInstruction + ":" + strArrTmp[3];
                            objStoredMessage = FofUtility.messageSplitter(strInstruction);
                        } catch (Exception ex) {
                        }
                        isCycleComplete = true;
                    } else {
                        if (FofUtility.isRecoveryAborted()) {
                            strLastInstuction = "";
                            String[] strArrTmp = strFullInstruction.split(":");
                            String strInstruction = strArrTmp[2];
                            strInstruction = strInstruction + ":" + strArrTmp[3];
                            objStoredMessage = FofUtility.messageSplitter(strInstruction);
                            FofUtility.saveConnectionDetails((Message) objStoredMessage, connectionToControl);
                            objStoredMessage.ack("F999", "");
                            objStoredMessage = null;
                            FofUtility.setRecoveryAborted(false);
                        }
                    }
                }
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            MessageIf msg = null;
            if (strLastInstuction.equals("")) {
                FofUtility.writeInstructionToFile("STSl:Receive:R1:0000", FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
            }
            if (strLastInstuction.equals("R1") || strLastInstuction.equals("")) {
                System.out.println("Waiting for instruction..");
                msg = connectionToControl.receive();
                System.out.println("Instruction received..");
                FofUtility.printMessage(msg);
                FofUtility.writeInstructionToFile("STSl:Receive:R2:" + FofUtility.messageGenerator((Message) msg), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                message = ((Message) msg);
                strLastInstuction = "";
            } else {
                message = (Message) objStoredMessage;

            }
            //Saving the connection details inside message object
            //This will later on used to send an ack.
            FofUtility.saveConnectionDetails(message, connectionToControl);

            boolean bCheckValidCommunication = isValidCommunication(message);

            if (bCheckValidCommunication) {
                if (strLastInstuction.equals("R2") || strLastInstuction.equals("")) {
                    message.ack("A001", "");
                    System.out.println("Acknowledgement 1 Sent");
                    FofUtility.writeInstructionToFile("STSl:Ack:A1:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                    strLastInstuction = "";
                }
                if (message.getCommand().endsWith("K001")) {//sumedh & Ashish
                    try {
                        char payload = message.getPayload().charAt(0);
                        objAckMessage = getTotalNumberOfSmarties(payload);
                        message.ack(objAckMessage.getStrCommand(), objAckMessage.getStrPayload());
                        System.out.println("Acknowledgement 2 Sent");
                        FofUtility.writeInstructionToFile("STSl:Ack:A2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else if (message.getCommand().endsWith("K002")) {//kedar & Thendral....
                    storageBean = StorageBean.deSerialzeStorageBean();
                    //objAckMessage=null;
                    if (storageBean != null) {
                        String strPayload = message.getPayload(); // Get Payload from message
                        char chSmartieColor = strPayload.charAt(0); // Separate payload to get color
                        String strRequestedSmartie = strPayload.substring(1); // Separate payload to get requested no. of smarties
                        try {
                            int intTotalSmarties = -1;
                            int intRequestedSmarties = -1;
                            switch (chSmartieColor) {
                                case 'r':
                                    intTotalSmarties = storageBean.getnTotalNumberOfRedSmarties(); // Get total no. of smarties in storage
                                    intRequestedSmarties = Integer.parseInt(strRequestedSmartie);
                                    objAckMessage = sendAcknowledementForK002(intRequestedSmarties, intTotalSmarties);
                                    break;
                                case 'g':
                                    intTotalSmarties = storageBean.getnTotalNumberOfGreenSmarties();
                                    intRequestedSmarties = Integer.parseInt(strRequestedSmartie);
                                    objAckMessage = sendAcknowledementForK002(intRequestedSmarties, intTotalSmarties);
                                    break;
                                case 'b':
                                    intTotalSmarties = storageBean.getnTotalNumberOfBlueSmarties();
                                    intRequestedSmarties = Integer.parseInt(strRequestedSmartie);
                                    objAckMessage = sendAcknowledementForK002(intRequestedSmarties, intTotalSmarties);
                                    break;
                                case 'y':
                                    intTotalSmarties = storageBean.getnTotalNumberOfYellowSmarties();
                                    intRequestedSmarties = Integer.parseInt(strRequestedSmartie);
                                    objAckMessage = sendAcknowledementForK002(intRequestedSmarties, intTotalSmarties);
                                    break;
                                case 'w':
                                    intTotalSmarties = storageBean.getnTotalNumberOfBrownSmarties();
                                    intRequestedSmarties = Integer.parseInt(strRequestedSmartie);
                                    objAckMessage = sendAcknowledementForK002(intRequestedSmarties, intTotalSmarties);
                                    break;
                                default:
                                    objAckMessage = new AckMessageBean("F003", "");
                            }
                        } catch (NumberFormatException e) {
                            objAckMessage = new AckMessageBean("F004", "");//Unknown Command and Syntax
                        }
                    } else {
                        objAckMessage = new AckMessageBean("F999", "");//fatel error
                    }
                    message.ack(objAckMessage.getStrCommand(), objAckMessage.getStrPayload());
                    System.out.println("Acknowledgement 2 Sent");
                    FofUtility.writeInstructionToFile("STSl:Ack:A2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);

                } else if (message.getCommand().endsWith("K003")) {//Naresh & Chiru
                    storageBean = StorageBean.deSerialzeStorageBean();
                    if (storageBean != null) {
                        int nEmptyTrayCount = storageBean.getEmptyTrays();
                        message.ack("A002", Utility.pad3(nEmptyTrayCount));
                        System.out.println("Acknowledgement 2 Sent");
                    } else {
                        message.ack("F999", "");//Fatel Error(if DeSerialze is not success)
                        System.out.println("Acknowledgement 2 Sent");
                    }
                    FofUtility.writeInstructionToFile("STSl:Ack:A2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);

                } else if (message.getCommand().endsWith("K004")) {//Naresh & Chiru
                    if (!strLastInstuction.equals("U2") || strLastInstuction.equals("")) {
                        objAckMessage = putStoragePallet(message.getPayload(), message);
                    } 
                    message.ack(objAckMessage.getStrCommand(), objAckMessage.getStrPayload());
                    System.out.println("Acknowledgement 2 Sent");
                    FofUtility.writeInstructionToFile("STSl:Ack:A2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                } else if (message.getCommand().endsWith("K005")) {//sumedh & Ashish
                    try {
                        String strRequirement = message.getPayload();
                        objAckMessage = getPalletIdAndPatternFromStorage(strRequirement, message);
                        message.ack(objAckMessage.getStrCommand(), objAckMessage.getStrPayload());
                        System.out.println("Acknowledgement 2 Sent");
                        FofUtility.writeInstructionToFile("STSl:Ack:A2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else if (message.getCommand().endsWith("K006")) {//kedar & Thendral                    
                    FofUtility.writeInstructionToFile("STSl:Ack:A2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                } else {
                    if (strLastInstuction.equals("U2")) {
                        message.ack(message.getCommand(), message.getPayload()); // Unknown command
                        FofUtility.writeInstructionToFile("STSl:Ack:A2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                        FofUtility.setRecovery(false);
                    } else {
                        message.ack("F000", ""); // Unknown command
                        FofUtility.writeInstructionToFile("STSl:Ack:A2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                    }
                }
            } else {
                message.ack("F000", "");//Unknown Command (Invalid Sender & Receiver)
                FofUtility.writeInstructionToFile("STSl:Ack:A2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
            }

            //Replacing the original connection details if the connection details were changed
            if (bConnectionChanged) {
                connectionToControl = message.getConnectionDetails();
                bConnectionChanged = false;
            }
            strLastInstuction = "";
        }
    }

    private AckMessageBean putStoragePallet(String strRequirement, Message message) {
        AckMessageBean objAck = null;
        String StrPalletId = strRequirement.substring(0, 3);
        String strPattern = strRequirement.substring(3);
        char charColor = '-';
        boolean bInvaidPattern = false;
        boolean bInvaidJunkPattern = false;
        int nSmartieCount = 0;
        if (strPattern.indexOf("r") > -1) {
            charColor = 'r';
            bInvaidPattern = chkInvaidPattern("g", "b", "w", "y", strPattern);
        } else if (strPattern.indexOf("g") > -1) {
            charColor = 'g';
            bInvaidPattern = chkInvaidPattern("r", "b", "w", "y", strPattern);
        } else if (strPattern.indexOf("b") > -1) {
            charColor = 'b';
            bInvaidPattern = chkInvaidPattern("r", "g", "w", "y", strPattern);
        } else if (strPattern.indexOf("w") > -1) {
            charColor = 'w';
            bInvaidPattern = chkInvaidPattern("r", "g", "b", "y", strPattern);
        } else if (strPattern.indexOf("y") > -1) {
            charColor = 'y';
            bInvaidPattern = chkInvaidPattern("r", "g", "w", "b", strPattern);
        }
        ArrayList arList = new ArrayList();
        arList.add('r');
        arList.add('g');
        arList.add('b');
        arList.add('w');
        arList.add('y');
        arList.add('-');

        for (int i = 0; i < strPattern.length(); i++) {
            if (!arList.contains(strPattern.charAt(i))) {
                bInvaidJunkPattern = true;
            }
            if (strPattern.charAt(i) == '-') {
                nSmartieCount = nSmartieCount + 1;
            }
        }
        nSmartieCount = 91 - nSmartieCount;//get Samrties count
        if (nSmartieCount == 0) {
            objAck = new AckMessageBean("F007", "");
            return objAck;//Empty Pallet
        }
        if (charColor == '-') {
            objAck = new AckMessageBean("F003", "");
            return objAck;//Invalid color
        }
        if ((bInvaidPattern) || (bInvaidJunkPattern)) {
            objAck = new AckMessageBean("F005", "");
            return objAck;//Invalid Pattern
        }
        String strAck2 = "";
        storageBean = StorageBean.deSerialzeStorageBean();
        if (storageBean != null) {
            strAck2 = storageBean.putPalletIntoStorage(StrPalletId, charColor, nSmartieCount, strPattern);
            String strSuccess = "";
            if (strAck2.startsWith("F")) {
                objAck = new AckMessageBean(strAck2, "");
                message.setCommand(objAck.getStrCommand());
                message.setPayload(objAck.getStrPayload());
                return objAck;
            }
            strSuccess = serverStorageControl.storagePalletOperation(StrPalletId, 1);
            if (strSuccess.equals("F001")) {//From Storage to Storage control
                objAck = new AckMessageBean("F008", "");
                return objAck;//Mechanical error
            } else if (strSuccess.equals("F000")) {//From Storage to Storage control
                objAck = new AckMessageBean("F000", "");
                return objAck;//Invalid Communication
            } else if (strSuccess.equals("A002")) {
                boolean bResult = false;
                FofUtility.writeInstructionToFile("STSl:Upd:U1:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                if (strLastInstuction.equals("U1") || strLastInstuction.equals("")) {
                    bResult = StorageBean.serialzeStorageBean(storageBean);

                }
                if (bResult) {
                    objAck = new AckMessageBean(strAck2, "");
                    message.setCommand(objAck.getStrCommand());
                    message.setPayload(objAck.getStrPayload());
                    FofUtility.writeInstructionToFile("STSl:Upd:U2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                    return objAck;
                } else {
                    objAck = new AckMessageBean("F999", "");
                    message.setCommand(objAck.getStrCommand());
                    message.setPayload(objAck.getStrPayload());
                    FofUtility.writeInstructionToFile("STSl:Upd:U2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                    return objAck;//Fatel Error(if serialze is not success)
                }
            }
        } else {
            objAck = new AckMessageBean("F999", "");
            return objAck;//Fatel Error(if deserialze is not success)
        }

        objAck = new AckMessageBean("F999", "");
        return objAck;//Unexpected Error(if deserialze is not success)
    }

    private boolean chkInvaidPattern(String color1, String color2, String color3, String color4, String Pattern) {
        if (Pattern.indexOf(color1) > -1 || Pattern.indexOf(color2) > -1 || Pattern.indexOf(color3) > -1 || Pattern.indexOf(color4) > -1) {
            return true;
        } else {
            return false;
        }
    }

    public ServerStorageControl getServerStorageControl() {
        return serverStorageControl;
    }

    public void setServerStorageControl(ServerStorageControl serverStorageControl) {
        this.serverStorageControl = serverStorageControl;
    }

    public static void main(String[] args) throws Exception {
        ClientStorageControl s = new ClientStorageControl();
        s.begin();
    }

    private AckMessageBean getTotalNumberOfSmarties(char payload) {
        int iTotalSmarties = -1;
        storageBean = StorageBean.deSerialzeStorageBean();
        if (storageBean != null) {
            switch (payload) {
                case 'r':
                    iTotalSmarties = storageBean.getnTotalNumberOfRedSmarties();// Get Total Red Smarties
                    break;
                case 'g':
                    iTotalSmarties = storageBean.getnTotalNumberOfGreenSmarties();// Get Total Green Smarties
                    break;
                case 'b':
                    iTotalSmarties = storageBean.getnTotalNumberOfBlueSmarties();// Get Total Blue Smarties
                    break;
                case 'y':
                    iTotalSmarties = storageBean.getnTotalNumberOfYellowSmarties();// Get Total Yellow Smarties
                    break;
                case 'w':
                    iTotalSmarties = storageBean.getnTotalNumberOfBrownSmarties();// Get Total Brown Smarties
                    break;
                default:
                    return (new AckMessageBean("F003", ""));// Invalid color error if object is not deserliazed
            }
            return (new AckMessageBean("A002", Utility.pad3(iTotalSmarties)));
        } else {
            return (new AckMessageBean("F999", ""));// Fatal error if object is not deserliazed
        }


    }

    private AckMessageBean retrievePallete(String strRequestedSmartieColor, String strRequestedSmarties, Message message) {
        AckMessageBean objAck = null;
        StoragePalletBean objStoragePalletBean = null;
        storageBean = StorageBean.deSerialzeStorageBean();
        if (storageBean != null) {
            int iTotalSmarties = findTotalRequestedColourSmarties(storageBean, strRequestedSmartieColor);
            if (Integer.parseInt(strRequestedSmarties) <= iTotalSmarties && Integer.parseInt(strRequestedSmarties) > 0) {
                objStoragePalletBean = storageBean.getPalletIdAndPattern(strRequestedSmartieColor);
                String strAckFromStorage = serverStorageControl.storagePalletOperation(objStoragePalletBean.getStrPalletId().substring(0, 3), 2);
                if (strAckFromStorage.equals("A002")) {
                    switch (strRequestedSmartieColor.charAt(0)) {
                        case 'r':
                            storageBean.setnTotalNumberOfRedSmarties(storageBean.getnTotalNumberOfRedSmarties() - objStoragePalletBean.getnSmartieCount());
                            break;
                        case 'g':
                            storageBean.setnTotalNumberOfGreenSmarties(storageBean.getnTotalNumberOfGreenSmarties() - objStoragePalletBean.getnSmartieCount());
                            break;
                        case 'y':
                            storageBean.setnTotalNumberOfYellowSmarties(storageBean.getnTotalNumberOfYellowSmarties() - objStoragePalletBean.getnSmartieCount());
                            break;
                        case 'b':
                            storageBean.setnTotalNumberOfBlueSmarties(storageBean.getnTotalNumberOfBlueSmarties() - objStoragePalletBean.getnSmartieCount());
                            break;
                        case 'w':
                            storageBean.setnTotalNumberOfBrownSmarties(storageBean.getnTotalNumberOfBrownSmarties() - objStoragePalletBean.getnSmartieCount());
                            break;
                        default:
                            objAck = new AckMessageBean("F003", "");
                            return objAck;//Invalid Color
                    }
                    String strPaletteIdAndPattern = objStoragePalletBean.getStrPalletId() + objStoragePalletBean.getStrStoragePallet() + "";
                    objStoragePalletBean.setStrPalletId("-");
                    objStoragePalletBean.setStrSmartieColor("");
                    objStoragePalletBean.setStrStoragePallet("");
                    objStoragePalletBean.setnSmartieCount(0);
                    boolean bResult = false;
                    FofUtility.writeInstructionToFile("STSl:Upd:U1:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                    if (strLastInstuction.equals("A1") || strLastInstuction.equals("U1") || strLastInstuction.equals("")) {
                        bResult = StorageBean.serialzeStorageBean(storageBean);

                    }
                    if (bResult) {
                        objAck = new AckMessageBean("A002", strPaletteIdAndPattern);
                        message.setCommand(objAck.getStrCommand());
                        message.setPayload(objAck.getStrPayload());
                        FofUtility.writeInstructionToFile("STSl:Upd:U2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);

                        return objAck;
                    } else {
                        objAck = new AckMessageBean("F999", "");
                        message.setCommand(objAck.getStrCommand());
                        message.setPayload(objAck.getStrPayload());
                        FofUtility.writeInstructionToFile("STSl:Upd:U2:" + FofUtility.messageGenerator((Message) message), FofUtility.INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);

                        return objAck;//Fatel Error(if serialze is not success)
                    }
                } else {
                    if (strAckFromStorage.equals("F000")) {
                        objAck = new AckMessageBean("F000", "");
                        return objAck;//Unknown Command
                    } else if (strAckFromStorage.equals("F002")) {
                        objAck = new AckMessageBean("F011", "");
                        return objAck;//Mechanical error
                    } else {
                        //you shouldn't be here.........
                        objAck = new AckMessageBean("F999", "");
                        return objAck;//Fatel Error(if deserialze is not success)
                    }
                }
            } else {
                if (Integer.parseInt(strRequestedSmarties) <= 0) {
                    objAck = new AckMessageBean("F004", "");
                    return objAck;//Invalid amount
                } else {
                    objAck = new AckMessageBean("F010", "");
                    return objAck;//Not enough smarties in storage
                }
            }
        } else {
            objAck = new AckMessageBean("F999", "");
            return objAck;// Fatal error if object is not deserliazed
        }
    }

    private AckMessageBean getPalletIdAndPatternFromStorage(String strRequirement, Message message) {
        String strRequestedSmartieColor = strRequirement.substring(0, 1);
        String strRequestedSmarties = strRequirement.substring(1);
        String strColor = "rgbwy";
        if (strColor.contains(strRequestedSmartieColor)) {
            return retrievePallete(strRequestedSmartieColor, strRequestedSmarties, message);
        } else {
            return (new AckMessageBean("F003", ""));//Invalid colour
        }
    }

    private AckMessageBean sendAcknowledementForK002(int intRequestedSmarties, int intTotalSmarties) {

        if (intRequestedSmarties <= intTotalSmarties && intRequestedSmarties > 0) {
            return (new AckMessageBean("A002", ""));
        } else if (intRequestedSmarties <= 0) {
            return (new AckMessageBean("F004", ""));
        } else {
            return (new AckMessageBean("F002", Utility.pad3(intTotalSmarties)));
        }
    }

    private int findTotalRequestedColourSmarties(StorageBean storageBean, String strRequestedSmartieColor) {
        int iTotalSmarties = -1;
        switch (strRequestedSmartieColor.charAt(0)) {
            case 'r':
                return iTotalSmarties = storageBean.getnTotalNumberOfRedSmarties();
            case 'g':
                return iTotalSmarties = storageBean.getnTotalNumberOfGreenSmarties();
            case 'y':
                return iTotalSmarties = storageBean.getnTotalNumberOfYellowSmarties();
            case 'b':
                return iTotalSmarties = storageBean.getnTotalNumberOfBlueSmarties();
            case 'w':
                return iTotalSmarties = storageBean.getnTotalNumberOfBrownSmarties();
            default:
                //We shouldn't be here
                return iTotalSmarties;
        }
    }

    private boolean isValidCommunication(Message objMessage) {
        boolean isValid = false;
        if (objMessage.getSender().equals(MSG_SENDER) && objMessage.getReceiver().equals(MSG_RECEIVER)) {
            isValid = true;
        }
        return isValid;
    }
}
