package org.west.formulario;

import org.west.componentes.DesktopSession;
import org.west.componentes.JMSManager;
import org.west.componentes.PanelContatos;
import com.sun.messaging.Queue;
import org.west.formulario.telefone.FrmTelefone;
import java.awt.BorderLayout;
import java.util.Random;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.hibernate.criterion.Projections;
import org.west.entidades.*;
import org.west.formulario.recados.FrmRecadosUsuario;

public class FrmMain extends javax.swing.JFrame implements MessageListener {

    private PanelContatos panelContatos;
    protected final Usuario usuario;

    public FrmMain() {
        initComponents();
        this.usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        this.panelContatos = new PanelContatos();
        this.panelContatos.setVisible(false);

        add(this.panelContatos, BorderLayout.EAST);

        try {
            ConnectionFactory fabrica = JMSManager.getFabrica();
            Connection conexao = fabrica.createConnection();
            Session session = conexao.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination dest = new Queue(usuario.getNome());

            MessageConsumer consome = session.createConsumer(dest);

            conexao.start();

            Thread.sleep(1000L);

            Message msg = consome.receiveNoWait();

            while (msg != null) {
                msg = consome.receiveNoWait();
            }
            consome.setMessageListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        verificaRecados();
        pegaFrase();
    }

    @Override
    public void onMessage(Message msg) {
        try {
            String tipo = msg.getStringProperty("tipo");

            if (tipo.equals("recado")) {
                String recados = this.botaoRecados.getText();
                Integer qt = new Integer(0);

                if (recados.contains("Recado não lido.")) {
                    qt = new Integer(this.botaoRecados.getText().substring(0, this.botaoRecados.getText().indexOf("R")).trim());
                }

                qt++;
                this.botaoRecados.setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/recados2.png")));
                this.botaoRecados.setToolTipText("Existem recados não lidos!");
                this.botaoRecados.setText(qt + " Recado não lido.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void verificaRecados() {
        WestPersistentManager.clear();
        Recado[] recados = RecadoDAO.listRecadoByQuery("usuario = " + this.usuario.getCodigo() + " and leitura is null", "codigo");

        if (recados.length > 0) {
            this.botaoRecados.setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/recados2.png")));
            this.botaoRecados.setToolTipText("Existem recados não lidos!");
            this.botaoRecados.setText(recados.length + " Recado não lido.");
        } else {
            this.botaoRecados.setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/Recados.png")));
            this.botaoRecados.setToolTipText("Não existem recados a serem lidos");
            this.botaoRecados.setText("Sem recados");
        }
    }

    public PanelContatos getContatos() {
        return this.panelContatos;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        botaoRecados = new javax.swing.JButton();
        botaoContatos = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblFrase = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema West");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        botaoRecados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/Recados.png"))); // NOI18N
        botaoRecados.setText("Sem Recados");
        botaoRecados.setToolTipText("Não existem recados a serem lidos");
        botaoRecados.setFocusable(false);
        botaoRecados.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        botaoRecados.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botaoRecados.setIconTextGap(8);
        botaoRecados.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoRecados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRecadosActionPerformed(evt);
            }
        });
        jToolBar1.add(botaoRecados);

        botaoContatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/whosonline-a.png"))); // NOI18N
        botaoContatos.setText("Contatos");
        botaoContatos.setFocusable(false);
        botaoContatos.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botaoContatos.setIconTextGap(8);
        botaoContatos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoContatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoContatosActionPerformed(evt);
            }
        });
        jToolBar1.add(botaoContatos);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/01.png"))); // NOI18N
        jButton1.setText("Telefones");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        lblFrase.setForeground(new java.awt.Color(0, 102, 0));
        lblFrase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/frase.png"))); // NOI18N
        jPanel1.add(lblFrase);

        jToolBar1.add(jPanel1);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoContatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoContatosActionPerformed
        this.panelContatos.setVisible(this.botaoContatos.isSelected());
        this.panelContatos.revalidate();
        this.panelContatos.repaint();
    }//GEN-LAST:event_botaoContatosActionPerformed

    private void botaoRecadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRecadosActionPerformed
        FrmRecadosUsuario frame = new FrmRecadosUsuario(this);
        frame.setVisible(true);
    }//GEN-LAST:event_botaoRecadosActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (usuario.getNivel() == 2) {
            JOptionPane.showMessageDialog(null, "Desculpe, você não tem acesso à esse recurso.");
        } else {
            FrmTelefone frm = new FrmTelefone();
            frm.setVisible(true);
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton botaoContatos;
    private javax.swing.JButton botaoRecados;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    protected javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblFrase;
    // End of variables declaration//GEN-END:variables

    private void pegaFrase() {
        try {
            FraseCriteria criteria = new FraseCriteria();
            criteria.setProjection(Projections.rowCount());
            Long count = (Long) criteria.uniqueResult();

            Random random = new Random();
            Integer codigo = random.nextInt(count.intValue()) + 1;

            criteria = new FraseCriteria();
            criteria.setMaxResults(1);
            criteria.setFirstResult(codigo);

            Frase frase = criteria.uniqueFrase();

            lblFrase.setText("Frase do dia: " + frase.getFrase());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}