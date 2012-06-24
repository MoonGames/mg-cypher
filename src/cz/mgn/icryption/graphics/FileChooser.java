/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FileChooser.java
 *
 * Created on Jul 25, 2011, 2:16:59 AM
 */
package cz.mgn.icryption.graphics;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author indy
 */
public class FileChooser extends javax.swing.JFrame implements WindowListener {

    public static final int FILES_TYPE_ALL = 0;
    public static final int FILES_TYPE_PNG = 2;
    public static final int FILES_TYPE_IMAGE = 1;
    protected FileChooserInterface fileChooserInterface = null;
    protected int code = 0;

    /** Creates new form FileChooser */
    public FileChooser(FileChooserInterface fileChooserInterface, int code, int filesType) {
        this.fileChooserInterface = fileChooserInterface;
        this.code = code;
        this.addWindowListener(this);
        initComponents();
        if (filesType != FILES_TYPE_ALL) {
            jFileChooser1.setFileFilter(new IDEFileFilter(filesType));
        }
    }

    protected void fileWasntChoosed() {
        fileChooserInterface.fileWasnChoosed(code);
        this.dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("File chooser...");

        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });
        getContentPane().add(jFileChooser1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed
        // TODO add your handling code here:
        if (jFileChooser1.getSelectedFile() != null) {
            fileChooserInterface.fileChoosed(jFileChooser1.getSelectedFile().getAbsolutePath(), code);
            this.dispose();
        } else {
            fileWasntChoosed();
        }
    }//GEN-LAST:event_jFileChooser1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser jFileChooser1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        fileWasntChoosed();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    class IDEFileFilter extends FileFilter {

        protected int filesType = 0;
        protected final String[] images = ImageIO.getReaderFileSuffixes(); //{"PNG", "JPG", "JPEG"};

        public IDEFileFilter(int filesType) {
            this.filesType = filesType;
        }

        private boolean compareSuffix(String ethanol, String suffix) {
            return ethanol.equalsIgnoreCase(suffix);
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String suffix = f.getAbsolutePath();
            suffix = suffix.substring(suffix.lastIndexOf(".") + 1);
            if (filesType == FileChooser.FILES_TYPE_IMAGE) {
                for (String ethanol : images) {
                    if (compareSuffix(ethanol, suffix)) {
                        return true;
                    }
                }
            } else if (filesType == FileChooser.FILES_TYPE_PNG) {
                return compareSuffix("PNG", suffix);
            }
            return false;
        }

        @Override
        public String getDescription() {
            if (filesType == FileChooser.FILES_TYPE_IMAGE) {
                return "Just images";
            } else if (filesType == FileChooser.FILES_TYPE_PNG) {
                return "Just PNG images";
            }
            return "";
        }
    }
}
