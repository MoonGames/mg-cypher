/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.icryption.utils;

import java.util.Random;

/**
 *
 * @author indy
 */
public class Password {
        
    /** vygeneruje heslo o nahodne delce 8 - 14 znaku */
    public static String generatePass() {
        String pass = "";
        Random r = new Random(System.currentTimeMillis());
        String usable = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+-!";
        for (int i = 0; i < (r.nextInt(6) + 8); i++) {
            pass = pass + usable.charAt(r.nextInt(usable.length()));
        }
        return pass;
    }
}
