package org.west.formulario.corretor;

import org.west.utilitarios.Util;
import org.west.entidades.Imobiliaria;
import org.west.entidades.ImobiliariaDAO;
import org.west.entidades.WestPersistentManager;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;

public class FrmAvisoCliente extends javax.swing.JDialog {

    private List<Imobiliaria> lista;
    private FrmCorretor frame;

    public FrmAvisoCliente(java.awt.Frame parent, boolean modal,List<Imobiliaria> lista, FrmCorretor frame) {
        super(parent, modal);
        initComponents();
        
        this.lista = lista;
        this.frame = frame;

        DefaultListModel modelo = new DefaultListModel();

        for (Iterator it = lista.iterator(); it.hasNext();) {
            Imobiliaria imobiliaria = (Imobiliaria) it.next();
            modelo.addElement(imobiliaria.getCliente());
        }

        this.listaAviso.setModel(modelo);
        setLocationRelativeTo(null);

        Util.tocarSom("/sons/warn.wav");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaAviso = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Aviso");
        setMinimumSize(new java.awt.Dimension(380, 320));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 16));
        jLabel1.setText("Clientes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel1, gridBagConstraints);

        jScrollPane1.setViewportView(listaAviso);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jButton1, gridBagConstraints);

        jButton2.setText("Marcar como lido");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jButton2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int[] valores = this.listaAviso.getSelectedIndices();
        WestPersistentManager.clear();
        if (valores.length > 0) {
            for (int x = 0; x < valores.length; ++x) {
                
                Imobiliaria atual = (Imobiliaria) this.lista.get(valores[x]);
                ImobiliariaDAO.refresh(atual);
                
                if (atual.getStatus() < Imobiliaria.SEM_RETORNO){
                    atual.setStatus(Imobiliaria.SEM_RETORNO);
                    ImobiliariaDAO.save(atual);
                }
            }
        }
        this.frame.atualizaLista();

        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList listaAviso;
    // End of variables declaration//GEN-END:variables
}