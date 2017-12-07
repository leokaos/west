package org.west.formulario.documentacao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.Timer;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.swingBean.actions.ActionChainFactory;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.actions.ValidationAction;
import org.swingBean.descriptor.BeanTreeTableModel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JActButton;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTreeTable;
import org.west.componentes.DesktopSession;
import org.west.componentes.JMSManager;
import org.west.componentes.JRCapaServico;
import org.west.componentes.PanelAgenda;
import org.west.entidades.Cliente;
import org.west.entidades.Modifica;
import org.west.entidades.ModificaDAO;
import org.west.entidades.Ponto;
import org.west.entidades.PontoDAO;
import org.west.entidades.Servico;
import org.west.entidades.ServicoCriteria;
import org.west.entidades.ServicoDAO;
import org.west.entidades.Tarefa;
import org.west.entidades.TarefaCriteria;
import org.west.entidades.TarefaDAO;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioDAO;
import org.west.entidades.WestPersistentManager;
import org.west.formulario.FrmMain;
import org.west.formulario.caixa.FrmLancamentos;
import org.west.formulario.cliente.FrmCliente;
import org.west.formulario.recados.FrmRecadosUsuario;
import org.west.formulario.usuario.FrmSenha;
import org.west.grafico.PanelGrafico;
import org.west.utilitarios.ModelPeriodo;
import org.west.utilitarios.RenderTree;
import org.west.utilitarios.Util;

public class FrmDocumentos extends FrmMain {

    private JBeanTreeTable table;
    private JBeanPanel<Tarefa> formTarefa;
    private JBeanPanel<Servico> formServico;
    private Servico servicoAtual;
    private PanelGrafico grafico;
    private PanelAgenda agenda;
    private Timer timer;
    private JScrollBar bar;

    public FrmDocumentos() {
        super();
        initComponents();

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.weightx = 1;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;

        DefaultComboBoxModel model = new DefaultComboBoxModel(getPastas().toArray());
        comboPasta.setModel(model);

        txtBusca.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    if (!txtBusca.getText().isEmpty()) {
                        carregaTarefas(getListaTarefas(txtBusca.getText()));
                    } else if (txtBusca.getText().length() == 0) {
                        carregaTarefas(getListaTarefas(""));
                    }
                }
            }
        });

        comboPasta.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {

                    if (!comboPasta.getSelectedItem().toString().equals("Minhas Tarefas")) {
                        carregaTarefas(getTarefasPorPasta(comboPasta.getSelectedItem().toString()));
                    } else {
                        carregaTarefas(getListaTarefas(""));
                    }
                }
            }
        });

        carregaTarefas(getListaTarefas(""));

        GenericFieldDescriptor descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Tarefa.class, "org/west/xml/tarefaFormDocumentacao.xml", "Informações sobre Tarefa");
        formTarefa = new JBeanPanel(Tarefa.class, "Informações da Tarefa", descriptorForm);

        formTarefa.setPropertyValue("usuario", usuario);

        formTarefa.addPropertyChangeListener("bean", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Tarefa tarefa = new Tarefa();
                formTarefa.populateBean(tarefa);
                Servico servico = ServicoDAO.load(tarefa.getServico().getCodigo());
                ServicoDAO.lock(servico);
                formServico.setBean(servico);

                abasPanel.setSelectedIndex(0);
            }
        });


        formTarefa.getComponent("previsaoTermino").addPropertyChangeListener(new PropertyChangeListener() {

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

        cons.gridx = 0;
        cons.gridy = 2;
        cons.weightx = 0;
        cons.weighty = 0;
        cons.gridwidth = 1;
        cons.ipady = 70;
        cons.ipadx = 50;
        cons.fill = GridBagConstraints.BOTH;
        this.panelDireito.add(formTarefa);

        descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Servico.class, "org/west/xml/servicoFormDoc.xml", "formServico");
        this.formServico = new JBeanPanel(Servico.class, "Informações do Serviço", descriptorForm);

        cons.gridx = 1;
        cons.weightx = 1;
        cons.ipadx = 0;
        this.panelEsquerdo.add(formServico, BorderLayout.SOUTH);

        final JList compradores = (JList) getComponentInside(formServico.getComponent("compradores"));

        compradores.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    Cliente cliente = (Cliente) compradores.getSelectedValue();
                    FrmCliente frmCliente = new FrmCliente(cliente);
                    frmCliente.setVisible(true);
                }
            }
        });

        final JList vendedores = (JList) getComponentInside(formServico.getComponent("vendedores"));

        vendedores.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    Cliente cliente = (Cliente) vendedores.getSelectedValue();
                    FrmCliente frmCliente = new FrmCliente(cliente);
                    frmCliente.setVisible(true);
                }
            }
        });

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        ApplicationAction gravaTarefa = new ApplicationAction() {

            @Override
            public void execute() {
                Tarefa tarefa = new Tarefa();
                formTarefa.populateBean(tarefa);

                tarefa.setDataAtualizacao(WestPersistentManager.getDateServer());

                if (tarefa.getCodigo() == null) {
                    tarefa.setUsuario(usuario);
                }

                if (tarefa.getTerminado()) {
                    if (tarefa.getDataTermino() == null) {
                        tarefa.setDataTermino(new Date());
                    }
                } else {
                    tarefa.setDataTermino(null);
                }

                if (isSaveble(tarefa)) {
                    Tarefa tarefaOriginal = tarefa;

                    if (!tarefa.isNovo()) {
                        tarefaOriginal = TarefaDAO.load(tarefa.getCodigo());
                    }

                    if (tarefaOriginal.getUsuario().equals(usuario) || usuario.isSupervisor()) {
                        tarefaOriginal = tarefa;
                    } else if (usuario.isDoGrupo(tarefa.getServico().getTipoServico())) {
                        tarefaOriginal.setDescricao(tarefa.getDescricao());
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuário logado não tem permissão para alterar a tarefa!");
                        return;
                    }

                    if (TarefaDAO.save(tarefaOriginal)) {
                        JOptionPane.showMessageDialog(null, "Tarefa atulizada com sucesso.");
                        agenda.construirPanel(new Date());
                        List<Tarefa> lista = getListaTarefas("");
                        carregaTimer();
                        carregaTarefas(lista);
                        grafico.setListaTarefas(lista);
                        formTarefa.cleanForm();
                        formTarefa.setPropertyValue("usuario", usuario);
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar tarefa!");
                    }
                }
            }
        };

        ValidationAction tarefaAction = new ValidationAction(formTarefa);

        ApplicationAction tarefaValida = ActionChainFactory.createChainActions(tarefaAction, gravaTarefa);

        JActButton botaoGravarTarefa = new JActButton("Gravar", tarefaValida);

        panelBotoes.add(botaoGravarTarefa);

        panelBotoes.add(new JActButton("Limpar", new ApplicationAction() {

            @Override
            public void execute() {
                formTarefa.cleanForm();
                ((JComboBox) formTarefa.getComponent("aviso")).setModel(new ModelPeriodo(new Date()));
                formTarefa.setPropertyValue("usuario", usuario);
            }
        }));

        cons.gridx = 0;
        cons.gridy = 3;
        cons.weightx = 0;
        cons.weighty = 0;
        cons.ipady = 0;
        cons.insets = new Insets(0, 0, 0, 0);
        cons.fill = GridBagConstraints.HORIZONTAL;
        panelDireito.add(panelBotoes, BorderLayout.SOUTH);

        this.agenda = new PanelAgenda(formTarefa);
        this.agenda.construirPanel(new Date());

        abasPanel.addTab("Agenda", new ImageIcon(getClass().getResource("/org/west/imagens/agenda.png")), agenda);

        grafico = new PanelGrafico(Arrays.asList(TarefaDAO.listTarefaByQuery("usuario=" + usuario.getCodigo() + " and terminado=0", "previsaoTermino")), formTarefa);
        grafico.setMaximumSize(new Dimension(300, 300));
        grafico.setMinimumSize(new Dimension(300, 300));
        grafico.setPreferredSize(new Dimension(300, 300));
        abasPanel.addTab("Dispersão", new ImageIcon(getClass().getResource("/org/west/imagens/dispersao.png")), grafico);

        this.bar = this.agenda.getBar();

        JTextArea area = (JTextArea) getComponentInside(formTarefa.getComponent("textoAdd"));

        area.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    StringBuilder bd = new StringBuilder();
                    String textoOriginal = formTarefa.getPropertyValue("descricao").toString();
                    Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

                    bd.append(textoOriginal);
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    bd.append("\n");
                    bd.append(format.format(PontoDAO.getDateServer()));
                    bd.append(" (").append(usuario.getNome()).append("): ");
                    bd.append(formTarefa.getPropertyValue("textoAdd").toString());

                    formTarefa.setPropertyValue("descricao", bd);
                    formTarefa.setPropertyValue("textoAdd", "");
                }
            }
        });

        addHierarchyListener(new HierarchyListener() {

            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                bar.setValue(Math.round(bar.getMaximum() * new GregorianCalendar().get(11) / 24));
            }
        });

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                verificaPendencia();
                carregaTimer();
            }
        });
    }

    private JComponent getComponentInside(Component comp) {
        JScrollPane scroll = (JScrollPane) comp;
        JViewport view = (JViewport) scroll.getComponent(0);
        JComponent component = (JComponent) view.getComponent(0);
        return component;
    }

    public void reload() {
        WestPersistentManager.clear();
        JComboBox combo = (JComboBox) this.formTarefa.getComponent("servico");
        DefaultComboBoxModel model = new DefaultComboBoxModel(ServicoDAO.listServicoByQuery("codigo > 0", "codigo"));
        combo.setModel(model);
        combo.getModel().setSelectedItem(null);
    }

    private Boolean isSaveble(Tarefa tarefa) {

        if (tarefa.getServico().getCodigo() == 92 || tarefa.getServico().getCodigo() == 193) {
            return true;
        }

        Date data = tarefa.getPrevisaoTermino();

        if (usuario.isSupervisor()) {
            return true;
        }

        if (tarefa.getCodigo() == null) {
            return true;
        }

        Tarefa testa = TarefaDAO.load(tarefa.getCodigo());

        if (!Util.isMesmoDia(data, testa.getPrevisaoTermino())) {

            Object[] modificacoes = ModificaDAO.listModificaByQuery("tarefa=" + testa.getCodigo(), "codigo");

            if (modificacoes.length < 25) {
                Modifica modifica = new Modifica();
                modifica.setDataModificacao(new Date());
                modifica.setUsuario(this.usuario);
                modifica.setTarefa(tarefa);
                modifica.setDataAnterior(testa.getPrevisaoTermino());

                ModificaDAO.save(modifica);
            } else {
                JOptionPane.showMessageDialog(null, "Limite de modificações excedido!");
                return false;
            }
        }

        return true;
    }

    @Override
    public void onMessage(Message msg) {
        super.onMessage(msg);

        try {
            String tipo = msg.getStringProperty("tipo");

            if (tipo.equals("servico")) {
                carregaTarefas(getListaTarefas(""));
                ObjectMessage objMsg = (ObjectMessage) msg;
                Servico servico = (Servico) objMsg.getObject();
                FrmAvisoServicos aviso = new FrmAvisoServicos(this, true, servico);
                aviso.setVisible(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void carregaTimer() {
        TarefaCriteria criteria = new TarefaCriteria();

        Date min = Util.corrigirDate(new Date(), Util.INICIO);
        Date max = Util.corrigirDate(new Date(), Util.FIM);

        criteria.add(Restrictions.between("aviso", min, max));
        criteria.add(Restrictions.eq("usuario", usuario));
        criteria.add(Restrictions.eq("terminado", false));
        criteria.addOrder(Order.asc("aviso"));


        final Queue<Tarefa> queue = new LinkedList<Tarefa>(criteria.list());

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Calendar calenda = new GregorianCalendar();
                calenda.setTime(new Date());
                List<Tarefa> lista = new ArrayList<Tarefa>();

                while (queue.peek() != null && queue.peek().getAviso().before(new Date())) {
                    lista.add(queue.poll());
                }

                if (!lista.isEmpty()) {
                    FrmTimer frm = new FrmTimer(lista);
                    frm.setVisible(true);
                }
            }
        });

        timer.start();
    }

    private List<Tarefa> getTarefasPorPasta(String busca) {
        List<Tarefa> lista = new ArrayList<Tarefa>();
        try {
            TarefaCriteria criteria = new TarefaCriteria();
            criteria.createServicoCriteria().add(Restrictions.eq("pastaMae", busca)).addOrder(Order.asc("pastaMae"));
            lista = criteria.list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public List<Tarefa> getListaTarefas(String busca) {
        WestPersistentManager.clear();

        TarefaCriteria criteriaTarefa = new TarefaCriteria();

        if (busca.isEmpty()) {

            criteriaTarefa.createAlias("servico", "servico");

            criteriaTarefa.add(Restrictions.eq("terminado", false));

            if ("Jurídico".equalsIgnoreCase(usuario.getGrupo())) {
                criteriaTarefa.createUsuarioCriteria().add(Restrictions.eq("grupo", "Jurídico"));
            } else {
                criteriaTarefa.add(Restrictions.eq("usuario", usuario));
            }

        } else {

            if (busca.matches("^[0-9]*$")) {
                criteriaTarefa.createServicoCriteria().add(Restrictions.eq("codigo", new Long(busca)));
            } else {
                criteriaTarefa.createAlias("servico", "servico");
                Disjunction dis = Restrictions.disjunction();
                dis.add(Restrictions.ilike("nome", busca, MatchMode.ANYWHERE));
                dis.add(Restrictions.ilike("descricao", busca, MatchMode.ANYWHERE));

                dis.add(Restrictions.ilike("servico.nome", busca, MatchMode.ANYWHERE));
                dis.add(Restrictions.ilike("servico.cliente", busca, MatchMode.ANYWHERE));
                dis.add(Restrictions.ilike("servico.endereco", busca, MatchMode.ANYWHERE));

                criteriaTarefa.createAlias("servico.compradores", "comprador", JoinType.LEFT_OUTER_JOIN);
                dis.add(Restrictions.ilike("comprador.nome", busca, MatchMode.ANYWHERE));

                criteriaTarefa.createAlias("servico.vendedores", "vendedor", JoinType.LEFT_OUTER_JOIN);
                dis.add(Restrictions.ilike("vendedor.nome", busca, MatchMode.ANYWHERE));

                criteriaTarefa.add(dis);
            }
        }

        criteriaTarefa.addOrder(Order.asc("servico"));
        criteriaTarefa.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return criteriaTarefa.list();
    }

    public void carregaTarefas(List<Tarefa> listaTarefas) {
        panelTree.removeAll();

        TableFieldDescriptor tableDescriptor = XMLDescriptorFactory.getTableFieldDescriptor(Tarefa.class, "org/west/xml/servicoTreeConstrutora.xml", null);
        BeanTreeTableModel<Tarefa> model = new BeanTreeTableModel<Tarefa>(tableDescriptor, Tarefa.class, listaTarefas);
        table = new JBeanTreeTable(model);
        table.getTree().setCellRenderer(new RenderTree(listaTarefas));

        final JPopupMenu menu = new JPopupMenu();

        final JMenuItem novo = new JMenuItem("Novo serviço...", new ImageIcon(super.getClass().getResource("/org/west/imagens/novo.png")));
        menu.add(novo);

        final FrmDocumentos origem = this;

        novo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FrmServico frame = new FrmServico(origem, true, new Servico());
                frame.setVisible(true);
            }
        });

        final JMenuItem passarToken = new JMenuItem("Passar token...", new ImageIcon(super.getClass().getResource("/org/west/imagens/token.png")));
        menu.add(passarToken);

        passarToken.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object valor = JOptionPane.showInputDialog(null,
                        "Escolha o usuário:", "Usuário",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        getUsuarios(), null);

                if (valor != null) {
                    servicoAtual.setToken((Usuario) valor);
                    servicoAtual.setDataToken(new Date());
                    servicoAtual.setPastaMae(servicoAtual.getPastaMae().toUpperCase());

                    if (ServicoDAO.save(servicoAtual)) {
                        JOptionPane.showMessageDialog(null, "Token passado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        carregaTarefas(getListaTarefas(""));
                        JMSManager.enviarObjectMensagem(servicoAtual, valor.toString(), "servico");
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao passar o token!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            private Usuario[] getUsuarios() {
                return UsuarioDAO.listUsuarioByQuery("nivel = 4 and status = 1", "nome");
            }
        });

        final JMenuItem refresh = new JMenuItem("Atualizar", new ImageIcon(super.getClass().getResource("/org/west/imagens/atualizar.png")));
        menu.add(refresh);

        refresh.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                carregaTarefas(getListaTarefas(""));
                grafico.setListaTarefas(Arrays.asList(TarefaDAO.listTarefaByQuery("usuario=" + usuario.getCodigo() + " and terminado=0", "previsaoTermino")));
                agenda.construirPanel(agenda.getDataAtual());
                DefaultComboBoxModel model = new DefaultComboBoxModel(getPastas().toArray());
                comboPasta.setModel(model);
                reload();
            }
        });

        menu.addSeparator();

        final JMenuItem imprimir = new JMenuItem("Imprimir...", new ImageIcon(getClass().getResource("/org/west/imagens/print.png")));
        menu.add(imprimir);

        imprimir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JasperReport report = (JasperReport) JRLoader.loadObject(
                            getClass().getResource("/org/west/relatorios/relatorio_capa.jasper"));

                    JRCapaServico source = new JRCapaServico(servicoAtual);

                    HashMap parametros = new HashMap();
                    ImageIcon icon = new ImageIcon(getClass().getResource("/org/west/imagens/cabecalho.png"));
                    parametros.put("LOGO", icon.getImage());

                    JasperPrint print = JasperFillManager.fillReport(report, parametros, source);
                    JasperViewer.viewReport(print, false);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        final JMenuItem propriedades = new JMenuItem("Propriedades", new ImageIcon(getClass().getResource("/org/west/imagens/propiedade.png")));
        menu.add(propriedades);

        propriedades.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FrmServico frame = new FrmServico(origem, true, servicoAtual);
                frame.setVisible(true);
            }
        });

        this.table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if ((e.getClickCount() == 2) && (e.getButton() == 1)) {
                    Tarefa tarefa = (Tarefa) table.getSelectedBean();
                    if (tarefa != null) {
                        formTarefa.setBean(tarefa);
                    }
                }

                if (e.getButton() == 3) {
                    JBeanTreeTable table = (JBeanTreeTable) e.getComponent();
                    JTree tree = table.getTree();
                    tree.setSelectionPath(tree.getClosestPathForLocation(e.getX(), e.getY()));

                    if (table.getSelectedBean() == null) {
                        if (tree.getSelectionPath().getPath().length == 1) {
                            propriedades.setEnabled(false);
                            passarToken.setEnabled(false);
                            imprimir.setEnabled(false);
                            menu.show(e.getComponent(), e.getX(), e.getY());
                        } else {
                            Object nome = tree.getSelectionPath().getPath()[1];
                            String codigo = nome.toString().substring(0, nome.toString().indexOf("-")).trim();
                            Servico servico = ServicoDAO.load(new Long(codigo));
                            if (servico != null) {
                                servicoAtual = servico;
                                propriedades.setEnabled(true);
                                passarToken.setEnabled(usuario.equals(servicoAtual.getToken()) || usuario.isSupervisor());
                                menu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    }
                }
            }
        });

        panelTree.add(new JScrollPane(table), BorderLayout.CENTER);
        panelTree.revalidate();
        panelTree.repaint();
    }

    private List getPastas() {
        List lista = new ArrayList();

        try {
            ServicoCriteria criteria = new ServicoCriteria();
            criteria.add(Restrictions.isNotNull("pastaMae"));
            criteria.add(Restrictions.not(Restrictions.eq("pastaMae", "")));

            ProjectionList proje = Projections.projectionList();
            proje.add(Projections.groupProperty("pastaMae"));

            criteria.setProjection(proje);
            lista = criteria.list();
            lista.add(0, "Minhas Tarefas");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    private void verificaPendencia() {
        TarefaCriteria criteria = new TarefaCriteria();
        criteria.add(Restrictions.eq("usuario", this.usuario));
        criteria.add(Restrictions.eq("terminado", false));

        criteria.addOrder(Order.asc("previsaoTermino"));

        List lista = criteria.list();

        if (lista.size() > 0) {
            FrmTarefa form = new FrmTarefa(lista, getListaServicoParados(), formTarefa);
            form.setVisible(true);
        }
    }

    private List<Servico> getListaServicoParados() {
        List<Servico> lista = new ArrayList<Servico>();

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            SQLQuery query = WestPersistentManager.getSession().createSQLQuery(
                    "SELECT DISTINCT T.* "
                    + "FROM tab_servico S INNER JOIN tab_tarefas T ON T.servico = S.codigo"
                    + " WHERE (SELECT COUNT(terminado) FROM tab_tarefas WHERE servico = S.codigo AND terminado = 0) = 0"
                    + " AND S.responsavel = " + usuario.getCodigo()
                    + " AND T.dataTermino < '" + format.format(getDataLimite(new Date(), 1)) + "'"
                    + " AND S.concluido = 0");
            query.addEntity(Tarefa.class);

            for (Object obj : query.list()) {

                Tarefa tarefa = (Tarefa) obj;

                if (!lista.contains(tarefa.getServico())) {
                    lista.add(tarefa.getServico());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    private Date getDataLimite(Date data, Integer dias) {
        Date retorno = new Date(data.getTime());

        boolean flag = true;

        while (flag) {

            if (dias > 0) {
                retorno = Util.addHoras(retorno, -24);
                GregorianCalendar calenda = new GregorianCalendar();
                calenda.setTime(retorno);

                int diaSemana = calenda.get(Calendar.DAY_OF_WEEK);

                if (diaSemana != Calendar.SATURDAY && diaSemana != Calendar.SUNDAY) {
                    dias--;
                }
            } else {
                flag = false;
            }
        }

        return retorno;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        abasPanel = new javax.swing.JTabbedPane();
        panelTarefas = new javax.swing.JPanel();
        panelDireito = new javax.swing.JPanel();
        panelEsquerdo = new javax.swing.JPanel();
        panelBusca = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBusca = new javax.swing.JTextField();
        comboPasta = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        panelTree = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Usuário: " + usuario.getNome());

        panelTarefas.setMaximumSize(new java.awt.Dimension(350, 2147483647));
        panelTarefas.setMinimumSize(new java.awt.Dimension(350, 82));
        panelTarefas.setPreferredSize(new java.awt.Dimension(350, 100));
        panelTarefas.setLayout(new java.awt.GridBagLayout());

        panelDireito.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelTarefas.add(panelDireito, gridBagConstraints);

        panelEsquerdo.setLayout(new java.awt.BorderLayout());

        panelBusca.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Busca:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelBusca.add(jLabel1, gridBagConstraints);

        txtBusca.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        txtBusca.setMinimumSize(new java.awt.Dimension(300, 26));
        txtBusca.setPreferredSize(new java.awt.Dimension(300, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelBusca.add(txtBusca, gridBagConstraints);

        comboPasta.setMaximumSize(new java.awt.Dimension(300, 32767));
        comboPasta.setMinimumSize(new java.awt.Dimension(300, 26));
        comboPasta.setPreferredSize(new java.awt.Dimension(300, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelBusca.add(comboPasta, gridBagConstraints);

        jLabel2.setText("Pasta Mãe:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelBusca.add(jLabel2, gridBagConstraints);

        panelEsquerdo.add(panelBusca, java.awt.BorderLayout.NORTH);

        panelTree.setLayout(new java.awt.BorderLayout());
        panelEsquerdo.add(panelTree, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelTarefas.add(panelEsquerdo, gridBagConstraints);

        abasPanel.addTab("Processos e Tarefas", new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/processo.png")), panelTarefas); // NOI18N

        getContentPane().add(abasPanel, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Arquivo");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/Recados.png"))); // NOI18N
        jMenuItem1.setText("Recados...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/chave.png"))); // NOI18N
        jMenuItem2.setText("Trocar Senha...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/pendecia.png"))); // NOI18N
        jMenuItem5.setText("Ver Pendências...");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/register.png"))); // NOI18N
        jMenuItem4.setText("Caixa...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);
        jMenu1.add(jSeparator1);

        jMenuItem3.setText("Sair");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        FrmRecadosUsuario recados = new FrmRecadosUsuario(this);
        recados.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        FrmSenha frm = new FrmSenha(null);
        frm.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Ponto ponto = new Ponto();
        ponto.setUsuario(usuario);
        ponto.setDataPonto(new Date());
        ponto.setObs("Saída");
        PontoDAO.save(ponto);
        System.exit(0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        FrmLancamentos frm = new FrmLancamentos();
        frm.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        verificaPendencia();
    }//GEN-LAST:event_jMenuItem5ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane abasPanel;
    private javax.swing.JComboBox comboPasta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPanel panelBusca;
    private javax.swing.JPanel panelDireito;
    private javax.swing.JPanel panelEsquerdo;
    private javax.swing.JPanel panelTarefas;
    private javax.swing.JPanel panelTree;
    private javax.swing.JTextField txtBusca;
    // End of variables declaration//GEN-END:variables
}