/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storagecontrol.beans;

import java.io.Serializable;

/**
 * Bean for storage shelf.
 * Store information like Pallet ID, Pallet Location,Pallete Color, Pattern of the color,
 * Total count of smartie color.
 * @author Sameer
 */
public class StoragePalletBean implements Serializable{

    private static final long serialVersionUID = -1287501176374518774L;
    public StoragePalletBean() {
    }

    public String getStrStorageLocation() {
        return strStorageLocation;
    }

    public void setStrStorageLocation(String strStorageLocation) {
        this.strStorageLocation = strStorageLocation;
    }



    public String getStrPalletId() {
        return strPalletId;
    }

    public void setStrPalletId(String strPalletId) {
        this.strPalletId = strPalletId;
    }

    public String getStrStoragePallet() {
        return strStoragePallet;
    }

    public void setStrStoragePallet(String strStoragePallet) {
        this.strStoragePallet = strStoragePallet;
    }

    public int getnSmartieCount() {
        return nSmartieCount;
    }

    public void setnSmartieCount(int nSmartieCount) {
        this.nSmartieCount = nSmartieCount;
    }

    public String getStrSmartieColor() {
        return strSmartieColor;
    }

    public void setStrSmartieColor(String strSmartieColor) {
        this.strSmartieColor = strSmartieColor;
    }

    
    
    String strStorageLocation;//00 to 14...shelf Number
    String strPalletId;//000 to 999
    String strStoragePallet;//Pattern of the color
    String strSmartieColor;//Color of the pallete
    int nSmartieCount;// Total smartie count of that color
}
