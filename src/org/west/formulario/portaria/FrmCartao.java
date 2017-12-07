package org.west.formulario.portaria;

import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanTable;
import org.west.entidades.Cartao;
import org.west.entidades.CartaoDAO;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioDAO;
import org.west.entidades.WestPersistentManager;

public class FrmCartao extends javax.swing.JDialog {

    private TableFieldDescriptor descriptor;
    private BeanTableModel<Cartao> modelCartao;
    private JBeanTable tabelaCartao;

    public FrmCartao(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        setLocationRelativeTo(null);

        descriptor = XMLDescriptorFactory.getTableFieldDescriptor(Cartao.class, "org/west/xml/cartaoControle.xml", "cartaoControle");
        modelCartao = new BeanTableModel<Cartao>(descriptor);

        tabelaCartao = new JBeanTable(modelCartao);
        tabelaCartao.setRowHeight(30);
        tabelaCartao.enableQuickEditing();

        jPanel1.add(new JScrollPane(tabelaCartao));

        modelCartao.setBeanList(Arrays.asList(CartaoDAO.listCartaoByQuery("usuario is not null", "usuario")));

        WestPersistentManager.clear();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Controle de Cartão");
        setMinimumSize(new java.awt.Dimension(443, 368));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Tabela de Cartões");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel1, gridBagConstraints);

        jButton1.setText("OK");
        jButton1.setPreferredSize(new java.awt.Dimension(75, 28));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jButton1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jPanel1, gridBagConstraints);

        jButton2.setText("Novo...");
        jButton2.setPreferredSize(new java.awt.Dimension(75, 28));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jButton2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        for(Cartao cartao : modelCartao.getUpdated())
            CartaoDAO.save(cartao);
        
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String str = JOptionPane.showInputDialog(null, "Nome do corretor:", "Novo Corretor...", JOptionPane.INFORMATION_MESSAGE);
        if (!str.isEmpty()){
            Usuario usuario = UsuarioDAO.loadUsuarioByQuery("nome='" + str + "'","nome");

            if (usuario == null)
                JOptionPane.showMessageDialog(null, "Usuário Inválido!","Erro",JOptionPane.ERROR_MESSAGE);
            else{
                Cartao novo = new Cartao();
                novo.setUsuario(usuario);
                novo.setQuantidade(0L);


                if (CartaoDAO.save(novo))
                    modelCartao.addBean(novo);
                else
                    JOptionPane.showMessageDialog(null, "Erro ao salvar registro!");
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}