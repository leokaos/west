package org.west.formulario.cliente;

import org.west.componentes.PanelContatoCliente;
import org.west.entidades.Cliente;
import org.west.entidades.ClienteDAO;
import org.west.entidades.Endereco;
import org.west.entidades.EnderecoCriteria;
import org.west.entidades.EnderecoDAO;
import org.west.entidades.Usuario;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
import org.west.entidades.Cep;
import org.west.entidades.WestPersistentManager;
import org.west.utilitarios.DocumentNotRemove;

public class FrmCliente extends javax.swing.JDialog {

    private GenericFieldDescriptor descriptorForm;
    private TableFieldDescriptor descriptorTable;
    private JBeanPanel<Cliente> formCliente;
    private Cliente cliente;
    private BeanTableModel<Endereco> modelEndereco;
    private JBeanTable tabelaEndereco;
    private JBeanPanel<Endereco> formEndereco;
    private PanelContatoCliente contatos;
    private Usuario usuario;
    
    public FrmCliente(){
        this(null);
    }
       
    public FrmCliente(Cliente cli) {
        super((JFrame) null, true);
        initComponents();
        setLocationRelativeTo(null);
        
        usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        this.cliente = cli;

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.anchor = GridBagConstraints.CENTER;
        cons.weightx = 1;
        cons.fill = GridBagConstraints.BOTH;

        descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Cliente.class, "org/west/xml/clienteForm.xml", "clienteFormDetalhes");
        formCliente = new JBeanPanel<Cliente>(Cliente.class, "Informações do Cliente", descriptorForm);
        
        if (cliente != null){
            if (usuario.getNivel() != 1 && !usuario.isSupervisor())
                ((JTextField) formCliente.getComponent("nome")).setDocument(new DocumentNotRemove(this.cliente.getNome()));
            
            formCliente.setBean(this.cliente);
        }

        cons.gridx = 0;
        cons.gridy = 0;
        cons.gridwidth = 2;
        add(formCliente, cons);

        contatos = new PanelContatoCliente(this.cliente);

        cons.gridy = 1;
        cons.gridwidth = 1;
        cons.weightx = 0.75;
        add(contatos, cons);

        descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Endereco.class, "org/west/xml/enderecoTabela.xml", "tabelaEndereco");
        modelEndereco = new BeanTableModel<Endereco>(descriptorTable);
        tabelaEndereco = new JBeanTable(modelEndereco);
        tabelaEndereco.enableHeaderOrdering();
        
        tabelaEndereco.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                formEndereco.setBean(modelEndereco.getBeanAt(tabelaEndereco.getSelectedRow()));
            }
        });        

        cons.gridx = 1;
        cons.weightx = 1;
        cons.weighty = 1;
        add(new JScrollPane(tabelaEndereco), cons);
        
        descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Endereco.class, "org/west/xml/enderecoForm.xml", "enderecoForm");
        formEndereco = new JBeanPanel<Endereco>(Endereco.class, "Informações do Endereço", descriptorForm);    
        
        ((JPanel) formEndereco.getComponent("cep")).getComponent(0).addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                Cep Cep = (Cep) formEndereco.getPropertyValue("cep");

                if (Cep != null)
                    formEndereco.setPropertyValue("logradouro", Cep.getTipo() + " " + Cep.getDescricao());
                else
                    formEndereco.setPropertyValue("logradouro", "");
            }
        });
        
        formEndereco.getComponent("logradouro").addPropertyChangeListener("selecionado",new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                formEndereco.setPropertyValue("cep", formEndereco.getPropertyValue("logradouro"));
                formEndereco.setPropertyValue("logradouro",formEndereco.getPropertyValue("logradouro").toString());
            }

        });        
               
        JPanel panelEndereco = new JPanel(new BorderLayout(5, 5));
        JPanel panelBotoesEndereco = new JPanel(new BorderLayout(5, 5));
        
        panelEndereco.add(formEndereco,BorderLayout.CENTER);
        panelEndereco.add(panelBotoesEndereco,BorderLayout.EAST);
        
        ValidationAction actionValidaEndereco = new ValidationAction(formEndereco);
        ApplicationAction actionGrava = new ApplicationAction() {

            @Override
            public void execute() {
                Endereco endereco = new Endereco();
                formEndereco.populateBean(endereco);
                endereco.setCliente(cliente);
                
                if (EnderecoDAO.save(endereco))
                    JOptionPane.showMessageDialog(null, "Endereço salvo com sucesso!");
                else
                    JOptionPane.showMessageDialog(null, "Erro ao salvar endereço!");
                
                WestPersistentManager.clear();
                carregarEnderecos();
            }
        };
        
        JActButton btnGravaEndereco = new JActButton("",ActionChainFactory.createChainActions(actionValidaEndereco,actionGrava));
        
        btnGravaEndereco.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/adicionar.png")));
        
        JActButton btnRemoveEndereco = new JActButton("", new ApplicationAction() {

            @Override
            public void execute() {
                if (tabelaEndereco.getSelectedRow() >= 0){
                    int x = JOptionPane.showConfirmDialog(null,"Tem certeza que deseja excluir o endereço selecionado?");

                    if ( x == JOptionPane.YES_OPTION){
                        Endereco endereco = modelEndereco.getBeanAt(tabelaEndereco.getSelectedRow());

                        if (EnderecoDAO.delete(endereco))
                            JOptionPane.showMessageDialog(null, "Endereço excluido com sucesso!");
                        else
                            JOptionPane.showMessageDialog(null, "Erro ao deletar endereço!");

                        WestPersistentManager.clear();
                        carregarEnderecos();
                        formEndereco.cleanForm();
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "Não foi selecionado um endereço!");                
            }
        });
        
        btnRemoveEndereco.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/cancel.png")));
        
        JActButton btnLimparEndereco = new JActButton("", new ApplicationAction() {

            @Override
            public void execute() {
                formEndereco.cleanForm();
            }
        });
        
        btnLimparEndereco.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/limpar.png")));
        
        panelBotoesEndereco.setLayout(new GridBagLayout());
        
        GridBagConstraints consInner = new GridBagConstraints();
        consInner.insets = new Insets(5, 5, 5, 5);
        consInner.anchor = GridBagConstraints.CENTER;
        consInner.fill = GridBagConstraints.NONE;
        consInner.weightx = 1;
        consInner.weighty = 1;
        consInner.gridx = 0;
        
        if (usuario.getNivel() != 2 || usuario.isSupervisor()) {
            consInner.gridy = 0;
            panelBotoesEndereco.add(btnGravaEndereco, consInner);
            consInner.gridy = 1;
            panelBotoesEndereco.add(btnLimparEndereco, consInner);
            consInner.gridy = 2;
            panelBotoesEndereco.add(btnRemoveEndereco, consInner);
        }
        
        cons.gridx = 0;
        cons.weighty = 0;
        cons.gridy = 2;
        cons.gridwidth = 2;
        add(panelEndereco, cons);
        
        JPanel panelBotoesCliente = new JPanel(new BorderLayout(0, 0));
        
        ValidationAction actionValida = new ValidationAction(formCliente);
        
        ApplicationAction actionGravaCliente = new ApplicationAction() {

            @Override
            public void execute() {
                cliente = new Cliente();
                formCliente.populateBean(cliente);
                
                if (cliente.getCPF().length() == 14){
                    Cliente testeCPF = ClienteDAO.loadClienteByQuery("cpf='" + cliente.getCPF() + "'", "nome");
                                        
                    if (testeCPF != null){
                        if (!testeCPF.equals(cliente)){
                            JOptionPane.showMessageDialog(null, "Já existe um cliente com esse CPF!");
                            return;
                        }
                    }
                }
                else{
                    if (cliente.getCNPJ().length() == 18){
                        Cliente testeCNPJ = ClienteDAO.loadClienteByQuery("cnpj='" + cliente.getCNPJ() + "'", "nome");
                        
                        if (testeCNPJ != null && !testeCNPJ.equals(cliente)){
                            JOptionPane.showMessageDialog(null, "Já existe um cliente com esse CNPJ!");
                            return;                            
                        }
                    }
                    else{
                        if (usuario.getNivel() >= Usuario.ADMINISTRATIVO){
                            JOptionPane.showMessageDialog(null, "CPF obrigatório!");
                            return;
                        }
                    }
                }
                
                if (!contatos.isValido()){
                    JOptionPane.showMessageDialog(null, "Preenchimento incorreto dos telefones!");
                    return;
                }
                
                if (ClienteDAO.save(cliente)) {
                    contatos.setCliente(cliente);
                    contatos.salvarCliente();
                    JOptionPane.showMessageDialog(null, "Cliente salvo com sucesso!");
                    
                } else
                    JOptionPane.showMessageDialog(null, "Erro ao salvar cliente!");    
            }
        };
        
        ApplicationAction actionCliente = ActionChainFactory.createChainActions(actionValida,actionGravaCliente);

        JActButton btnGravarCliente = new JActButton("Gravar",actionCliente);
        btnGravarCliente.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/save.png")));

        JActButton btnLimpaCliente = new JActButton("Limpar", new ApplicationAction() {
            
            @Override
            public void execute() {
                formCliente.cleanForm();
                formEndereco.cleanForm();
                modelEndereco.setBeanList(new ArrayList<Endereco>());
                contatos.limpar();
            }
        });
        btnLimpaCliente.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/limpar.png")));       
        
        if (usuario.isSupervisor() && usuario.getNivel() != 2)
            panelBotoesCliente.add(btnLimpaCliente,BorderLayout.WEST);
        
        panelBotoesCliente.add(btnGravarCliente,BorderLayout.EAST);

        cons.gridy = 3;
        cons.fill = GridBagConstraints.HORIZONTAL;
        add(panelBotoesCliente,cons);    
        
        if (cliente != null)        
            carregarEnderecos();      
    }
    
    public void addTelefones(String str){
        String[] telefones = str.split(" ");
        
        for (String string : telefones)
            contatos.addTelefone(string);
    }
    
    private void carregarEnderecos(){
        try{
            EnderecoCriteria criteria = new EnderecoCriteria();
            criteria.add(Restrictions.eq("cliente", cliente));
            
            modelEndereco.setBeanList(criteria.listEnderecos());
            
            WestPersistentManager.clear();
       }
        catch(Exception ex){ex.printStackTrace();}
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Cadastro de Cliente");
        setMinimumSize(new java.awt.Dimension(700, 700));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}