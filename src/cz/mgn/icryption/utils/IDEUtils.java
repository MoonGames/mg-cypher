/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.icryption.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.mgn.icryption.image.IDEImage;
import javax.imageio.ImageIO;

/**
 *
 * @author indy
 */
public class IDEUtils {

    public static void loadImage(final String source, final IDEUtilsInterface utilsInterface) {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    BufferedImage image = ImageIO.read(new File(source));
                    utilsInterface.imageLoaded(source, image);
                } catch (IOException ex) {
                    Logger.getLogger(IDEUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }

    public static void writeFile(final String target, final byte[] data, final IDEUtilsInterface utilsInterface) {
        Thread t = new Thread() {

            @Override
            public void run() {
                OutputStream writer = null;
                try {
                    File targetFile = new File(target);
                    writer = new FileOutputStream(targetFile);
                    writer.write(data);
                    writer.flush();
                    writer.close();
                    utilsInterface.fileWrited(target);
                } catch (IOException ex) {
                    Logger.getLogger(IDEUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }

    public static void loadFile(final String source, final IDEUtilsInterface utilsInterface) {
        Thread t = new Thread() {

            @Override
            public void run() {
                File sourceFile = new File(source);
                InputStream reader = null;
                try {
                    reader = new FileInputStream(sourceFile);
                    int b = 0;
                    byte[] data = new byte[(int) sourceFile.length()];
                    int i = 0;
                    while ((b = reader.read()) != -1) {
                        int z = b;
                        if (z > 127) {
                            z -= 256;
                        }
                        data[i] = (byte) z;
                        i++;
                    }
                    reader.close();
                    utilsInterface.fileLoaded(source, data);
                } catch (IOException ex) {
                    Logger.getLogger(IDEUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }

    public static int[] countImageCapacity(BufferedImage image) {
        int[] cap = new int[2];
        cap[0] = IDEImage.countImageCapacity(image);
        cap[1] = cap[0] - 30;
        return cap;
    }
}
