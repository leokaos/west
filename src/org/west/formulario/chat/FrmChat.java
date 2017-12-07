package org.west.formulario.chat;

import org.west.entidades.Usuario;
import org.west.entidades.UsuarioDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.packet.Message;

public class FrmChat extends javax.swing.JFrame {

    private Chat chat;
    private String destino;
    private String origem;
    private Usuario usuario;

    public FrmChat(ChatManager manager, String dest, String ori) {
        initComponents();

        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(jButton2);

        this.destino = dest;
        this.origem = ori;

        usuario = UsuarioDAO.loadUsuarioByQuery("nome='" + this.origem + "'", "codigo");

        setTitle(getNome(dest) + " - " + getTitle());

        this.chat = manager.createChat(dest, null);
    }

    private String getNome(String valor) {
        String retorno = null;
        int pos = valor.indexOf("@");

        if (pos == -1) {
            retorno = valor;
        } else {
            retorno = valor.substring(0, pos);
        }
        return retorno;
    }

    private void rolarBarra() {
        this.jTextArea1.setCaretPosition(this.jTextArea1.getText().length());
    }

    private void mandarMensagem() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Message msg = new Message();
            msg.setBody(this.jTextField1.getText());
            msg.setSubject(df.format(new Date()));

            df = new SimpleDateFormat("HH:mm");
            this.chat.sendMessage(msg);

            this.jTextArea1.append(getNome(this.origem) + " (" + df.format(new Date()) + ") : \n    ");
            this.jTextArea1.append(this.jTextField1.getText() + " \n");

            rolarBarra();

            ArquivoChat arquivo = new ArquivoChat(getNome(this.destino), usuario, ArquivoChat.GRAVAR);
            arquivo.setTexto(getNome(this.origem) + " (" + df.format(new Date()) + ") : \n    " + this.jTextField1.getText() + " \n");

            new Thread(arquivo).start();

            this.jTextField1.setText("");
            this.jButton2.setEnabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addMensagem(Message msg) {
        String texto = getNome(msg.getFrom()) + " (" + msg.getSubject() + ") : \n    " + msg.getBody() + "\n";
        this.jTextArea1.append(texto);
        ArquivoChat arqChat = new ArquivoChat(getNome(destino),usuario, ArquivoChat.GRAVAR);
        arqChat.setTexto(texto);
        new Thread(arqChat).start();
        rolarBarra();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West");
        setMinimumSize(new java.awt.Dimension(500, 300));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jButton1.setText("Histórico");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jButton1, gridBagConstraints);

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jLabel1.setText("Texto:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField1, gridBagConstraints);

        jButton2.setText("Enviar");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButton2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ArquivoChat arquivo = new ArquivoChat(getNome(this.destino), usuario, ArquivoChat.LER);
        arquivo.setArea(this.jTextArea1);
        new Thread(arquivo).start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        mandarMensagem();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        this.jButton2.setEnabled(!(this.jTextField1.getText().length() == 0));
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if (evt.getKeyCode() == 27)
            dispose();
    }//GEN-LAST:event_jTextField1KeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}