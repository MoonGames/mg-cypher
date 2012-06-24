/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.icryption.code.key;

/**
 *
 * @author indy
 */
public class IDEKeyData {
    
    protected String salt = "";
    protected byte[] keyData = null;
    
    public IDEKeyData(String salt, byte[] keyData) {
        this.salt = salt;
        this.keyData = keyData;
    }
    
    public String getSalt() {
        return salt;
    }
    
    public byte[] getKeyData() {
        return keyData;
    }
}
