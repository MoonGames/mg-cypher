/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.icryption.graphics;

/**
 *
 * @author indy
 */
public interface FileChooserInterface {

    public void fileChoosed(String filePath, int code);

    public void fileWasnChoosed(int code);
}
