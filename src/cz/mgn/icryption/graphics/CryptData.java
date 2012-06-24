/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.icryption.graphics;

/**
 *
 * @author indy
 */
public class CryptData {

    protected String password = "";
    protected String sourceFile = "";
    protected String targetFile = "";
    protected String sourceImage = "";
    protected String targetImage = "";
    protected boolean useOTP = false;
    protected String otpImage = "";

    public CryptData(String password, String sourceFile, String targetFile, String sourceImage, String targetImage, boolean useOTP, String otpImage) {
        this.password = password;
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
        this.sourceImage = sourceImage;
        this.targetImage = targetImage;
        this.useOTP = useOTP;
        this.otpImage = otpImage;
    }

    public boolean getUseOTP() {
        return useOTP;
    }

    public String getOTPImage() {
        return otpImage;
    }

    public String getPassword() {
        return password;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public String getTargetFile() {
        return targetFile;
    }

    public String getSourceImage() {
        return sourceImage;
    }

    public String getTargetImage() {
        return targetImage;
    }
}
