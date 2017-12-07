package org.west.formulario.corretor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JActButton;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;
import org.west.componentes.JMSManager;
import org.west.componentes.PanelImovel;
import org.west.componentes.PanelPortaria;
import org.west.entidades.Altera;
import org.west.entidades.AlteraCriteria;
import org.west.entidades.Cliente;
import org.west.entidades.ClienteDAO;
import org.west.entidades.Historico;
import org.west.entidades.HistoricoDAO;
import org.west.entidades.Imobiliaria;
import org.west.entidades.ImobiliariaCriteria;
import org.west.entidades.ImobiliariaDAO;
import org.west.entidades.Imovel;
import org.west.entidades.ImovelDAO;
import org.west.entidades.Portaria;
import org.west.entidades.PortariaDAO;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioDAO;
import org.west.entidades.WestPersistentManager;
import org.west.formulario.FrmMain;
import org.west.formulario.cliente.FrmCliente;
import org.west.formulario.corretor.actions.ActionBuscaCliente;
import org.west.formulario.portaria.FrmFrequencia;
import org.west.formulario.recados.FrmRecadosUsuario;
import org.west.formulario.usuario.FrmSenha;
import org.west.utilitarios.DocumentMaxCharacter;
import org.west.utilitarios.RenderImob;
import org.west.utilitarios.Util;

public class FrmCorretor extends FrmMain {

    private GenericFieldDescriptor descriptorForm;
    private JBeanPanel<ClienteFiltro> formBuscaCliente;
    private TableFieldDescriptor descriptorTable;
    private BeanTableModel<Cliente> modeloCliente;
    private JBeanTable tabelaCliente;
    private RenderImob renderizador;
    private JBeanPanel<Cliente> formCliente;
    private BeanTableModel<Imobiliaria> modeloImobiliaria;
    private JBeanTable tabelaImobiliaria;
    private JBeanPanel<Imobiliaria> formImobiliaria;
    private BeanTableModel<Historico> modeloHistorico;
    private JBeanTable tabelaHistorico;
    private JBeanPanel<Imovel> formImovel;
    private JBeanPanel<Portaria> formPortaria;
    private JTabbedPane tabsImovel;
    private PanelImovel panelImoveis;
    private PanelPortaria panelPortaria;
    private FrmAvisoImovel frameAvisoImovel;

    public FrmCorretor() {
        super();
        initComponents();

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel esquerdo = new JPanel(new GridBagLayout());

        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.anchor = GridBagConstraints.CENTER;
        cons.weightx = 1;
        cons.weighty = 0;
        cons.insets = new Insets(5, 5, 5, 5);

        cons.gridx = 0;
        cons.gridy = 0;

        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(ClienteFiltro.class, "org/west/xml/buscaClienteCorretor.xml", "cliente");
        this.formBuscaCliente = new JBeanPanel(ClienteFiltro.class, "Busca de Clientes", this.descriptorForm);
        esquerdo.add(this.formBuscaCliente, cons);

        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Cliente.class, "org/west/xml/clienteTabelaCorretor.xml", "tabelaCliente");
        this.modeloCliente = new BeanTableModel(this.descriptorTable);
        this.tabelaCliente = new JBeanTable(this.modeloCliente);

        this.tabelaCliente.enableHeaderOrdering();

        this.tabelaCliente.addDoubleClickAction(new ApplicationAction() {
            @Override
            public void execute() {
                FrmCliente frmCliente = new FrmCliente(modeloCliente.getBeanAt(tabelaCliente.getSelectedRow()));
                frmCliente.setVisible(true);
            }
        });

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        cons.gridy = 1;
        cons.fill = GridBagConstraints.BOTH;
        cons.anchor = GridBagConstraints.EAST;
        cons.weightx = 0;
        cons.weighty = 0;
        cons.ipady = 0;

        JActButton buscaCliente = new JActButton("Buscar", new ActionBuscaCliente(formBuscaCliente, modeloCliente, tabelaCliente));

        panelBotoes.add(buscaCliente);

        JActButton botaoLimpar = new JActButton("Limpar", new ApplicationAction() {
            @Override
            public void execute() {
                formBuscaCliente.cleanForm();
                atualizaLista();
            }
        });

        panelBotoes.add(botaoLimpar);

        esquerdo.add(panelBotoes, cons);

        cons.gridy = 2;
        cons.gridx = 0;
        cons.fill = 1;
        cons.anchor = 10;
        cons.weighty = 1;
        cons.weightx = 1;

        esquerdo.add(new JScrollPane(this.tabelaCliente), cons);

        this.renderizador = new RenderImob(null);
        this.tabelaCliente.setDefaultRenderer(Object.class, renderizador);

        cons.gridy = 3;
        cons.ipady = 0;
        cons.weighty = 0;

        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Cliente.class, "org/west/xml/clienteRecepcao.xml", "clienteForm");
        this.formCliente = new JBeanPanel(Cliente.class, "Clientes", this.descriptorForm);

        esquerdo.add(this.formCliente, cons);

        //direito
        GridBagLayout layout = new GridBagLayout();
        JPanel direito = new JPanel(layout);

        cons = new GridBagConstraints();
        cons.insets = new Insets(5, 10, 5, 10);
        cons.weightx = 1;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;

        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Imobiliaria.class, "org/west/xml/imobiliariaTabelaRecepcao.xml", "tabelaImobiliaria");
        this.modeloImobiliaria = new BeanTableModel(this.descriptorTable);
        this.tabelaImobiliaria = new JBeanTable(this.modeloImobiliaria);
        this.tabelaImobiliaria.enableHeaderOrdering();

        cons.gridx = 0;
        cons.gridy = 0;
        cons.gridwidth = 2;
        direito.add(new JScrollPane(this.tabelaImobiliaria), cons);

        if (usuario.isSupervisor() || usuario.isCoordenador()) {
            JPanel panelBotoesSuper = new JPanel(new BorderLayout(20, 0));

            if (usuario.isSupervisor()) {

                JActButton btnImob = new JActButton("Gravar", new ApplicationAction() {
                    @Override
                    public void execute() {
                        Long codigo = new Long(formCliente.getPropertyValue("codigo").toString());

                        if (codigo.longValue() == 0L) {
                            JOptionPane.showMessageDialog(null, "Não existe um cliente selecionado!", "Erro", 0);
                        } else {
                            Imobiliaria atual = new Imobiliaria();
                            formImobiliaria.populateBean(atual);

                            if (atual.getCodigo() == 0) {
                                atual.setDataEntrada(new Date());
                                atual.setStatus(Imobiliaria.SEM_CIENCIA);
                            } else {
                                Imobiliaria bancoImob = ImobiliariaDAO.load(atual.getCodigo());
                                atual.setStatus(bancoImob.getStatus());
                                WestPersistentManager.clear();
                            }

                            Cliente cliente = ClienteDAO.load(codigo.longValue());
                            ClienteDAO.lock(cliente);
                            atual.setCliente(cliente);

                            if (ImobiliariaDAO.save(atual)) {
                                String[] usuarios = atual.getUsuario().getNome().split("/");

                                for (String string : usuarios) {
                                    JMSManager.enviarObjectMensagem(atual, string, "cliente");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao salvar atendimento!");
                            }

                            formImobiliaria.cleanForm();
                            modeloImobiliaria.setBeanList(new ArrayList<Imobiliaria>(cliente.getImobiliarias()));

                            WestPersistentManager.clear();
                        }
                    }
                });

                panelBotoesSuper.add(btnImob, BorderLayout.EAST);
            }

            if (usuario.isCoordenador()) {
                JActButton btnAcompanha = new JActButton("Desacompanhar / Acompanhar", new ApplicationAction() {
                    @Override
                    public void execute() {
                        Long codigo = (Long) formImobiliaria.getPropertyValue("codigo");

                        if (codigo != null && codigo != 0) {
                            Imobiliaria imob = ImobiliariaDAO.load(codigo);
                            imob.setAcompanhado(!imob.isAcompanhado());

                            ImobiliariaDAO.save(imob);

                            atualizaLista();
                        }
                    }
                });

                panelBotoesSuper.add(btnAcompanha, BorderLayout.WEST);
            }

            cons.gridy = 2;
            cons.weightx = 0;
            cons.weighty = 0;
            cons.fill = GridBagConstraints.HORIZONTAL;
            cons.anchor = GridBagConstraints.LAST_LINE_END;
            direito.add(panelBotoesSuper, cons);
        }

        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Imobiliaria.class, "org/west/xml/imobiliariaFormRecepcao.xml", "formImobiliaira");
        this.formImobiliaria = new JBeanPanel(Imobiliaria.class, "Atendimento em Imobiliária", this.descriptorForm);

        cons.gridx = 0;
        cons.gridy = 1;
        cons.gridwidth = 1;
        cons.weighty = 0;
        cons.weightx = 1;
        cons.fill = GridBagConstraints.BOTH;
        direito.add(this.formImobiliaria, cons);

        cons.gridx = 1;
        tabsImovel = new JTabbedPane(JTabbedPane.TOP);

        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Imobiliaria.class, "org/west/xml/imovelCorretor.xml", "corretorImovelleo");
        formImovel = new JBeanPanel(Imovel.class, "Imóvel", this.descriptorForm);
        formImovel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Portaria.class, "org/west/xml/portariaForm.xml", "portariaForm");
        formPortaria = new JBeanPanel(Portaria.class, "Portaria", this.descriptorForm);
        formPortaria.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

        tabsImovel.addTab("Imóvel", formImovel);
        tabsImovel.addTab("Portaria", formPortaria);

        direito.add(tabsImovel, cons);

        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Historico.class, "org/west/xml/historicoTabelaRecepcao.xml", "dataEntrada");
        this.modeloHistorico = new BeanTableModel(this.descriptorTable);
        this.tabelaHistorico = new JBeanTable(this.modeloHistorico);
        this.tabelaHistorico.enableHeaderOrdering();

        cons.gridx = 0;
        cons.gridwidth = 2;
        cons.gridy = 3;
        cons.weighty = 1;
        direito.add(new JScrollPane(this.tabelaHistorico), cons);

        JPanel base = new JPanel(new BorderLayout(40, 40));

        JLabel lblDescri = new JLabel("Novo histórico:");
        final JTextField txtOBS = new JTextField(33);
        txtOBS.setDocument(new DocumentMaxCharacter(255));
        JButton btnADD = new JButton("Acrescentar");

        base.add(lblDescri, BorderLayout.WEST);
        base.add(txtOBS, BorderLayout.CENTER);
        base.add(btnADD, BorderLayout.EAST);

        cons.gridy = 4;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weighty = 0;
        direito.add(base, cons);

        JSplitPane divisor = new JSplitPane();

        divisor.setOneTouchExpandable(true);
        divisor.setLeftComponent(esquerdo);
        divisor.setRightComponent(direito);
        divisor.getLeftComponent().setMinimumSize(new Dimension(425, 0));
        divisor.setDividerLocation(425);

        tabClientes.add(divisor, BorderLayout.CENTER);

        jToolBar1.remove(2);
        JButton btnAviso = new JButton("Pendências", new ImageIcon(getClass().getResource("/org/west/imagens/aviso_imovel.png")));
        btnAviso.setFocusable(false);
        btnAviso.setIconTextGap(8);
        btnAviso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificaAlteracoes();
            }
        });

        jToolBar1.add(btnAviso, 2);

        panelImoveis = new PanelImovel();
        panelPortaria = new PanelPortaria(panelImoveis);

        jTabbedPane1.addTab("Imóveis", new ImageIcon(getClass().getResource("/org/west/imagens/home.png")), panelImoveis);
        jTabbedPane1.addTab("Portarias", new ImageIcon(getClass().getResource("/org/west/imagens/predio.png")), panelPortaria);

        this.formImobiliaria.getComponent("imovel").addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);

                formPortaria.cleanForm();
                formImovel.cleanForm();

                Object objImovel = formImobiliaria.getPropertyValue("imovel");
                Object objVeiculo = formImobiliaria.getPropertyValue("veiculo");

                if (objImovel != null && objVeiculo != null) {
                    String textoImovel = objImovel.toString();

                    if (isNumber(textoImovel)) {
                        if (!textoImovel.isEmpty()) {
                            if (objVeiculo.toString().equals("Portaria")) {
                                Portaria portaria = PortariaDAO.load(Long.parseLong(textoImovel));
                                if (portaria != null) {
                                    formPortaria.setBean(portaria);
                                    tabsImovel.setSelectedIndex(1);
                                }
                            } else {
                                Imovel imovel = ImovelDAO.load(Long.parseLong(textoImovel));
                                if (imovel != null) {
                                    formImovel.setBean(imovel);
                                    tabsImovel.setSelectedIndex(0);
                                    formImobiliaria.setPropertyValue("valor", imovel.getValor());
                                }
                            }
                        }
                    }
                }
            }
        });

        formImovel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    Long codigo = new Long(formImovel.getPropertyValue("referencia").toString());
                    if (codigo != null && codigo > 0) {
                        panelImoveis.exibeDetalhes(ImovelDAO.load(codigo));
                        jTabbedPane1.setSelectedIndex(1);
                    }
                }
            }
        });

        formPortaria.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    Long codigo = new Long(formPortaria.getPropertyValue("codigo").toString());
                    if (codigo != null && codigo > 0) {
                        panelImoveis.exibeDetalhes(Arrays.asList(ImovelDAO.listImovelByQuery("portaria=" + codigo, "status,atualizado")));
                        jTabbedPane1.setSelectedIndex(1);
                    }
                }
            }
        });

        btnADD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer codigo = new Integer(formImobiliaria.getPropertyValue("codigo").toString());

                if (codigo.intValue() == 0) {
                    JOptionPane.showMessageDialog(null, "Não foi selecionado um atendimento!", "Erro", 0);
                } else if (txtOBS.getText().length() < 10) {
                    JOptionPane.showMessageDialog(null, "A mensagem deve conter mais de 10 caracteres!", "Erro", 0);
                } else {
                    Imobiliaria atual = ImobiliariaDAO.load(codigo.longValue());

                    Historico novo = new Historico();

                    novo.setDataEntrada(new Date());
                    novo.setDescricao(txtOBS.getText());
                    novo.setImobiliaria(atual);
                    novo.setUsuario(usuario);

                    if (atual.getStatus() != Imobiliaria.ATENDIDO) {

                        int option = JOptionPane.showOptionDialog(null, "Atendimento Concluído?", "Status do Atendimento",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sem Contato!", "Conluído!"}, null);

                        if (option == 0) {
                            atual.setStatus(Imobiliaria.SEM_CONTATO);
                        } else {
                            atual.setStatus(Imobiliaria.ATENDIDO);
                        }
                    } else {
                        atual.setStatus(Imobiliaria.ATENDIDO);
                    }

                    try {

                        if (ImobiliariaDAO.save(atual) && HistoricoDAO.save(novo)) {
                            modeloHistorico.addBeanAt(0, novo);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao adicionar historico!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    txtOBS.setText("");
                    WestPersistentManager.clear();
                    atualizaLista();
                }
            }
        });

        this.tabelaCliente.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (tabelaCliente.getSelectedRow() > -1) {
                    carregaCliente();
                }
            }
        });

        this.tabelaImobiliaria.addDoubleClickAction(new ApplicationAction() {
            @Override
            public void execute() {
                Imobiliaria atual = (Imobiliaria) modeloImobiliaria.getBeanAt(tabelaImobiliaria.getSelectedRow());
                formImobiliaria.setBean(atual);
                carregaImovel(atual);
                modeloHistorico.setBeanList(Arrays.asList(HistoricoDAO.listHistoricoByQuery("imobiliaria=" + atual.getCodigo(), "dataEntrada")));
                WestPersistentManager.clear();
            }
        });

        this.formImobiliaria.associateAction("prioridade", new ApplicationAction() {
            @Override
            public void execute() {
                Object codigo = formImobiliaria.getPropertyValue("codigo");

                if (codigo != null) {
                    Imobiliaria imobAtual = ImobiliariaDAO.load(new Long(codigo.toString()));

                    if (imobAtual.getStatus() >= Imobiliaria.SEM_RETORNO) {

                        imobAtual.setPrioridade((Boolean) formImobiliaria.getPropertyValue("prioridade"));

                        if (ImobiliariaDAO.save(imobAtual)) {
                            JOptionPane.showMessageDialog(null, "Atendimento salvo com sucesso!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            atualizaLista();
                        } else {
                            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gravar objecto!", "Aviso", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Atendimento sem histórico!!", "Aviso", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Não foi selecionado um atendimento!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelImoveis.addPropertyChangeListener("listagem", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                jTabbedPane1.setSelectedIndex(1);
            }
        });

        panelImoveis.getDetalhes().addPropertyChangeListener("portaria", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                jTabbedPane1.setSelectedIndex(2);
                panelPortaria.setPortaria((Portaria) evt.getNewValue());
            }
        });

        panelImoveis.getListagemImo().addPropertyChangeListener("listagem", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                jTabbedPane1.setSelectedIndex(1);
            }
        });

        atualizaLista();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                verificaPendencias();
                verificaAlteracoes();

                try {
                    for (Usuario usu : UsuarioDAO.listUsuarioByQuery("nivel = 1 and status = 1 and not nome like '%/%'", "nome")) {
                        JMSManager.enviarObjectMensagem(usuario, usu.getNome(), "usuario");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }
        });
    }

    private void verificaAlteracoes() {
        AlteraCriteria criteria = new AlteraCriteria();
        criteria.createImovelCriteria().createUsuarioCriteria().add(Restrictions.eq("nome", usuario.getNome()));

        if (!criteria.list().isEmpty()) {
            frameAvisoImovel = new FrmAvisoImovel(this, false, criteria.listAlteras(), panelImoveis.getListagemImo());
            frameAvisoImovel.setVisible(true);
        }
    }

    private boolean isNumber(String str) {
        boolean retorno = true;
        for (int x = 0; x < str.length(); x++) {
            if (!Character.isDigit(str.charAt(x))) {
                retorno = false;
            }
        }
        return retorno;
    }

    @Override
    public void onMessage(Message msg) {
        super.onMessage(msg);
        try {
            String tipo = msg.getStringProperty("tipo");

            if (tipo.equals("cliente")) {
                atualizaLista();
                List lista = new ArrayList();
                ObjectMessage objMsg = (ObjectMessage) msg;
                lista.add((Imobiliaria) objMsg.getObject());
                FrmAvisoCliente aviso = new FrmAvisoCliente(this, false, new ArrayList(lista), this);
                aviso.setVisible(true);
            }

            if (tipo.equals("imovel")) {
                ObjectMessage objMsg = (ObjectMessage) msg;
                Altera Altera = (Altera) objMsg.getObject();

                frameAvisoImovel.addAltera(Altera);
                frameAvisoImovel.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public final void atualizaLista() {
        try {

            Calendar c = new GregorianCalendar();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_YEAR, -30);

            ImobiliariaCriteria imobCriteria = new ImobiliariaCriteria();

            Disjunction dis = Restrictions.disjunction();
            dis.add(Restrictions.eq("prioridade", true));
            dis.add(Restrictions.gt("dataEntrada", Util.corrigirDate(c.getTime(), Util.INICIO)));
            dis.add(Restrictions.eq("acompanhado", true));

            imobCriteria.add(dis);

            imobCriteria.addOrder(Order.desc("acompanhado")).addOrder(Order.desc("prioridade"))
                    .addOrder(Order.asc("status")).addOrder(Order.desc("dataEntrada"));

            imobCriteria.createUsuarioCriteria().add(Restrictions.ilike("nome", usuario.getNome(), MatchMode.ANYWHERE));

            List<Imobiliaria> listaImob = new ArrayList<Imobiliaria>();
            List<Cliente> listaCli = new ArrayList<Cliente>();

            for (Imobiliaria imob : imobCriteria.listImobiliarias()) {

                if (!listaCli.contains(imob.getCliente())) {
                    listaCli.add(imob.getCliente());
                    listaImob.add(imob);
                }
            }

            RenderImob render = new RenderImob(listaImob);
            modeloCliente.setBeanList(listaCli);

            for (int x = 0; x < tabelaCliente.getColumnModel().getColumnCount(); ++x) {
                tabelaCliente.getColumnModel().getColumn(x).setCellRenderer(render);
            }

            WestPersistentManager.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void carregaCliente() {
        WestPersistentManager.clear();

        Cliente atual = (Cliente) modeloCliente.getBeanAt(tabelaCliente.getSelectedRow());
        formCliente.setBean(atual);

        ClienteDAO.lock(atual);

        List<Imobiliaria> imobiliarias = new ArrayList<Imobiliaria>(atual.getImobiliarias());

        this.modeloImobiliaria.setBeanList(imobiliarias);

        RenderImob render = new RenderImob(imobiliarias);

        for (int x = 0; x < tabelaImobiliaria.getColumnModel().getColumnCount(); ++x) {
            tabelaImobiliaria.getColumnModel().getColumn(x).setCellRenderer(render);
        }

        this.formImobiliaria.cleanForm();
        this.modeloHistorico.setBeanList(new ArrayList());

        Imobiliaria imoAtual = (Imobiliaria) imobiliarias.get(0);
        this.formImobiliaria.setBean(imoAtual);

        ImobiliariaDAO.lock(imoAtual);
        this.modeloHistorico.setBeanList(new ArrayList<Historico>(imoAtual.getHistoricos()));
        carregaImovel(imoAtual);

        WestPersistentManager.clear();
    }

    private void carregaImovel(Imobiliaria Imoatual) {
        if (!Imoatual.getImovel().isEmpty()) {
            String txtImovel = Imoatual.getImovel();

            formPortaria.cleanForm();
            formImovel.cleanForm();
            tabsImovel.setSelectedIndex(0);

            //portaria
            if (Imoatual.getVeiculo().getNome().equals("Portaria")) {
                Long codigo = new Long(txtImovel);
                if (codigo > 0) {
                    Portaria portaria = PortariaDAO.get(codigo);
                    if (portaria != null) {
                        formPortaria.setBean(portaria);
                        tabsImovel.setSelectedIndex(1);
                    }
                }
            } else {
                //imovel
                if (isNumber(txtImovel) && !txtImovel.equals("0")) {
                    Long codigo = new Long(Imoatual.getImovel());
                    if (codigo > 0) {
                        Imovel imovel = ImovelDAO.loadImovelByQuery("referencia = " + txtImovel, "referencia");
                        if (imovel != null) {
                            formImovel.setBean(imovel);
                            tabsImovel.setSelectedIndex(0);
                        }
                    }
                }
            }
        }
    }

    private void verificaPendencias() {
        try {
            ImobiliariaCriteria criteria = new ImobiliariaCriteria();
            criteria.add(Restrictions.eq("status", Imobiliaria.SEM_CIENCIA));
            criteria.createUsuarioCriteria().add(Restrictions.ilike("nome", this.usuario.getNome(), MatchMode.ANYWHERE));

            List lista = criteria.list();

            if (lista.size() > 0) {
                FrmAvisoCliente aviso = new FrmAvisoCliente(this, true, lista, this);
                aviso.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabClientes = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema West - Corretor");

        tabClientes.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Clientes", new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/clientes.png")), tabClientes); // NOI18N

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

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

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/frequencia.png"))); // NOI18N
        jMenuItem4.setText("Marcar Frequência...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/jornal.png"))); // NOI18N
        jMenuItem5.setText("Anúncios...");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);
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

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        FrmSenha frm = new FrmSenha(this);
        frm.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        FrmFrequencia frm = new FrmFrequencia();
        frm.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        FrmRecadosUsuario recadosUsuario = new FrmRecadosUsuario(this);
        recadosUsuario.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        FrmAnuncios anuncio = new FrmAnuncios(this, false);
        anuncio.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel tabClientes;
    // End of variables declaration//GEN-END:variables
}