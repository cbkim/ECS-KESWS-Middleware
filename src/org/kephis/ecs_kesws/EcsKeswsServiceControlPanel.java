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
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnPostPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbPrePayment, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnStart)
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbPrePayment)
                    .addComponent(btnStop)
                    .addComponent(btnStart)
                    .addComponent(jbtnPostPayment))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
           // new IncomingMessageProcessor(scenario);
        }
    }//GEN-LAST:event_btnStartActionPerformed

    private void jbtnPostPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPostPaymentActionPerformed
                       jbPrePayment.setEnabled(false);
                       btnStart.setEnabled(true);
                       btnStop.setEnabled(false);
                       scenario=3;
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
