package org.west.formulario.recepcao;

import java.text.SimpleDateFormat;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.west.componentes.DesktopSession;
import org.west.entidades.Ponto;
import org.west.entidades.PontoDAO;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioDAO;

public class FrmLiberarUsuario extends javax.swing.JDialog {

    public FrmLiberarUsuario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        setSize(350,350);
        setLocationRelativeTo(null);
        
        DefaultListModel model = new DefaultListModel();
        
        for(Usuario usuario : UsuarioDAO.listUsuarioByQuery("nivel=2 and status=1 and bloqueado=0", "nome"))
            model.addElement(usuario);
        
        listaUsuarios.setModel(model);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaUsuarios = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Liberar Usuário");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel1.setText("Liberar Usuário");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setText("Selecione o usuário a ser liberado e clique em Liberar.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel2, gridBagConstraints);

        listaUsuarios.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listaUsuarios);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jButton1.setText("Liberar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jButton1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Object[] options  = {"Sim","Não"};
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja liberar o usuário " + listaUsuarios.getSelectedValue().toString() + "?",
                   "Liberar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                   options, options[1]);
                
        if (resp == 0){
            Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");
            
            Ponto ponto = new Ponto();
            ponto.setDataPonto(PontoDAO.getDateServer());
            ponto.setLiberadoPor(usuario.getNome());
            ponto.setObs("Entrada - Liberada");
            ponto.setUsuario((Usuario) listaUsuarios.getSelectedValue());
            
            if (PontoDAO.save(ponto)){
                
                try{
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Email email = new SimpleEmail();
                    email.setHostName("smtp.westguerra.com.br");
                    email.setSmtpPort(587);
                    email.setCharset("UTF-8");
                    email.setAuthenticator(new DefaultAuthenticator("suporte@westguerra.com.br", "west1234"));
                    email.setFrom("suporte@westguerra.com.br");
                    email.setSubject("[MEMO] Aviso de Liberação - NÃO RESPONDER A ESTE E-MAIL");
                    email.setMsg("O usuário " + ponto.getUsuario().getNome() + " foi liberado em " + format.format(ponto.getDataPonto())
                            + " pelo usuário " + ponto.getLiberadoPor() + ".");
                    email.addTo("enio@westguerra.com.br");
                    email.send();       
                }
                catch(Exception ex){ex.printStackTrace();}
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList listaUsuarios;
    // End of variables declaration//GEN-END:variables
}