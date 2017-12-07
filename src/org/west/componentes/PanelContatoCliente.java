package org.west.componentes;

import org.west.entidades.Cliente;
import org.west.entidades.Email;
import org.west.entidades.EmailDAO;
import org.west.entidades.Telefone;
import org.west.entidades.TelefoneDAO;
import org.west.entidades.Usuario;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;
import org.hibernate.LockMode;
import org.hibernate.criterion.Restrictions;
import org.jfree.ui.tabbedui.VerticalLayout;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanPanel;
import org.west.entidades.ClienteDAO;
import org.west.entidades.TelefoneCriteria;

public class PanelContatoCliente extends javax.swing.JPanel {
    
    private Cliente cliente;
    private Integer cont;
    private Usuario usuario;
    
    private List<JBeanPanel<Telefone>> listaTelefone;
    private List<JBeanPanel<Email>> listaEmail;    

    public PanelContatoCliente(Cliente cli) {
        initComponents();
        
        this.cliente = cli;
        this.cont = 0;
        
        this.panelContato.setLayout(new VerticalLayout());
        
        this.usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");
        
        this.listaEmail = new ArrayList<JBeanPanel<Email>>();
        this.listaTelefone = new ArrayList<JBeanPanel<Telefone>>();  
        
        carregarCliente();
    }
    
    private JBeanPanel<Telefone> addPanelTelefone(){
        GenericFieldDescriptor descriptor = 
                XMLDescriptorFactory.getFieldDescriptor(Telefone.class,"org/west/xml/telefoneForm.xml","telefone"+cont);
        
        JBeanPanel<Telefone> panel = new JBeanPanel<Telefone>(Telefone.class, descriptor);
        panel.setBorder(null);
        panel.setPreferredSize(new Dimension(0, 50));
              
        cont++;
        
        listaTelefone.add(panel);
                
        ajustarPanel(panel);
        
        return panel;
    }
    
    private JBeanPanel<Email> addPanelEmail(){
        GenericFieldDescriptor descriptor = 
                XMLDescriptorFactory.getFieldDescriptor(Email.class,"org/west/xml/emailForm.xml","email"+cont);
        
        JBeanPanel<Email> panel = new JBeanPanel<Email>(Email.class, descriptor);
        panel.setBorder(null);
        panel.setPreferredSize(new Dimension(0, 50));
                              
        cont++;
        
        listaEmail.add(panel);
        
        ajustarPanel(panel);
        
        return panel;
    }    
    
    private void ajustarPanel(JPanel panel){
        panelContato.add(panel);        
        
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                
                Component comp = (Component) e.getSource();
                
                if (listaEmail.contains(e.getSource())){
                    Email email = new Email();
                    ((JBeanPanel<Email>) e.getSource()).populateBean(email); 
                    
                    if (email.getCodigo() == null){
                        panelContato.remove(comp);
                        listaEmail.remove(comp);
                        cont--;
                    }
                    else{
                        if (usuario.isSupervisor() && usuario.getNivel() != 2){
                            int x = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o contato selecionado?");
                            
                            if (x == JOptionPane.YES_OPTION){
                                if (EmailDAO.delete(email)) {
                                    listaEmail.remove(comp);
                                    panelContato.remove(comp);
                                    cont--;
                                } else 
                                    JOptionPane.showMessageDialog(null, "Erro ao deletar contato!");                            
                            }
                        }
                    }
                    
                }
                
                if (listaTelefone.contains(e.getSource())){
                    Telefone telefone = new Telefone();
                    ((JBeanPanel<Telefone>) e.getSource()).populateBean(telefone);
                    
                    if (telefone.getCodigo() == null){
                        panelContato.remove(comp);
                        listaTelefone.remove(comp);
                        cont--;
                    }
                    else{
                        if (usuario.isSupervisor() && usuario.getNivel() != 2){
                            int x = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o contato selecionado?");
                            
                            if (x == JOptionPane.YES_OPTION){
                                if (TelefoneDAO.delete(telefone)) {
                                    listaTelefone.remove(comp);
                                    panelContato.remove(comp);
                                    cont--;
                                } else 
                                    JOptionPane.showMessageDialog(null, "Erro ao deletar contato!");                            
                            }
                        }
                    }
                }    
                
                ajustarPanel();
            }
        });
        
        ajustarPanel();
    }
    
    private void ajustarPanel(){
        panelContato.setPreferredSize(new Dimension(0,50* cont)); 
        panelContato.revalidate();
        panelContato.repaint();        
    }
    
    private void carregarCliente(){
        panelContato.removeAll();
        cont = 0;

        if (cliente != null) {
            ClienteDAO.refresh(cliente);
            ClienteDAO.lock(cliente, LockMode.NONE);
            
            listaTelefone = new ArrayList<JBeanPanel<Telefone>>();
            listaEmail = new ArrayList<JBeanPanel<Email>>();

            for (Object obj : cliente.getTelefones()) {
                Telefone telefone = (Telefone) obj;
                addPanelTelefone().setBean(telefone);
            }

            for (Object obj : cliente.getEmails()) {
                Email email = (Email) obj;
                addPanelEmail().setBean(email);
            }
        }
        
        panelContato.revalidate();
        panelContato.repaint();
    }
    
    public void limpar(){
        cont=0;
        panelContato.removeAll();
        ajustarPanel();
        cliente = null;
    }
    
    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }
    
    public void addTelefone(String str){
        Telefone telefone = new Telefone();           
        telefone.setTelefone("11" + str);
        addPanelTelefone().setBean(telefone);
    }
    
    public Boolean isValido(){
        if (listaTelefone.size() <=0)
            return false;
        else{
            Boolean retorno = true;
            
            for(JBeanPanel<Telefone> panelTelefone : listaTelefone)
                retorno = retorno && panelTelefone.getValidationResult().isEmpty();
            
            return retorno;
        }
    }
        
    public void salvarCliente(){
        jButton3.setEnabled(false);
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (cliente != null){
                    for(JBeanPanel<Telefone> panel : listaTelefone){
                        Telefone telefone = new Telefone();
                        panel.populateBean(telefone);

                        telefone.setCliente(cliente);

                        TelefoneDAO.save(telefone);
                    }
                    
                    for(JBeanPanel<Email> panel : listaEmail){
                        Email email = new Email();
                        panel.populateBean(email);

                        email.setCliente(cliente);

                        EmailDAO.save(email);
                    }
                    
                    ClienteDAO.refresh(cliente);
                    ClienteDAO.lock(cliente);
                    
                    carregarCliente();
                    
                    List<String> listaTelefones = new ArrayList<String>();
                                        
                    for (Telefone telefone : cliente.getTelefones())
                        listaTelefones.add(telefone.getTelefone());
                    
                    TelefoneCriteria criteria = new TelefoneCriteria();
                    criteria.add(Restrictions.in("telefone", listaTelefones));
                    criteria.add(Restrictions.not(Restrictions.eq("cliente", cliente)));   
                    
                    List<Telefone> listaResultado = new ArrayList<Telefone>(criteria.list());
               
                    if (!listaResultado.isEmpty()){
                        
                        try{
                        
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            String msg = "\n" + cliente.getCodigo() + "\n";

                            for (Telefone telefone : listaResultado) {
                                msg = msg + telefone.getCliente().getCodigo() + "\n";
                            }

                            msg = msg + "\n\n Mensagem gerada em: " + format.format(new Date()) + " com o usuário "
                                    + DesktopSession.getInstance().getObject("usuario").toString() + " logado.";

                            org.apache.commons.mail.Email email = new SimpleEmail();
                            email.setHostName("smtp.westguerra.com.br");
                            email.setSmtpPort(587);
                            email.setAuthenticator(new DefaultAuthenticator("suporte@westguerra.com.br", "west1234"));
                            email.setFrom("suporte@westguerra.com.br");
                            email.setSubject("Aviso de Duplicidade - NÃO RESPONDER A ESTE E-MAIL");
                            email.setMsg("Os seguintes Clientes estão com número de telefone igual:" + msg);
                            email.addTo("enio@westguerra.com.br");
                            email.send();
                        }
                        catch(Exception ex){ex.printStackTrace();}
                    }
                    carregarCliente();
                }
                else
                    JOptionPane.showMessageDialog(null, "Cliente inexistente! Salve o cliente primeiro!");   
                
                jButton3.setEnabled(true);
            }            
        }).start();        
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelContato = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/phone_add.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/mail_add.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout panelContatoLayout = new javax.swing.GroupLayout(panelContato);
        panelContato.setLayout(panelContatoLayout);
        panelContatoLayout.setHorizontalGroup(
            panelContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );
        panelContatoLayout.setVerticalGroup(
            panelContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 405, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panelContato);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/save.png"))); // NOI18N
        jButton3.setText("Gravar");
        jButton3.setIconTextGap(8);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        add(jPanel2, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addPanelTelefone();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addPanelEmail();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        salvarCliente();
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelContato;
    // End of variables declaration//GEN-END:variables
}