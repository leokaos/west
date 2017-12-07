package org.west.formulario.corretor;

import java.util.ArrayList;
import java.util.HashSet;
import org.west.entidades.Imovel;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import org.west.componentes.PanelListaImovel;
import org.west.entidades.Altera;
import org.west.entidades.AlteraDAO;
import org.west.utilitarios.RenderAltera;
import org.west.utilitarios.Util;

public class FrmAvisoImovel extends javax.swing.JDialog{
    
    private PanelListaImovel panelListaImovel;
    private Set<Imovel> listaImovel;
    private List<Altera> listaAltera = new ArrayList<Altera>();
    
    public FrmAvisoImovel(java.awt.Frame parent, boolean modal,List<Altera> lista,PanelListaImovel panelListaImovel) {
        super(parent, modal);
        initComponents();
        
        setSize(620,420);
        setLocationRelativeTo(null);
          
        this.listaAltera = lista;
        this.panelListaImovel = panelListaImovel;
        this.listaImovel = new HashSet<Imovel>();
        
        jlistAltera.setCellRenderer(new RenderAltera());
        jlistAltera.setFixedCellHeight(30);
        
        if (listaAltera != null && !listaAltera.isEmpty())
            fillList();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        btnExibi = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlistAltera = new javax.swing.JList();
        lblEndereco = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnExcluir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Aviso de Modificação de Imóvel");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 14));
        jLabel1.setText("Imóveis");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel1, gridBagConstraints);

        btnExibi.setText("Exibir Imóveis...");
        btnExibi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExibiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(btnExibi, gridBagConstraints);

        jScrollPane1.setViewportView(jlistAltera);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        lblEndereco.setFont(new java.awt.Font("Verdana", 1, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblEndereco, gridBagConstraints);

        jLabel3.setText("Os seguintes imóveis foram alterados:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        getContentPane().add(jLabel3, gridBagConstraints);

        btnExcluir.setText("Excluir");
        btnExcluir.setIconTextGap(10);
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        getContentPane().add(btnExcluir, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExibiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibiActionPerformed
        List<Imovel> listaAux = new ArrayList<Imovel>();
        
        for(Integer index : jlistAltera.getSelectedIndices())
            listaAux.add(listaAltera.get(index).getImovel());
        
        panelListaImovel.setListagem(listaAux);
        this.dispose();
    }//GEN-LAST:event_btnExibiActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        btnExcluir.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/load_small.gif")));
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                for(Object obj : jlistAltera.getSelectedValues()){
                    AlteraDAO.delete((Altera) obj);
                    ((DefaultListModel) jlistAltera.getModel()).removeElement(obj);
                }
                
                btnExcluir.setIcon(null);
            }
        }).start();
        
    }//GEN-LAST:event_btnExcluirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnExibi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList jlistAltera;
    private javax.swing.JLabel lblEndereco;
    // End of variables declaration//GEN-END:variables

    public void addAltera(Altera altera){
        listaAltera.add(altera);        
        ((DefaultListModel)jlistAltera.getModel()).addElement(altera);
    }
    
    private void fillList(){        
        DefaultListModel modelo = new DefaultListModel();

        for(Altera altera : listaAltera){
            modelo.addElement(altera);
            listaImovel.add(altera.getImovel());
        }

        this.jlistAltera.setModel(modelo);

        Util.tocarSom("/sons/warn.wav");          
    }    
}