/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.icryption.utils;

import java.awt.image.BufferedImage;

/**
 *
 * @author indy
 */
public interface IDEUtilsInterface {

    public void imageLoaded(String file, BufferedImage image);

    public void fileLoaded(String file, byte[] data);

    public void fileWrited(String file);
}
