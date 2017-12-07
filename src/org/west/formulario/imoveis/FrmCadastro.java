package org.west.formulario.imoveis;

import java.awt.Component;
import javax.swing.JFrame;

public class FrmCadastro extends javax.swing.JDialog {

    public FrmCadastro() {
        super((JFrame) null, true);

        initComponents();

        setSize(800, 600);
        setLocationRelativeTo(null);

        panelTab.addTab("Gerenciar Anúncios", createFormAnuncio());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelTab = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Gerenciar");
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(panelTab, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane panelTab;
    // End of variables declaration//GEN-END:variables

    private Component createFormAnuncio() {
        return new PanelGenenciarAnuncios();
    }
}
