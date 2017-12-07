package org.west.formulario.documentacao;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.west.componentes.JRHibernateSource;
import org.west.entidades.ServicoCriteria;

public class FrmRelatorioFechados extends javax.swing.JFrame {

    private static final long serialVersionUID = 7109240059128675687L;

    public FrmRelatorioFechados() {
        super();

        initComponents();

        setSize(600, 150);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void gerarRelatorio(List lista, HashMap mapa, URL url) {
        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(url);
            JRHibernateSource source = new JRHibernateSource(lista);

            HashMap parametros = new HashMap();
            parametros.put("DATA_RELATORIO", new Date());
            ImageIcon icon = new ImageIcon(getClass().getResource("/org/west/imagens/cabecalho.png"));
            parametros.put("LOGO", icon.getImage());

            parametros.putAll(mapa);

            JasperPrint print = JasperFillManager.fillReport(report, parametros, source);
            JasperViewer.viewReport(print, false);

        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        dataInicial = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        dataFinal = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Relatório de Serviços Fechados");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Data Inicial:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(dataInicial, gridBagConstraints);

        jLabel2.setText("Data Final:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(dataFinal, gridBagConstraints);

        jButton1.setText("Exibir Relatório");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButton1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            ServicoCriteria criteria = new ServicoCriteria();

            criteria.add(Restrictions.between("dataTermino", dataInicial.getDate(), dataFinal.getDate()));
            criteria.add(Restrictions.eq("concluido", Integer.valueOf(1)));
            criteria.addOrder(Order.asc("dataTermino"));

            gerarRelatorio(criteria.list(), new HashMap(), getClass().getResource("/org/west/relatorios/relatorio_servicos_finalizados.jasper"));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar relatório:" + ex.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dataFinal;
    private com.toedter.calendar.JDateChooser dataInicial;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
