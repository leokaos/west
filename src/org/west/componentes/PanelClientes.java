package org.west.componentes;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JButton;
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
import org.hibernate.criterion.CriteriaSpecification;
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
import org.west.entidades.Portaria;
import org.west.entidades.PortariaDAO;
import org.west.entidades.Usuario;
import org.west.entidades.WestPersistentManager;
import org.west.formulario.cliente.FrmCliente;
import org.west.formulario.corretor.ClienteFiltro;
import org.west.utilitarios.DocumentMaxCharacter;
import org.west.utilitarios.RenderImob;
import org.west.utilitarios.Util;

public class PanelClientes extends javax.swing.JPanel {

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
    private Usuario usuario;

    public PanelClientes() {
        initComponents();

        usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

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

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        cons.gridy = 1;
        cons.fill = GridBagConstraints.BOTH;
        cons.anchor = GridBagConstraints.EAST;
        cons.weightx = 0;
        cons.weighty = 0;
        cons.ipady = 0;

        JActButton buscaCliente = new JActButton("Buscar", new ApplicationAction() {

            @Override
            public void execute() {

                try {
                    ImobiliariaCriteria imobCriteria = new ImobiliariaCriteria();
                    ClienteCriteria clienteCriteria = imobCriteria.createClienteCriteria();

                    if (!usuario.isSupervisor()) {
                        imobCriteria.createCriteria("usuario").add(Restrictions.like("nome", "%" + usuario.getNome() + "%"));
                    }

                    String codigo = formBuscaCliente.getPropertyValue("codigo").toString();

                    if (new Double(codigo) != 0) {
                        clienteCriteria.add(Restrictions.eq("codigo", new Double(codigo).longValue()));
                    } else {
                        Date inicio = Util.corrigirDate((Date) formBuscaCliente.getPropertyValue("inicio"), Util.INICIO);
                        Date fim = Util.corrigirDate((Date) formBuscaCliente.getPropertyValue("fim"), Util.FIM);

                        imobCriteria.add(Restrictions.between("dataEntrada", inicio, fim));

                        if (!formBuscaCliente.getPropertyValue("imovel").toString().isEmpty()) {
                            imobCriteria.add(Restrictions.like("imovel", "%" + formBuscaCliente.getPropertyValue("imovel").toString() + "%"));
                        }

                        if (!formBuscaCliente.getPropertyValue("nome").toString().isEmpty()) {
                            clienteCriteria.add(Restrictions.like("nome", "%" + formBuscaCliente.getPropertyValue("nome").toString() + "%"));
                        }

                        if (!formBuscaCliente.getPropertyValue("telefone").toString().isEmpty()) {
                            clienteCriteria.createAlias("telefones", "tel", CriteriaSpecification.LEFT_JOIN);

                            clienteCriteria.add(Restrictions.or(
                                    Restrictions.ilike("telefone", formBuscaCliente.getPropertyValue("telefone").toString(), MatchMode.ANYWHERE),
                                    Restrictions.ilike("tel.telefone", formBuscaCliente.getPropertyValue("telefone").toString(), MatchMode.ANYWHERE)));
                        }

                        if (formBuscaCliente.getPropertyValue("acompanhado") != null) {
                            String valor = formBuscaCliente.getPropertyValue("acompanhado").toString();

                            if (valor.equals("Acompanhado")) {
                                imobCriteria.add(Restrictions.eq("acompanhado", true));
                            }

                            if (valor.equals("Não Acompanhado")) {
                                imobCriteria.add(Restrictions.eq("acompanhado", false));
                            }
                        }
                    }

                    imobCriteria.addOrder(Order.desc("prioridade")).addOrder(Order.desc("dataEntrada"));

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

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

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

        if (usuario.isSupervisor()) {
            JPanel panelBotoesSuper = new JPanel(new BorderLayout(20, 0));

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
            panelBotoesSuper.add(btnImob, BorderLayout.EAST);

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

        add(divisor, BorderLayout.CENTER);

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

        formImovel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    Long codigo = new Long(formImovel.getPropertyValue("referencia").toString());
                    if (codigo != null && codigo > 0) {
                        firePropertyChange("panelImovel", null, ImovelDAO.load(codigo));
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
                        List listaImovel = Arrays.asList(ImovelDAO.listImovelByQuery("portaria=" + codigo, "status,atualizado"));
                        firePropertyChange("panelPortaria", null, listaImovel);
                    }
                }
            }
        });

        atualizaLista();
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

            imobCriteria.addOrder(Order.desc("acompanhado")).addOrder(Order.desc("prioridade")).addOrder(Order.asc("status")).addOrder(Order.desc("dataEntrada"));

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}