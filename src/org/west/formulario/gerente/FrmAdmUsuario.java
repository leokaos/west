package org.west.formulario.gerente;

import org.west.componentes.CriptoManager;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioCriteria;
import org.west.entidades.UsuarioDAO;
import org.west.entidades.WestPersistentManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
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

public class FrmAdmUsuario extends javax.swing.JDialog {

    private final TableFieldDescriptor descriptorTable;
    private final BeanTableModel<Usuario> modeloUsuario;
    private final JBeanTable tabelaUsuario;
    private GenericFieldDescriptor descriptorFormUsuario;
    private GenericFieldDescriptor descriptorFormDepartamento;
    private JBeanPanel<Usuario> panelUsuario;
    private JBeanPanel<Usuario> panelDepartamento;
    private List<Usuario> listaUsuarios;
    private JTextField txtNome;
    private JList listMembros;
    private JButton btnAdd;
    private final JList listaGrupos;
    private Usuario usuarioAtual;

    public FrmAdmUsuario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        usuarioAtual = new Usuario();
        initComponents();

        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(10, 10, 10, 10);
        cons.weighty = 1;
        cons.weightx = 1;
        cons.fill = GridBagConstraints.BOTH;

        cons.gridx = 0;
        cons.gridy = 0;
        cons.ipadx = 200;
        cons.gridheight = 2;

        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Usuario.class, "org/west/xml/usuarioTabelaGerente.xml", "tabelaUsuario");
        this.modeloUsuario = new BeanTableModel(this.descriptorTable);
        this.tabelaUsuario = new JBeanTable(this.modeloUsuario);

        getContentPane().add(new JScrollPane(this.tabelaUsuario), cons);

        this.tabelaUsuario.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                usuarioAtual = modeloUsuario.getBeanAt(tabelaUsuario.getSelectedRow());
                panelUsuario.setBean(usuarioAtual);
            }
        });
        this.listaUsuarios = this.modeloUsuario.getSynchronizedList();

        this.listaUsuarios = Arrays.asList(UsuarioDAO.listUsuarioByQuery("codigo > 0", "codigo"));

        this.modeloUsuario.setBeanList(this.listaUsuarios);

        cons.gridx = 1;
        cons.ipadx = 0;
        cons.gridheight = 1;
        cons.ipadx = 0;

        this.descriptorFormUsuario = XMLDescriptorFactory.getFieldDescriptor(Usuario.class, "org/west/xml/usuarioFormGerente.xml", "Usuario");
        this.panelUsuario = new JBeanPanel(Usuario.class, "", this.descriptorFormUsuario);
        this.panelUsuario.setBorder(new EmptyBorder(2, 2, 2, 2));

        this.descriptorFormDepartamento = XMLDescriptorFactory.getFieldDescriptor(Usuario.class, "org/west/xml/departamentoUsuario.xml", "UsuarioDepartamento");
        this.panelDepartamento = new JBeanPanel(Usuario.class, "", this.descriptorFormDepartamento);
        this.panelDepartamento.setBorder(new EmptyBorder(2, 2, 2, 2));

        JTabbedPane panelUsuarioTab = new JTabbedPane();
        panelUsuarioTab.addTab("Dados Básicos", panelUsuario);
        panelUsuarioTab.addTab("Departamento", panelDepartamento);

        getContentPane().add(panelUsuarioTab, cons);

        JPanel panelBotoes = new JPanel(new FlowLayout(2, 10, 5));

        ApplicationAction gravaUsuario = new ApplicationAction() {

            @Override
            public void execute() {
                WestPersistentManager.clear();

                panelUsuario.populateBean(usuarioAtual);
                panelDepartamento.populateBean(usuarioAtual);

                if (usuarioAtual.getSenha().length() != 40) {
                    usuarioAtual.setSenha(CriptoManager.hash(usuarioAtual.getSenha()));
                }

                if (usuarioAtual.getCodigo() == null) {
                    modeloUsuario.addBean(usuarioAtual);
                }
                UsuarioDAO.save(usuarioAtual);

                panelUsuario.cleanForm();
                panelDepartamento.cleanForm();
            }
        };

        ValidationAction validaUsuario = new ValidationAction(panelUsuario);

        ApplicationAction valida = ActionChainFactory.createChainActions(validaUsuario, gravaUsuario);

        JActButton botaoGravaUsuario = new JActButton("Gravar", valida);

        JActButton botaoLimpar = new JActButton("Limpar", new ApplicationAction() {

            @Override
            public void execute() {
                panelUsuario.cleanForm();
            }
        });
        panelBotoes.add(botaoGravaUsuario);
        panelBotoes.add(botaoLimpar);

        cons.gridx = 1;
        cons.gridy = 1;
        cons.weighty = 0;
        cons.fill = GridBagConstraints.HORIZONTAL;

        getContentPane().add(panelBotoes, cons);

        cons.gridx = 0;
        cons.gridy = 2;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
        JPanel panelGrupos = new JPanel(new BorderLayout());
        panelGrupos.setBorder(BorderFactory.createTitledBorder("Grupos Existentes"));

        listaGrupos = new JList(getGrupos().toArray());
        listaGrupos.setVisibleRowCount(6);

        listaGrupos.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (listaGrupos.getSelectedValue() != null) {
                    setGrupo(listaGrupos.getSelectedValue().toString());
                }
            }
        });

        panelGrupos.add(new JScrollPane(listaGrupos));

        getContentPane().add(panelGrupos, cons);

        cons.gridx = 1;
        JPanel panelAdmGrupos = getPanelGrupos();

        getContentPane().add(panelAdmGrupos, cons);
    }

    private List<String> getGrupos() {

        List<String> lista = new ArrayList<String>();
        try {
            UsuarioCriteria criteria = new UsuarioCriteria();

            ProjectionList proje = Projections.projectionList();
            proje.add(Projections.groupProperty("grupo"));

            criteria.setProjection(proje);
            lista = criteria.list();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    private void setGrupo(String item) {
        List<Usuario> lista = Arrays.asList(UsuarioDAO.listUsuarioByQuery("grupo='" + item + "'", "nome"));
        listMembros.setListData(lista.toArray());
        txtNome.setText(item);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Controle de Usuários");
        setPreferredSize(new java.awt.Dimension(800, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 666, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 507, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private JPanel getPanelGrupos() {
        JPanel panelAdmGrupos = new JPanel(new GridBagLayout());
        panelAdmGrupos.setBorder(BorderFactory.createTitledBorder("Grupo"));

        GridBagConstraints cons = new GridBagConstraints();
        cons.weightx = 1;
        cons.weighty = 1;
        cons.gridx = 0;
        cons.gridy = 0;
        cons.gridwidth = 2;
        cons.insets = new Insets(5, 5, 5, 5);
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.anchor = GridBagConstraints.PAGE_START;

        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panelAdmGrupos.add(panel, cons);

        panel.add(new JLabel("Nome:"), BorderLayout.WEST);

        txtNome = new JTextField();
        txtNome.setMaximumSize(new Dimension(0, 23));
        panel.add(txtNome, BorderLayout.CENTER);

        cons.gridx = 0;
        cons.gridy = 1;
        cons.gridwidth = 1;
        cons.weightx = 1;
        cons.gridheight = 3;
        cons.weighty = 1;
        cons.ipady = 80;
        cons.fill = GridBagConstraints.BOTH;
        listMembros = new JList();
        panelAdmGrupos.add(new JScrollPane(listMembros), cons);

        cons.gridx = 1;
        cons.gridy = 2;
        cons.weightx = 0;
        cons.weighty = 0;
        cons.ipady = 0;
        cons.gridheight = 1;
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.PAGE_END;
        btnAdd = new JButton("Adicionar");
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNome.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Não há grupo selecionado!");
                } else {
                    for (int usuario : tabelaUsuario.getSelectedRows()) {
                        listaUsuarios.get(usuario).setGrupo(txtNome.getText());
                        UsuarioDAO.save(listaUsuarios.get(usuario));
                    }
                    listaGrupos.setListData(getGrupos().toArray());
                    setGrupo(txtNome.getText());
                }
            }
        });
        panelAdmGrupos.add(btnAdd, cons);

        JButton btnLimpar = new JButton("  Novo...   ");
        btnLimpar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                txtNome.setText("");
                listMembros.setModel(new DefaultListModel());
            }
        });
        cons.gridy = 1;
        panelAdmGrupos.add(btnLimpar, cons);

        cons.gridy = 3;
        JButton btnDel = new JButton("Remover");
        btnDel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (listMembros.getSelectedIndices().length < 1) {
                    JOptionPane.showMessageDialog(null, "Não foi selecionado um membro!");
                } else {
                    for (int index : listMembros.getSelectedIndices()) {
                        Usuario usuario = (Usuario) listMembros.getModel().getElementAt(index);
                        usuario.setGrupo(null);
                        UsuarioDAO.save(usuario);
                    }
                    setGrupo(txtNome.getText());
                    listaGrupos.setListData(getGrupos().toArray());
                }
            }
        });

        panelAdmGrupos.add(btnDel, cons);

        return panelAdmGrupos;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
