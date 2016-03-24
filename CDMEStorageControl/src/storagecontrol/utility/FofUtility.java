/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storagecontrol.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import net.Connection;
import net.Message;
import net.MessageIf;
import storagecontrol.beans.StorageBean;

/**
 *
 * @author Sameer
 */
public class FofUtility {

    public final static String INSTRUCTION_STORAGE_SEQUENCE_FILENAME = "f:\\cdme\\instruction_storage.seq";
    public final static String INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME = "f:\\cdme\\instruction_storagecontrol.seq";
    public final static String INSTRUCTION_STORAGE_SEQUENCE_BKP_FILENAME = "f:\\cdme\\instruction_storage_bkp.seq";
    public final static String INSTRUCTION_STORAGE_CONTROL_SEQUENCE_BKP_FILENAME = "f:\\cdme\\instruction_storagecontrol_bkp.seq";
    private static boolean recovery = false;
    private static boolean recoveryAborted = false;

    public static void saveConnectionDetails(Message objMessage, Connection objConnection) {
        objMessage.getConnectionDetails().setSocket(objConnection.getSocket());//clients details
        objMessage.getConnectionDetails().setIn(objConnection.getIn());//clients details
        objMessage.getConnectionDetails().setOut(objConnection.getOut());//clients details
        objMessage.getConnectionDetails().setStrGuest(objConnection.getStrGuest());//clients details
        objMessage.getConnectionDetails().setStrHost(objConnection.getStrHost());//clients details
        objMessage.getConnectionDetails().setServerSocket(objConnection.getServerSocket());
        objMessage.getConnectionDetails().setIsaClient(objConnection.getIsaClient());
        objMessage.getConnectionDetails().setStrIpAddress(objConnection.getStrIpAddress());
        objMessage.getConnectionDetails().setnPort(objConnection.getnPort());
    }

    public static String readLastInstructionFromFile(String strFileName) {
        String lastLine = "";
        FileInputStream in = null;
        BufferedReader br = null;
        File filePtr = null;
        try {
            filePtr = new File(strFileName);
            in = new FileInputStream(strFileName);
            br = new BufferedReader(new InputStreamReader(in));

            String strLine = null, tmp;
            if (filePtr.exists()) {
                while ((tmp = br.readLine()) != null) {
                    strLine = tmp;
                }
                String[] strArrFullInstruction = strLine.split(":");
                lastLine = strArrFullInstruction[1] + ":" + strArrFullInstruction[2] + ":" + strArrFullInstruction[3];
                if (strFileName.equals(INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME)) {
                    lastLine = lastLine + ":" + strArrFullInstruction[4];
                } else {
                    lastLine = lastLine + ":" + strArrFullInstruction[4];
                    if (strArrFullInstruction[5] != null) {
                        lastLine = lastLine + ":" + strArrFullInstruction[5];
                    }
                }
            }
        } catch (Exception ex) {
        } finally {
            try {
                br.close();
                in.close();
            } catch (Exception ex) {
            }
            return lastLine;
        }
    }

    public static void writeInstructionToFile(String strInstrution, String strFileName) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(strFileName, true));
            out.write(strInstrution + "\n");
            out.close();
        } catch (IOException e) {
            System.out.println("Exception ");

        }
    }

    public static boolean checkCycleIsCompleteFromFile(String strFileName) {
        FileInputStream in = null;
        BufferedReader br = null;
        BufferedReader brReadFromConsole = null;
        try {
            File fileTest = new File(strFileName);
            if (fileTest.exists()) {
                in = new FileInputStream(strFileName);
                br = new BufferedReader(new InputStreamReader(in));
                brReadFromConsole = new BufferedReader(new InputStreamReader(System.in));
                String strLine = "", tmp;

                while ((tmp = br.readLine()) != null) {
                    strLine = tmp;
                }
                if (!strLine.contains("A2") && !strLine.contains("R1") && !strLine.equals("")) {
                    System.out.println("Last cycle was not completed. Do you want to Start Recovery (y/n)?");
                    String strReply = brReadFromConsole.readLine();
                    strReply = strReply.toLowerCase();
                    if (strReply.equals("n")) {
                        try {
                            recoveryAborted = true;
                            br.close();
                            in.close();
                            copyFileContents(INSTRUCTION_STORAGE_SEQUENCE_FILENAME, INSTRUCTION_STORAGE_SEQUENCE_BKP_FILENAME);
                            copyFileContents(INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME, INSTRUCTION_STORAGE_CONTROL_SEQUENCE_BKP_FILENAME);
                            (new File(INSTRUCTION_STORAGE_SEQUENCE_FILENAME)).delete();
                            (new File(INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME)).delete();
                            File file = new File(INSTRUCTION_STORAGE_SEQUENCE_FILENAME);
                            file.createNewFile();
                            file = new File(INSTRUCTION_STORAGE_CONTROL_SEQUENCE_FILENAME);
                            file.createNewFile();
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }
                        return true;
                    }
                    recovery = true;
                    return false;
                }
            } else {
                File file = new File(strFileName);
                file.createNewFile();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                br.close();
                in.close();
            } catch (Exception ex) {
            }
        }
        return true;
    }

    public static Message messageSplitter(String strCommand) {
        Message objmessage = new Message();
        try {
            objmessage.setSender(strCommand.substring(0, 2));//sender
            objmessage.setReceiver(strCommand.substring(2, 4));//reciever
            objmessage.setCommand(strCommand.substring(4, 8));//command
            objmessage.setMsgId(strCommand.substring(8, 21));//Message ID
            objmessage.setPayload(strCommand.substring(25));//Payload

        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println(ex);
        }
        return objmessage;
    }

    public static String messageGenerator(Message objmessage) {
        String Message = "";
        try {
            Message = Message + objmessage.getSender();
            Message = Message + objmessage.getReceiver();
            Message = Message + objmessage.getCommand();
            Message = Message + objmessage.getMsdId();
            Message = Message + Utility.pad4(objmessage.getPayload().length());
            Message = Message + objmessage.getPayload();
        } catch (UnknownError ex) {
            System.out.println(ex);
        }
        return Message.trim();
    }

    public static void printMessage(MessageIf objmessage) {
        String strMsg = messageGenerator((Message) objmessage);
        String strMsgType = strMsg.substring(4, 8);
        String strSource = strMsg.substring(0, 2);
        String strDest = strMsg.substring(2, 4);

        String strSourceDecode = "";
        if (strSource.contains("ST")) {
            strSourceDecode = "Control";
        } else if (strSource.contains("Sl")) {
            strSourceDecode = "Storage-Control";
        } else if (strSource.contains("la")) {
            strSourceDecode = "Storage";
        }

        String strDestDecode = "";
        if (strDest.contains("Sl")) {
            strDestDecode = "Storage-Control";
        } else if (strDest.contains("la")) {
            strDestDecode = "Storage";
        } else if (strDest.contains("ST")) {
            strDestDecode = "Control";
        }

        String strMsgTypeDecode = "";
        if (strMsgType.contains("A")) {
            strMsgTypeDecode = "Acknowledgement";
        } else if (strMsgType.contains("K")) {
            strMsgTypeDecode = "Command";
        } else if (strMsgType.contains("F")) {
            strMsgTypeDecode = "Failure report";
        }

        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("--- Message from " + strSourceDecode + " to " + strDestDecode);
        System.out.println("--- Message Type " + strMsgTypeDecode);
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("\t\tCommand\t\tMessageID\t\tPayload\t\t");
        System.out.println("\t\t" + strMsg.substring(4, 8)
                + "\t\t" + strMsg.substring(8, 21) + "\t\t" + strMsg.substring(25) + "\t\t");
        System.out.println("----------------------------------------------------------------------------------------------------------");

    }

    public static boolean isRecovery() {
        return recovery;
    }

    public static void setRecovery(boolean recovery) {
        FofUtility.recovery = recovery;
    }

    public static void createStorage() {
        StorageBean objStorageBean = StorageBean.fillStorage();
        StorageBean.serialzeStorageBean(objStorageBean);
    }

    public static boolean isRecoveryAborted() {
        return recoveryAborted;
    }

    public static void setRecoveryAborted(boolean recoveryAborted) {
        FofUtility.recoveryAborted = recoveryAborted;
    }

    public static void copyFileContents(String strSourceFileName, String strDestinationFileName) {
        File destiFile = new File(strDestinationFileName);
        File SourceFile = new File(strSourceFileName);
        try {
            if (!destiFile.exists()) {
                destiFile.createNewFile();
            }
            byte[] readData = new byte[1024];
            FileInputStream fis = new FileInputStream(SourceFile);
            FileOutputStream fos = new FileOutputStream(destiFile, true);
            int i = fis.read(readData);

            while (i != -1) {
                fos.write(readData);
                i = fis.read(readData);
            }
            fis.close();
            fos.close();
        } catch (Exception e) {
        }
    }
}
