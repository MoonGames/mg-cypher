/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.icryption.main;

/**
 *
 * @author indy
 */
public interface IDECoderInterface {

    public void dataSuccessfullyEncrypted(String file);

    public void encryptionError(String file, String error);
    
    public void dataSuccessfullyDecoded(String file, byte[] data);
    
    public void decodingError(String file, String error);
}
