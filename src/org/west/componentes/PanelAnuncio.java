package org.west.componentes;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.west.entidades.Anuncio;
import org.west.entidades.Imovel;
import org.west.entidades.Informacao;
import org.west.entidades.InformacaoDAO;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioDAO;
import org.west.entidades.Zap;
import org.west.entidades.ZapDAO;
import org.west.utilitarios.ValidadorUtil;

public class PanelAnuncio extends javax.swing.JPanel {

    private static final long serialVersionUID = -3396028711459717710L;
    private Imovel imovel;
    private Anuncio anuncio;
    private Informacao info;
    private JPanel panelCheckBox;
    private JComboBox boxUsuario;
    private Usuario usuario;
    private JScrollPane scrollPane;

    public PanelAnuncio(Imovel imovel, Anuncio anuncio) {
        initComponents();

        this.imovel = imovel;
        this.anuncio = anuncio;

        usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        configTela();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jCheckBox1 = new javax.swing.JCheckBox();
        btnGravar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        textoAnuncio = new javax.swing.JTextArea();
        comboDestaque = new javax.swing.JComboBox();
        mostrarValor = new javax.swing.JCheckBox();
        btnCopiar = new javax.swing.JButton();
        btnLiberar = new javax.swing.JButton();
        mostrarIptu = new javax.swing.JCheckBox();
        mostrarCondominio = new javax.swing.JCheckBox();
        panelVideo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtVideo = new javax.swing.JTextField();

        jCheckBox1.setText("jCheckBox1");

        setLayout(new java.awt.GridBagLayout());

        btnGravar.setText("Gravar");
        btnGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnGravar, gridBagConstraints);

        textoAnuncio.setColumns(20);
        textoAnuncio.setLineWrap(true);
        textoAnuncio.setRows(5);
        textoAnuncio.setWrapStyleWord(true);
        jScrollPane1.setViewportView(textoAnuncio);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(comboDestaque, gridBagConstraints);

        mostrarValor.setSelected(true);
        mostrarValor.setText("Mostrar Valor?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(mostrarValor, gridBagConstraints);

        btnCopiar.setText("Copiar");
        btnCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopiarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(btnCopiar, gridBagConstraints);

        btnLiberar.setText("Pedir Liberação");
        btnLiberar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiberarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(btnLiberar, gridBagConstraints);

        mostrarIptu.setText("Mostrar IPTU?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        add(mostrarIptu, gridBagConstraints);

        mostrarCondominio.setText("Mostrar Condomínio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        add(mostrarCondominio, gridBagConstraints);

        panelVideo.setBorder(javax.swing.BorderFactory.createTitledBorder("Vídeo"));
        panelVideo.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("URL:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelVideo.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelVideo.add(txtVideo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(panelVideo, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravarActionPerformed
        if (info == null) {
            info = new Informacao(anuncio.getNome(), imovel.getReferencia());
            info.setLiberado(Boolean.FALSE);
            info.setNovo(Boolean.FALSE);
        }

        if (usuario.getNivel() == Usuario.CORRETOR && !usuario.isSupervisor()) {
            info.setNovo(Boolean.TRUE);
        }

        if (isUsuarioPermissaoAdministrativo() && !anuncio.isInformacaoAdicional()) {
            info.setUsuario((Usuario) boxUsuario.getSelectedItem());
        }

        if (anuncio.isPermiteVideo()) {
            info.setVideo(txtVideo.getText());
        }

        info.setMostrarValor(mostrarValor.isSelected());
        info.setCondominio(mostrarCondominio.isSelected());
        info.setIptu(mostrarIptu.isSelected());
        info.setTextoAnuncio(textoAnuncio.getText());
        info.setDestaque(comboDestaque.getSelectedIndex() + 1);

        if (anuncio.getNome().equals("Zap") || anuncio.getNome().equals("Viva Real") || anuncio.getNome().equals("Imóvel Web")) {
            info.setItens(extraiItens());
        }

        if (InformacaoDAO.save(info)) {
            JOptionPane.showMessageDialog(null, "Anúncio gravado com sucesso!", "Anúncio", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao gravar anuncio!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGravarActionPerformed

    private void btnCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopiarActionPerformed
        Informacao informacao = copiarInformacao();

        if (informacao != null) {
            info = informacao;
        }
    }//GEN-LAST:event_btnCopiarActionPerformed

    private void btnLiberarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiberarActionPerformed
        if (ValidadorUtil.isNotNull(info)) {
            info.setLiberado(null);
            if (InformacaoDAO.save(info)) {
                JOptionPane.showMessageDialog(null, "Solicitação aberta com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao pedir solicitação!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Grave primeiro o anúncio!");
        }
    }//GEN-LAST:event_btnLiberarActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCopiar;
    private javax.swing.JButton btnGravar;
    private javax.swing.JButton btnLiberar;
    private javax.swing.JComboBox comboDestaque;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox mostrarCondominio;
    private javax.swing.JCheckBox mostrarIptu;
    private javax.swing.JCheckBox mostrarValor;
    private javax.swing.JPanel panelVideo;
    private javax.swing.JTextArea textoAnuncio;
    private javax.swing.JTextField txtVideo;
    // End of variables declaration//GEN-END:variables

    private void configTela() {

        btnGravar.setEnabled(!anuncio.getTravado() || usuario.isSupervisor());

        this.info = InformacaoDAO.get(imovel, anuncio);

        if (panelCheckBox != null) {
            this.remove(scrollPane);
        }

        btnLiberar.setVisible(anuncio.isLiberavel());

        if (anuncio.getModeloDestaque() != null && !anuncio.getModeloDestaque().isEmpty()) {
            comboDestaque.setModel(new DefaultComboBoxModel(anuncio.getModeloDestaque().split(";")));
            comboDestaque.setEnabled(usuario.isSupervisor());
        } else {
            comboDestaque.setVisible(false);
        }

        if (this.anuncio.isInformacaoAdicional()) {

            panelCheckBox = new JPanel(new GridLayout(0, 4, 2, 2));

            Zap[] tipos = ZapDAO.listZapByQuery(null, null);

            Font font = new Font("Arial", Font.PLAIN, 10);

            for (Zap zap : tipos) {
                JCheckBox box = new JCheckBox(zap.getTipo());
                box.setFont(font);

                if (info != null && info.getItens() != null) {
                    box.setSelected(info.getItens().indexOf(zap.getTipo()) > -1);
                }

                panelCheckBox.add(box);
            }

            GridBagConstraints cons = new GridBagConstraints();
            cons.insets = new Insets(10, 10, 10, 10);
            cons.gridy = 1;
            cons.gridx = 0;
            cons.weightx = 1;
            cons.weighty = 1;
            cons.gridwidth = 6;
            cons.fill = GridBagConstraints.BOTH;

            this.scrollPane = new JScrollPane(panelCheckBox);

            add(scrollPane, cons);

        } else {

            if (isUsuarioPermissaoAdministrativo()) {

                Usuario[] usuarios = UsuarioDAO.listUsuarioByQuery("nivel = " + Usuario.CORRETOR + " and status = 1", "nome");
                boxUsuario = new JComboBox(usuarios);
                boxUsuario.setSelectedItem(null);

                GridBagConstraints cons = new GridBagConstraints();
                cons.insets = new Insets(5, 5, 5, 5);
                cons.gridy = 3;
                cons.gridx = 1;
                add(boxUsuario, cons);
            }

            btnCopiar.setVisible(false);
        }

        if (!anuncio.isPermiteVideo()) {
            panelVideo.setVisible(false);
        }

        if (anuncio.getModeloDestaque() == null || anuncio.getModeloDestaque().isEmpty()) {
            comboDestaque.setVisible(false);
        }

        if (info != null) {
            textoAnuncio.setText(info.getTextoAnuncio());

            if (boxUsuario != null) {
                boxUsuario.setSelectedItem(info.getUsuario());
            }

            if (info.getDestaque() != null && comboDestaque != null && anuncio.getModeloDestaque() != null && !anuncio.getModeloDestaque().isEmpty()) {
                comboDestaque.setSelectedIndex(info.getDestaque() - 1);
            }

            mostrarValor.setSelected(info.getMostrarValor());
            mostrarCondominio.setSelected(info.isCondominio());
            mostrarIptu.setSelected(info.isIptu());
        }

        this.invalidate();
        this.repaint();
    }

    private boolean isUsuarioPermissaoAdministrativo() {
        return usuario.getNivel() == Usuario.RECEPCAO || usuario.isSupervisor();
    }

    private String extraiItens() {
        String str = "";

        for (Component comp : panelCheckBox.getComponents()) {
            if (comp instanceof JCheckBox) {
                if (((JCheckBox) comp).isSelected()) {
                    str += ((JCheckBox) comp).getText() + ";";
                }
            }
        }

        return str;
    }

    private Informacao copiarInformacao() {

        Informacao informacao = getInformacaoExistente();

        Informacao copy = null;

        if (informacao == null) {
            JOptionPane.showMessageDialog(null, "Não existe informções anteriores!");
        } else {

            copy = new Informacao(anuncio.getNome(), imovel.getReferencia());

            copy.setDestaque(Informacao.NORMAL);
            copy.setItens(informacao.getItens());
            copy.setLiberado(Boolean.FALSE);
            copy.setMostrarValor(Boolean.TRUE);
            copy.setNovo(Boolean.TRUE);
            copy.setTextoAnuncio(informacao.getTextoAnuncio());
            copy.setUsuario(usuario);

            InformacaoDAO.save(copy);
        }

        return copy;
    }

    private Informacao getInformacaoExistente() {
        for (Informacao info : imovel.getInformacoes()) {
            if (info.getAnuncio().isInformacaoAdicional()) {
                return info;
            }
        }

        return null;
    }
}
