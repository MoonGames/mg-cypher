/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.mgn.icryption.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author indy
 */
public class IDEImage {

    /**
     * z delky dat vytvori 4 byty vypovidajici o delce zasifrovaneho souboru
     */
    protected static byte[] createLengthBytes(int length) {
        byte[] lb = new byte[4];
        for (int i = 3; i >= 0; i--) {
            lb[i] = (byte) (length >> (8 * i));
        }
        return lb;
    }

    /** ze 4 bytu vypovicajicich o delce vytvori integer s delkou zasifrovaneho souboru */
    protected static int readLengthBytes(byte[] lengthBytes) {
        int length = 0;
        for (int i = 3; i >= 0; i--) {
            int val = lengthBytes[i];
            if (val < 0) {
                val += 256;
            }
            length += val << (8 * i);
        }
        return length;
    }

    public static int countImageCapacity(BufferedImage in) {
        int bits = in.getHeight() * (in.getWidth() / 4);
        return (bits - 32) / 8;
    }

    protected static BufferedImage copyImage(BufferedImage origin) {
        BufferedImage copy = new BufferedImage(origin.getWidth(), origin.getHeight(), origin.getType());
        Graphics g = copy.getGraphics();
        g.drawImage(origin, 0, 0, null);
        return copy;
    }

    /** ze dvou poli vytvori jedno */
    protected static byte[] fuse(byte[] lengthBytes, byte[] dataBytes) {
        byte[] fused = new byte[lengthBytes.length + dataBytes.length];
        System.arraycopy(lengthBytes, 0, fused, 0, lengthBytes.length);
        System.arraycopy(dataBytes, 0, fused, lengthBytes.length, dataBytes.length);
        return fused;
    }

    public static byte[] readData(BufferedImage image, byte[] otp) {
        byte[] lengthBytes = new byte[4];
        int bit = 0;
        int dataLength = 4;
        byte[] bytes = null;

        for (int y = 0; y < image.getHeight() && (bit / 8) < dataLength; y++) {
            for (int x = 0; (x + 3) < image.getWidth() && (bit / 8) < dataLength; x += 4) {

                int sum = 0;
                for (int xx = 0; xx < 4; xx++) {
                    int sample[] = new int[4];
                    sample = image.getRaster().getPixel(x + xx, y, sample);
                    for (int ii = 0; ii < 3; ii++) {
                        sum += sample[ii];
                    }
                }

                int bitValue = sum % 2;
                if (otp != null) {
                    bitValue = bitValue ^ otp[bit];
                }
                bitValue = bitValue << (bit % 8);

                if ((bit / 8) < 4) {
                    lengthBytes[bit / 8] += bitValue;
                    if (bit == 31) {
                        dataLength = readLengthBytes(lengthBytes);
                        bytes = new byte[dataLength];
                        dataLength += 4;
                    }
                } else {
                    bytes[(bit / 8) - 4] += bitValue;
                }
                bit++;
            }
        }
        return bytes;
    }

    public static byte[] readOtp(BufferedImage key) {
        byte[] otp = new byte[key.getWidth() * key.getHeight()];
        int bit = 0;
        for (int x = 0; x < key.getWidth(); x++) {
            for (int y = 0; y < key.getHeight(); y++) {
                int sum = 0;
                int sample[] = new int[4];
                sample = key.getRaster().getPixel(x, y, sample);
                for (int ii = 0; ii < 3; ii++) {
                    sum += sample[ii];
                }
                otp[bit] = (byte) (sum % 2);
                bit++;
            }
        }
        return otp;
    }

    public static BufferedImage writeData(byte[] data, BufferedImage origin, byte[] otp) {
        int capacity = countImageCapacity(origin);
        if (data.length > capacity) {
            throw new Error("Byte array lenght is over image capacity! " + data.length + "B > " + capacity + "B");
        }
        BufferedImage out = copyImage(origin);
        out = rewriteData(data, out, otp);
        return out;
    }

    protected static BufferedImage rewriteData(byte[] data, BufferedImage image, byte[] otp) {
        Random r = new Random(System.currentTimeMillis());
        byte[] dataBytes = fuse(createLengthBytes(data.length), data);

        int bit = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; (x + 3) < image.getWidth(); x += 4) {
                int bitValue = 0;
                if ((bit / 8) < dataBytes.length) {
                    int byteI = dataBytes[bit / 8];
                    int mask = 1;
                    mask = mask << (bit % 8);
                    bitValue = (byteI & mask) >> (bit % 8);
                    if (otp != null) {
                        bitValue = bitValue ^ otp[bit];
                    }
                } else {
                    bitValue = r.nextInt(2);
                }

                int sum = 0;
                for (int xx = 0; xx < 4; xx++) {
                    int sample[] = new int[4];
                    sample = image.getRaster().getPixel(x + xx, y, sample);
                    for (int ii = 0; ii < 3; ii++) {
                        sum += sample[ii];
                    }
                }

                if ((sum % 2) != bitValue) {
                    int xC = r.nextInt(3);
                    int indexC = r.nextInt(3);

                    int sample[] = new int[4];
                    sample = image.getRaster().getPixel(x + xC, y, sample);

                    if ((sample[indexC] == 255 || r.nextBoolean()) && sample[indexC] > 0) {
                        sample[indexC]--;
                    } else {
                        sample[indexC]++;
                    }
                    image.getRaster().setPixel(x + xC, y, sample);
                }


                bit++;
            }
        }
        return image;
    }
}
