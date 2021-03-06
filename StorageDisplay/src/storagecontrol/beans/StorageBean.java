/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storagecontrol.beans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
import Utility.Utility;

/**
 *
 * @author Sameer
 */
public class StorageBean implements Serializable {

    final static int NUMBER_OF_STORAGE_PALLETS = 15;
    final static int MAXIMUM_NUMBER_OF_SMARTIES_ON_PALLET = 91;
    final static char[] SMARTIE_COLOR = {'r', 'g', 'b', 'w', 'y'};
    final static String FILE_NAME = "../CDMEStorageControl/storagebean.ser";

    public StorageBean() {
        vtrStorageTray = new Vector<StoragePalletBean>();
    }

    public StorageBean(int nTotalNumberOfStoragePallet, int nTotalNumberOfRedSmarties, int nTotalNumberOfYellowSmarties, int nTotalNumberOfBrownSmarties, int nTotalNumberOfWhiteSmarties, int nTotalNumberOfGreenSmarties, Vector<StoragePalletBean> vtrStorageTray) {
        this.nTotalNumberOfStoragePallet = nTotalNumberOfStoragePallet;
        this.nTotalNumberOfRedSmarties = nTotalNumberOfRedSmarties;
        this.nTotalNumberOfYellowSmarties = nTotalNumberOfYellowSmarties;
        this.nTotalNumberOfBrownSmarties = nTotalNumberOfBrownSmarties;
        this.nTotalNumberOfBlueSmarties = nTotalNumberOfWhiteSmarties;
        this.nTotalNumberOfGreenSmarties = nTotalNumberOfGreenSmarties;
        vtrStorageTray = new Stack<StoragePalletBean>();
    }

    public int getnTotalNumberOfBrownSmarties() {
        return nTotalNumberOfBrownSmarties;
    }

    public void setnTotalNumberOfBrownSmarties(int nTotalNumberOfBrownSmarties) {
        this.nTotalNumberOfBrownSmarties = nTotalNumberOfBrownSmarties;
    }

    public int getnTotalNumberOfGreenSmarties() {
        return nTotalNumberOfGreenSmarties;
    }

    public void setnTotalNumberOfGreenSmarties(int nTotalNumberOfGreenSmarties) {
        this.nTotalNumberOfGreenSmarties = nTotalNumberOfGreenSmarties;
    }

    public int getnTotalNumberOfRedSmarties() {
        return nTotalNumberOfRedSmarties;
    }

    public void setnTotalNumberOfRedSmarties(int nTotalNumberOfRedSmarties) {
        this.nTotalNumberOfRedSmarties = nTotalNumberOfRedSmarties;
    }

    public int getnTotalNumberOfStoragePallet() {
        return nTotalNumberOfStoragePallet;
    }

    public void setnTotalNumberOfStoragePallet(int nTotalNumberOfStoragePallet) {
        this.nTotalNumberOfStoragePallet = nTotalNumberOfStoragePallet;
    }

    public int getnTotalNumberOfBlueSmarties() {
        return nTotalNumberOfBlueSmarties;
    }

    public void setnTotalNumberOfBlueSmarties(int nTotalNumberOfBlueSmarties) {
        this.nTotalNumberOfBlueSmarties = nTotalNumberOfBlueSmarties;
    }

    public int getnTotalNumberOfYellowSmarties() {
        return nTotalNumberOfYellowSmarties;
    }

    public void setnTotalNumberOfYellowSmarties(int nTotalNumberOfYellowSmarties) {
        this.nTotalNumberOfYellowSmarties = nTotalNumberOfYellowSmarties;
    }

    public Vector<StoragePalletBean> getVtrStorageTray() {
        return vtrStorageTray;
    }

    public void setVtrStorageTray(Vector<StoragePalletBean> vtrStorageTray) {
        this.vtrStorageTray = vtrStorageTray;
    }

    public static StorageBean fillStorage() {
        StorageBean objStorageBean = new StorageBean();
        StoragePalletBean objStoragePalletBean = null;
        String strPattern = "";
        Random randomSmartieCountGenerator = new Random(92);//Need to remove
        int[] nArrStorageTrayNumber = new int[NUMBER_OF_STORAGE_PALLETS];
        nArrStorageTrayNumber = getStorageTrayNumbers(NUMBER_OF_STORAGE_PALLETS);//Generating pallete IDs.which reruns array of 15 ID's


        objStorageBean.setnTotalNumberOfStoragePallet(NUMBER_OF_STORAGE_PALLETS);
        int i = 0;
        char[] charArrSmartieColorSequence = getSmartieColorSequence(NUMBER_OF_STORAGE_PALLETS);//Assigning color for palletes.for each color 3 pallets must.
        while (i < NUMBER_OF_STORAGE_PALLETS) {

//            char charColor = SMARTIE_COLOR[randomColorGenerator.nextInt(5)];
            char charColor = charArrSmartieColorSequence[i];
//            int nSmartieCount = randomSmartieCountGenerator.nextInt(92);
            int nSmartieCount = 91;
            switch (charColor) {
                case 'r':
                    objStorageBean.setnTotalNumberOfRedSmarties(objStorageBean.getnTotalNumberOfRedSmarties() + nSmartieCount);
                    break;
                case 'g':
                    objStorageBean.setnTotalNumberOfGreenSmarties(objStorageBean.getnTotalNumberOfGreenSmarties() + nSmartieCount);
                    break;
                case 'w':
                    objStorageBean.setnTotalNumberOfBrownSmarties(objStorageBean.getnTotalNumberOfBrownSmarties() + nSmartieCount);
                    break;
                case 'b':
                    objStorageBean.setnTotalNumberOfBlueSmarties(objStorageBean.getnTotalNumberOfBlueSmarties() + nSmartieCount);
                    break;
                case 'y':
                    objStorageBean.setnTotalNumberOfYellowSmarties(objStorageBean.getnTotalNumberOfYellowSmarties() + nSmartieCount);
                    break;
                case '-':
                    nSmartieCount = 0;
                    break;
                default:
                    nSmartieCount = 0;
            }
            if (charColor == '-') {
                nSmartieCount = 0;
            }
            objStoragePalletBean = new StoragePalletBean();
            strPattern = getStoragePattern(nSmartieCount, charColor);
            objStoragePalletBean.setnSmartieCount(nSmartieCount);
            objStoragePalletBean.setStrSmartieColor((charColor + ""));
            objStoragePalletBean.setStrPalletId(Utility.pad3(nArrStorageTrayNumber[i]));
            objStoragePalletBean.setStrStorageLocation(Utility.pad2(i));
            objStoragePalletBean.setStrStoragePallet(strPattern);
            objStorageBean.vtrStorageTray.add(objStoragePalletBean);
//            if(i==1){
//                objStoragePalletBean.setStrPalletId("-");
//            }
            i++;
        }
        return objStorageBean;
    }

    private static String getStoragePattern(int nCount, char charColor) {
        int i = 0;
        String strStoragePattern = "";
        while (i < nCount) {
            strStoragePattern += charColor;
            i++;
        }
        while (i < MAXIMUM_NUMBER_OF_SMARTIES_ON_PALLET) {
            strStoragePattern += '-';
            i++;
        }
        return strStoragePattern;

    }

    private static int[] getStorageTrayNumbers(int nNumberOfTrays) {
        Random randomStorageTrayGenerator = new Random(999);
        int nRandomStorageTrayNumber = 0;

        int[] nArrStorageTrayNumber = new int[nNumberOfTrays];
        HashSet set = new HashSet();
        nRandomStorageTrayNumber = randomStorageTrayGenerator.nextInt(1000);
        for (int i = 0; i < nNumberOfTrays; i++) {

            while (set.contains(nRandomStorageTrayNumber) && nRandomStorageTrayNumber != 0) {
                nRandomStorageTrayNumber = randomStorageTrayGenerator.nextInt(1000);
            }
            nArrStorageTrayNumber[i] = nRandomStorageTrayNumber;
            set.add(nRandomStorageTrayNumber);
        }
        return nArrStorageTrayNumber;
    }

    private static char[] getSmartieColorSequence(int nNumberOfTrays) {
        int nRandomStorageTrayNumber = 0;
        Random randomColorGenerator = new Random(5);
        char[] charArrSmartieColorSequence = new char[nNumberOfTrays];
        HashMap map = new HashMap();
        map.put(SMARTIE_COLOR[0], 0);
        map.put(SMARTIE_COLOR[1], 0);
        map.put(SMARTIE_COLOR[2], 0);
        map.put(SMARTIE_COLOR[3], 0);
        map.put(SMARTIE_COLOR[4], 0);
//        map.put(SMARTIE_COLOR[5],0);
        for (int i = 0; i < nNumberOfTrays;) {

            char tmpChar = SMARTIE_COLOR[randomColorGenerator.nextInt(5)];
            int tempInt = (Integer) (map.get(tmpChar));
            if (tempInt == 3) {
                continue;
            }
            charArrSmartieColorSequence[i] = tmpChar;
            tempInt++;
            i++;
            map.put(tmpChar, tempInt);
        }
        return charArrSmartieColorSequence;
    }

    public StoragePalletBean getPalletIdAndPattern(String requestedcolor) {
        StoragePalletBean objStoragePalletBean = new StoragePalletBean();
        Iterator ite = vtrStorageTray.iterator();
        String strTrayIdAndPattern = "";
        while (ite.hasNext()) {
            objStoragePalletBean = (StoragePalletBean) ite.next();
            String color = objStoragePalletBean.getStrSmartieColor();
            if (requestedcolor.equals(color)) {
                strTrayIdAndPattern = objStoragePalletBean.getStrPalletId() + objStoragePalletBean.getStrStoragePallet();
                break;
            }
        }
        return (objStoragePalletBean);
    }
    private int nTotalNumberOfStoragePallet;
    private int nTotalNumberOfRedSmarties;
    private int nTotalNumberOfYellowSmarties;
    private int nTotalNumberOfBrownSmarties;
    private int nTotalNumberOfBlueSmarties;
    private int nTotalNumberOfGreenSmarties;
    private Vector<StoragePalletBean> vtrStorageTray;

    public int getEmptyTrays() {
        int nTotalEmptyCount = 0;
        StoragePalletBean objStoragePalletBean = null;
        Iterator ite = vtrStorageTray.iterator();

        while (ite.hasNext()) {
            objStoragePalletBean = (StoragePalletBean) ite.next();
            if (objStoragePalletBean.getnSmartieCount() == 0) {
                nTotalEmptyCount = nTotalEmptyCount + 1;
            }
        }
        return nTotalEmptyCount;
    }

    public String putPalletIntoStorage(String StrPalletId, char charColor, int nSmartieCount, String strPattern) {
        StoragePalletBean objStoragePalletBean = null;
        int nTraylocation = -1;
        Iterator ite = vtrStorageTray.iterator();
        while (ite.hasNext()) {
            objStoragePalletBean = (StoragePalletBean) ite.next();
            if (objStoragePalletBean.getStrPalletId().equals(StrPalletId)) {
                return "F009";//Duplicate PalleteId
            } else if (objStoragePalletBean.getStrPalletId().equals("-")) {
                nTraylocation = Integer.parseInt(objStoragePalletBean.getStrStorageLocation());
            }
        }
        if (nTraylocation == -1) {
            return "F006";//no storage space for pallet
        } else {
            objStoragePalletBean = null;
            objStoragePalletBean = vtrStorageTray.elementAt(nTraylocation);
            objStoragePalletBean.setStrPalletId(StrPalletId);
            objStoragePalletBean.setStrSmartieColor((charColor + ""));
            objStoragePalletBean.setnSmartieCount(nSmartieCount);
            objStoragePalletBean.setStrStoragePallet(strPattern);
            switch (charColor) {
                case 'r':
                    this.setnTotalNumberOfRedSmarties(this.getnTotalNumberOfRedSmarties() + nSmartieCount);
                    break;
                case 'g':
                    this.setnTotalNumberOfGreenSmarties(this.getnTotalNumberOfGreenSmarties() + nSmartieCount);
                    break;
                case 'w':
                    this.setnTotalNumberOfBrownSmarties(this.getnTotalNumberOfBrownSmarties() + nSmartieCount);
                    break;
                case 'b':
                    this.setnTotalNumberOfBlueSmarties(this.getnTotalNumberOfBlueSmarties() + nSmartieCount);
                    break;
                case 'y':
                    this.setnTotalNumberOfYellowSmarties(this.getnTotalNumberOfYellowSmarties() + nSmartieCount);
                    break;
                case '-':
                    nSmartieCount = 0;
                    break;
                default:
                    nSmartieCount = 0;
            }
            return "A002";//For Ack2
        }


    }

    public static boolean serialzeStorageBean(StorageBean objStorageBean) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        boolean isWritten = false;
        try {
            fos = new FileOutputStream(FILE_NAME);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(objStorageBean);
            isWritten = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                oos.close();
                fos.close();
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
        }
        return isWritten;
    }

    public static StorageBean deSerialzeStorageBean() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        StorageBean tmpStorageBean = null;
        try {
            fis = new FileInputStream(FILE_NAME);
            ois = new ObjectInputStream(fis);
            tmpStorageBean = (StorageBean) ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ois.close();
                fis.close();
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
        }
        return tmpStorageBean;
    }
}
