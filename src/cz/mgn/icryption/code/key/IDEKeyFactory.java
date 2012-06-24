/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.icryption.code.key;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author indy
 */
public class IDEKeyFactory {

    public static IDEKeyData generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String salt = BCrypt.gensalt(15);
        String key1 = BCrypt.hashpw(password, salt);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] keyData = md.digest(key1.getBytes("UTF-8"));
        return new IDEKeyData(salt, keyData);
    }

    public static IDEKeyData generateKeyKnownSalt(String password, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String key1 = BCrypt.hashpw(password, salt);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] keyData = md.digest(key1.getBytes("UTF-8"));
        return new IDEKeyData(salt, keyData);
    }
}
