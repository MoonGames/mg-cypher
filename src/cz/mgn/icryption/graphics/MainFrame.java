/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on Jul 25, 2011, 1:36:30 AM
 */
package cz.mgn.icryption.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.mgn.icryption.main.IDECoder;
import cz.mgn.icryption.main.IDECoderInterface;
import cz.mgn.icryption.utils.IDEUtils;
import cz.mgn.icryption.utils.IDEUtilsInterface;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author indy
 */
public class MainFrame extends javax.swing.JFrame implements FileChooserInterface {
    
    protected static final int FILE_CHOOSER_CODE_SOURCE_IMAGE = 1;
    protected static final int FILE_CHOOSER_CODE_TARGET_IMAGE = 2;
    protected static final int FILE_CHOOSER_CODE_SOURCE_FILE = 3;
    protected static final int FILE_CHOOSER_CODE_TARGET_FILE = 4;
    protected static final int FILE_CHOOSER_CODE_OTP_IMAGE = 5;
    protected BufferedImage sourceImage = null;
    protected int imageCapacityTotal = 0;
    protected int imageCapacityPassw = 0;

    /** Creates new form MainFrame */
    public MainFrame() {
        initComponents();
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                recountAvailibleCharacters();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                recountAvailibleCharacters();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                recountAvailibleCharacters();
            }
        });
    }
    
    protected CryptData createCryptData(String password, String sourceFile, String targetFile, String sourceImage, String targetImage, boolean useOTP, String otpImage) {
        return new CryptData(password, sourceFile, targetFile, sourceImage, targetImage, useOTP, otpImage);
    }
    
    protected void chooseFile(final int code, final int filesType) {
        this.setVisible(false);
        final MainFrame mf = this;
        Thread t = new Thread() {
            
            @Override
            public void run() {
                new FileChooser(mf, code, filesType).setVisible(true);
            }
        };
        t.start();
    }
    
    protected void log(String text) {
        logArea.insert(text + "\n", 0);
    }
    
    protected void decode() {
        if (encryptText.isSelected()) {
            log("Text decoding started, it's take few secons. Please wait...");
            IDECoder.decode(new String(passwordField.getPassword()), sourceImageField.getText(), useOTPCheckBox.isSelected(), otpImage.getText(), new IDECoderInterface() {
                
                @Override
                public void dataSuccessfullyEncrypted(String file) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
                
                @Override
                public void encryptionError(String file, String error) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
                
                @Override
                public void dataSuccessfullyDecoded(String file, byte[] data) {
                    log("File " + file + " successfully decoded.");
                    try {
                        textArea.setText(new String(data, "UTF-8"));
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                @Override
                public void decodingError(String file, String error) {
                    log("Error in decoding " + file + ": " + error);
                }
            });
        } else {
            log("File decoding started, it's take few secons. Please wait...");
            final CryptData cd = createCryptData(new String(passwordField.getPassword()), sourceFileField.getText(), targetFileField.getText(), sourceImageField.getText(), targetImageField.getText(), useOTPCheckBox.isSelected(), otpImage.getText());
            
            IDECoder.decode(cd.getPassword(), cd.getSourceImage(), cd.getUseOTP(), cd.getOTPImage(), new IDECoderInterface() {
                
                @Override
                public void dataSuccessfullyEncrypted(String file) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
                
                @Override
                public void encryptionError(String file, String error) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
                
                @Override
                public void dataSuccessfullyDecoded(String file, byte[] data) {
                    log("File " + file + " successfully decoded. Writing to target file. Please wait...");
                    IDEUtils.writeFile(cd.getTargetFile(), data, new IDEUtilsInterface() {
                        
                        @Override
                        public void imageLoaded(String file, BufferedImage image) {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }
                        
                        @Override
                        public void fileLoaded(String file, byte[] data) {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }
                        
                        @Override
                        public void fileWrited(String file) {
                            log("Data successfully decoded to file " + file);
                        }
                    });
                }
                
                @Override
                public void decodingError(String file, String error) {
                    log("Error in decoding " + file + ": " + error);
                }
            });
        }
        
    }
    
    protected void encrypt() {
        if (encryptText.isSelected()) {
            log("Text encrypting started, it's take few secons. Please wait...");
            try {
                IDECoder.encrypt(textArea.getText().getBytes("UTF-8"), new String(passwordField.getPassword()), sourceImageField.getText(), useOTPCheckBox.isSelected(), otpImage.getText(), targetImageField.getText(), new IDECoderInterface() {
                    
                    @Override
                    public void dataSuccessfullyEncrypted(String file) {
                        log("File " + file + " successfully encrypted.");
                    }
                    
                    @Override
                    public void encryptionError(String file, String error) {
                        log("Error in encrypting " + file + ": " + error);
                    }
                    
                    @Override
                    public void dataSuccessfullyDecoded(String file, byte[] data) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    
                    @Override
                    public void decodingError(String file, String error) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                });
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            log("File encrypting started, it's take few secons. Please wait...");
            final CryptData cd = createCryptData(new String(passwordField.getPassword()), sourceFileField.getText(), targetFileField.getText(), sourceImageField.getText(), targetImageField.getText(), useOTPCheckBox.isSelected(), otpImage.getText());
            IDEUtils.loadFile(cd.getSourceFile(), new IDEUtilsInterface() {
                
                @Override
                public void imageLoaded(String file, BufferedImage image) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
                
                @Override
                public void fileLoaded(String file, byte[] data) {
                    log("File " + file + " loaded, now starting encryption. Please wait...");
                    IDECoder.encrypt(data, cd.getPassword(), cd.getSourceImage(), cd.getUseOTP(), cd.getOTPImage(), cd.getTargetImage(), new IDECoderInterface() {
                        
                        @Override
                        public void dataSuccessfullyEncrypted(String file) {
                            log("File " + file + " successfully encrypted.");
                        }
                        
                        @Override
                        public void encryptionError(String file, String error) {
                            log("Error in encrypting " + file + ": " + error);
                        }
                        
                        @Override
                        public void dataSuccessfullyDecoded(String file, byte[] data) {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }
                        
                        @Override
                        public void decodingError(String file, String error) {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }
                    });
                }
                
                @Override
                public void fileWrited(String file) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        }
    }
    
    protected void recountAvailibleCharacters() {
        try {
            int lengthCh = textArea.getText().length();
            int lengthB = textArea.getText().getBytes("UTF-8").length;
            int availible = imageCapacityPassw - lengthB - (imageCapacityPassw % 16);
            
            avAvailible.setText("" + availible);
            avCharacters.setText("" + lengthCh);
            
            if ((availible >= 0) != encryptButton.isEnabled()) {
                encryptButton.setEnabled(availible >= 0);
                if (availible >= 0) {
                    avAvailible.setForeground(Color.BLACK);
                } else {
                    avAvailible.setForeground(Color.RED);
                }
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void recountImageCapacity() {
        if (sourceImage != null) {
            int[] cap = IDEUtils.countImageCapacity(sourceImage);
            imageCapacityTotal = cap[0];
            imageCapacityPassw = cap[1];
        } else {
            imageCapacityTotal = 0;
            imageCapacityPassw = 0;
        }
        capacityTotal.setText("" + imageCapacityTotal + "B");
        capacityThisPassw.setText("" + imageCapacityPassw + "B");
        recountAvailibleCharacters();
    }
    
    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
        recountImageCapacity();
    }
    
    protected void sourceImageSelected(String sourceImage) {
        sourceImageField.setText(sourceImage);
        if (sourceImage.length() > 2) {
            final MainFrame mf = this;
            IDEUtils.loadImage(sourceImage, new IDEUtilsInterface() {
                
                @Override
                public void imageLoaded(String file, BufferedImage image) {
                    mf.setSourceImage(image);
                }
                
                @Override
                public void fileLoaded(String file, byte[] data) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
                
                @Override
                public void fileWrited(String file) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        } else {
            setSourceImage(null);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        encryptButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        logArea = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        sourceImageField = new javax.swing.JLabel();
        targetImageField = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        capacityTotal = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        capacityThisPassw = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        passwordShow = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        encryptText = new javax.swing.JRadioButton();
        encryptFile = new javax.swing.JRadioButton();
        sourceFileField = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel6 = new javax.swing.JLabel();
        targetFileField = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        avCharacters = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        avAvailible = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        useOTPCheckBox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        helpText = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        otpImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Icryption");

        jLabel1.setText("Camuflage image (source):");

        jLabel2.setText("Image capacity:");

        jLabel3.setText("Target image:");

        jLabel5.setText("Password:");

        jLabel7.setText("Encrypt:");

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 0, 18));
        jLabel8.setText("Text");

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jButton1.setText("decode");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        encryptButton.setText("encrypt");
        encryptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encryptButtonActionPerformed(evt);
            }
        });
        jPanel3.add(encryptButton);

        logArea.setColumns(20);
        logArea.setEditable(false);
        logArea.setRows(5);
        jScrollPane1.setViewportView(logArea);

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 0, 18));
        jLabel9.setText("Log:");

        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane2.setViewportView(textArea);

        sourceImageField.setText("./");

        targetImageField.setText("./");

        jButton3.setText("browse");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("browse");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel12.setText("total:");

        capacityTotal.setText("0B");

        jLabel14.setText("this password:");

        capacityThisPassw.setText("0B");

        passwordShow.setText("show");
        passwordShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordShowActionPerformed(evt);
            }
        });

        jLabel4.setText("Source file:");

        encryptText.setSelected(true);
        encryptText.setText("text");
        encryptText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encryptTextActionPerformed(evt);
            }
        });

        encryptFile.setText("file");
        encryptFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encryptFileActionPerformed(evt);
            }
        });

        sourceFileField.setText("./");

        jToggleButton1.setText("browse");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Target file:");

        targetFileField.setText("./");

        jButton5.setText("browse");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel10.setText("Characters:");

        avCharacters.setText("0");

        jLabel16.setText("Availible:");

        avAvailible.setText("0");

        jLabel11.setText("Next options:");

        useOTPCheckBox.setText("use OTP (if is checked control OTP settings tab!)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(encryptText)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(encryptFile))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(capacityTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(capacityThisPassw, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(targetFileField, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                                            .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                                            .addComponent(sourceFileField, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                                            .addComponent(targetImageField, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                                            .addComponent(sourceImageField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                                            .addComponent(useOTPCheckBox))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                            .addComponent(passwordShow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 244, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(avCharacters, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(avAvailible, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(211, 211, 211))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(sourceImageField)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel12)
                    .addComponent(capacityTotal)
                    .addComponent(jLabel14)
                    .addComponent(capacityThisPassw))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(targetImageField)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordShow))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(encryptText)
                    .addComponent(encryptFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(sourceFileField)
                    .addComponent(jToggleButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(targetFileField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(useOTPCheckBox)))
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(avCharacters)
                    .addComponent(jLabel16)
                    .addComponent(avAvailible))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Encription", jPanel1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        helpText.setColumns(20);
        helpText.setLineWrap(true);
        helpText.setRows(5);
        helpText.setText("TODO");
        helpText.setWrapStyleWord(true);
        jScrollPane3.setViewportView(helpText);

        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Help", jPanel2);

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Camuflage (OTP) image will be used as key for one-tim pad (Vernam's) . Zdrojovy OTP obrazek musi mit dostatecnou kapacitu, tzn. jeho rozmeru muzou byt maximalne 4x mensi nez obrazek se zasifrovanymi daty. K OTP klici kvuli bezpecnosti pouzijte jiny obrazek, nez k vlastnim datum!");
        jTextArea1.setWrapStyleWord(true);
        jScrollPane4.setViewportView(jTextArea1);

        jLabel13.setText("Camuflage (OTP) image:");

        jButton2.setText("browse");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        otpImage.setText("./");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(otpImage, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jButton2)
                    .addComponent(otpImage))
                .addContainerGap(562, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("OTP", jPanel4);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void passwordShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordShowActionPerformed
        if (passwordShow.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar((char) 8226);
        }
    }//GEN-LAST:event_passwordShowActionPerformed
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        chooseFile(FILE_CHOOSER_CODE_SOURCE_IMAGE, FileChooser.FILES_TYPE_IMAGE);
    }//GEN-LAST:event_jButton3ActionPerformed
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        chooseFile(FILE_CHOOSER_CODE_TARGET_IMAGE, FileChooser.FILES_TYPE_PNG);
    }//GEN-LAST:event_jButton4ActionPerformed
    
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        chooseFile(FILE_CHOOSER_CODE_SOURCE_FILE, FileChooser.FILES_TYPE_ALL);
    }//GEN-LAST:event_jToggleButton1ActionPerformed
    
    private void encryptTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encryptTextActionPerformed
        // TODO add your handling code here:
        encryptFile.setSelected(!encryptText.isSelected());
    }//GEN-LAST:event_encryptTextActionPerformed
    
    private void encryptFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encryptFileActionPerformed
        // TODO add your handling code here:
        encryptText.setSelected(!encryptFile.isSelected());
    }//GEN-LAST:event_encryptFileActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        decode();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void encryptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encryptButtonActionPerformed
        // TODO add your handling code here:
        encrypt();
    }//GEN-LAST:event_encryptButtonActionPerformed
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        chooseFile(FILE_CHOOSER_CODE_TARGET_FILE, FileChooser.FILES_TYPE_ALL);
}//GEN-LAST:event_jButton5ActionPerformed
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        chooseFile(FILE_CHOOSER_CODE_OTP_IMAGE, FileChooser.FILES_TYPE_IMAGE);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avAvailible;
    private javax.swing.JLabel avCharacters;
    private javax.swing.JLabel capacityThisPassw;
    private javax.swing.JLabel capacityTotal;
    private javax.swing.JButton encryptButton;
    private javax.swing.JRadioButton encryptFile;
    private javax.swing.JRadioButton encryptText;
    private javax.swing.JTextArea helpText;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextArea logArea;
    private javax.swing.JLabel otpImage;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JCheckBox passwordShow;
    private javax.swing.JLabel sourceFileField;
    private javax.swing.JLabel sourceImageField;
    private javax.swing.JLabel targetFileField;
    private javax.swing.JLabel targetImageField;
    private javax.swing.JTextArea textArea;
    private javax.swing.JCheckBox useOTPCheckBox;
    // End of variables declaration//GEN-END:variables

    @Override
    public void fileChoosed(String filePath, int code) {
        this.setVisible(true);
        if (filePath.length() > 0) {
            if (code == FILE_CHOOSER_CODE_SOURCE_FILE) {
                sourceFileField.setText(filePath);
            } else if (code == FILE_CHOOSER_CODE_SOURCE_IMAGE) {
                sourceImageSelected(filePath);
            } else if (code == FILE_CHOOSER_CODE_TARGET_IMAGE) {
                targetImageField.setText(filePath);
            } else if (code == FILE_CHOOSER_CODE_TARGET_FILE) {
                targetFileField.setText(filePath);
            } else if (code == FILE_CHOOSER_CODE_OTP_IMAGE) {
                otpImage.setText(filePath);
            }
        }
    }
    
    @Override
    public void fileWasnChoosed(int code) {
        this.setVisible(true);
        if (code == FILE_CHOOSER_CODE_SOURCE_FILE) {
            sourceFileField.setText("./");
        } else if (code == FILE_CHOOSER_CODE_SOURCE_IMAGE) {
            sourceImageSelected("./");
        } else if (code == FILE_CHOOSER_CODE_TARGET_IMAGE) {
            targetImageField.setText("./");
        } else if (code == FILE_CHOOSER_CODE_TARGET_FILE) {
            targetFileField.setText("./");
        } else if (code == FILE_CHOOSER_CODE_OTP_IMAGE) {
            otpImage.setText("./");
        }
    }
}
