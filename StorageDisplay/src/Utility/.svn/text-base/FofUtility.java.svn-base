/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import storagecontrol.beans.StorageBean;
import storagecontrol.beans.StoragePalletBean;

/**
 *
 * @author Sameer
 */
public class FofUtility {

    public static String storageToString(StorageBean objStorageBean) {
        String strOutput = "";
        try {
            strOutput += "Red:" + objStorageBean.getnTotalNumberOfRedSmarties();
            strOutput += "\n";
            strOutput += "Brown:" + objStorageBean.getnTotalNumberOfBrownSmarties();
            strOutput += "\n";
            strOutput += "Yellow:" + objStorageBean.getnTotalNumberOfYellowSmarties();
            strOutput += "\n";
            strOutput += "Green:" + objStorageBean.getnTotalNumberOfGreenSmarties();
            strOutput += "\n";
            strOutput += "Blue:" + objStorageBean.getnTotalNumberOfBlueSmarties();
            strOutput += "\n";
            strOutput += "-----------------------------------------------------------------\n";
            Iterator ite = objStorageBean.getVtrStorageTray().iterator();
            while (ite.hasNext()) {
                StoragePalletBean oStoragePalletBean = (StoragePalletBean) ite.next();
                strOutput += oStoragePalletBean.getStrStorageLocation() + "";
                strOutput += ":";
                strOutput += oStoragePalletBean.getStrPalletId();
                strOutput += ":";
                strOutput += oStoragePalletBean.getStrStoragePallet();
                strOutput += ":";
                strOutput += oStoragePalletBean.getStrSmartieColor();
                strOutput += ":";
                strOutput += oStoragePalletBean.getnSmartieCount() + "";
                strOutput += "\n";
            }

        } catch (Exception ex) {
        }
        return strOutput;
    }

    public static String readFromFile(String strFileName) {
        String strFContent = "";
        FileInputStream in = null;
        BufferedReader br = null;
        try {
            in = new FileInputStream(strFileName);
            br = new BufferedReader(new InputStreamReader(in));

            String strLine = null, tmp;

            while ((tmp = br.readLine()) != null) {
                strFContent += tmp;
                strFContent += "\n";
            }
        } catch (Exception ex) {
        } finally {
            try {
                br.close();
                in.close();
            } catch (IOException ex) {
            }
            return strFContent;
        }
    }
}
