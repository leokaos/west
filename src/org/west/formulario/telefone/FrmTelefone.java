package org.west.formulario.telefone;

import org.west.entidades.Contato;
import org.west.entidades.ContatoCriteria;
import org.west.entidades.ContatoDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.swingBean.actions.ActionChainFactory;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.actions.ValidationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JActButton;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;
import org.west.componentes.DesktopSession;
import org.west.entidades.Usuario;
import org.west.entidades.WestPersistentManager;

public class FrmTelefone extends javax.swing.JFrame {

    public FrmTelefone() {
        initComponents();
        setLayout(new GridBagLayout());
        setSize(750, 550);
        setLocationRelativeTo(null);

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(10, 10, 10, 10);
        cons.weightx = 1;
        
        JPanel panelBusca = new JPanel(new BorderLayout(10, 10));
        panelBusca.add(new JLabel("Busca: "),BorderLayout.WEST);
        final JTextField txtBusca = new JTextField();
        panelBusca.add(txtBusca);

        cons.gridx = 0;
        cons.gridy = 0;
        cons.fill = GridBagConstraints.BOTH;
        cons.gridwidth = 2;
        add(panelBusca,cons);

        TableFieldDescriptor descritor = XMLDescriptorFactory.getTableFieldDescriptor(Contato.class, "org/west/xml/contatoTabela.xml","tabelaContato");
        final BeanTableModel<Contato> modelo =  new BeanTableModel<Contato>(descritor);
        final JBeanTable tabela = new JBeanTable(modelo);

        cons.gridy = 1;
        cons.gridwidth = 1;
        cons.gridheight = 2;
        cons.weighty = 1;
        add(new JScrollPane(tabela),cons);

        GenericFieldDescriptor descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Contato.class, "org/west/xml/contatoForm.xml","formContato");
        final JBeanPanel<Contato> formContato = new JBeanPanel<Contato>(Contato.class,"Informações de Contatos",descriptorForm);

        cons.gridx = 1;
        cons.gridheight = 1;
        cons.weightx = 0;
        cons.weighty = 0;
        cons.ipady = 30;
        cons.fill = GridBagConstraints.NONE;
        add(formContato,cons);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT,20,0));

        ApplicationAction gravaContato = new ApplicationAction() {

            @Override
            public void execute() {
                WestPersistentManager.clear();

                Contato contato = new Contato();
                formContato.populateBean(contato);

                if (ContatoDAO.save(contato))
                    JOptionPane.showMessageDialog(null, "Contato gravado com sucesso!");
                else
                    JOptionPane.showMessageDialog(null, "Erro ao gravar contato!");
            }
        };

        ValidationAction validaContato = new ValidationAction(formContato);
        ApplicationAction contato = ActionChainFactory.createChainActions(validaContato,gravaContato);

        panelBotoes.add(new JActButton("Gravar", contato));
        panelBotoes.add(new JActButton("Limpar", new ApplicationAction() {

            @Override
            public void execute() {
                formContato.cleanForm();
            }
        }));

        Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        if (usuario.isSupervisor()){
            panelBotoes.add(new JActButton("Deletar",new ApplicationAction() {

                @Override
                public void execute() {
                    WestPersistentManager.clear();

                    Contato contato = new Contato();
                    formContato.populateBean(contato);

                    if (ContatoDAO.delete(contato))
                        JOptionPane.showMessageDialog(null, "Contato excluido com sucesso!");
                    else
                        JOptionPane.showMessageDialog(null, "Erro ao excluir contato!");
                    }
            }));
        }

        cons.gridx = 1;
        cons.gridy = 2;
        cons.weightx = 0;
        cons.weighty = 1;
        cons.anchor = GridBagConstraints.FIRST_LINE_END;
        add(panelBotoes,cons);

        txtBusca.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                modelo.setBeanList(getLista(txtBusca.getText()));
            }
        });

        tabela.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                Contato contato = modelo.getBeanAt(tabela.getSelectedRow());
                formContato.setBean(contato);
            }
        });
        
    }

    private List<Contato> getLista(String nome){

        String[] busca = nome.split(" ");
        List<Contato> lista = new ArrayList<Contato>();

        try {
            ContatoCriteria criteriaContato = new ContatoCriteria();
            Conjunction con = Restrictions.conjunction();

            for (String string : busca)
                con.add(Restrictions.like("nome","%" + string + "%"));

            criteriaContato.add(con);
            criteriaContato.addOrder(Order.asc("nome"));

            lista = criteriaContato.list();

        } catch (Exception ex) {ex.printStackTrace();}

        return lista;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Telefones");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 743, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 489, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}