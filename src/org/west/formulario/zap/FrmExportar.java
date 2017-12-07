package org.west.formulario.zap;

import java.awt.event.ItemEvent;
import java.util.Date;
import javax.swing.JOptionPane;
import org.west.converters.ControlExport;
import org.west.converters.ControlExportFactory;
import org.west.converters.ExportEvent;
import org.west.converters.ExportListener;
import org.west.exception.WestException;

public class FrmExportar extends javax.swing.JDialog {

    private ControlExport export;

    public FrmExportar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        pg = new javax.swing.JProgressBar();
        dateValida = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        comboTipo = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Exportar");
        setMinimumSize(new java.awt.Dimension(500, 230));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Exportar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setText("Data:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(jLabel3, gridBagConstraints);

        btnIniciar.setText("Iniciar");
        btnIniciar.setEnabled(false);
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(btnIniciar, gridBagConstraints);

        btnOk.setText("OK");
        btnOk.setEnabled(false);
        btnOk.setPreferredSize(new java.awt.Dimension(61, 23));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(btnOk, gridBagConstraints);

        pg.setPreferredSize(new java.awt.Dimension(146, 23));
        pg.setString("");
        pg.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(pg, gridBagConstraints);

        dateValida.setDate(new Date());
        dateValida.setEnabled(false);
        dateValida.setMinimumSize(new java.awt.Dimension(100, 20));
        dateValida.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(dateValida, gridBagConstraints);

        jLabel1.setText("Tipo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(jLabel1, gridBagConstraints);

        comboTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Site", "Viva Real", "ZAP", "123i", "ImovelWeb" }));
        comboTipo.setSelectedItem(null);
        comboTipo.setMinimumSize(new java.awt.Dimension(100, 18));
        comboTipo.setPreferredSize(new java.awt.Dimension(150, 20));
        comboTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboTipoItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(comboTipo, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        if (comboTipo.getSelectedItem() != null) {

            btnIniciar.setEnabled(false);
            export = null;

            if (comboTipo.getSelectedItem().equals("Site")) {
                export = ControlExportFactory.createSiteControl(dateValida.getDate());
            }

            if (comboTipo.getSelectedItem().equals("ZAP")) {
                export = ControlExportFactory.createZapControl();
            }

            if (comboTipo.getSelectedItem().equals("Viva Real")) {
                export = ControlExportFactory.createVivaRealControl(dateValida.getDate());
            }

            if (comboTipo.getSelectedItem().equals("123i")) {
                export = ControlExportFactory.create123iControl();
            }

            if (comboTipo.getSelectedItem().equals("ImovelWeb")) {
                export = ControlExportFactory.createImovelWebControl();
            }

            if (export != null) {
                pg.setMaximum(2 * export.getQuantidadeImoveis() + 2);

                export.addExportListener(new ExportListener() {

                    @Override
                    public void exportInit(ExportEvent evt) {
                        pg.setString(evt.getMessage());
                        pg.setValue(evt.getCount());
                    }

                    @Override
                    public void exportMove(ExportEvent evt) {
                        pg.setString(evt.getMessage());
                        pg.setValue(evt.getCount());
                    }

                    @Override
                    public void exportFinish(ExportEvent evt) {
                        pg.setString(evt.getMessage());
                        pg.setValue(evt.getCount());
                    }
                });

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            export.gerarExport();

                            export.enviarExport();

                            pg.setValue(pg.getMaximum());
                            pg.setString("Enviado!");
                            btnOk.setEnabled(true);

                        } catch (WestException ex) {
                            ex.printStackTrace();
                            JOptionPane.showConfirmDialog(null, ex.getMessage());
                        }
                    }
                }).start();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Escolha um dos tipos de exportação!");
        }
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    private void comboTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboTipoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {

            if (comboTipo.getSelectedItem() != null) {
                btnIniciar.setEnabled(true);
            } else {
                btnIniciar.setEnabled(false);
            }

            dateValida.setEnabled(comboTipo.getSelectedItem().equals("Site") || comboTipo.getSelectedItem().equals("Viva Real"));
        }
    }//GEN-LAST:event_comboTipoItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox comboTipo;
    private com.toedter.calendar.JDateChooser dateValida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JProgressBar pg;
    // End of variables declaration//GEN-END:variables
}
