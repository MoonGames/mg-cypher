/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.icryption.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import cz.mgn.icryption.code.key.IDEKeyData;
import cz.mgn.icryption.code.key.IDEKeyFactory;
import cz.mgn.icryption.encryption.EncryptionFactory;
import cz.mgn.icryption.image.IDEImage;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

/**
 *
 * @author indy
 */
public class IDECoder {

    public static void encrypt(final byte[] data, final String password, final String originImagePath, final boolean useOTP, final String otpImagePath, final String targetImagePath, final IDECoderInterface coderInterface) {
        Thread t = new Thread() {

            @Override
            public void run() {
                String error = "";
                boolean success = false;
                try {
                    File originImage = new File(originImagePath);
                    File targetImage = new File(targetImagePath);
                    String dir = targetImage.getAbsoluteFile().toString();
                    dir = dir.substring(0, dir.lastIndexOf("/") + 1);
                    File targetImageDir = new File(dir);
                    if (originImage.exists() && originImage.canRead() && targetImageDir.exists() && targetImageDir.canWrite()) {
                        BufferedImage origin = ImageIO.read(originImage);
                        BufferedImage otp = null;
                        if (useOTP) {
                            otp = ImageIO.read(new File(otpImagePath));
                        }
                        BufferedImage code = codeData(data, password, origin, otp);
                        ImageIO.write(code, "PNG", targetImage);
                        success = true;
                    }
                } catch (Exception ex) {
                    error = ex.getMessage();
                    success = false;
                }
                if (success) {
                    coderInterface.dataSuccessfullyEncrypted(targetImagePath);
                } else {
                    coderInterface.encryptionError(targetImagePath, error);
                }
            }
        };
        t.start();
    }

    public static void decode(final String password, final String codeImagePath, final boolean useOTP, final String otpImagePath, final IDECoderInterface coderInterface) {
        Thread t = new Thread() {

            @Override
            public void run() {
                byte[] data = null;
                String error = "";
                boolean success = false;
                try {
                    File codeImage = new File(codeImagePath);
                    if (codeImage.exists() && codeImage.canRead()) {
                        BufferedImage code = ImageIO.read(codeImage);
                        BufferedImage otp = null;
                        if (useOTP) {
                            otp = ImageIO.read(new File(otpImagePath));
                        }
                        data = decodeData(password, code, otp);
                        success = true;
                    }
                } catch (Exception ex) {
                    error = ex.getMessage();
                    success = false;
                }
                if (success) {
                    coderInterface.dataSuccessfullyDecoded(codeImagePath, data);
                } else {
                    coderInterface.decodingError(codeImagePath, error);
                }
            }
        };
        t.start();
    }

    protected static BufferedImage codeData(byte[] data, String password, BufferedImage origin, BufferedImage otp) throws NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException {
        IDEKeyData key = IDEKeyFactory.generateKey(password);
        byte[] salt = key.getSalt().getBytes("UTF-8");
        byte saltLength = (byte) salt.length;
        if (salt.length > 127) {
            saltLength = (byte) (salt.length - 256);
        }
        byte[] codedData = EncryptionFactory.code(data, key.getKeyData());
        byte[] allData = new byte[1 + salt.length + codedData.length];
        allData[0] = saltLength;
        System.arraycopy(salt, 0, allData, 1, salt.length);
        System.arraycopy(codedData, 0, allData, 1 + salt.length, codedData.length);
        byte[] otpA = null;
        if (otp != null) {
            otpA = IDEImage.readOtp(otp);
        }
        return IDEImage.writeData(allData, origin, otpA);
    }

    protected static byte[] decodeData(String password, BufferedImage origin, BufferedImage otp) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] otpA = null;
        if (otp != null) {
            otpA = IDEImage.readOtp(otp);
        }
        byte[] allData = IDEImage.readData(origin, otpA);
        int saltLength = allData[0];
        if (saltLength < 0) {
            saltLength += 256;
        }
        byte[] saltBytes = new byte[saltLength];
        for (int i = 1; i <= saltLength; i++) {
            saltBytes[i - 1] = allData[i];
        }
        String salt = new String(saltBytes, "UTF-8");
        IDEKeyData key = IDEKeyFactory.generateKeyKnownSalt(password, salt);
        byte[] data = new byte[allData.length - saltLength - 1];
        for (int i = (saltLength + 1); i < allData.length; i++) {
            data[i - (saltLength + 1)] = allData[i];
        }
        byte[] decodedData = EncryptionFactory.decode(data, key.getKeyData());
        return decodedData;
    }
}
