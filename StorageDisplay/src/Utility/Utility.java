/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

/**
 *
 * @author Sameer
 */
public class Utility {

    private static final String[] ZEROS4 = {"", "0", "00", "000", "0000"};

    public static String pad4(int length) {
        if (length < 0 || length > 999) {
            throw new IllegalArgumentException("out of range");
        }
        String s = Integer.toString(length);
        s = ZEROS3[4 - s.length()] + s;
        return s;
    }
    private static final String[] ZEROS3 = {"", "0", "00", "000"};

    public static String pad3(int length) {
        if (length < 0 || length > 999) {
            throw new IllegalArgumentException("out of range");
        }
        String s = Integer.toString(length);
        s = ZEROS3[3 - s.length()] + s;
        return s;
    }
    private static final String[] ZEROS2 = {"", "0", "00"};

    public static String pad2(int length) {
        if (length < 0 || length > 99) {
            throw new IllegalArgumentException("out of range");
        }
        String s = Integer.toString(length);
        s = ZEROS2[2 - s.length()] + s;
        return s;
    }


}
