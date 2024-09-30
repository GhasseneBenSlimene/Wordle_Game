package graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;

//import javax.swing.SwingUtilities;
import controllers.*;

public class PropFrame extends javax.swing.JPanel {

    private GameControls gameControls;
    private MotusFrame motusFrame;

    public PropFrame(GameControls gameControls, MotusFrame motusFrame) {
        initComponents();
        this.gameControls = gameControls;
        this.motusFrame = motusFrame;
        startCountdown();
    }

    private void startCountdown() {
        new Thread(() -> {
            while (true) {
                try {
                    if (gameControls.getCountdownTime() > 20) {
                        // Mettez à jour le JLabel avec le temps restant
                        jLabel3.setText(formatTime(gameControls.getCountdownTime()));
                        Thread.sleep(1000);
                    } else {
                        jLabel3.setText("<html><font color='red'>" + formatTime(gameControls.getCountdownTime())
                                + "</font></html>");
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12));
        jLabel2.setText("Votre proposition :");

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12));
        jLabel1.setText("Temps restant :");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(190, 220, 220));
        jButton1.setText("Recommencer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(190, 220, 220));
        jButton2.setText("Quitter");
        jButton2.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 28)); // NOI18N

        jLabelError = new javax.swing.JLabel();
        jLabelError.setFont(new java.awt.Font("Segoe UI", 0, 12));
        jLabelError.setForeground(Color.RED); // Texte rouge
        jLabelError.setVisible(false); // Caché par défaut

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(61, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 140,
                                                Short.MAX_VALUE)
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 140,
                                                Short.MAX_VALUE)
                                        .addComponent(jTextField2)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabelError)) // Ajout de jLabelError en horizontal
                                .addContainerGap(61, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(8, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 29,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelError) // Ajout de jLabelError en vertical, juste sous jTextField2
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addGap(18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(70, Short.MAX_VALUE));
    }// </editor-fold>

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {
        String attempt = jTextField2.getText().trim().toUpperCase();
        if (gameControls.validateAttempt(attempt)) {
            motusFrame.updateRowContentAndColors(attempt);
            jTextField2.setText(""); // Effacer le champ de texte pour la prochaine tentative
            jLabelError.setVisible(false); // Cacher le message d'erreur s'il était visible
        } else {
            // Afficher un message d'erreur
            jLabelError.setText("<html><body>Entrez un mot qui respecte les critères suivants :<br><ul>"
                    + "<li>Il doit être alphabétique (a-z, A-Z).</li>"
                    + "<li>Il doit avoir une longueur de " + gameControls.getCurrentWord().length()
                    + " caractères.</li>"
                    + "<li>Il doit être commencé par " + gameControls.getCurrentWord().charAt(0)
                    + " et contient " + gameControls.getMatrice().getRandomChar() + "<br>dans la position "
                    + gameControls.getMatrice().getRandomIndex()
                    + "</li>"
                    + "</ul></body></html>");
            jLabelError.setVisible(true);
        }
    }

    // Inside PropFrame.java
    public interface ButtonActionListener {
        void onRestartActionPerformed(ActionEvent evt);
    }

    private ButtonActionListener actionListener;

    public void setButtonActionListener(ButtonActionListener listener) {
        this.actionListener = listener;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (actionListener != null) {
            actionListener.onRestartActionPerformed(evt);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    public void setTextField2Editable(boolean editable) {
        jTextField2.setEditable(editable);
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelError;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration
}