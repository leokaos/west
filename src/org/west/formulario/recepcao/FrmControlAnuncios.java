package org.west.formulario.recepcao;

import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.west.componentes.JRHibernateSource;
import org.west.entidades.Anuncio;
import org.west.entidades.AnuncioDAO;
import org.west.entidades.Informacao;
import org.west.entidades.InformacaoCriteria;
import org.west.entidades.InformacaoDAO;
import org.west.entidades.WestPersistentManager;
import org.west.model.table.ModelSumario;
import org.west.utilitarios.Util;

public class FrmControlAnuncios extends javax.swing.JFrame {

    private List<JButton> listaBtn;
    private List<Informacao> informacoes;
    private Anuncio anuncioAtual;

    public FrmControlAnuncios() {
        initComponents();

        this.listaBtn = new ArrayList<JButton>();

        listaBtn.add(btnFase1);
        listaBtn.add(btnFase2);
        listaBtn.add(btnFase3);

        setSize(650, 350);
        setLocationRelativeTo(null);
    }

    private ComboBoxModel createModelAnuncios() {
        return new DefaultComboBoxModel(AnuncioDAO.listAnuncioByQuery("veiculo='Anuncio'", "nome"));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        comboAnuncios = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaSumario = new javax.swing.JTable();
        panelFases = new javax.swing.JPanel();
        btnFase1 = new javax.swing.JButton();
        btnFase2 = new javax.swing.JButton();
        btnFase3 = new javax.swing.JButton();
        btnRelatorio = new javax.swing.JButton();
        btnReload = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Controle de Anúncios");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Anúncios:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        comboAnuncios.setModel(createModelAnuncios());
        comboAnuncios.setSelectedIndex(-1);
        comboAnuncios.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboAnunciosItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(comboAnuncios, gridBagConstraints);

        tabelaSumario.setModel(new DefaultTableModel());
        jScrollPane1.setViewportView(tabelaSumario);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        panelFases.setBorder(javax.swing.BorderFactory.createTitledBorder("Fases"));
        panelFases.setLayout(new java.awt.GridBagLayout());

        btnFase1.setText("1 - Destravar Acesso");
        btnFase1.setEnabled(false);
        btnFase1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFase1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelFases.add(btnFase1, gridBagConstraints);

        btnFase2.setText("2 - Travar Acesso");
        btnFase2.setEnabled(false);
        btnFase2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFase2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelFases.add(btnFase2, gridBagConstraints);

        btnFase3.setText("3 - Finalizar Anúncio");
        btnFase3.setEnabled(false);
        btnFase3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFase3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelFases.add(btnFase3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(panelFases, gridBagConstraints);

        btnRelatorio.setText("Gerar Relatório...");
        btnRelatorio.setEnabled(false);
        btnRelatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelatorioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(btnRelatorio, gridBagConstraints);

        btnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/atualizar.png"))); // NOI18N
        btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(btnReload, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblStatus, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboAnunciosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboAnunciosItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            anuncioAtual = (Anuncio) evt.getItem();
            carregarSumario();
            configButtons();
        }
    }//GEN-LAST:event_comboAnunciosItemStateChanged

    private void btnFase1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFase1ActionPerformed
        int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja destravar " + anuncioAtual.getNome() + "?",
                "Destravar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            anuncioAtual.setTravado(false);
            AnuncioDAO.save(anuncioAtual);

            configButtons();
        }
    }//GEN-LAST:event_btnFase1ActionPerformed

    private void btnFase2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFase2ActionPerformed
        ModelSumario modelSumario = (ModelSumario) tabelaSumario.getModel();

        if (!modelSumario.hasSumario()) {
            JOptionPane.showMessageDialog(null, "Não existe sumário, portanto, não pode ser travado!", "Finalizar", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirmaRelatorio = JOptionPane.showConfirmDialog(null, "O relatório já foi gerado?",
                    "Relatório", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

            if (confirmaRelatorio == JOptionPane.NO_OPTION) {
                exibirRelatorio();
            } else {
                int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja travar " + anuncioAtual.getNome() + "?",
                        "Travar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (resposta == JOptionPane.YES_OPTION) {
                    anuncioAtual.setTravado(true);
                    AnuncioDAO.save(anuncioAtual);

                    configButtons();
                }
            }
        }
    }//GEN-LAST:event_btnFase2ActionPerformed

    private void btnFase3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFase3ActionPerformed
        int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja finalizar " + anuncioAtual.getNome() + "?\nO relatório final será gerado ao final da operação.",
                "Finalizar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lblStatus.setVisible(Boolean.TRUE);
                    lblStatus.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/load_small.gif")));
                    lblStatus.setText("Exportando...");

                    consolidarListaInformacao();

                    for (Informacao info : informacoes) {
                        info.setLiberado(false);
                    }

                    exportarRelatorio();

                    for (Informacao informacao : informacoes) {
                        informacao.setLiberado(Boolean.FALSE);
                        InformacaoDAO.save(informacao);
                    }

                    carregarSumario();
                    configButtons();

                    lblStatus.setVisible(Boolean.FALSE);
                }
            }).start();
        }
    }//GEN-LAST:event_btnFase3ActionPerformed

    private void btnRelatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatorioActionPerformed
        exibirRelatorio();
    }//GEN-LAST:event_btnRelatorioActionPerformed

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadActionPerformed
        if (anuncioAtual != null) {
            carregarSumario();
        }
    }//GEN-LAST:event_btnReloadActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFase1;
    private javax.swing.JButton btnFase2;
    private javax.swing.JButton btnFase3;
    private javax.swing.JButton btnRelatorio;
    private javax.swing.JButton btnReload;
    private javax.swing.JComboBox comboAnuncios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel panelFases;
    private javax.swing.JTable tabelaSumario;
    // End of variables declaration//GEN-END:variables

    private void configButtons() {
        btnRelatorio.setEnabled(true);
        ModelSumario model = (ModelSumario) tabelaSumario.getModel();

        if (anuncioAtual.getTravado()) {
            if (model.hasSumario()) {
                disableButtons(2);
            } else {
                disableButtons(0);
            }
        } else {
            disableButtons(1);
        }
    }

    private void disableButtons(int index) {
        for (JButton jButton : listaBtn) {
            jButton.setEnabled(listaBtn.indexOf(jButton) == index);
        }
    }

    private void consolidarListaInformacao() {

        InformacaoCriteria criteria = new InformacaoCriteria();
        criteria.add(Restrictions.eq("anuncio", anuncioAtual));
        criteria.createImovelCriteria().createAnuncioCriteria().add(Restrictions.eq("nome", anuncioAtual.getNome()));
        criteria.createAlias("usuario", "usuario", JoinType.LEFT_OUTER_JOIN);
        criteria.add(Restrictions.eq("liberado", true));
        criteria.addOrder(Order.asc("usuario.nome"));

        informacoes = criteria.list();
    }

    private void carregarSumario() {
        WestPersistentManager.clear();

        ModelSumario model = new ModelSumario(anuncioAtual);
        tabelaSumario.setModel(model);
    }

    private void exibirRelatorio() {
        try {
            JasperViewer.viewReport(gerarRelatorio(), false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar relatório!");
            ex.printStackTrace();
        }
    }

    private void exportarRelatorio() {
        try {
            String arquivoSalvar = getNomeArquivo();
            JasperExportManager.exportReportToPdfFile(gerarRelatorio(), arquivoSalvar);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao exportar relatório!");
        }
    }

    private JasperPrint gerarRelatorio() throws Exception {

        consolidarListaInformacao();

        JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/org/west/relatorios/relatorio_anuncio.jasper"));
        JRHibernateSource source = new JRHibernateSource(informacoes);

        HashMap parametros = new HashMap();
        parametros.put("DATA_RELATORIO", new Date());
        ImageIcon icon = new ImageIcon(getClass().getResource("/org/west/imagens/cabecalho.png"));
        parametros.put("LOGO", icon.getImage());

        parametros.put("MOSTRAR_USUARIO", !anuncioAtual.getTravado());

        JasperPrint print = JasperFillManager.fillReport(report, parametros, source);

        return print;
    }

    private String getNomeArquivo() {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        builder.append(Util.getPropriedade("west.anuncio_dir"));
        builder.append(anuncioAtual.getNome()).append("\\");
        builder.append(anuncioAtual.getNome()).append(" ").append(format.format(new Date()));
        builder.append(".pdf");

        return builder.toString();
    }
}
