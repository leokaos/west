package org.west.formulario.recepcao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.swingBean.actions.ActionChainFactory;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.actions.ValidationAction;
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
import org.west.entidades.Cliente;
import org.west.entidades.ClienteCriteria;
import org.west.entidades.ClienteDAO;
import org.west.entidades.Historico;
import org.west.entidades.HistoricoDAO;
import org.west.entidades.Imobiliaria;
import org.west.entidades.ImobiliariaCriteria;
import org.west.entidades.ImobiliariaDAO;
import org.west.entidades.Imovel;
import org.west.entidades.ImovelDAO;
import org.west.entidades.Informacao;
import org.west.entidades.InformacaoCriteria;
import org.west.entidades.PontoCriteria;
import org.west.entidades.PontoDAO;
import org.west.entidades.Portaria;
import org.west.entidades.PortariaDAO;
import org.west.entidades.Usuario;
import org.west.entidades.WestPersistentManager;
import org.west.formulario.FrmMain;
import org.west.formulario.cliente.FrmCliente;
import org.west.formulario.corretor.ClienteFiltro;
import org.west.formulario.portaria.FrmCartao;
import org.west.formulario.recados.FrmRecados;
import org.west.formulario.usuario.FrmSenha;
import org.west.formulario.zap.FrmExportar;
import org.west.utilitarios.DocumentMaxCharacter;
import org.west.utilitarios.RenderImob;
import org.west.utilitarios.Util;

public class FrmRecepcao extends FrmMain {

    private GenericFieldDescriptor descriptorForm;
    private TableFieldDescriptor descriptorTable;
    private JBeanPanel<Imobiliaria> formImobiliaria;
    private JBeanPanel<ClienteFiltro> formBuscaCliente;
    private BeanTableModel<Historico> modeloHistorico;
    private JBeanTable tabelaHistorico;
    private BeanTableModel<Cliente> modeloCliente;
    private JBeanTable tabelaCliente;
    private BeanTableModel<Imobiliaria> modeloImobiliaria;
    private JBeanTable tabelaImobiliaria;
    private JBeanPanel<Imovel> formImovel;
    private JBeanPanel<Portaria> formPortaria;
    private JTabbedPane tabsImovel;
    private PanelImovel panelImoveis;
    private PanelPortaria panelPortaria;
    private JScrollPane scrollTabela;
    private Cliente clienteAtual;

    public FrmRecepcao() {
        super();
        initComponents();

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        GridBagConstraints cons = new GridBagConstraints();
        cons.anchor = GridBagConstraints.NORTHEAST;
        cons.weightx = 1;
        cons.weighty = 0;
        cons.insets = new Insets(5, 5, 5, 5);
        cons.fill = GridBagConstraints.HORIZONTAL;

        cons.gridy = 2;
        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(ClienteFiltro.class, "org/west/xml/buscaClienteRecepcao.xml", "buscaCliente");
        this.formBuscaCliente = new JBeanPanel(ClienteFiltro.class, "Busca de Clientes", this.descriptorForm);
        esquerdo.add(this.formBuscaCliente, cons);

        cons.gridy = 3;
        cons.fill = GridBagConstraints.NONE;
        JActButton buscaCliente = new JActButton("Buscar", new ApplicationAction() {

            @Override
            public void execute() {

                try {

                    ImobiliariaCriteria imobiliariaCriteria = new ImobiliariaCriteria();
                    ClienteCriteria clienteCriteria = imobiliariaCriteria.createClienteCriteria();

                    ClienteFiltro filtro = new ClienteFiltro();
                    formBuscaCliente.populateBean(filtro);

                    String codigo = filtro.getCodigo().toString();

                    if (new Double(codigo) != 0) {
                        clienteCriteria.add(Restrictions.eq("codigo", new Double(codigo).longValue()));
                    } else {
                        if (filtro.getAtendimento() > 0.0) {
                            imobiliariaCriteria.add(Restrictions.eq("codigo", filtro.getAtendimento().longValue()));
                        } else {
                            Date inicio = Util.corrigirDate((Date) filtro.getInicio(), Util.INICIO);
                            Date fim = Util.corrigirDate((Date) filtro.getFim(), Util.FIM);

                            imobiliariaCriteria.add(Restrictions.between("dataEntrada", inicio, fim));

                            if (!filtro.getImovel().isEmpty()) {
                                imobiliariaCriteria.add(Restrictions.ilike("imovel", filtro.getImovel(), MatchMode.ANYWHERE));
                            }
                            if (!filtro.getNome().isEmpty()) {
                                clienteCriteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
                            }
                            if (!filtro.getTelefone().isEmpty()) {
                                clienteCriteria.add(Restrictions.ilike("telefone", filtro.getTelefone(), MatchMode.ANYWHERE));
                            }
                            if (!filtro.getCorretor().isEmpty()) {
                                imobiliariaCriteria.createUsuarioCriteria().add(Restrictions.ilike("nome", filtro.getCorretor(), MatchMode.ANYWHERE));
                            }
                        }
                    }

                    imobiliariaCriteria.addOrder(Order.asc("usuario")).addOrder(Order.desc("prioridade")).addOrder(Order.desc("dataEntrada"));

                    List<Imobiliaria> listaImob = new ArrayList<Imobiliaria>();
                    List<Cliente> listaCli = new ArrayList<Cliente>();

                    for (Imobiliaria imob : imobiliariaCriteria.listImobiliarias()) {

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

                    modeloImobiliaria.setBeanList(new ArrayList<Imobiliaria>());
                    formImobiliaria.setBean(new Imobiliaria());
                    modeloHistorico.setBeanList(new ArrayList<Historico>());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        esquerdo.add(buscaCliente, cons);

        cons.gridy = 4;
        cons.weighty = 1;
        cons.weightx = 1;
        cons.fill = GridBagConstraints.BOTH;

        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Cliente.class, "org/west/xml/clienteTabelaRecepcao.xml", "tabelaCliente");
        this.modeloCliente = new BeanTableModel(this.descriptorTable);
        this.tabelaCliente = new JBeanTable(this.modeloCliente);
        esquerdo.add(new JScrollPane(this.tabelaCliente), cons);

        this.tabelaCliente.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                FrmCliente frmCliente = new FrmCliente(modeloCliente.getBeanAt(tabelaCliente.getSelectedRow()));
                frmCliente.setVisible(true);
            }
        });

        //direito
        cons.gridx = 0;
        cons.gridy = 1;
        cons.gridwidth = 2;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;

        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Imobiliaria.class, "org/west/xml/imobiliariaTabelaRecepcao.xml", "tabelaImobiliaria");
        this.modeloImobiliaria = new BeanTableModel(this.descriptorTable);
        this.tabelaImobiliaria = new JBeanTable(this.modeloImobiliaria);
        direito.add(new JScrollPane(this.tabelaImobiliaria), cons);

        cons.gridy = 2;
        cons.gridwidth = 1;
        cons.ipady = 0;
        cons.weighty = 0;
        cons.weightx = 1;
        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Imobiliaria.class, "org/west/xml/imobiliariaFormRecepcao.xml", "formImobiliaira");
        this.formImobiliaria = new JBeanPanel(Imobiliaria.class, "Atendimento em Imobiliária", this.descriptorForm);
        direito.add(this.formImobiliaria, cons);

        cons.gridx = 1;
        cons.weightx = 1;
        cons.ipadx = 0;
        tabsImovel = new JTabbedPane(JTabbedPane.TOP);

        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Imobiliaria.class, "org/west/xml/imovelCorretor.xml", "corretorImovelleo");
        formImovel = new JBeanPanel(Imovel.class, "Imóvel", this.descriptorForm);
        formImovel.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Portaria.class, "org/west/xml/portariaForm.xml", "portariaForm");
        formPortaria = new JBeanPanel(Portaria.class, "Portaria", this.descriptorForm);
        formPortaria.setBorder(new EmptyBorder(5, 5, 5, 5));

        formPortaria.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    Long codigo = new Long(formPortaria.getPropertyValue("codigo").toString());
                    if (codigo != null && codigo > 0) {
                        panelImoveis.exibeDetalhes(Arrays.asList(ImovelDAO.listImovelByQuery("portaria=" + codigo, "referencia")));
                        jTabbedPane1.setSelectedIndex(1);
                    }
                }
            }
        });

        tabsImovel.addTab("Imóvel", formImovel);
        tabsImovel.addTab("Portaria", formPortaria);

        direito.add(tabsImovel, cons);

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
                                Imovel imovel = ImovelDAO.loadImovelByQuery("referencia=" + textoImovel, "referencia");
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

        cons.gridy = 3;
        cons.gridwidth = 2;
        cons.gridx = 0;

        JPanel panelBotaoImob = new JPanel(new FlowLayout(2, 20, 0));

        ApplicationAction gravaImob = new ApplicationAction() {

            @Override
            public void execute() {
                if (clienteAtual == null) {
                    JOptionPane.showMessageDialog(null, "Não existe um cliente selecionado!", "Erro", 0);
                } else {
                    Imobiliaria imob = new Imobiliaria();

                    if (formImobiliaria.getPropertyValue("codigo") != null) {
                        imob = ImobiliariaDAO.load((Long) formImobiliaria.getPropertyValue("codigo"));
                    } else {
                        imob.setDataEntrada(new Date());
                        imob.setStatus(Imobiliaria.SEM_CIENCIA);
                    }

                    formImobiliaria.populateBean(imob);

                    imob.setCliente(clienteAtual);

                    if (imob.getUsuario() == null) {
                        int choice = JOptionPane.showOptionDialog(null, "Gravar sem usuário?", "Confirmar",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sim", "Não"}, null);

                        if (choice == 0) {
                            save(imob);
                        }
                    } else {
                        if (isLogado(imob.getUsuario())) {
                            save(imob);
                        } else {
                            JOptionPane.showMessageDialog(null, "Usuário não Logado!", "Erro", JOptionPane.ERROR_MESSAGE);

                            DefaultListModel model = new DefaultListModel();

                            for (Usuario usuario : getUsuariosLogados()) {
                                model.addElement(usuario);
                            }

                            JList listaUsuario = new JList(model);

                            int resp = JOptionPane.showOptionDialog(null, new Object[]{"Selecione:", new JScrollPane(listaUsuario)},
                                    "Corretores em Escala", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,
                                    null, new Object[]{"OK", "Cancelar"}, null);

                            if (resp == 0) {
                                formImobiliaria.setPropertyValue("usuario", listaUsuario.getSelectedValue());
                                formImobiliaria.populateBean(imob);
                                save(imob);
                            }
                        }
                    }
                }
            }

            private List<Usuario> getUsuariosLogados() {
                List<Usuario> lista = new ArrayList<Usuario>();
                try {
                    Date dataServidor = PontoDAO.getDateServer();
                    PontoCriteria criteria = new PontoCriteria();
                    criteria.add(Restrictions.between("dataPonto",
                            Util.corrigirDate(dataServidor, Util.INICIO), Util.corrigirDate(dataServidor, Util.FIM)));
                    criteria.createUsuarioCriteria().add(Restrictions.eq("nivel", new Integer(2)));
                    criteria.addOrder(Order.asc("dataPonto"));

                    criteria.setProjection(Projections.distinct(Projections.property("usuario")));

                    for (Object obj : criteria.list()) {
                        lista.add((Usuario) obj);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return lista;
            }

            private void save(Imobiliaria imob) {
                JMSManager.enviarObjectMensagem(imob, imob.getUsuario().getNome(), "cliente");
                ImobiliariaDAO.save(imob);
                carregaCliente();
            }
        };

        ValidationAction validaImob = new ValidationAction(this.formImobiliaria);

        ApplicationAction valida = ActionChainFactory.createChainActions(new ApplicationAction[]{validaImob, gravaImob});

        JActButton botaoGravaImob = new JActButton("Gravar", valida);

        JActButton botaoLimpaImob = new JActButton("Limpar", new ApplicationAction() {

            @Override
            public void execute() {
                Imobiliaria atual = new Imobiliaria();
                formImobiliaria.populateBean(atual);

                if (((atual.getCodigo() == null) && (JOptionPane.showConfirmDialog(null, "Dados sobre atendimento não foram gravados, deseja limpar assim mesmo?", "Objeto não salvo", 2) == JOptionPane.OK_OPTION)) || (atual.getCodigo() != null)) {
                    formImobiliaria.cleanForm();
                    formImovel.cleanForm();
                    formPortaria.cleanForm();
                    tabsImovel.setSelectedIndex(0);
                    modeloHistorico.setBeanList(new ArrayList());
                    WestPersistentManager.clear();
                }
            }
        });
        panelBotaoImob.add(botaoGravaImob);
        panelBotaoImob.add(botaoLimpaImob);

        direito.add(panelBotaoImob, cons);

        cons.gridy = 4;
        cons.weighty = 1;
        cons.gridwidth = 2;

        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Historico.class, "org/west/xml/historicoTabelaRecepcao.xml", "dataEntrada");
        this.modeloHistorico = new BeanTableModel(this.descriptorTable);
        this.tabelaHistorico = new JBeanTable(this.modeloHistorico);
        this.scrollTabela = new JScrollPane(this.tabelaHistorico);

        direito.add(scrollTabela, cons);

        cons.gridy = 5;
        cons.fill = 2;
        cons.weighty = 0;

        JPanel base = new JPanel(new BorderLayout(40, 40));

        JLabel lblDescri = new JLabel("Novo histórico:");
        final JTextField txtOBS = new JTextField(33);
        txtOBS.setDocument(new DocumentMaxCharacter(255));
        JButton btnADD = new JButton("Acrescentar");

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

                    HistoricoDAO.save(novo);
                    ImobiliariaDAO.save(atual);

                    ImobiliariaDAO.lock(atual);

                    String[] usuarios = atual.getUsuario().getNome().split("/");

                    for (String string : usuarios) {
                        JMSManager.enviarObjectMensagem(atual, string, "cliente");
                    }

                    modeloHistorico.addBean(novo);
                    scrollTabela.getVerticalScrollBar().setValue(scrollTabela.getVerticalScrollBar().getMaximum());

                    txtOBS.setText("");
                    WestPersistentManager.clear();
                }
            }
        });

        base.add(lblDescri, BorderLayout.WEST);
        base.add(txtOBS, BorderLayout.CENTER);
        base.add(btnADD, BorderLayout.EAST);

        direito.add(base, cons);

        panelImoveis = new PanelImovel();
        panelPortaria = new PanelPortaria(panelImoveis);

        jTabbedPane1.addTab("Imóveis", new ImageIcon(getClass().getResource("/org/west/imagens/home.png")), panelImoveis);
        jTabbedPane1.addTab("Portarias", new ImageIcon(getClass().getResource("/org/west/imagens/predio.png")), panelPortaria);

        this.tabelaImobiliaria.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                Imobiliaria atual = (Imobiliaria) modeloImobiliaria.getBeanAt(tabelaImobiliaria.getSelectedRow());
                formImobiliaria.setBean(atual);

                modeloHistorico.setBeanList(Arrays.asList(HistoricoDAO.listHistoricoByQuery("imobiliaria=" + atual.getCodigo(), "dataEntrada")));
                scrollTabela.getVerticalScrollBar().setValue(scrollTabela.getVerticalScrollBar().getMaximum());

                carregaImovel(atual);
                WestPersistentManager.clear();
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

        this.tabelaCliente.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (tabelaCliente.getSelectedRow() > -1) {
                    carregaCliente();
                }
            }
        });

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);

                List lista = Arrays.asList(ImobiliariaDAO.listImobiliariaByQuery("usuario is null", "codigo"));

                if (!lista.isEmpty()) {
                    Util.tocarSom("/sons/warn.wav");
                    JOptionPane.showMessageDialog(null, "Existem " + lista.size() + " fichas a serem liberadas!",
                            "Fichas", JOptionPane.INFORMATION_MESSAGE);
                }

                verificarLiberacoes();
            }
        });
    }

    private void verificarLiberacoes() {
        InformacaoCriteria criteria = new InformacaoCriteria();
        criteria.add(Restrictions.isNull("liberado"));
        criteria.createAnuncioCriteria().add(Restrictions.eq("liberavel", true));
        criteria.addOrder(Order.asc("imovel"));

        List<Informacao> listInformacoes = criteria.listInformacoes();

        if (!listInformacoes.isEmpty()) {
            int option = JOptionPane.showConfirmDialog(null, "Existem " + listInformacoes.size() + " solicitações a "
                    + "serem liberados!\nDeseja abri-los agora?", "Liberação", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (option == 0) {
                Set<Imovel> listaImoveis = new HashSet<Imovel>();
                for (Informacao info : listInformacoes) {
                    listaImoveis.add(info.getImovel());
                }

                panelImoveis.getListagemImo().setListagem(new ArrayList<Imovel>(listaImoveis));
            }
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

    private boolean isLogado(Usuario usuario) {
        try {
            if (usuario.isSupervisor() || usuario.getNome().indexOf("/") != -1) {
                return true;
            }

            WestPersistentManager.clear();

            Date dataServidor = PontoDAO.getDateServer();
            PontoCriteria criteria = new PontoCriteria();

            criteria.add(Restrictions.between("dataPonto",
                    Util.corrigirDate(dataServidor, Util.INICIO),
                    Util.corrigirDate(dataServidor, Util.FIM)));

            criteria.add(Restrictions.eq("usuario", usuario));

            return !criteria.list().isEmpty();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void carregaCliente() {
        formImobiliaria.cleanForm();
        formImovel.cleanForm();
        formPortaria.cleanForm();
        modeloHistorico.setBeanList(new ArrayList<Historico>());
        modeloImobiliaria.setBeanList(new ArrayList<Imobiliaria>());

        clienteAtual = (Cliente) modeloCliente.getBeanAt(tabelaCliente.getSelectedRow());

        ClienteDAO.refresh(clienteAtual);

        List<Imobiliaria> imobiliarias = new ArrayList<Imobiliaria>(clienteAtual.getImobiliarias());

        if (imobiliarias.size() > 0) {
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
                    Portaria portaria = PortariaDAO.load(codigo);
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

    @Override
    public void onMessage(Message msg) {
        super.onMessage(msg);

        try {
            if (msg.getStringProperty("tipo").equals("usuario")) {

                List lista = Arrays.asList(ImobiliariaDAO.listImobiliariaByQuery("usuario is null", "codigo"));
                ObjectMessage message = (ObjectMessage) msg;
                Usuario usuarioMsg = (Usuario) message.getObject();

                if (!lista.isEmpty()) {
                    Util.tocarSom("/sons/warn.wav");
                    JOptionPane.showMessageDialog(null, "O usuário " + usuarioMsg.getNome() + " acabou de se logar e existem "
                            + lista.size() + " fichas a serem liberadas!", "Fichas", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabClientes = new javax.swing.JPanel();
        divisor = new javax.swing.JSplitPane();
        divisor.setDividerLocation(0.5);
        esquerdo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtBusca = new javax.swing.JTextField();
        lblConsulta = new javax.swing.JLabel();
        btnGravar = new javax.swing.JButton();
        direito = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema West - Recepção");

        tabClientes.setLayout(new java.awt.BorderLayout());

        divisor.setOneTouchExpandable(true);

        esquerdo.setMinimumSize(new java.awt.Dimension(450, 100));
        esquerdo.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 16)); // NOI18N
        jLabel1.setText("Clientes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        esquerdo.add(jLabel1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Consultar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel3, gridBagConstraints);

        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscaKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(txtBusca, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblConsulta, gridBagConstraints);

        btnGravar.setText("Cadastrar...");
        btnGravar.setEnabled(false);
        btnGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnGravar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        esquerdo.add(jPanel1, gridBagConstraints);

        divisor.setLeftComponent(esquerdo);

        direito.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 16)); // NOI18N
        jLabel2.setText("Atendimentos e Histórico");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        direito.add(jLabel2, gridBagConstraints);

        divisor.setRightComponent(direito);

        tabClientes.add(divisor, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Clientes", new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/clientes.png")), tabClientes); // NOI18N

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Arquivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/Recados.png"))); // NOI18N
        jMenuItem1.setText("Anotar Recados...");
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

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/export.png"))); // NOI18N
        jMenuItem5.setText("Exportar...");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/card.png"))); // NOI18N
        jMenuItem6.setText("Cartões...");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/jornal.png"))); // NOI18N
        jMenuItem4.setText("Anúncios...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F10, 0));
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/liberar.png"))); // NOI18N
        jMenuItem7.setText("Liberar Usuário...");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        jMenuItem8.setText("Verificar Liberações..");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);
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
        FrmRecados frame = new FrmRecados();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        FrmSenha frame = new FrmSenha(this);
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        FrmControlAnuncios anuncios = new FrmControlAnuncios();
        anuncios.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

private void txtBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaKeyReleased
    String str = txtBusca.getText().replace(" ", "");
    if (str.length() % 8 == 0 && str.length() > 0) {
        lblConsulta.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/load_small.gif")));

        new Thread(new Runnable() {

            @Override
            public void run() {

                List<Cliente> listaCliente = ClienteControl.getLista(txtBusca.getText());

                if (listaCliente.isEmpty()) {
                    lblConsulta.setText("Busca não retornou resultados!");
                    btnGravar.setEnabled(true);
                    modeloCliente.setBeanList(new ArrayList<Cliente>());
                } else {
                    lblConsulta.setText("");
                    btnGravar.setEnabled(false);
                    modeloCliente.setBeanList(listaCliente);

                    for (int x = 0; x < tabelaCliente.getColumnModel().getColumnCount(); ++x) {
                        tabelaCliente.getColumnModel().getColumn(x).setCellRenderer(null);
                    }
                }

                lblConsulta.setIcon(null);
            }
        }).start();
    }
}//GEN-LAST:event_txtBuscaKeyReleased

private void btnGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravarActionPerformed
    FrmCliente frmCliente = new FrmCliente(null);
    frmCliente.addTelefones(txtBusca.getText());
    frmCliente.setVisible(true);
}//GEN-LAST:event_btnGravarActionPerformed

private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
    FrmExportar form = new FrmExportar(this, true);
    form.setVisible(true);
}//GEN-LAST:event_jMenuItem5ActionPerformed

private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
    FrmCartao frm = new FrmCartao(this, true);
    frm.setVisible(true);
}//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        FrmLiberarUsuario frm = new FrmLiberarUsuario(this, true);
        frm.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        verificarLiberacoes();
    }//GEN-LAST:event_jMenuItem8ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGravar;
    private javax.swing.JPanel direito;
    private javax.swing.JSplitPane divisor;
    private javax.swing.JPanel esquerdo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblConsulta;
    private javax.swing.JPanel tabClientes;
    private javax.swing.JTextField txtBusca;
    // End of variables declaration//GEN-END:variables
}