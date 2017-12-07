package org.west.formulario.documentacao;

import org.west.utilitarios.RenderTarefas;
import org.west.entidades.Tarefa;
import org.west.entidades.WestPersistentManager;
import java.awt.BorderLayout;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.hibernate.cfg.CollectionSecondPass;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;
import org.west.entidades.Servico;
import org.west.utilitarios.Util;

public class FrmTarefa extends JFrame {

    private JBeanPanel<Tarefa> formTarefa;
    private JBeanTable tableTarefas;
    private BeanTableModel modelTarefas;

    public FrmTarefa(List<Tarefa> listaTarefas, List<Servico> listaServico, JBeanPanel<Tarefa> form) {
        initComponents();
        setLocationRelativeTo(null);
        
        Collections.sort(listaTarefas);

        this.formTarefa = form;

        TableFieldDescriptor descriptor = XMLDescriptorFactory.getTableFieldDescriptor(Tarefa.class, "org/west/xml/tarefaTabelaAviso.xml", "tabelaAviso");
        modelTarefas = new BeanTableModel(descriptor);
        tableTarefas = new JBeanTable(modelTarefas);

        tableTarefas.addDoubleClickAction(new ApplicationAction() {
            @Override
            public void execute() {
                formTarefa.setBean((Tarefa) modelTarefas.getBeanAt(tableTarefas.getSelectedRow()));
            }
        });

        modelTarefas.setBeanList(listaTarefas);
        this.panelTarefas.add(new JScrollPane(tableTarefas), BorderLayout.CENTER);

        RenderTarefas render = new RenderTarefas(listaTarefas);

        for (int x = 0; x < modelTarefas.getColumnCount(); ++x) {
            tableTarefas.getColumnModel().getColumn(x).setCellRenderer(render);
        }

        descriptor = XMLDescriptorFactory.getTableFieldDescriptor(Servico.class, "org/west/xml/servicoTabelaAviso.xml", "ServicoAviso");
        BeanTableModel modelServico = new BeanTableModel(descriptor);
        JBeanTable tableServico = new JBeanTable(modelServico);

        this.panelServicos.add(new JScrollPane(tableServico), BorderLayout.CENTER);

        if (!listaServico.isEmpty()) {
            modelServico.setBeanList(listaServico);
        }

        Util.tocarSom("/sons/warn.wav");

        WestPersistentManager.clear();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        panelTarefas = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panelServicos = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Tarefas a cumprir");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 16));
        jLabel1.setText("Tarefas");

        panelTarefas.setMinimumSize(new java.awt.Dimension(300, 200));
        panelTarefas.setLayout(new java.awt.BorderLayout());

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("As seguintes tarefas estão atrasadas ou com o prazo esgotando:");

        jLabel3.setText("Os serviços abaixo não possuem tarefas ativas há mais de 24 horas:");

        panelServicos.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelServicos, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(panelTarefas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTarefas, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelServicos, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel panelServicos;
    private javax.swing.JPanel panelTarefas;
    // End of variables declaration//GEN-END:variables
}
