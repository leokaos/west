package org.west.formulario.caixa;

import java.awt.BorderLayout;
import org.west.componentes.DesktopSession;
import org.west.componentes.JRHibernateSource;
import org.west.utilitarios.Util;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.swingBean.actions.ActionChainFactory;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.actions.ValidationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;
import org.west.entidades.*;

public class FrmLancamentos extends JFrame {

    private JBeanPanel<Lancamento> formLancamentos;
    private BeanTableModel model;
    private JBeanTable tabelaLancamentos;
    private Usuario usuario;
    private Map<String, Integer> mapaTipos;

    public FrmLancamentos() {
        initComponents();

        mapaTipos = new HashMap<String, Integer>();
        mapaTipos.put("Jurídico", 4);
        mapaTipos.put("Contratos", 5);
        mapaTipos.put("Financeiro", 1);
        mapaTipos.put("Financiamento", 3);
        mapaTipos.put("Consignado", 7);
        mapaTipos.put("Seguros", 8);

        this.usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        if (usuario.getGrupo() != null && usuario.getGrupo().equals("Financeiro")) {
            btnFinanceiro.setEnabled(true);
            btnMovimento.setEnabled(true);
            btnFolha.setEnabled(true);
        }

        setLocationRelativeTo(null);

        TableFieldDescriptor tableDscriptor = XMLDescriptorFactory.getTableFieldDescriptor(Lancamento.class, "org/west/xml/lancamentosTabela.xml", "lancamentosTabela");
        this.model = new BeanTableModel(tableDscriptor);
        this.tabelaLancamentos = new JBeanTable(model);
        this.tabelaLancamentos.enableHeaderOrdering();

        this.panelTabela.add(new JScrollPane(this.tabelaLancamentos), BorderLayout.CENTER);

        GenericFieldDescriptor formDescriptor = XMLDescriptorFactory.getFieldDescriptor(Lancamento.class, "org/west/xml/lancamentosForm.xml", "lancamentosForm");
        this.formLancamentos = new JBeanPanel<Lancamento>(Lancamento.class, "Lançamentos", formDescriptor);

        this.panelForm.add(this.formLancamentos, BorderLayout.CENTER);

        model.setBeanList(carregaLista(new Date()));

        comboServico.setModel(new DefaultComboBoxModel(ServicoDAO.listServicoByQuery("codigo>0", "codigo")));
        comboServico.setSelectedIndex(-1);

        comboGrupo.setModel(new DefaultComboBoxModel(getGrupos().toArray()));
        comboGrupo.setSelectedIndex(-1);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        panelForm = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        panelTabela = new javax.swing.JPanel();
        panelRelatorios = new javax.swing.JPanel();
        btnDiario = new javax.swing.JButton();
        dataDiario = new com.toedter.calendar.JDateChooser();
        dataInicial = new com.toedter.calendar.JDateChooser();
        dataFinal = new com.toedter.calendar.JDateChooser();
        comboServico = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboGrupo = new javax.swing.JComboBox();
        btnFinanceiro = new javax.swing.JButton();
        btnMovimento = new javax.swing.JButton();
        btnFolha = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Controle de Caixa");
        setMinimumSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 0));

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 13));
        jLabel1.setText("Data:");
        jPanel1.add(jLabel1);

        jDateChooser1.setDate(new Date());
        jDateChooser1.setPreferredSize(new java.awt.Dimension(100, 20));
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });
        jPanel1.add(jDateChooser1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanel1, gridBagConstraints);

        panelForm.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(panelForm, gridBagConstraints);

        jButton5.setText("Limpar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton5);

        jButton2.setText("Gravar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanel3, gridBagConstraints);

        panelTabela.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 6);
        getContentPane().add(panelTabela, gridBagConstraints);

        panelRelatorios.setBorder(javax.swing.BorderFactory.createTitledBorder("Relatórios"));
        panelRelatorios.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        panelRelatorios.setPreferredSize(new java.awt.Dimension(300, 204));
        panelRelatorios.setLayout(new java.awt.GridBagLayout());

        btnDiario.setText("Gerar");
        btnDiario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(btnDiario, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(dataDiario, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(dataInicial, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(dataFinal, gridBagConstraints);

        comboServico.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(comboServico, gridBagConstraints);

        jLabel2.setText("Diário");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(jLabel2, gridBagConstraints);

        jLabel4.setText("Por Serviço");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Inicial");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Final");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(jLabel6, gridBagConstraints);

        jLabel3.setText("Por Grupo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(jLabel3, gridBagConstraints);

        comboGrupo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(comboGrupo, gridBagConstraints);

        btnFinanceiro.setText("Financeiro");
        btnFinanceiro.setEnabled(false);
        btnFinanceiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinanceiroActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(btnFinanceiro, gridBagConstraints);

        btnMovimento.setText("Caixa");
        btnMovimento.setEnabled(false);
        btnMovimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovimentoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(btnMovimento, gridBagConstraints);

        btnFolha.setText("Pagamento");
        btnFolha.setEnabled(false);
        btnFolha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolhaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelRelatorios.add(btnFolha, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(panelRelatorios, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        if (evt.getPropertyName().equals("date")) {
            model.setBeanList(carregaLista(jDateChooser1.getDate()));
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (isValida(jDateChooser1.getDate())) {

            ApplicationAction gravarLancamento = new ApplicationAction() {

                @Override
                public void execute() {
                    Lancamento novo = new Lancamento();
                    formLancamentos.populateBean(novo);
                    novo.setDataLancamento(jDateChooser1.getDate());
                    novo.setUsuario(usuario);
                    novo.setArea(novo.getServico().getTipoServico().getDescricao());

                    if (LancamentoDAO.save(novo)) {
                        JOptionPane.showMessageDialog(null, "Lançamento inserido com sucesso!");
                        model.addBean(novo);
                        formLancamentos.cleanForm();
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao inserir lançamento!");
                    }
                }
            };

            ValidationAction lancamentoAction = new ValidationAction(formLancamentos);
            ApplicationAction tarefaValida = ActionChainFactory.createChainActions(lancamentoAction, gravarLancamento);

            tarefaValida.executeActionChain();
        } else {
            JOptionPane.showMessageDialog(null, "Data inválida!");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        formLancamentos.cleanForm();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnDiarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiarioActionPerformed
        try {
            Usuario usu = (Usuario) DesktopSession.getInstance().getObject("usuario");
            LancamentoCriteria criteria = getCriteria(usu.getGrupo());
            criteria.addOrder(Order.asc("dataLancamento")).addOrder(Order.asc("codigo"));
            gerarRelatorio(criteria.list(), new HashMap(), getClass().getResource("/org/west/relatorios/relatorio_diario.jasper"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnDiarioActionPerformed

    private void btnFinanceiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinanceiroActionPerformed
        try {
            LancamentoCriteria criteria = getCriteria(null);
            criteria.addOrder(Order.asc("area")).addOrder(Order.asc("servico")).addOrder(Order.asc("dataLancamento")).addOrder(Order.asc("codigo"));
            gerarRelatorio(criteria.list(), new HashMap(), getClass().getResource("/org/west/relatorios/relatorio_financeiro.jasper"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnFinanceiroActionPerformed

    private void btnMovimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovimentoActionPerformed
        try {
            LancamentoCriteria criteria = getCriteria("Financeiro");
            criteria.addOrder(Order.asc("dataLancamento")).addOrder(Order.asc("codigo"));

            List<Long> servicos = new ArrayList<Long>();
            servicos.add(1l);
            servicos.add(1009l);

            criteria.createServicoCriteria().add(Restrictions.in("codigo", servicos));

            HashMap parametros = new HashMap();

            List lista = Arrays.asList(SaldoDAO.listSaldoByQuery("area is not null", "area"));
            JRBeanCollectionDataSource sourceA = new JRBeanCollectionDataSource(lista);

            Double saldoFinanceiro = getSaldoAnterior();

            parametros.put("MAPA", sourceA);
            parametros.put("SUB_TOTAL", saldoFinanceiro);

            gerarRelatorio(criteria.list(), parametros, getClass().getResource("/org/west/relatorios/relatorio_movimento_caixa.jasper"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnMovimentoActionPerformed

    private void btnFolhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFolhaActionPerformed
        try {
            Object valor = JOptionPane.showInputDialog(null,
                    "Escolha o usuário:", "Usuário",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    getUsuarios().toArray(), null);

            if (valor != null) {

                String nome = valor.toString().substring(valor.toString().indexOf("-") + 1).trim();
                Usuario usuarioRelatorio = UsuarioDAO.loadUsuarioByQuery("nome='" + nome + "'", "codigo");

                ServicoCriteria criteria = new ServicoCriteria();
                criteria.add(Restrictions.not(Restrictions.eq("codigo", new Long(1))));
                criteria.add(Restrictions.eq("responsavel", usuarioRelatorio));

                if (dataFinal.getDate() != null && dataInicial.getDate() != null) {
                    if (dataFinal.getDate().after(dataInicial.getDate())) {
                        criteria.add(Restrictions.between("dataTermino", dataInicial.getDate(), dataFinal.getDate()));
                    }
                }

                String grupo = usuarioRelatorio.getGrupo();

                if (!grupo.equals("Seguros")) {
                    criteria.add(Restrictions.eq("concluido", 1));
                }

                criteria.add(Restrictions.eq("tipo", mapaTipos.get(grupo)));

                LancamentoCriteria criteriaL = new LancamentoCriteria();
                criteriaL.add(Restrictions.eq("usuario", usuarioRelatorio));
                criteriaL.add(Restrictions.eq("descricao", "Vale"));
                criteriaL.add(Restrictions.eq("servico", ServicoDAO.load(1L)));

                if (dataFinal.getDate() != null && dataInicial.getDate() != null) {
                    if (dataFinal.getDate().after(dataInicial.getDate())) {
                        criteriaL.add(Restrictions.between("dataLancamento", dataInicial.getDate(), dataFinal.getDate()));
                    }
                }

                JRHibernateSource jr = new JRHibernateSource(criteriaL.list());

                HashMap mapa = new HashMap();
                mapa.put("JRVALE", jr);
                mapa.put("NOME_COMPLETO", usuarioRelatorio.getNomeCompleto());
                mapa.put("TIPO", usuarioRelatorio.getGrupo());

                gerarRelatorio(criteria.list(), mapa, getClass().getResource("/org/west/relatorios/folha_de_pagamento.jasper"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnFolhaActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDiario;
    private javax.swing.JButton btnFinanceiro;
    private javax.swing.JButton btnFolha;
    private javax.swing.JButton btnMovimento;
    private javax.swing.JComboBox comboGrupo;
    private javax.swing.JComboBox comboServico;
    private com.toedter.calendar.JDateChooser dataDiario;
    private com.toedter.calendar.JDateChooser dataFinal;
    private com.toedter.calendar.JDateChooser dataInicial;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel panelForm;
    private javax.swing.JPanel panelRelatorios;
    private javax.swing.JPanel panelTabela;
    // End of variables declaration//GEN-END:variables

    protected boolean isValida(Date data) {
        int hora = 0;
        int dia = 0;

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(data);

        if (Util.isMesmoDia(data, new Date())) {
            return true;
        }

        dia = calendar.get(Calendar.DAY_OF_YEAR) - Util.getCampo(new Date(), Calendar.DAY_OF_YEAR);
        calendar.setTime(PontoDAO.getDateServer());
        hora = calendar.get(Calendar.HOUR_OF_DAY);

        if (dia < -1) {
            return false;
        }

        if (dia == -1) {
            if (hora < 10) {
                return true;
            }
        }

        return false;

    }

    private List carregaLista(Date date) {
        WestPersistentManager.clear();
        List lista = new ArrayList();
        try {
            LancamentoCriteria criteria = new LancamentoCriteria();
            criteria.add(Restrictions.eq("dataLancamento", date));
            criteria.createUsuarioCriteria().add(Restrictions.eq("grupo", usuario.getGrupo()));
            criteria.addOrder(Order.asc("codigo"));
            lista = criteria.list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
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

    //GERAÇÃO DE RELATÓRIOS
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

    private LancamentoCriteria getCriteria(String grupo) {
        LancamentoCriteria lancamentoCriteria = null;

        try {
            lancamentoCriteria = new LancamentoCriteria();

            if (grupo != null && comboServico.getSelectedItem() == null) {
                lancamentoCriteria.createUsuarioCriteria().add(Restrictions.eq("grupo", grupo));
            }

            if (dataFinal.getDate() != null && dataInicial.getDate() != null) {
                if (dataFinal.getDate().after(dataInicial.getDate())) {
                    lancamentoCriteria.add(Restrictions.between("dataLancamento", dataInicial.getDate(), dataFinal.getDate()));
                }
            }

            if (dataDiario.getDate() != null) {
                lancamentoCriteria.add(Restrictions.eq("dataLancamento", dataDiario.getDate()));
            }

            if (comboServico.getSelectedItem() != null) {
                lancamentoCriteria.add(Restrictions.eq("servico", (Servico) comboServico.getSelectedItem()));
            }

            if (comboGrupo.getSelectedItem() != null) {
                lancamentoCriteria.add(Restrictions.eq("area", comboGrupo.getSelectedItem()));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lancamentoCriteria;
    }

    private List getUsuarios() {
        List lista = new ArrayList();

        for (Usuario usu : UsuarioDAO.listUsuarioByQuery("grupo is not null and grupo != ''", "grupo,nomeCompleto")) {
            lista.add(usu.getGrupo() + " - " + usu.getNome());
        }

        return lista;
    }

    private Double getSaldoAnterior() {
        Double saldo = new Double(0);
        try {
            LancamentoCriteria saldoCriteria = new LancamentoCriteria();
            saldoCriteria.add(Restrictions.eq("servico", ServicoDAO.load(1L)));
            saldoCriteria.createUsuarioCriteria().add(Restrictions.eq("grupo", "Financeiro"));

            GregorianCalendar calendar = new GregorianCalendar();

            if (dataDiario.getDate() != null) {
                calendar.setTime(dataDiario.getDate());
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

                Date aux = dataDiario.getDate();

                if (!Util.isMesmoDia(aux, calendar.getTime())) {
                    aux = Util.addDias(aux, -1);
                }

                saldoCriteria.add(Restrictions.between("dataLancamento",
                        calendar.getTime(), aux));
            }

            if (dataFinal.getDate() != null && dataInicial.getDate() != null) {
                calendar.setTime(dataInicial.getDate());
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                saldoCriteria.add(Restrictions.between("dataLancamento",
                        calendar.getTime(),
                        Util.addDias(dataInicial.getDate(), -1)));
            }

            ProjectionList proje = Projections.projectionList();
            proje.add(Projections.sum("entrada"));
            proje.add(Projections.sum("saida"));

            saldoCriteria.setProjection(proje);

            Object[] somas = (Object[]) saldoCriteria.uniqueResult();

            somas[0] = (somas[0] == null ? new Double(0) : new Double(somas[0].toString()));
            somas[1] = (somas[1] == null ? new Double(0) : new Double(somas[1].toString()));

            saldo = (Double) somas[0] - (Double) somas[1];

            if (Util.isMesmoDia(dataDiario.getDate(), calendar.getTime())) {
                saldo = 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return saldo;
    }
}