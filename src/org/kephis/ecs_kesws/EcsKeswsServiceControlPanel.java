/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws;

import javax.swing.JOptionPane;

/**
 *
 * @author kim
 */
public class EcsKeswsServiceControlPanel extends javax.swing.JFrame {

    
    private boolean isOutGoingMode = true; //when true, the mode is set to Outgoing mde
     
    private int scenario = 0;
    
    /**
     * Creates new form EcsKeswsServiceControlPanel
     */
    public EcsKeswsServiceControlPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        btnEcsKeswsMode = new javax.swing.JToggleButton();
        btnKeswsEcsMode = new javax.swing.JToggleButton();
        btnStart = new javax.swing.JButton();
        jbtnPostPayment = new javax.swing.JRadioButton();
        jbPrePayment = new javax.swing.JRadioButton();
        btnStop = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtOutPutArea = new javax.swing.JTextArea();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jCheckBoxMenuItem2.setSelected(true);
        jCheckBoxMenuItem2.setText("jCheckBoxMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ECS KESWS GUI");

        btnEcsKeswsMode.setText("ECS KESWS mode");
        btnEcsKeswsMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEcsKeswsModeActionPerformed(evt);
            }
        });

        btnKeswsEcsMode.setText("KESWS ECS Mode ");
        btnKeswsEcsMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeswsEcsModeActionPerformed(evt);
            }
        });

        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        jbtnPostPayment.setText("Post Payment");
        jbtnPostPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPostPaymentActionPerformed(evt);
            }
        });

        jbPrePayment.setText("Pre Payment");
        jbPrePayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPrePaymentActionPerformed(evt);
            }
        });

        btnStop.setText("Stop");
        btnStop.setSelected(true);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        jLabel1.setText("Files processed");

        txtOutPutArea.setColumns(20);
        txtOutPutArea.setRows(5);
        jScrollPane1.setViewportView(txtOutPutArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnKeswsEcsMode)
                            .addComponent(btnEcsKeswsMode, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbPrePayment, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtnPostPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnStart)
                            .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(84, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnKeswsEcsMode)
                    .addComponent(jbtnPostPayment)
                    .addComponent(btnStart))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEcsKeswsMode)
                    .addComponent(jbPrePayment)
                    .addComponent(btnStop))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
        );

        btnEcsKeswsMode.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnKeswsEcsModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeswsEcsModeActionPerformed
 
        btnEcsKeswsMode.setEnabled(false);
        jbPrePayment.setEnabled(true);
        jbtnPostPayment.setEnabled(true);
        
        //the variable specifies if its incoming or outgoing
        isOutGoingMode = false ; //when the isOutGoingMode is set to FALSE, it means its incoming mode
    }//GEN-LAST:event_btnKeswsEcsModeActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
                              

        btnStart.setEnabled(false);
        btnStop.setEnabled(true);

        //if its outgoing mode
        //
        if (this.isOutGoingMode) {
             //OUTGOING MODE
            new OutgoingMessageProcessor(scenario);
        }else{
            //INCOMING MODE
            new IncomingMessageProcessor(scenario);
        }
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnEcsKeswsModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEcsKeswsModeActionPerformed
        btnKeswsEcsMode.setEnabled(false);
        jbPrePayment.setEnabled(true);
        jbtnPostPayment.setEnabled(true);
        isOutGoingMode = true; //when true the mode is set to outgoing
    }//GEN-LAST:event_btnEcsKeswsModeActionPerformed

    private void jbtnPostPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPostPaymentActionPerformed
                       jbPrePayment.setEnabled(false);
                       btnStart.setEnabled(true);
                       btnStop.setEnabled(false);
                       scenario=1;
    }//GEN-LAST:event_jbtnPostPaymentActionPerformed

    private void jbPrePaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPrePaymentActionPerformed
        jbtnPostPayment.setEnabled(false);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        scenario=2;
    }//GEN-LAST:event_jbPrePaymentActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
           btnStart.setEnabled(false);
           btnStop.setEnabled(false);
         
          jbPrePayment.setEnabled(false);
          jbtnPostPayment.setEnabled(false);
          btnKeswsEcsMode.setEnabled(true);
          btnEcsKeswsMode.setEnabled(true);
         
    }//GEN-LAST:event_btnStopActionPerformed

    public static void main(String args[]){
         /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EcsKeswsServiceControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EcsKeswsServiceControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EcsKeswsServiceControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EcsKeswsServiceControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EcsKeswsServiceControlPanel ecsKeswsServiceControlPanelnew = new EcsKeswsServiceControlPanel();
                ecsKeswsServiceControlPanelnew.setVisible(true);        
                ecsKeswsServiceControlPanelnew.btnStart.setEnabled(false);
                ecsKeswsServiceControlPanelnew.btnStop.setEnabled(false);
                ecsKeswsServiceControlPanelnew.jbPrePayment.setEnabled(false);
                ecsKeswsServiceControlPanelnew.jbtnPostPayment.setEnabled(false);
                
                }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnEcsKeswsMode;
    private javax.swing.JToggleButton btnKeswsEcsMode;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnStop;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton jbPrePayment;
    private javax.swing.JRadioButton jbtnPostPayment;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextArea txtOutPutArea;
    // End of variables declaration//GEN-END:variables
}
