package org.west.formulario.gerente;

import java.awt.BorderLayout;
import java.awt.Component;
import org.west.entidades.Cliente;
import org.west.entidades.ClienteDAO;
import org.west.entidades.Imobiliaria;
import org.west.formulario.usuario.FrmSenha;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.west.componentes.TipImovel;
import org.west.entidades.ImovelDAO;
import org.west.entidades.Usuario;
import org.west.formulario.imoveis.FrmCadastro;
import org.west.utilitarios.ExtendedStackedBarRenderer;
import org.west.utilitarios.ModelClientes;
import org.west.utilitarios.RenderImob;
import org.west.utilitarios.Util;

public class FrmGerentes extends javax.swing.JFrame {

    private GerenteControl control;
    private ChartPanel chartPanel;

    public FrmGerentes() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        control = new GerenteControl();

        chartPanel = new ChartPanel(null);
        graf.add(chartPanel, BorderLayout.CENTER);

        tabelaClientes.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if ((e.getClickCount() == 2) && (tabelaClientes.getSelectedRow() > -1)) {
                    Integer codigo = new Integer(tabelaClientes.getModel().getValueAt(tabelaClientes.getSelectedRow(), 0).toString());
                    Cliente cliente = ClienteDAO.load(codigo.longValue());
                    FrmCliente frame = new FrmCliente(null, true, cliente);
                    frame.setVisible(true);
                }
            }
        });

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                carregarSumario(control.getSumario());
                carregarClientes();
                carregarAnuncios();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        graficos = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        panelEsquerdo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dataInicial = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        dataFinal = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblNAtendidos = new javax.swing.JLabel();
        lblSemRetorno = new javax.swing.JLabel();
        lblComRetorno = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblSemContato = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblGrafico = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        tempoMedio = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        panelDireito = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        imovelGrafico = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        comboUsuario = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        comboVeiculo = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        agrupar = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        qtImovel = new javax.swing.JSpinner();
        jButton3 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        panelAnuncios = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        comboUsuarioAnuncio = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        chkMedia = new javax.swing.JCheckBox();
        chkAnterior = new javax.swing.JCheckBox();
        spnMeta = new javax.swing.JSpinner();
        jButton6 = new javax.swing.JButton();
        chkMeta = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        periodoInicio = new com.toedter.calendar.JDateChooser();
        periodoFim = new com.toedter.calendar.JDateChooser();
        graf = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaClientes = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        menuGerenciar = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();

        graficos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        graficos.setBorder(javax.swing.BorderFactory.createTitledBorder("Gráficos"));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema West - Gerencial");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setDividerSize(4);

        panelEsquerdo.setMaximumSize(new java.awt.Dimension(300, 0));
        panelEsquerdo.setPreferredSize(new java.awt.Dimension(300, 0));
        panelEsquerdo.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Sumário de Atendimentos"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Data Inicial:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        dataInicial.setDate(new Date());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(dataInicial, gridBagConstraints);

        jLabel2.setText("Data Final:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        dataFinal.setDate(new Date());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(dataFinal, gridBagConstraints);

        jButton1.setText("Atualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 15, 5);
        jPanel1.add(jButton1, gridBagConstraints);

        jLabel3.setText("Sem Ciência:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Sem Retorno:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Com Retorno:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        lblNAtendidos.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblNAtendidos, gridBagConstraints);

        lblSemRetorno.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblSemRetorno, gridBagConstraints);

        lblComRetorno.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblComRetorno, gridBagConstraints);

        jLabel14.setText("Sem Contato");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel14, gridBagConstraints);

        lblSemContato.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblSemContato, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        panelEsquerdo.add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Gráfico de Atendimentos"));
        jPanel2.setMaximumSize(new java.awt.Dimension(250, 250));
        jPanel2.setMinimumSize(new java.awt.Dimension(250, 250));
        jPanel2.setPreferredSize(new java.awt.Dimension(250, 250));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        lblGrafico.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblGrafico, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        panelEsquerdo.add(jPanel2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Tempo Médio de Atendimento"));

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel7.setText("Tempo:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(tempoMedio, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tempoMedio, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        panelEsquerdo.add(jPanel3, gridBagConstraints);

        jSplitPane1.setLeftComponent(panelEsquerdo);

        panelDireito.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setMaximumSize(new java.awt.Dimension(0, 300));
        jPanel4.setMinimumSize(new java.awt.Dimension(0, 300));
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 300));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(230, 254));

        jPanel8.setMaximumSize(new java.awt.Dimension(156, 209));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel9.setText("Imóvel:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel8.add(jLabel9, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel8.add(imovelGrafico, gridBagConstraints);

        jLabel10.setText("Corretor:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel8.add(jLabel10, gridBagConstraints);

        comboUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel8.add(comboUsuario, gridBagConstraints);

        jLabel11.setText("Veículo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel8.add(jLabel11, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel8.add(comboVeiculo, gridBagConstraints);

        jButton2.setText("Mostrar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 3, 3);
        jPanel8.add(jButton2, gridBagConstraints);

        agrupar.setText("Agrupar Por Usuário");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(agrupar, gridBagConstraints);

        jTabbedPane1.addTab("Veículos", jPanel8);

        jPanel9.setMaximumSize(new java.awt.Dimension(156, 209));
        jPanel9.setMinimumSize(new java.awt.Dimension(156, 209));
        jPanel9.setPreferredSize(new java.awt.Dimension(156, 209));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        jLabel12.setText("Tipo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel9.add(jLabel12, gridBagConstraints);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Somatório" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel9.add(jComboBox3, gridBagConstraints);

        jLabel13.setText("Quantidade:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel9.add(jLabel13, gridBagConstraints);

        qtImovel.setModel(new javax.swing.SpinnerNumberModel(10, 0, 100, 1));
        qtImovel.setValue(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel9.add(qtImovel, gridBagConstraints);

        jButton3.setText("Mostrar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel9.add(jButton3, gridBagConstraints);

        jTabbedPane1.addTab("Imóvel", jPanel9);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout panelAnunciosLayout = new javax.swing.GroupLayout(panelAnuncios);
        panelAnuncios.setLayout(panelAnunciosLayout);
        panelAnunciosLayout.setHorizontalGroup(
            panelAnunciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 235, Short.MAX_VALUE)
        );
        panelAnunciosLayout.setVerticalGroup(
            panelAnunciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 185, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(panelAnuncios, gridBagConstraints);

        jButton5.setText("Mostrar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(jButton5, gridBagConstraints);

        comboUsuarioAnuncio.setModel(new DefaultComboBoxModel());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(comboUsuarioAnuncio, gridBagConstraints);

        jLabel6.setText("Usuário:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(jLabel6, gridBagConstraints);

        jTabbedPane1.addTab("Anúncios", jPanel7);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jLabel15.setText("Meta Atual:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(jLabel15, gridBagConstraints);

        chkMedia.setText("Mostrar Média");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(chkMedia, gridBagConstraints);

        chkAnterior.setText("Mostrar Período Anterior");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(chkAnterior, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 25, 5, 25);
        jPanel10.add(spnMeta, gridBagConstraints);

        jButton6.setText("Mostrar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(jButton6, gridBagConstraints);

        chkMeta.setText("Mostrar Meta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(chkMeta, gridBagConstraints);

        jTabbedPane1.addTab("Metas", jPanel10);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jTabbedPane1, gridBagConstraints);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Período"));
        jPanel11.setMaximumSize(new java.awt.Dimension(250, 99));
        jPanel11.setMinimumSize(new java.awt.Dimension(250, 99));
        jPanel11.setPreferredSize(new java.awt.Dimension(250, 99));
        jPanel11.setLayout(new java.awt.GridBagLayout());

        jLabel16.setText("Início:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel11.add(jLabel16, gridBagConstraints);

        jLabel17.setText("Fim:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel11.add(jLabel17, gridBagConstraints);

        periodoInicio.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                periodoInicioPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel11.add(periodoInicio, gridBagConstraints);

        periodoFim.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                periodoFimPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel11.add(periodoFim, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jPanel11, gridBagConstraints);

        graf.setBorder(javax.swing.BorderFactory.createTitledBorder("Gráficos"));
        graf.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(graf, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panelDireito.add(jPanel4, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Fichas Sem Atendimento a Mais de 48 Horas"));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        tabelaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabelaClientes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        jPanel5.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 200;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panelDireito.add(jPanel5, gridBagConstraints);

        jSplitPane1.setRightComponent(panelDireito);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jSplitPane1, gridBagConstraints);

        jMenu1.setText("Arquivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        jMenuItem1.setText("Atualizar");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Administrar Usuários...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Trocar Senha...");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        menuGerenciar.setText("Gerenciar...");
        menuGerenciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGerenciarActionPerformed(evt);
            }
        });
        jMenu1.add(menuGerenciar);
        jMenu1.add(jSeparator1);

        jMenuItem4.setText("Sair");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        FrmAdmUsuario admUsuario = new FrmAdmUsuario(this, true);
        admUsuario.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        FrmSenha frmSenha = new FrmSenha(null);
        frmSenha.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (dataFinal.getDate().before(dataInicial.getDate())) {
            JOptionPane.showMessageDialog(null, "Data final menor que data inicial!", "Datas incorretas!", 0);
        } else {
            carregarSumario(control.getSumario(dataInicial.getDate(), dataFinal.getDate()));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void comboUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboUsuarioActionPerformed
        if (comboUsuario.getSelectedIndex() > -1) {
            comboVeiculo.setModel(carregarVeiculosPorCorretor(comboUsuario.getSelectedItem().toString()));
            comboVeiculo.setSelectedIndex(-1);
        }
    }//GEN-LAST:event_comboUsuarioActionPerformed

    private void periodoInicioPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_periodoInicioPropertyChange
        if (evt.getPropertyName().equals("date")) {
            carregaUsuarios();
        }
    }//GEN-LAST:event_periodoInicioPropertyChange

    private void periodoFimPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_periodoFimPropertyChange
        if (evt.getPropertyName().equals("date")) {
            carregaUsuarios();
        }
    }//GEN-LAST:event_periodoFimPropertyChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if ((periodoInicio.getDate() != null) && (periodoFim.getDate() != null) && (periodoFim.getDate().after(periodoInicio.getDate()))) {

            List lista = control.getContagemVeiculos(periodoInicio.getDate(),
                    periodoFim.getDate(),
                    imovelGrafico.getText(),
                    String.valueOf(comboUsuario.getSelectedItem()),
                    String.valueOf(comboVeiculo.getSelectedItem()),
                    agrupar.isSelected());
            if (lista.isEmpty()) {
                this.graficos.setIcon(null);
                this.graficos.setText("Não existem atendimentos nessas condições.");
            } else {
                DefaultCategoryDataset data = new DefaultCategoryDataset();

                for (Iterator it = lista.iterator(); it.hasNext();) {
                    Object[] linha = (Object[]) it.next();

                    if (agrupar.isSelected()) {
                        Usuario usuario = (Usuario) linha[2];

                        data.addValue((Long) linha[1], linha[0].toString(), usuario.getNome());
                    } else {
                        Double valor = new Double(linha[1].toString());

                        data.addValue(valor, linha[0].toString(), linha[0].toString());
                    }
                }

                desenhaGrafico(data, agrupar.isSelected());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Período inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (periodoInicio.getDate() != null && periodoFim.getDate() != null && periodoFim.getDate().after(periodoInicio.getDate())) {

            List lista = control.getContagemImovel(periodoInicio.getDate(),
                    periodoFim.getDate(),
                    (Integer) qtImovel.getValue());

            if (lista.isEmpty()) {
                this.graficos.setIcon(null);
                this.graficos.setText("Não existem atendimentos nessas condições.");
            } else {
                DefaultCategoryDataset data = new DefaultCategoryDataset();

                for (Iterator it = lista.iterator(); it.hasNext();) {
                    Object[] object = (Object[]) (Object[]) it.next();

                    Double valor = new Double(object[1].toString());

                    data.addValue(valor, object[0].toString(), "");
                }
                desenhaGrafico(data);

                chartPanel.addChartMouseListener(new ChartMouseListener() {

                    @Override
                    public void chartMouseClicked(ChartMouseEvent cme) {
                        ChartEntity entity = cme.getEntity();

                        if (entity instanceof CategoryItemEntity) {
                            CategoryItemEntity cie = (CategoryItemEntity) entity;
                            Long codigo = new Long(cie.getRowKey().toString());
                            TipImovel tip = new TipImovel(chartPanel, null, ImovelDAO.get(codigo));
                            tip.setVisible(true);
                        }
                    }

                    @Override
                    public void chartMouseMoved(ChartMouseEvent cme) {
                    }
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "Período inválido.", "Erro", JOptionPane.OK_OPTION);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (periodoInicio.getDate() != null && periodoFim.getDate() != null && periodoFim.getDate().after(periodoInicio.getDate())) {

            List lista = control.getContagemAtendimentosPorMes(periodoInicio.getDate(), periodoFim.getDate(), getSelecionados(), comboUsuarioAnuncio.getSelectedItem());

            if (lista.isEmpty()) {
                this.graficos.setIcon(null);
                this.graficos.setText("Não existem atendimentos nessas condições.");
            } else {
                String[] meses = new String[]{"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                for (Object object : lista) {
                    Object[] valores = (Object[]) object;

                    String texto = "Todos";
                    Integer mes = (Integer) valores[0] - 1;
                    Integer ano = (Integer) valores[1];
                    Long valor = (Long) valores[2];

                    if (valores.length == 4 && valores[3] != null) {
                        texto = valores[3].toString();
                    }

                    if (valor != 0) {
                        dataset.addValue(valor, texto, meses[mes] + "/" + ano.toString());
                    }
                }

                JFreeChart chart = ChartFactory.createLineChart("", "", "", dataset, PlotOrientation.VERTICAL, true, true, false);
                CategoryPlot plot = (CategoryPlot) chart.getPlot();
                plot.getRenderer().setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                plot.getRenderer().setItemLabelsVisible(true);
                plot.getRenderer().setSeriesItemLabelsVisible(0, Boolean.TRUE);
                ValueAxis rangeAxis = ((CategoryPlot) chart.getPlot()).getRangeAxis();
                rangeAxis.setLowerMargin(0.1);
                rangeAxis.setUpperMargin(0.1);

                chartPanel.setChart(chart);
                chartPanel.setDomainZoomable(true);
                chartPanel.setRangeZoomable(true);
                chartPanel.setMouseWheelEnabled(true);
                chartPanel.setMouseZoomable(true);
                graf.revalidate();
                graf.repaint();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Período inválido.", "Erro", JOptionPane.OK_OPTION);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
    }//GEN-LAST:event_jButton6ActionPerformed

    private void menuGerenciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuGerenciarActionPerformed
        FrmCadastro frmCadastro = new FrmCadastro();
        frmCadastro.setVisible(true);
    }//GEN-LAST:event_menuGerenciarActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox agrupar;
    private javax.swing.JCheckBox chkAnterior;
    private javax.swing.JCheckBox chkMedia;
    private javax.swing.JCheckBox chkMeta;
    private javax.swing.JComboBox comboUsuario;
    private javax.swing.JComboBox comboUsuarioAnuncio;
    private javax.swing.JComboBox comboVeiculo;
    private com.toedter.calendar.JDateChooser dataFinal;
    private com.toedter.calendar.JDateChooser dataInicial;
    private javax.swing.JPanel graf;
    private javax.swing.JLabel graficos;
    private javax.swing.JTextField imovelGrafico;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblComRetorno;
    private javax.swing.JLabel lblGrafico;
    private javax.swing.JLabel lblNAtendidos;
    private javax.swing.JLabel lblSemContato;
    private javax.swing.JLabel lblSemRetorno;
    private javax.swing.JMenuItem menuGerenciar;
    private javax.swing.JPanel panelAnuncios;
    private javax.swing.JPanel panelDireito;
    private javax.swing.JPanel panelEsquerdo;
    private com.toedter.calendar.JDateChooser periodoFim;
    private com.toedter.calendar.JDateChooser periodoInicio;
    private javax.swing.JSpinner qtImovel;
    private javax.swing.JSpinner spnMeta;
    private javax.swing.JTable tabelaClientes;
    private javax.swing.JLabel tempoMedio;
    // End of variables declaration//GEN-END:variables

    private void carregarSumario(List lista) {

        lblNAtendidos.setText("0");
        lblSemRetorno.setText("0");
        lblSemContato.setText("0");
        lblComRetorno.setText("0");

        for (Object obj : lista) {
            Object[] valores = (Object[]) obj;

            if (valores[0].equals(Imobiliaria.ATENDIDO)) {
                lblComRetorno.setText(valores[1].toString());
            } else if (valores[0].equals(Imobiliaria.SEM_RETORNO)) {
                lblSemRetorno.setText(valores[1].toString());
            } else if (valores[0].equals(Imobiliaria.SEM_CIENCIA)) {
                lblNAtendidos.setText(valores[1].toString());
            } else if (valores[0].equals(Imobiliaria.SEM_CONTATO)) {
                lblSemContato.setText(valores[1].toString());
            }
        }

        carregarGrafico(lista);
    }

    private void carregarGrafico(List lista) {
        String[] nomes = {"Sem Ciência", "Sem retorno", "Sem contato", "Com retorno"};
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        for (Object obj : lista) {
            Object[] valores = (Object[]) obj;
            dataSet.addValue((Long) valores[1], nomes[((Integer) valores[0]) - 1], "");
        }

        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataSet, PlotOrientation.HORIZONTAL, true, true, false);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        BarRenderer render = new BarRenderer();
        render.setBaseLegendTextFont(new Font("Verdana", 0, 12));

        plot.setRenderer(render);
        this.lblGrafico.setIcon(new ImageIcon(chart.createBufferedImage(lblGrafico.getWidth(), lblGrafico.getHeight())));

        carregarTempo();
    }

    private void carregarTempo() {
        Double tempo = control.getTempoMedio(Util.corrigirDate(dataInicial.getDate(), Util.INICIO), Util.corrigirDate(dataFinal.getDate(), Util.FIM));

        DecimalFormat formato = new DecimalFormat("#,###.00");

        if (tempo.doubleValue() == -1.0D) {
            this.tempoMedio.setText("Sem atendimentos completos.");
        } else {
            tempo = Double.valueOf(tempo.doubleValue() / 1000.0D);
        }

        if ((tempo.doubleValue() > 0.0D) && (tempo.doubleValue() <= 1.0D)) {
            this.tempoMedio.setText("Menos de 1 segundo.");
        }
        if ((tempo.doubleValue() > 1.0D) && (tempo.doubleValue() <= 60.0D)) {
            this.tempoMedio.setText(tempo + " segundos.");
        }
        if ((tempo.doubleValue() > 60.0D) && (tempo.doubleValue() < 3600.0D)) {
            this.tempoMedio.setText(formato.format(tempo.doubleValue() / 60.0D) + " minutos.");
        }
        if (tempo.doubleValue() >= 3600.0D) {
            this.tempoMedio.setText(formato.format(tempo.doubleValue() / 3600.0D) + " horas.");
        }
    }

    private void carregarClientes() {
        List<Imobiliaria> lista = new ArrayList<Imobiliaria>(control.getClientes());

        ModelClientes model = new ModelClientes(lista);
        tabelaClientes.setModel(model);

        RenderImob render = new RenderImob(lista);
        tabelaClientes.setDefaultRenderer(Object.class, render);
    }

    private void carregaUsuarios() {
        if ((this.periodoFim.getDate() != null) && (this.periodoInicio != null)
                && (this.periodoInicio.getDate().before(this.periodoFim.getDate()))) {
            List listagem = this.control.getListaCorretores(this.periodoInicio.getDate(), this.periodoFim.getDate());
            listagem.add(0, "Todos");
            DefaultComboBoxModel model = new DefaultComboBoxModel(listagem.toArray());
            comboUsuario.setModel(model);
            comboUsuario.setSelectedItem(null);

            comboUsuarioAnuncio.setModel(model);
            comboUsuarioAnuncio.setSelectedItem(null);

            comboVeiculo.setModel(new DefaultComboBoxModel());
        }
    }

    private ComboBoxModel carregarVeiculosPorCorretor(String toString) {
        List lista = new ArrayList();

        lista = control.getVeiculosPorCorretor(periodoInicio.getDate(), periodoFim.getDate(), toString);

        return new DefaultComboBoxModel(lista.toArray());
    }

    private void carregarAnuncios() {
        panelAnuncios.setLayout(new GridLayout(0, 2, 3, 3));

        for (String veiculo : control.getListaVeiculos()) {
            JCheckBox check = new JCheckBox(veiculo);
            check.setFont(check.getFont().deriveFont(Font.PLAIN, 10f));
            panelAnuncios.add(check);
        }

        for (String anuncio : control.getListaAnuncios()) {
            JCheckBox check = new JCheckBox(anuncio);
            check.setFont(check.getFont().deriveFont(Font.PLAIN, 10f));
            panelAnuncios.add(check);
        }
    }

    private String getSelecionados() {
        String str = "";

        for (Component comp : panelAnuncios.getComponents()) {
            if (comp instanceof JCheckBox) {
                if (((JCheckBox) comp).isSelected()) {
                    str += ((JCheckBox) comp).getText() + ";";
                }
            }
        }

        return str;
    }

    private void desenhaGrafico(DefaultCategoryDataset data) {
        desenhaGrafico(data, false);
    }

    private void desenhaGrafico(DefaultCategoryDataset data, boolean selected) {
        JFreeChart chart = null;

        ChartMouseListener[] listeners = (ChartMouseListener[]) chartPanel.getListeners(ChartMouseListener.class);

        for (ChartMouseListener listener : listeners) {
            chartPanel.removeChartMouseListener(listener);
        }

        if (selected) {
            chart = ChartFactory.createStackedBarChart("", "", "", data, PlotOrientation.VERTICAL, true, true, false);
            CategoryPlot plot = chart.getCategoryPlot();
            CategoryItemRenderer renderer = new ExtendedStackedBarRenderer();
            renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            renderer.setBaseItemLabelsVisible(true);
            plot.setRenderer(renderer);
        } else {
            chart = ChartFactory.createBarChart("", "", "", data, PlotOrientation.VERTICAL, true, true, false);

            CategoryPlot plot = (CategoryPlot) chart.getPlot();

            BarRenderer render = new BarRenderer();
            render.setBaseItemLabelsVisible(true);
            render.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            render.setBaseLegendTextFont(new Font("Verdana", 0, 12));

            plot.setRenderer(render);
        }

        chartPanel.setChart(chart);
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setMouseZoomable(true);

        ValueAxis rangeAxis = ((CategoryPlot) chart.getPlot()).getRangeAxis();
        rangeAxis.setLowerMargin(0.1);
        rangeAxis.setUpperMargin(0.1);

        graf.revalidate();
        graf.repaint();
    }
}