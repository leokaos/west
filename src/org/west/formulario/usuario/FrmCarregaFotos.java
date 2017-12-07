package org.west.formulario.usuario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.hibernate.criterion.Restrictions;
import org.west.entidades.Foto;
import org.west.entidades.FotoCriteria;
import org.west.entidades.FotoDAO;
import org.west.entidades.Imovel;
import org.west.entidades.ImovelDAO;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioDAO;
import org.west.entidades.WestPersistentManager;
import org.west.utilitarios.Util;

public class FrmCarregaFotos extends javax.swing.JFrame {

    public FrmCarregaFotos() {
        initComponents();

        setLocationRelativeTo(null);

        Usuario usuario = UsuarioDAO.load(10L);

        setTitle("Sistema West - Usuario " + usuario.getNome());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("   ");

        jLabel3.setText("Arquivo:");

        jButton1.setText("Começar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setEnabled(false);
        jScrollPane1.setViewportView(jTextArea1);

        jProgressBar1.setStringPainted(true);

        jButton2.setText("Exportar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jButton1.setEnabled(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    File files = new File("\\\\servidor\\Fotos\\Fachadas");

                    int qt = files.listFiles().length;

                    jProgressBar1.setMaximum(qt);
                    int cont = 0;

                    for (File file : files.listFiles()) {
                        jTextArea1.append(file.getAbsolutePath() + "\n");

                        jLabel2.setText(file.getAbsolutePath());

                        if (file.getName().toLowerCase().endsWith("jpg") || file.getName().toLowerCase().endsWith("jpeg")) {

                            Imovel imovel = determinarImovel(file.getName());

                            transferirFoto(file);

                            if (imovel != null) {
                                WestPersistentManager.clear();
                                GregorianCalendar calend = new GregorianCalendar();
                                calend.set(2000, 1, 1);

                                Foto foto = new Foto();
                                foto.setImovel(imovel);
                                foto.setCaminho(file.getName());
                                foto.setSite(false);
                                foto.setPrincipal(false);
                                foto.setDataEntrada(calend.getTime());

                                if (!FotoDAO.save(foto)) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                        jTextArea1.setCaretPosition(jTextArea1.getText().length());

                        jProgressBar1.setValue(cont);
                        cont++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            private Imovel determinarImovel(String name) {
                String imovel = "";

                for (int x = 0; x < name.length(); x++) {
                    if (Character.isDigit(name.charAt(x))) {
                        imovel += name.charAt(x);
                    } else {
                        break;
                    }
                }

                if (imovel.isEmpty()) {
                    return null;
                } else {
                    return ImovelDAO.get(new Long(imovel));
                }
            }
        }).start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jButton2.setEnabled(false);

        new Thread(new Runnable() {

            @Override
            public void run() {
                GregorianCalendar ini = new GregorianCalendar(2011, 4, 1, 0, 0, 0);
                GregorianCalendar fim = new GregorianCalendar(2012, 2, 28, 23, 59, 59);

                FotoCriteria criteria = new FotoCriteria();
                criteria.add(Restrictions.eq("site", true));
                criteria.createImovelCriteria()
                        .add(Restrictions.gt("referencia", 31995l))
                        .add(Restrictions.eq("status", "Ativo"))
                        .add(Restrictions.between("atualizado", ini.getTime(), fim.getTime()));

                List<Foto> lista = criteria.list();

                jProgressBar1.setMaximum(lista.size());
                int cont = 0;

                try {
                    FTPClient ftp = new FTPClient();
                    ftp.connect("ftp.westguerra.com.br");

                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://mysql01.westguerra.hospedagemdesites.ws/westguerra?autoReconnect=true", "westguerra", "w3s4t5");


                    if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {

                        if (ftp.login("westguerra", "w3s4t5")) {

                            String caminhoLocal = Util.getPropriedade("west.foto_dir") + "\\";
                            String caminhoRemoto = Util.getPropriedade("west.ftp_dir");

                            ftp.changeWorkingDirectory(caminhoRemoto);
                            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

                            for (Foto foto : lista) {

                                conn.prepareStatement("insert into westguerra.tab_fotos (imovel,caminho,descricao,principal)"
                                        + " VALUES (" + foto.getImovel().getReferencia() + ",'" + foto.getCaminho() + "','" + foto.getDescricao()
                                        + "'," + foto.isPrincipal() + ")").executeUpdate();

                                String caminho = caminhoLocal + foto.getCaminho();
                                FileInputStream in = new FileInputStream(new File(caminho));
                                ftp.storeFile(foto.getCaminho(), in);
                                cont++;
                                jLabel3.setText(foto.getCaminho());
                                jProgressBar1.setValue(cont);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao logar!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao conectar!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

    }//GEN-LAST:event_jButton2ActionPerformed

    /* 
     public static void main(String args[]) {
     java.awt.EventQueue.invokeLater(new Runnable() {
     @Override
     public void run() {
     new FrmCarregaFotos().setVisible(true);
     }
     });
     }*/
    private void transferirFoto(File file) throws Exception {
        File pastaDestino = new File(Util.getPropriedade("west.foto_dir"));

        FileChannel chanelOrigem = new FileInputStream(file).getChannel();

        String nome = file.getName();

        File fileDestino = new File(pastaDestino.getAbsolutePath() + "\\" + nome);
        FileChannel chanelDestino = new FileOutputStream(fileDestino).getChannel();

        chanelDestino.transferFrom(chanelOrigem, 0, chanelOrigem.size());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}