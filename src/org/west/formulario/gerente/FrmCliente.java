package org.west.formulario.gerente;

import org.west.entidades.Cliente;
import org.west.entidades.Historico;
import org.west.entidades.HistoricoDAO;
import org.west.entidades.Imobiliaria;
import org.west.entidades.ImobiliariaDAO;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JScrollPane;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;

public class FrmCliente extends javax.swing.JDialog {

    public FrmCliente(java.awt.Frame parent, boolean modal,Cliente cliente) {
        super(parent, modal);
        initComponents();

        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        cons.weightx = 1;
        cons.weighty = 0;
        cons.fill = 1;
        cons.anchor = 10;
        cons.insets = new Insets(7, 7, 7, 7);

        GenericFieldDescriptor descritor = XMLDescriptorFactory.getFieldDescriptor(Cliente.class, "org/west/xml/clienteRecepcao.xml", "cliente");
        JBeanPanel panel = new JBeanPanel(Cliente.class,"Cliente", descritor);
        panel.setBean(cliente);
        add(panel, cons);

        TableFieldDescriptor descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Imobiliaria.class, "org/west/xml/imobiliariaTabelaRecepcao.xml", "tabelaImobiliaria");
        final BeanTableModel modeloImobiliaria = new BeanTableModel(descriptorTable);
        final JBeanTable tabelaImobiliaria = new JBeanTable(modeloImobiliaria);

        cons.gridy = 1;
        cons.weighty = 1.0D;
        add(new JScrollPane(tabelaImobiliaria), cons);

        descritor = XMLDescriptorFactory.getFieldDescriptor(Imobiliaria.class, "org/west/xml/imobiliariaFormRecepcao.xml", "formImobiliaira");
        final JBeanPanel panelImobiliaria = new JBeanPanel(Imobiliaria.class,"Atendimento em Imobiliária", descritor);

        cons.gridy = 2;
        cons.weighty = 0.0D;
        add(panelImobiliaria, cons);

        descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Historico.class, "org/west/xml/historicoTabelaRecepcao.xml", "dataEntrada");
        final BeanTableModel modeloHistorico = new BeanTableModel(descriptorTable);
        JBeanTable tabelaHistorico = new JBeanTable(modeloHistorico);

        cons.gridy = 3;
        cons.weighty = 1.0D;
        add(new JScrollPane(tabelaHistorico), cons);

        List lista = Arrays.asList(ImobiliariaDAO.listImobiliariaByQuery("cliente=" + cliente.getCodigo(), "codigo"));
        modeloImobiliaria.setBeanList(lista);

        if (lista.size() == 1) {
            Imobiliaria imo = (Imobiliaria)lista.get(0);
            panelImobiliaria.setBean(imo);
            modeloHistorico.setBeanList(Arrays.asList(HistoricoDAO.listHistoricoByQuery("imobiliaria=" + imo.getCodigo(),"dataEntrada desc")));
        }

        tabelaImobiliaria.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                Imobiliaria imo = (Imobiliaria)modeloImobiliaria.getBeanAt(tabelaImobiliaria.getSelectedRow());
                panelImobiliaria.setBean(imo);
                modeloHistorico.setBeanList(new ArrayList<Historico>(imo.getHistoricos()));
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Cliente");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 549, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
