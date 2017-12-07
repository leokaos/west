package org.west.componentes;

import org.west.entidades.Imovel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import org.west.componentes.tabelafilter.ImoveisComparator;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import javax.swing.JPanel;
import org.jfree.ui.tabbedui.VerticalLayout;
import org.west.entidades.WestPersistentManager;

public class PanelListaImovel extends javax.swing.JPanel {

    private List<Imovel> listagem;
    private List<PanelVisuImovel> listaPanel;
    private List<Imovel> selecionados;

    public PanelListaImovel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        comboOrdem = new javax.swing.JComboBox();
        selecionarTodos = new javax.swing.JCheckBox();
        btnExibir = new javax.swing.JButton();
        lblContador = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        panelResultados = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(100, 50));
        jPanel1.setPreferredSize(new java.awt.Dimension(512, 50));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Ordenar por:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        comboOrdem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Valor", "Referencia", "Tipo", "Status", "Bairro", "Atualizado" }));
        comboOrdem.setEnabled(false);
        comboOrdem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdemItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        jPanel1.add(comboOrdem, gridBagConstraints);

        selecionarTodos.setText("Selecionar Todos");
        selecionarTodos.setEnabled(false);
        selecionarTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selecionarTodosActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        jPanel1.add(selecionarTodos, gridBagConstraints);

        btnExibir.setText("Mostrar selecionados");
        btnExibir.setEnabled(false);
        btnExibir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExibirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        jPanel1.add(btnExibir, gridBagConstraints);
        jPanel1.add(lblContador, new java.awt.GridBagConstraints());

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        scroll.setViewportView(panelResultados);

        add(scroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void selecionarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selecionarTodosActionPerformed
        for (Iterator<PanelVisuImovel> it = listaPanel.iterator(); it.hasNext();) {
            it.next().setSelecionado(selecionarTodos.isSelected());
        }
    }//GEN-LAST:event_selecionarTodosActionPerformed

    private void btnExibirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibirActionPerformed
        if (selecionados == null) 
            selecionados = new ArrayList<Imovel>();

        selecionados.clear();

        for (Iterator<PanelVisuImovel> it = listaPanel.iterator(); it.hasNext();) {
            PanelVisuImovel panel = it.next();
            if (panel.isSelecionado()) {
                Imovel imovel = panel.getImovel();
                selecionados.add(imovel);
            }
        }

        if (selecionados.size() > 0)
            firePropertyChange("selecionados", null, getSelecionados());
        else
            JOptionPane.showMessageDialog(null, "Não foi selecionado nenhum item!");

    }//GEN-LAST:event_btnExibirActionPerformed

    private void comboOrdemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdemItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED){
            Collections.sort(listagem, new ImoveisComparator(comboOrdem.getSelectedItem().toString()));
            setListagem(listagem);
        }
    }//GEN-LAST:event_comboOrdemItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExibir;
    private javax.swing.JComboBox comboOrdem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblContador;
    private javax.swing.JPanel panelResultados;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JCheckBox selecionarTodos;
    // End of variables declaration//GEN-END:variables

    public void setListagem(List<Imovel> listagem) {
        if (listagem.size() > 0){
            WestPersistentManager.clear();

            if (!listagem.equals(this.listagem) && comboOrdem != null && comboOrdem.getItemCount() >= 0)
                comboOrdem.setSelectedIndex(0);
            
            this.listagem = listagem;
            this.listaPanel = new ArrayList<PanelVisuImovel>();
            selecionarTodos.setEnabled(true);
            selecionarTodos.setSelected(false);
            btnExibir.setEnabled(true);
            comboOrdem.setEnabled(true);
            lblContador.setText(String.valueOf(this.listagem.size()) + " Encontrados");
            scroll.setVisible(false);
            drawResultados();
        }
        else{
            panelResultados.removeAll();
            panelResultados.revalidate();
            panelResultados.repaint();
            selecionarTodos.setEnabled(false);
            selecionarTodos.setSelected(false);
            btnExibir.setEnabled(false);
            comboOrdem.setEnabled(false);
            lblContador.setText("0 Encontrados");
            JOptionPane.showMessageDialog(null, "Busca não retornou resultados!");
        }
        
        firePropertyChange("listagem", new ArrayList<Imovel>(), listagem);
    }

    private void drawResultados() {

        VerticalLayout layout = new VerticalLayout();

        panelResultados.removeAll();
        panelResultados.setLayout(layout);
        
        int altura = scroll.getHeight() / 4;

        int cont = 0;

        for (Iterator<Imovel> it = listagem.iterator(); it.hasNext();) {
            Imovel imovel = it.next();

            //criação do componente
            PanelVisuImovel visu = new PanelVisuImovel(imovel);
            visu.setPreferredSize(new Dimension(0, altura));
            visu.setBackground((cont % 2 == 0 ? Color.white : new Color(190, 218, 231)));
            listaPanel.add(visu);

            cont++;
            panelResultados.add(visu);
        }

        scroll.setVisible(true);
        scroll.getVerticalScrollBar().setUnitIncrement(altura);
        panelResultados.revalidate();
        panelResultados.repaint();
    }

    public List<Imovel> getSelecionados() {
        return selecionados;
    }

    public void setSelecionados(List<Imovel> selecionados) {
        this.selecionados = selecionados;
    }
    
    public JPanel getPanelResultado(){
        return panelResultados;
    }
}