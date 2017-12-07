package org.west.formulario.documentacao;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JActButton;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;
import org.west.componentes.DesktopSession;
import org.west.componentes.PanelClientes;
import org.west.componentes.PanelImovel;
import org.west.componentes.PanelPortaria;
import org.west.entidades.Imovel;
import org.west.entidades.Lancamento;
import org.west.entidades.LancamentoCriteria;
import org.west.entidades.LancamentoDAO;
import org.west.entidades.Modifica;
import org.west.entidades.Portaria;
import org.west.entidades.Servico;
import org.west.entidades.ServicoCriteria;
import org.west.entidades.ServicoDAO;
import org.west.entidades.Tarefa;
import org.west.entidades.TarefaDAO;
import org.west.entidades.Usuario;
import org.west.entidades.WestPersistentManager;
import org.west.formulario.gerente.FrmAdmUsuario;
import org.west.utilitarios.ModelPeriodo;
import org.west.utilitarios.RenderLancamentos;
import org.west.utilitarios.RenderServicos;
import org.west.utilitarios.RenderTarefas;

public class FrmSupervisor extends javax.swing.JFrame {

    private GenericFieldDescriptor descriptorForm;
    private TableFieldDescriptor descriptorTable;
    private JBeanTable tabelaServico;
    private JBeanTable tabelaTarefa;
    private JBeanPanel<Tarefa> formTarefa;
    private JBeanPanel<Servico> formServico;
    private BeanTableModel<Servico> modeloServico;
    private BeanTableModel<Tarefa> modeloTarefa;
    private Servico servicoAtual;
    private BeanTableModel<Lancamento> modeloLancamento;
    private JBeanTable tabelaLancamento;
    private JBeanPanel<Lancamento> formLancamento;
    private PanelClientes panelClientes;
    private PanelImovel panelImovel;
    private PanelPortaria panelPortaria;
    private Usuario usuario;

    public FrmSupervisor() {
        initComponents();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);

        usuario = DesktopSession.getInstance().getObjetoSessao("usuario");

        txtBusca.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !txtBusca.getText().isEmpty()) {
                    List lista = getServicos(txtBusca.getText());
                    modeloServico.setBeanList(lista);

                    RenderServicos render = new RenderServicos(lista);

                    for (int x = 0; x < tabelaServico.getColumnModel().getColumnCount(); ++x) {
                        tabelaServico.getColumnModel().getColumn(x).setCellRenderer(render);
                    }
                }
            }
        });

        JActButton botaoSalvar = new JActButton("Salvar", new ApplicationAction() {

            @Override
            public void execute() {
                List lista = modeloServico.getUpdated();
                Boolean ok = false;
                for (Iterator it = lista.iterator(); it.hasNext();) {
                    Servico servico = (Servico) it.next();
                    servico.setDataTermino(new Date());
                    servico.setPastaMae(servico.getPastaMae().toUpperCase());
                    ok = ServicoDAO.save(servico);
                    if (!ok) {
                        break;
                    }
                }

                if (ok) {
                    JOptionPane.showMessageDialog(null, "Serviços finalizados com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao atualizar serviços!");
                }
            }
        });

        cons.gridx = 4;
        cons.gridy = 0;
        cons.weightx = 1;
        cons.anchor = GridBagConstraints.LINE_END;
        panelServicos.add(botaoSalvar, cons);

        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Servico.class, "org/west/xml/servicoTabela.xml", "tabelaServico");
        this.modeloServico = new BeanTableModel(this.descriptorTable);
        this.tabelaServico = new JBeanTable(this.modeloServico);
        this.tabelaServico.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.tabelaServico.enableHeaderOrdering();
        this.tabelaServico.setRowHeight(25);

        this.tabelaServico.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (tabelaServico.getSelectedRow() > -1) {
                    servicoAtual = (Servico) modeloServico.getBeanAt(tabelaServico.getSelectedRow());
                    carregaTarefas();
                }
            }
        });

        this.tabelaServico.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                FrmServico frm = new FrmServico(null, true, servicoAtual);
                frm.setVisible(true);
            }
        });

        cons.gridx = 0;
        cons.gridy = 1;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.gridwidth = 5;
        cons.fill = GridBagConstraints.BOTH;
        panelServicos.add(new JScrollPane(tabelaServico), cons);

        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Tarefa.class, "org/west/xml/tarefaTabela.xml", "tabelaTarefa");
        this.modeloTarefa = new BeanTableModel(this.descriptorTable);
        this.tabelaTarefa = new JBeanTable(this.modeloTarefa);
        this.tabelaTarefa.enableHeaderOrdering();

        this.tabelaTarefa.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                Tarefa tarefa = (Tarefa) modeloTarefa.getBeanAt(tabelaTarefa.getSelectedRow());
                formTarefa.setBean(tarefa);
                carregaGraficoModificacao(tarefa);
            }
        });

        cons.gridx = 0;
        cons.gridy = 0;
        panelLista.add(new JScrollPane(tabelaTarefa), cons);

        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Servico.class, "org/west/xml/servicoFormSupervisor.xml", "servicoFormGerente");
        this.formServico = new JBeanPanel<Servico>(Servico.class, "Informações de serviços", this.descriptorForm);

        cons.gridy = 0;
        cons.gridx = 0;
        cons.gridheight = 1;
        cons.gridwidth = 2;
        cons.weightx = 0;
        cons.weighty = 0;
        cons.insets = new Insets(3, 3, 3, 3);
        panelForm.add(formServico, cons);

        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Tarefa.class, "org/west/xml/tarefaSupervisorForm.xml", "formTarefa");
        this.formTarefa = new JBeanPanel<Tarefa>(Tarefa.class, "Informações de tarefas", this.descriptorForm);

        this.formTarefa.getComponent("previsaoTermino").addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("date")) {
                    JComboBox combo = (JComboBox) formTarefa.getComponent("aviso");
                    ModelPeriodo model = new ModelPeriodo((Date) formTarefa.getPropertyValue("previsaoTermino"));
                    combo.setModel(model);
                    combo.getModel().setSelectedItem(null);
                }
            }
        });

        cons.gridy = 1;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
        panelForm.add(formTarefa, cons);

        cons.gridx = 0;
        cons.gridy = 2;
        cons.weighty = 0;
        cons.weightx = 1;
        cons.gridwidth = 1;
        cons.ipady = 0;
        cons.anchor = GridBagConstraints.LINE_END;
        cons.fill = GridBagConstraints.NONE;

        panelForm.add(new JActButton("Gravar", new ApplicationAction() {

            @Override
            public void execute() {
                WestPersistentManager.clear();
                Tarefa tarefa = new Tarefa();
                formTarefa.populateBean(tarefa);
                tarefa.setDataAtualizacao(WestPersistentManager.getDateServer());

                tarefa.setServico(ServicoDAO.load(servicoAtual.getCodigo()));

                if (TarefaDAO.save(tarefa)) {
                    JOptionPane.showMessageDialog(null, "Tarefa atualizada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao atulizar a tarefa!");
                }
            }
        }), cons);

        cons.gridx = 1;
        cons.weightx = 0;
        cons.weighty = 0;

        panelForm.add(new JActButton("Limpar", new ApplicationAction() {

            @Override
            public void execute() {
                formTarefa.cleanForm();
            }
        }), cons);

        //carregando interface
        List<Servico> lista = getServicos("");
        modeloServico.setBeanList(lista);

        RenderServicos render = new RenderServicos(lista);

        for (int x = 0; x < this.tabelaServico.getColumnModel().getColumnCount(); ++x) {
            this.tabelaServico.getColumnModel().getColumn(x).setCellRenderer(render);
        }

        if (usuario.isSupervisor()) {

            //panelLancamentos
            descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Lancamento.class, "org/west/xml/lancamentosTabelaSuper.xml", "lancamentosTabelaSuper");
            modeloLancamento = new BeanTableModel<Lancamento>(descriptorTable);
            tabelaLancamento = new JBeanTable(modeloLancamento);
            tabelaLancamento.enableHeaderOrdering();
            panel2.add(new JScrollPane(tabelaLancamento), BorderLayout.CENTER);

            descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Lancamento.class, "org/west/xml/formLancamentoSuper.xml", "formLancamentoSuper");
            formLancamento = new JBeanPanel<Lancamento>(Lancamento.class, descriptorForm);
            panel1.add(formLancamento, BorderLayout.CENTER);

            tabelaLancamento.addDoubleClickAction(new ApplicationAction() {

                @Override
                public void execute() {
                    formLancamento.setBean(modeloLancamento.getBeanAt(tabelaLancamento.getSelectedRow()));
                }
            });

            RenderLancamentos reder2 = new RenderLancamentos();

            for (int x = 0; x < tabelaLancamento.getColumnCount(); x++) {
                tabelaLancamento.getColumnModel().getColumn(x).setCellRenderer(reder2);
            }

            tabelaLancamento.setRowHeight(25);

            carregaMultiplosPerfis();
        } else {
            jMenu1.remove(1);
            panelTab.remove(1);
        }
    }

    private void carregaMultiplosPerfis() {

        panelClientes = new PanelClientes();
        panelTab.addTab("Clientes", panelClientes);

        panelImovel = new PanelImovel();
        panelTab.addTab("Imovel", panelImovel);

        panelPortaria = new PanelPortaria(panelImovel);
        panelTab.addTab("Portarias", panelPortaria);

        panelClientes.addPropertyChangeListener("panelImovel", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                panelImovel.exibeDetalhes((Imovel) evt.getNewValue());
                panelTab.setSelectedIndex(3);
            }
        });

        panelClientes.addPropertyChangeListener("panelPortaria", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                panelImovel.exibeDetalhes((List) evt.getNewValue());
                panelTab.setSelectedIndex(3);
            }
        });

        panelImovel.getDetalhes().addPropertyChangeListener("portaria", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                panelTab.setSelectedIndex(4);
                panelPortaria.setPortaria((Portaria) evt.getNewValue());
            }
        });

        panelImovel.addPropertyChangeListener("listagem", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                panelTab.setSelectedIndex(3);
            }
        });

        panelImovel.getListagemImo().addPropertyChangeListener("listagem", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                panelTab.setSelectedIndex(3);
            }
        });
    }

    private void carregaGraficoModificacao(Tarefa tarefa) {

        WestPersistentManager.clear();
        TarefaDAO.lock(tarefa);

        TimeSeries serie = new TimeSeries("Modificações");
        if (tarefa.getModifica().size() > 0) {
            for (Iterator it = tarefa.getModifica().iterator(); it.hasNext();) {
                Modifica modi = (Modifica) it.next();
                serie.addOrUpdate(new Day(modi.getDataAnterior()), modi.getDataModificacao().getTime());
            }

            JFreeChart chart = ChartFactory.createXYLineChart("", "", "", new TimeSeriesCollection(serie), PlotOrientation.VERTICAL, false, false, false);

            XYPlot plot = (XYPlot) chart.getPlot();

            DateAxis dateRange = new DateAxis();
            dateRange.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
            dateRange.setLabel("Datas de modificação");

            DateAxis dateDomain = new DateAxis();
            dateDomain.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
            dateDomain.setLabel("Data Anteriores");

            plot.setRangeAxis(dateRange);
            plot.setDomainAxis(dateDomain);

            plot.setRenderer(new XYSplineRenderer(1));

            graficoModi.setText(null);
            graficoModi.setHorizontalAlignment(0);
            graficoModi.setVerticalAlignment(0);

            ImageIcon icon = new ImageIcon(chart.createBufferedImage(graficoModi.getWidth() - 25, graficoModi.getHeight() - 25));
            icon.getImage().getScaledInstance(graficoModi.getWidth() - 25, graficoModi.getHeight() - 25, 1);
            graficoModi.setIcon(icon);
            graficoModi.setText("");

            WestPersistentManager.clear();
        } else {
            graficoModi.setIcon(null);
            graficoModi.setText("Não existem modificações nessa tarefa!");
        }
    }

    private void carregaTarefas() {
        ServicoDAO.lock(servicoAtual);
        List<Tarefa> listaTarefas = new ArrayList<Tarefa>(servicoAtual.getTarefas());

        modeloTarefa.setBeanList(listaTarefas);
        formServico.setBean(servicoAtual);

        RenderTarefas render = new RenderTarefas(listaTarefas);

        for (int x = 0; x < this.tabelaTarefa.getColumnModel().getColumnCount(); ++x) {
            this.tabelaTarefa.getColumnModel().getColumn(x).setCellRenderer(render);
        }

        WestPersistentManager.clear();
    }

    private List<Servico> getServicos(String busca) {

        ServicoCriteria criteria = null;
        try {
            criteria = new ServicoCriteria();

            if (!busca.isEmpty()) {
                if (busca.matches("^[0-9]*$")) {
                    criteria.add(Restrictions.eq("codigo", new Long(busca)));
                } else {
                    Disjunction dis = Restrictions.disjunction();
                    dis.add(Restrictions.ilike("nome", busca, MatchMode.ANYWHERE));
                    dis.add(Restrictions.ilike("cliente", busca, MatchMode.ANYWHERE));
                    dis.add(Restrictions.ilike("endereco", busca, MatchMode.ANYWHERE));

                    criteria.createAlias("compradores", "comprador", JoinType.LEFT_OUTER_JOIN);
                    dis.add(Restrictions.ilike("comprador.nome", busca, MatchMode.ANYWHERE));

                    criteria.createAlias("vendedores", "vendedor", JoinType.LEFT_OUTER_JOIN);
                    dis.add(Restrictions.ilike("vendedor.nome", busca, MatchMode.ANYWHERE));

                    criteria.add(dis);
                }
            } else {
                criteria.add(Restrictions.eq("concluido", 0));
            }

            if (!usuario.isSupervisor()) {
                formTarefa.setEnable("valor", false);
            }

            criteria.addOrder(Order.asc("codigo"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ((criteria == null) ? null : criteria.list());
    }

    private boolean isServicoAtransando(Servico servico) {
        Boolean atrasado = Boolean.FALSE;
        ServicoDAO.lock(servico);

        for (Tarefa tarefa : servico.getTarefas()) {

            double relacao = 0.0;
            relacao = (new Date()).getTime() - tarefa.getDataInicio().getTime();
            relacao /= tarefa.getPrevisaoTermino().getTime() - tarefa.getDataInicio().getTime();

            if (relacao > 0.7) {
                atrasado = Boolean.TRUE;
                break;
            }
        }

        return atrasado;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelTab = new javax.swing.JTabbedPane();
        panelServicos = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtBusca = new javax.swing.JTextField();
        comboOrdenar = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        panelLista = new javax.swing.JPanel();
        graficoModi = new javax.swing.JLabel();
        panelForm = new javax.swing.JPanel();
        panelLancamentos = new javax.swing.JPanel();
        panelControles = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dataInicial = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        dataFinal = new com.toedter.calendar.JDateChooser();
        btnFiltrar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtServico = new javax.swing.JTextField();
        panel1 = new javax.swing.JPanel();
        panel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        panel2 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema West  - Gerente Administrativo");

        panelServicos.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Busca de Serviços:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelServicos.add(jLabel4, gridBagConstraints);

        txtBusca.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        txtBusca.setMinimumSize(new java.awt.Dimension(300, 26));
        txtBusca.setPreferredSize(new java.awt.Dimension(300, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelServicos.add(txtBusca, gridBagConstraints);

        comboOrdenar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tipo de Serviço", "Atraso" }));
        comboOrdenar.setSelectedIndex(-1);
        comboOrdenar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboOrdenarItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelServicos.add(comboOrdenar, gridBagConstraints);

        jLabel5.setText("Ordenar:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelServicos.add(jLabel5, gridBagConstraints);

        panelLista.setLayout(new java.awt.GridBagLayout());

        graficoModi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        graficoModi.setBorder(javax.swing.BorderFactory.createTitledBorder("Modificações da Tarefa"));
        graficoModi.setMaximumSize(new java.awt.Dimension(12, 50));
        graficoModi.setMinimumSize(new java.awt.Dimension(12, 50));
        graficoModi.setPreferredSize(new java.awt.Dimension(12, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelLista.add(graficoModi, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelServicos.add(panelLista, gridBagConstraints);

        panelForm.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelServicos.add(panelForm, gridBagConstraints);

        panelTab.addTab("Serviços", panelServicos);

        panelLancamentos.setLayout(new java.awt.GridBagLayout());

        panelControles.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Data Inicial:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        panelControles.add(jLabel1, gridBagConstraints);

        dataInicial.setDate(new Date());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        panelControles.add(dataInicial, gridBagConstraints);

        jLabel2.setText("Data Final:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        panelControles.add(jLabel2, gridBagConstraints);

        dataFinal.setDate(new Date());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        panelControles.add(dataFinal, gridBagConstraints);

        btnFiltrar.setText("Filtrar");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        panelControles.add(btnFiltrar, gridBagConstraints);

        jLabel3.setText("Serviço");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        panelControles.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        panelControles.add(txtServico, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panelLancamentos.add(panelControles, gridBagConstraints);

        panel1.setLayout(new java.awt.BorderLayout());

        jButton1.setText("Gravar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panel3.add(jButton1);

        jButton2.setText("Limpar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        panel3.add(jButton2);

        panel1.add(panel3, java.awt.BorderLayout.PAGE_END);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelLancamentos.add(panel1, gridBagConstraints);

        panel2.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelLancamentos.add(panel2, gridBagConstraints);

        panelTab.addTab("Lançamentos", panelLancamentos);

        getContentPane().add(panelTab, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Arquivo");

        jMenuItem1.setText("Atualizar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setText("Administrar Usuários...");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Relatório de Serviços Fechados...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);
        jMenu1.add(jSeparator1);

        jMenuItem2.setText("Sair");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        List lista = getServicos("");

        RenderServicos render = new RenderServicos(lista);

        for (int x = 0; x < this.tabelaServico.getColumnModel().getColumnCount(); x++) {
            this.tabelaServico.getColumnModel().getColumn(x).setCellRenderer(render);
        }
        this.modeloServico.setBeanList(lista);

        this.comboOrdenar.setSelectedIndex(-1);

        WestPersistentManager.clear();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        FrmAdmUsuario frm = new FrmAdmUsuario(this, true);
        frm.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        try {
            LancamentoCriteria criteria = new LancamentoCriteria();
            criteria.add(Restrictions.between("dataLancamento", dataInicial.getDate(), dataFinal.getDate()));

            if (!txtServico.getText().isEmpty()) {
                Long codigo = new Long(txtServico.getText());
                criteria.add(Restrictions.eq("servico", ServicoDAO.load(codigo.longValue())));
            }

            modeloLancamento.setBeanList(criteria.list());

            WestPersistentManager.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        formLancamento.cleanForm();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Lancamento lancamento = new Lancamento();
        formLancamento.populateBean(lancamento);

        if (LancamentoDAO.save(lancamento)) {
            JOptionPane.showMessageDialog(null, "Lançamento salvo com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao salvar lançamento!");
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void comboOrdenarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOrdenarItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (comboOrdenar.getSelectedIndex() != -1) {

                List<Servico> listaServico = new ArrayList<Servico>(modeloServico.getCompleteList());
                Comparator<Servico> comparator = null;

                if (comboOrdenar.getSelectedItem().equals("Tipo de Serviço")) {
                    comparator = new Comparator<Servico>() {

                        @Override
                        public int compare(Servico o1, Servico o2) {
                            return o1.getTipoServico().compareTo(o2.getTipoServico());
                        }
                    };
                }

                if (comboOrdenar.getSelectedItem().equals("Atraso")) {
                    comparator = new Comparator<Servico>() {

                        @Override
                        public int compare(Servico o1, Servico o2) {
                            return getValorAtraso(o1) - getValorAtraso(o2);
                        }

                        private int getValorAtraso(Servico servico) {
                            int value = 4; //Branco é padrão

                            if (servico.isInativo()) {
                                value = 1;
                            } else if (servico.getUltimaPrevisaoTermino() != null && servico.getUltimaPrevisaoTermino().before(new Date())) {
                                value = 2;
                            } else if (isServicoAtransando(servico)) {
                                value = 3;
                            } else if (servico.getConcluido() != 0) {
                                value = 5;
                            }

                            return value;
                        }
                    };
                }

                Collections.sort(listaServico, comparator);
                modeloServico.setBeanList(listaServico);
                comboOrdenar.setSelectedIndex(-1);
            }
        }
    }//GEN-LAST:event_comboOrdenarItemStateChanged

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
       FrmRelatorioFechados frm  = new FrmRelatorioFechados();
       frm.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JComboBox comboOrdenar;
    private com.toedter.calendar.JDateChooser dataFinal;
    private com.toedter.calendar.JDateChooser dataInicial;
    private javax.swing.JLabel graficoModi;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panel2;
    private javax.swing.JPanel panel3;
    private javax.swing.JPanel panelControles;
    private javax.swing.JPanel panelForm;
    private javax.swing.JPanel panelLancamentos;
    private javax.swing.JPanel panelLista;
    private javax.swing.JPanel panelServicos;
    private javax.swing.JTabbedPane panelTab;
    private javax.swing.JTextField txtBusca;
    private javax.swing.JTextField txtServico;
    // End of variables declaration//GEN-END:variables
}