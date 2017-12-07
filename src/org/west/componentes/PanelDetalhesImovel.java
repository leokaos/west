package org.west.componentes;

import com.toedter.calendar.JDateChooser;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.swingBean.actions.ActionChainFactory;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.actions.ValidationAction;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanPanel;
import org.west.componentes.email.EmailImovel;
import org.west.componentes.email.EmailManager;
import org.west.entidades.*;
import org.west.formulario.imoveis.FrmFotos;
import org.west.formulario.imoveis.FrmInfoAnuncios;
import org.west.formulario.imoveis.FrmUsuarioImovel;
import org.west.utilitarios.FilterFotos;
import org.west.utilitarios.Util;
import org.west.utilitarios.ValidadorUtil;

public class PanelDetalhesImovel extends JPanel implements ItemListener {

    private Imovel[] listaImovel;
    private Integer position;
    private JBeanPanel<Imovel> formAnuncio;
    private JBeanPanel<Imovel> formImovel;
    private JBeanPanel<Imovel> formImovelEstru;
    private JBeanPanel<Imovel> formPagamento;
    private JBeanPanel<Imovel> formPortaria;
    private JBeanPanel<Portaria> formLazer;
    private PanelEstatistica panelEstatistica;
    private TipValores tip;
    private FrmFotos frmFotos;
    private FrmUsuarioImovel frmUsuario;
    private FrmInfoAnuncios frmAnuncio;
    private final Usuario usuario;

    public PanelDetalhesImovel() {
        initComponents();
        this.position = 0;

        usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        if (this.listaImovel != null && this.listaImovel.length > 0) {
            atualizaTexto();
        }

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(3, 3, 3, 3);
        cons.weightx = 1;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
        cons.anchor = GridBagConstraints.CENTER;

        cons.gridx = 0;
        cons.gridy = 0;
        cons.weighty = 0;
        GenericFieldDescriptor descriptor =
                XMLDescriptorFactory.getFieldDescriptor(Imovel.class, "org/west/xml/imovelFormCorretor.xml", "imovelFormCorretor");
        formImovel = new JBeanPanel<Imovel>(Imovel.class, "Localização do Imóvel", descriptor);
        panelIncluir.add(formImovel, cons);

        cons.gridx = 2;
        cons.ipadx = 0;
        panelEstatistica = new PanelEstatistica(null);
        panelIncluir.add(panelEstatistica, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.gridheight = 3;
        cons.ipadx = 150;
        descriptor = XMLDescriptorFactory.getFieldDescriptor(Imovel.class, "org/west/xml/imovelEstruturaForm.xml", "imovelEstrutura");
        formImovelEstru = new JBeanPanel<Imovel>(Imovel.class, "Características do Imóvel", descriptor);
        panelIncluir.add(formImovelEstru, cons);

        cons.gridy = 3;
        cons.gridx = 1;
        cons.gridwidth = 2;
        cons.weighty = 0;
        cons.gridheight = 1;
        descriptor = XMLDescriptorFactory.getFieldDescriptor(Imovel.class, "org/west/xml/portariaImovel.xml", "portariaImovel");
        formPortaria = new JBeanPanel<Imovel>(Imovel.class, "Portaria", descriptor);
        panelIncluir.add(formPortaria, cons);

        cons.gridx = 1;
        cons.gridy = 1;
        cons.gridwidth = 1;
        descriptor = XMLDescriptorFactory.getFieldDescriptor(Imovel.class, "org/west/xml/portariaImovelLazer.xml", "portariaImovelLazer");
        formLazer = new JBeanPanel<Portaria>(Portaria.class, "Informações da Portaria", descriptor);
        panelIncluir.add(formLazer, cons);

        cons.gridx = 1;
        cons.gridy = 2;
        cons.weightx = 1;
        cons.weighty = 0.4;
        cons.fill = GridBagConstraints.BOTH;
        descriptor = XMLDescriptorFactory.getFieldDescriptor(Imovel.class, "org/west/xml/imovelCorretorPagamento.xml", "imovelCorretorPagamento");
        formPagamento = new JBeanPanel<Imovel>(Imovel.class, "Condições de Pagamento", descriptor);
        panelIncluir.add(formPagamento, cons);

        cons.gridx = 2;
        cons.gridy = 1;
        cons.weighty = 1;
        cons.gridheight = 1;
        cons.gridwidth = 1;
        descriptor = XMLDescriptorFactory.getFieldDescriptor(Imovel.class, "org/west/xml/anuncioImovelForm.xml", "anuncioImovelForm");
        formAnuncio = new JBeanPanel<Imovel>(Imovel.class, "Anunciado em", descriptor);
        panelIncluir.add(formAnuncio, cons);

        configPanel();

        enableAllForms(false);

        ((JComboBox) formImovel.getComponent("tipo")).addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    configPanel();
                }
            }
        });

        ((JPanel) formImovel.getComponent("cep")).getComponent(0).addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                Cep cep = (Cep) formImovel.getPropertyValue("cep");

                if (cep != null) {
                    formImovel.setPropertyValue("logradouro", cep.getTipo() + " " + cep.getDescricao());
                } else {
                    formImovel.setPropertyValue("logradouro", "");
                }
            }
        });

        ((JComboBox) formPortaria.getComponent("portaria")).addItemListener(this);

        formImovel.getComponent("logradouro").addPropertyChangeListener("selecionado", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Cep cep = (Cep) formImovel.getPropertyValue("cep");

                if (cep == null) {
                    cep = (Cep) formImovel.getPropertyValue("logradouro");

                    formImovel.setPropertyValue("logradouro", cep.toString());
                    formImovel.setPropertyValue("cep", cep);
                } else {
                    formImovel.setPropertyValue("logradouro", cep.toString());
                }
            }
        });

        ((JPanel) formImovel.getComponent("numeros")).addPropertyChangeListener("numero", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    List<Numero> listaNumeros = (List<Numero>) formImovel.getPropertyValue("numeros");

                    if (formImovel.getPropertyValue("cep") != null && ValidadorUtil.isNotEmpty(listaNumeros)) {
                        Cep cep = (Cep) formImovel.getPropertyValue("cep");

                        PortariaCriteria criteria = new PortariaCriteria();
                        criteria.add(Restrictions.in("numero", createNumeroParaPortaria()));
                        criteria.createCepCriteria().add(Restrictions.eq("descricao", cep.getDescricao())).add(Restrictions.eq("tipo", cep.getTipo()));

                        Portaria portaria = criteria.uniquePortaria();

                        if (portaria != null) {
                            ((JComboBox) formPortaria.getComponent("portaria")).getModel().setSelectedItem(portaria);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Cep ou número inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            private List<String> createNumeroParaPortaria() {
                List<Numero> numeros = (ArrayList<Numero>) formImovel.getPropertyValue("numeros");
                List<String> valores = new ArrayList<String>();

                for (Numero numero : numeros) {
                    valores.add(String.valueOf(numero.getNumeroPK().getNumero()));
                }

                return valores;
            }
        });

        formImovel.associateAction("bairro", new ApplicationAction() {

            @Override
            public void execute() {
                Bairro bairro = (Bairro) formImovel.getPropertyValue("bairro");
                if (bairro != null) {
                    formImovel.setPropertyValue("cidade", bairro.getCidade());
                }
            }
        });

        formImovelEstru.getComponent("valor").addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                if (tip != null) {
                    tip.setVisible(true);
                    tip.refreshLocation();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

                if (tip != null) {
                    tip.setVisible(false);
                }
            }
        });

        if (usuario.isSupervisor()) {
            lblCaptadoEm.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);

                    JDateChooser dataChooser = new JDateChooser();
                    dataChooser.setDateFormatString("dd/MM/yyyy");

                    JOptionPane.showMessageDialog(null, new Object[]{"Entra com a nova data de captação:", dataChooser});

                    if (dataChooser.getDate() != null) {
                        listaImovel[position].setCaptacao(dataChooser.getDate());
                        setBean(listaImovel[position]);
                    }
                }
            });
        }

        if (usuario.getCodigo().equals(10L)) {
            btnExcluir.setEnabled(true);
        }

        //action
        AbstractAction actionAnterior = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnAnterior.isEnabled()) {
                    mostrarAnterior();
                }
            }
        };

        AbstractAction actionPosterior = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnPosterior.isEnabled()) {
                    mostrarPosterior();
                }
            }
        };

        this.getActionMap().put("anterior", actionAnterior);
        this.getActionMap().put("posterior", actionPosterior);

        this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "anterior");
        this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "posterior");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnAnterior = new javax.swing.JButton();
        lblEndereco = new javax.swing.JLabel();
        btnPosterior = new javax.swing.JButton();
        lblContador = new javax.swing.JLabel();
        btnSalvar = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        lblTipo = new javax.swing.JLabel();
        lblValor = new javax.swing.JLabel();
        panelIncluir = new javax.swing.JPanel();
        panelOpera = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblAtualizadoEm = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblAtualizadoPor = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblCaptadoEm = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblCaptadoPor = new javax.swing.JLabel();
        btnAtualizar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        lblDataEntrada = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnUsuario = new javax.swing.JButton();
        btnFotos = new javax.swing.JButton();
        btnPortaria = new javax.swing.JButton();
        btnAnuncios = new javax.swing.JButton();
        btnLiberar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();

        jLabel8.setText("jLabel8");

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(682, 35));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        btnAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/imovel_esquerda.png"))); // NOI18N
        btnAnterior.setEnabled(false);
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnAnterior, gridBagConstraints);

        lblEndereco.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblEndereco, gridBagConstraints);

        btnPosterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/imovel_direita.png"))); // NOI18N
        btnPosterior.setEnabled(false);
        btnPosterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPosteriorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnPosterior, gridBagConstraints);

        lblContador.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblContador, gridBagConstraints);

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/save.png"))); // NOI18N
        btnSalvar.setToolTipText("Salvar Imóvel Atual");
        btnSalvar.setEnabled(false);
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnSalvar, gridBagConstraints);

        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/imovel_novo.png"))); // NOI18N
        btnNovo.setToolTipText("Novo Imóvel");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnNovo, gridBagConstraints);

        lblTipo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblTipo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblTipo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblValor, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        panelIncluir.setLayout(new java.awt.GridBagLayout());

        panelOpera.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Informações Operacionais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(0, 70, 213))); // NOI18N
        panelOpera.setMinimumSize(new java.awt.Dimension(150, 47));
        panelOpera.setPreferredSize(new java.awt.Dimension(250, 100));
        panelOpera.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel1.setText("Atualizado Em");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelOpera.add(jLabel1, gridBagConstraints);

        lblAtualizadoEm.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelOpera.add(lblAtualizadoEm, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel3.setText("Atualizado Por");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelOpera.add(jLabel3, gridBagConstraints);

        lblAtualizadoPor.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelOpera.add(lblAtualizadoPor, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel5.setText("Primeira Entrada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelOpera.add(jLabel5, gridBagConstraints);

        lblCaptadoEm.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelOpera.add(lblCaptadoEm, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel7.setText("Captado Por");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelOpera.add(jLabel7, gridBagConstraints);

        lblCaptadoPor.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelOpera.add(lblCaptadoPor, gridBagConstraints);

        btnAtualizar.setText("Atualizar");
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelOpera.add(btnAtualizar, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel6.setText("Captado Em");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelOpera.add(jLabel6, gridBagConstraints);

        lblDataEntrada.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelOpera.add(lblDataEntrada, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelIncluir.add(panelOpera, gridBagConstraints);

        jPanel3.setPreferredSize(new java.awt.Dimension(200, 55));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        btnUsuario.setText("Usuários...");
        btnUsuario.setEnabled(false);
        btnUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(btnUsuario, gridBagConstraints);

        btnFotos.setText("Fotos...");
        btnFotos.setEnabled(false);
        btnFotos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotosActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(btnFotos, gridBagConstraints);

        btnPortaria.setText("Exibir Portaria");
        btnPortaria.setEnabled(false);
        btnPortaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPortariaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(btnPortaria, gridBagConstraints);

        btnAnuncios.setText("Anúncios...");
        btnAnuncios.setEnabled(false);
        btnAnuncios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnunciosActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(btnAnuncios, gridBagConstraints);

        btnLiberar.setText("Liberar Anúncios...");
        btnLiberar.setEnabled(false);
        btnLiberar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiberarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(btnLiberar, gridBagConstraints);

        btnExcluir.setText("Excluir");
        btnExcluir.setEnabled(false);
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(btnExcluir, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelIncluir.add(jPanel3, gridBagConstraints);

        add(panelIncluir, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        mostrarAnterior();
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnPosteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPosteriorActionPerformed
        mostrarPosterior();
    }//GEN-LAST:event_btnPosteriorActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        limparImovel();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        salvar(false);
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        salvar(true);
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void btnUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuarioActionPerformed
        if (frmUsuario == null) {
            if (listaImovel != null && listaImovel.length > 0) {
                frmUsuario = new FrmUsuarioImovel((Frame) null, true, listaImovel[position]);
            } else {
                frmUsuario = new FrmUsuarioImovel(null, true, null);
            }
        }

        frmUsuario.setVisible(true);
    }//GEN-LAST:event_btnUsuarioActionPerformed

    private void btnPortariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPortariaActionPerformed
        firePropertyChange("portaria", null, listaImovel[position].getPortaria());
    }//GEN-LAST:event_btnPortariaActionPerformed

    private void btnFotosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotosActionPerformed
        if (frmFotos == null) {
            if (listaImovel != null && listaImovel.length > 0) {
                frmFotos = new FrmFotos(ImovelDAO.load(listaImovel[position].getReferencia()));
            } else {
                frmFotos = new FrmFotos(null);
            }
        }

        frmFotos.setVisible(true);
    }//GEN-LAST:event_btnFotosActionPerformed

    private void btnAnunciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnunciosActionPerformed
        frmAnuncio = new FrmInfoAnuncios(null, true, listaImovel[position]);
        frmAnuncio.setVisible(true);
    }//GEN-LAST:event_btnAnunciosActionPerformed

    private void btnLiberarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiberarActionPerformed

        List<Informacao> infos = listaImovel[position].getInformacoesALiberar();

        JPanel panelAnuncios = new JPanel(new GridLayout(infos.size() + 1, 1, 20, 20));
        List<JCheckBox> listaBoxes = new ArrayList<JCheckBox>();

        panelAnuncios.add(new JLabel("Selecione os anúncios e depois a ação:   "));

        for (Informacao informacao : infos) {
            JCheckBox box = new JCheckBox(informacao.getAnuncio().getNome());
            panelAnuncios.add(box);
            listaBoxes.add(box);
        }

        String[] options = new String[]{"Liberar", "Rejeitar", "Cancelar"};

        int option = JOptionPane.showOptionDialog(null, panelAnuncios, "Controle de Zap",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, "Liberar");

        if (option != 2) {
            WestPersistentManager.clear();
            Imovel imovel = listaImovel[position];
            ImovelDAO.lock(imovel);

            if (option == 0) {

                for (int i = 0; i < listaBoxes.size(); i++) {
                    JCheckBox jCheckBox = listaBoxes.get(i);

                    if (jCheckBox.isSelected()) {
                        Informacao info = infos.get(i);
                        info.setLiberado(Boolean.TRUE);
                        InformacaoDAO.save(info);

                        imovel.getAnuncios().add(info.getAnuncio());
                        ImovelDAO.save(imovel);
                    }
                }
            }

            if (option == 1) {

                for (int i = 0; i < listaBoxes.size(); i++) {
                    JCheckBox jCheckBox = listaBoxes.get(i);

                    if (jCheckBox.isSelected()) {
                        Informacao info = infos.get(i);
                        info.setLiberado(Boolean.FALSE);
                        InformacaoDAO.save(info);

                        imovel.getAnuncios().remove(info.getAnuncio());
                        ImovelDAO.save(imovel);
                    }
                }
            }
            configLiberar(imovel);
            atualizaImovel();
        }
    }//GEN-LAST:event_btnLiberarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        int resp = JOptionPane.showConfirmDialog(null, "Atenção! Operação ireversível, deseja continuar?", "Excluir Imovel", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            Imovel imovel = listaImovel[position];

            WestPersistentManager.clear();
            ImovelDAO.delete(imovel);

            JOptionPane.showMessageDialog(null, "Imóvel excluido com sucesso!");
            limparImovel();
        }
    }//GEN-LAST:event_btnExcluirActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnAnuncios;
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFotos;
    private javax.swing.JButton btnLiberar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPortaria;
    private javax.swing.JButton btnPosterior;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblAtualizadoEm;
    private javax.swing.JLabel lblAtualizadoPor;
    private javax.swing.JLabel lblCaptadoEm;
    private javax.swing.JLabel lblCaptadoPor;
    private javax.swing.JLabel lblContador;
    private javax.swing.JLabel lblDataEntrada;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblValor;
    private javax.swing.JPanel panelIncluir;
    private javax.swing.JPanel panelOpera;
    // End of variables declaration//GEN-END:variables

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {

            if (formImovel.getPropertyValue("referencia") == null) {
                Portaria portaria = (Portaria) formPortaria.getPropertyValue("portaria");

                try {
                    PortariaDAO.lock(portaria);
                    formLazer.setBean(portaria);
                    preencherComPortaria(portaria);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                formImovel.setEnable("numeros", false);
            }
        } else {
            formLazer.cleanForm();
        }
    }

    /**
     * Mostra o imóvel imediatamente anterior ao atual, atualizando os
     * formulários e o texto informativo. O frame cadastra uma action em seu
     * MapAction para executar este método ao se pressionar a tecla esquerda.
     */
    private void mostrarAnterior() {
        if (position > 0) {
            position--;
        }

        atualizaImovel();
    }

    /**
     * Mostra o imóvel imediatamente posterior ao atual, atualizando os
     * formulários e o texto informativo. O frame cadastra uma action em seu
     * MapAction para executar este método ao se pressionar a tecla direita.
     */
    private void mostrarPosterior() {
        if (position < listaImovel.length) {
            position++;
        }

        atualizaImovel();
    }

    /**
     * Preenche os formulários usando as informações da portaria. Preenche os
     * campos: Bairro,Cep, Número, Logradouro,Tipo e Edifício. Se houver Plantas
     * cadastradas, o usuário poderá selecionar uma delas e em seguida executará
     * o método
     * {@link PanelDetalhesImovel#preencherComPlanta(org.west.entidades.Planta)}
     *
     * @param portaria Portaria selecionada pelo usuário ou encontrada pelo
     * sistema ao se digitar o endereço.
     */
    private void preencherComPortaria(Portaria portaria) {
        try {
            WestPersistentManager.getSession();

            PortariaDAO.lock(portaria, LockMode.NONE);

            String[] numerosStr = portaria.getNumero().split("e");
            List<Numero> numeros = new ArrayList<Numero>();

            for (String string : numerosStr) {
                numeros.add(new Numero(null, new Long(string)));
            }

            formImovel.setPropertyValue("numeros", numeros);

            if (!portaria.getPlantas().isEmpty()) {

                formImovel.setPropertyValue("bairro", portaria.getBairro());
                formPortaria.setPropertyValue("portaria", portaria);
                formImovel.setPropertyValue("cep", portaria.getCep());
                formImovel.setPropertyValue("numero", portaria.getNumero());
                formImovel.setPropertyValue("logradouro", portaria.getCep().getTipo() + " " + portaria.getCep().getDescricao());
                formImovelEstru.setPropertyValue("edificio", portaria.getEdificio());

                Set<Planta> plantas = portaria.getPlantas();

                Object valor = JOptionPane.showInputDialog(null,
                        "Existem " + plantas.size() + " plantas:", "Plantas Disponíveis",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        plantas.toArray(), null);

                if (valor != null) {
                    preencherComPlanta((Planta) valor);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Não existem plantas cadastradas nessa portaria!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método responsável pelo preechimento de parte das informações da planta
     * selecionada. Preenche os seguintes campos: Dormitórios, Suites, Salas,
     * Banheiros, Área Privativa e Garagens.
     *
     * @param planta Planta selecionada pelo usuário.
     */
    private void preencherComPlanta(Planta planta) {
        formImovelEstru.setPropertyValue("dorms", planta.getDorms());
        formImovelEstru.setPropertyValue("suites", planta.getSuites());
        formImovelEstru.setPropertyValue("salas", planta.getSalas());
        formImovelEstru.setPropertyValue("banheiros", planta.getBanheiros());
        formImovelEstru.setPropertyValue("privativa", planta.getPrivativa());
        formImovelEstru.setPropertyValue("aptoPorAndar", planta.getAptoPorAndar());
        formImovelEstru.setPropertyValue("andares", planta.getAndares());

        if (planta.getReversivel() > 0) {
            formImovelEstru.setPropertyValue("reversivel", Boolean.TRUE);
        }

        JComponent comp = (JComponent) formImovelEstru.getComponent("garagens");

        if (!comp.getClass().equals(JLabel.class)) {
            ((JFormattedTextField) comp.getComponent(0)).setValue(planta.getVagas());
        }
    }

    /**
     * Configura o estado dos campos Número, Apto, Andar e Bloco, com a
     * finalidade de evitar preechimento incorreto, de acordo com o Tipo do
     * imóvel.
     */
    private void configPanel() {
        formImovel.setEnable("numeros", false);
        formImovel.setEnable("apto", false);
        formImovel.setEnable("andar", false);
        formImovel.setEnable("bloco", false);

        formImovel.setPropertyValue("numeros", null);
        formImovel.setPropertyValue("apto", null);
        formImovel.setPropertyValue("andar", 0);
        formImovel.setPropertyValue("bloco", null);

        if (formImovel.getPropertyValue("tipo") != null) {

            Tipo tipo = (Tipo) formImovel.getPropertyValue("tipo");
            formImovel.setEnable("numeros", true);

            if (tipo.isColetivo()) {
                formImovel.setEnable("apto", true);
                formImovel.setEnable("andar", true);
                formImovel.setEnable("bloco", true);
            }

        }
    }

    /**
     * Modifica o texto descritivo de acordo com o imóvel selecionado,
     * atualizando os labels referentes a Endereço, Tipo e Valor.
     */
    private void atualizaTexto() {
        try {
            Imovel imovel = this.listaImovel[position];

            lblEndereco.setText("Endereço: "
                    + imovel.getCep().getTipo() + " "
                    + imovel.getCep().getDescricao() + ", " + getTextoNumero(imovel));

            lblContador.setText("( " + (position + 1) + " de " + listaImovel.length + " )");

            lblTipo.setText(imovel.getTipo().getTipo());

            NumberFormat nf = NumberFormat.getCurrencyInstance();
            lblValor.setText(nf.format(imovel.getValor()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getTextoNumero(Imovel imovel) {
        ImovelDAO.lock(imovel);

        StringBuilder numeros = new StringBuilder();

        if (ValidadorUtil.isNotEmpty(imovel.getNumeros())) {

            numeros.append("Nº ");

            for (Numero numero : imovel.getNumeros()) {
                numeros.append(numero.getNumero());
                numeros.append(", ");
            }

            String retorno = numeros.toString();

            return retorno.substring(0, retorno.length() - 2);
        }

        return numeros.toString();
    }

    /**
     * Método responsavel por carregar o imóvel atual nos formulários,
     * configurando os botões de navegação e executando o método
     * {@link #atualizaTexto()}
     */
    private void atualizaImovel() {
        try {
            WestPersistentManager.clear();
            Imovel imovel = listaImovel[position];
            ImovelDAO.lock(imovel);

            btnFotos.setText("Ver " + imovel.getFotos().size() + " Fotos");

            formImovel.setBean(imovel);
            formImovelEstru.setBean(imovel);
            formAnuncio.cleanForm();
            formAnuncio.setBean(imovel);
            formPagamento.setBean(imovel);

            if (!usuario.isSupervisor()) {
                formImovel.setEnable("numeros", false);
            }

            if (imovel.getPortaria() != null) {
                formPortaria.setBean(imovel);
                PortariaDAO.lock(imovel.getPortaria());
                formLazer.setBean(imovel.getPortaria());
            } else {
                formLazer.cleanForm();
                formPortaria.cleanForm();
            }

            setBean(imovel);
            panelEstatistica.setImovel(imovel);

            frmFotos = new FrmFotos(imovel);
            frmUsuario = new FrmUsuarioImovel(null, true, imovel);

            btnAnterior.setEnabled(position > 0);
            btnPosterior.setEnabled(position < (listaImovel.length - 1));

            btnPortaria.setEnabled(imovel.getPortaria() != null);

            btnAnuncios.setEnabled(true);

            configLiberar(imovel);

            tip = new TipValores((JComponent) formImovelEstru.getComponent("valor"), "", imovel);
            tip.getStyle().flip(true, false);

            WestPersistentManager.clear();

            atualizaTexto();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void configLiberar(Imovel imovel) {
        btnLiberar.setEnabled(imovel.hasAnunciosLiberaveis() && usuario.isSupervisor());
    }

    /**
     * Faz o preenchimento manual do formulário operacional.
     *
     * @param imovel Imóvel atual para o preechimento.
     */
    private void setBean(Imovel imovel) {
        ImovelDAO.lock(imovel);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        lblAtualizadoPor.setText((imovel.getAtualizadoPor() == null ? "" : imovel.getAtualizadoPor().toString()));
        lblAtualizadoEm.setText((imovel.getAtualizado() == null ? "" : format.format(imovel.getAtualizado())));
        lblCaptadoEm.setText((imovel.getCaptacao() == null ? "" : format.format(imovel.getCaptacao())));
        lblDataEntrada.setText((imovel.getDataEntrada() == null ? "" : format.format(imovel.getDataEntrada())));

        String usuarios = "";

        for (Object obj : imovel.getUsuarios()) {
            usuarios += ((Usuario) obj).getNome() + "/";
        }

        if (!usuarios.isEmpty()) {
            lblCaptadoPor.setText(usuarios.substring(0, usuarios.length() - 1));
        } else {
            lblCaptadoPor.setText("");
        }

        WestPersistentManager.clear();
    }

    /**
     * Reinializa o formulário operacional.
     */
    private void cleanForm() {
        lblAtualizadoEm.setText("");
        lblAtualizadoPor.setText("");
        lblCaptadoEm.setText("");
        lblCaptadoPor.setText("");
        lblDataEntrada.setText("");

        panelEstatistica.cleanPanel();
    }

    /**
     * Este método tem por finalidade validar as informações do imóvel atual
     * verificando as informações adicionadas pelo usuário e efetivamente
     * gravando os dados.
     *
     * @param atualizar true para atualizar a data de atualização, false caso
     * contrário.
     */
    private void salvar(final boolean atualizar) {
        ValidationAction actionValida = new ValidationAction(formImovel, formImovelEstru, formPagamento, formAnuncio);

        ApplicationAction gravaImovel = new ApplicationAction() {

            @Override
            public void execute() {

                Long codigo = (Long) formImovel.getPropertyValue("referencia");
                Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");
                Date dataServer = PontoDAO.getDateServer();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                if (isSaveble(codigo)) {
                    WestPersistentManager.clear();

                    Imovel imovel = new Imovel();

                    if (codigo != null) {
                        imovel = ImovelDAO.load(codigo);
                    } else {
                        imovel.setStatus("Ativo");
                        imovel.setAtualizadoPor(usuario);
                        imovel.setAtualizado(dataServer);
                        imovel.setDataEntrada(dataServer);
                        imovel.setCaptacao(dataServer);
                        imovel.setDestaque(0);
                        imovel.setUsuarios(frmUsuario.getUsuarios());
                    }
                    Double valor = 0.0;

                    if (imovel.getValor() != null) {
                        valor = new Double(imovel.getValor().doubleValue());
                    }

                    if (codigo != null) {
                        ImovelDAO.lock(imovel);

                        try {
                            imovel.setCaptacao(format.parse(lblCaptadoEm.getText()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    checkUpdateAndSendMessage(imovel);

                    formImovel.populateBean(imovel);
                    formImovelEstru.populateBean(imovel);
                    formPagamento.populateBean(imovel);
                    formPortaria.populateBean(imovel);

                    imovel.setDivulgar((Boolean) formAnuncio.getPropertyValue("divulgar"));

                    if (usuario.isSupervisor()) {
                        imovel.setDestaque(new Integer(formAnuncio.getPropertyValue("destaque").toString()));
                        imovel.setUsuarios(frmUsuario.getUsuarios());
                        formAnuncio.populateBean(imovel);

                        Anuncio ipirangaNews = AnuncioDAO.load("Ipiranga News");

                        if (imovel.getAnuncios().contains(ipirangaNews)) {
                            Informacao info = InformacaoDAO.load(imovel, ipirangaNews);

                            if (info != null) {
                                info.setLiberado(Boolean.TRUE);
                                InformacaoDAO.save(info);
                            }
                        }
                    }

                    if (atualizar) {
                        imovel.setAtualizado(dataServer);
                        imovel.setAtualizadoPor(usuario);
                    }

                    if (!imovel.getStatus().equals((Imovel.ATIVO))) {
                        imovel.getAnuncios().clear();
                    }

                    checkPlaca(imovel);

                    consolidarNumeros(imovel);

                    if (ImovelDAO.save(imovel)) {

                        checkPortais(imovel);
                        checkValor(valor, imovel);

                        if (imovel.isRemovido()) {
                            deletePhotosFromFTP(imovel);
                        }

                        if (listaImovel == null) {
                            listaImovel = new Imovel[1];
                        }

                        listaImovel[position] = imovel;
                        atualizaImovel();
                        JOptionPane.showMessageDialog(null, "Imóvel salvo com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao salvar imóvel!");
                    }
                }
            }
        };

        ApplicationAction action = ActionChainFactory.createChainActions(actionValida, gravaImovel);
        action.executeActionChain();
    }

    /**
     * Consolida os numeros existentes no imovel.
     *
     * @param imovel
     */
    private void consolidarNumeros(Imovel imovel) {
        WestPersistentManager.clear();
        Session session = WestPersistentManager.getSession();
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery("delete from tab_numero where imovel = " + imovel.getReferencia());
        query.executeUpdate();
        session.getTransaction().commit();

        for (Numero numero : imovel.getNumeros()) {
            numero.getNumeroPK().setImovel(imovel);
        }
    }

    /**
     * Método responsável por verificar alterações de valor no imóvel.
     *
     * @param valor valor original.
     * @param imovel objeto a ser verificado.
     */
    private void checkValor(Double valor, Imovel imovel) {

        if (valor != null && !valor.equals(imovel.getValor())) {
            Valor novo = new Valor();
            novo.setImovel(imovel);
            novo.setDataAltera(new Date());
            novo.setValor(imovel.getValor());

            ValorDAO.save(novo);
        }
    }

    /**
     * Método responsável por verificar informações correlatas ao imóvel
     * como:<br><br> - Configuração da metragem do imóvel;<br> - Verificação do
     * atributo Usuário do imóvel;<br> - A existência de imóvel de mesmo
     * endereço; - Mudança de situação válida.
     *
     * @param imovel Imóvel a ser verificado pelo método.
     * @return True apenas se todas as verificações acima retornarem corretas.
     */
    private boolean isSaveble(Long codigo) {
        Imovel imovel = new Imovel();
        Usuario usuarioSessao = (Usuario) DesktopSession.getInstance().getObject("usuario");

        if (codigo != null) {
            imovel = ImovelDAO.get(codigo);
        }

        String status = (String) formImovel.getPropertyValue("status");

        if (imovel.getReferencia() != null && !status.equals("Vendido") && imovel.getStatus().equals("Vendido") && !usuarioSessao.isSupervisor()) {
            JOptionPane.showMessageDialog(null, "Mudança de situação do imóvel não permitida!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        formImovel.populateBean(imovel);
        formImovelEstru.populateBean(imovel);

        if (imovel.getTipo().isColetivo()) {
            if (imovel.getPrivativa() == null || imovel.getPrivativa() == 0.0) {
                JOptionPane.showMessageDialog(null, "Área privativa necessária para esse tipo de imóvel!");
                return false;
            }
        } else {
            if (imovel.getTerreno() == null || imovel.getTerreno() == 0.0) {
                JOptionPane.showMessageDialog(null, "Área do terreno e Medidas do terreno são necessárias para esse tipo de imóvel!");
                return false;
            }

            if (imovel.getConstruido() == null || imovel.getConstruido() == 0.0) {
                if (!imovel.getTipo().getTipo().equals("Terreno")) {
                    JOptionPane.showMessageDialog(null, "Área construida necessária para esse tipo de imóvel!");
                    return false;
                }
            }
        }

        if (imovel.getTipo().getExigeAndar()) {
            if (imovel.getAndares() == null || imovel.getAndares() == 0 || imovel.getAptoPorAndar() == null || imovel.getAptoPorAndar() == 0) {
                JOptionPane.showMessageDialog(null, "Número de andares e número de apartamento por andar deve ser preenchido!");
                return false;
            }
        }

        if (frmUsuario == null || frmUsuario.getUsuarios().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preenchimento do campo Usuários incorreto!", "Usuários", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            if (imovel.getReferencia() == null) {
                ImovelCriteria criteria = new ImovelCriteria();
                criteria.createCepCriteria().add(Restrictions.eq("descricao", imovel.getCep().getDescricao()));
                criteria.createNumeroCriteria().add(Restrictions.in("numero", converteNumeroParaLong(imovel.getNumeros())));

                if (imovel.getTipo().isColetivo() && !imovel.getApto().isEmpty()) {
                    criteria.add(Restrictions.eq("apto", imovel.getApto()));
                }

                if (usuarioSessao.isSupervisor() && imovel.getTipo().isColetivo()) {
                    criteria.add(Restrictions.eq("bloco", imovel.getBloco()));
                }

                if (criteria.list().isEmpty()) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Já existe um imóvel nesse endereço!");
                    return false;
                }
            } else {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

    private List<Long> converteNumeroParaLong(Collection<Numero> numeros) {
        List<Long> lista = new ArrayList<Long>();

        for (Numero numero : numeros) {
            lista.add(numero.getNumeroPK().getNumero());
        }

        return lista;
    }

    /**
     * Verifica se o usuario selecionou Placa como anúncio. Tanto supervisor
     * como usuário comum podem usar este recurso.
     *
     * @param imovel Objeto Imovel para adicionar placa.
     */
    private void checkPlaca(Imovel imovel) {
        Set listaAnuncio = (Set) formAnuncio.getPropertyValue("anuncios");

        if (!listaAnuncio.isEmpty()) {
            Anuncio anuncio = AnuncioDAO.load("Placa");

            if (listaAnuncio.contains(anuncio)) {
                imovel.getAnuncios().add(anuncio);
            } else {
                imovel.getAnuncios().remove(anuncio);
            }
        }
    }

    /**
     * Verifica se o usuário selecionou o Zap como anúncio.Caso o usuário for
     * supervisor o anúncio já é liberado, caso contrário o supervisor ainda
     * necessita fazer a liberação.
     *
     * @param imovel Objeto Imovel para conferência.
     */
    private void checkPortais(Imovel imovel) {
        Anuncio zap = AnuncioDAO.load("Zap");
        Anuncio vivaReal = AnuncioDAO.load("Viva Real");
        Anuncio site = AnuncioDAO.load("Site");

        checkAnuncioAndSave(zap, imovel);
        checkAnuncioAndSave(vivaReal, imovel);
        checkAnuncioAndSave(site, imovel);
    }

    private void checkAnuncioAndSave(Anuncio anuncio, Imovel imovel) {
        WestPersistentManager.clear();
        ImovelDAO.lock(imovel);
        AnuncioDAO.lock(anuncio);

        if (imovel.getAnuncios().isEmpty() || !imovel.getAnuncios().contains(anuncio)) {
            Set<Anuncio> listaAnuncio = (Set) formAnuncio.getPropertyValue("anuncios");

            if (!listaAnuncio.isEmpty() && listaAnuncio.contains(anuncio) && imovel.getStatus().equals("Ativo")) {
                Informacao info = InformacaoDAO.get(imovel, anuncio);

                if (info == null) {
                    JOptionPane.showMessageDialog(null, "Inclusão no " + anuncio.getNome() + " não efetuada, pois o "
                            + "imóvel não contém as informações necessárias.", "Erro", JOptionPane.WARNING_MESSAGE);
                } else {
                    info.setLiberado(null);

                    if (!InformacaoDAO.save(info)) {
                        JOptionPane.showMessageDialog(null, "Erro ao verificar entrada no " + anuncio.getNome());
                    }
                }
            }
        }
    }

    /**
     * Metódo executado apenas se o imóvel tiver seu status modificado para
     * Vendido. Ele deleta os registros de fotos e deleta os arquivos do FTP.
     *
     * @param imovel Imóvel para ser tirar fotos do site.
     */
    private void deletePhotosFromFTP(final Imovel imovel) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://mysql01.westguerra.hospedagemdesites.ws/westguerra?autoReconnect=true", "westguerra", "w3s4t5");

                    PreparedStatement state = conn.prepareStatement("delete from westguerra.tab_fotos where imovel = " + imovel.getReferencia());

                    state.executeUpdate();

                    FTPClient ftp = new FTPClient();

                    ftp.connect("ftp.westguerra.com.br");

                    if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {

                        if (ftp.login("westguerra", "w3s4t5")) {

                            String caminhoRemoto = Util.getPropriedade("west.ftp_dir");

                            ftp.changeWorkingDirectory(caminhoRemoto);
                            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

                            List<FTPFile> listaArquivos = Arrays.asList(ftp.listFiles(caminhoRemoto, new FilterFotos(imovel.getReferencia())));

                            for (FTPFile file : listaArquivos) {
                                ftp.deleteFile(caminhoRemoto + file.getName());
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Verifica se existem modificações de valor ou de situação. Caso posivito
     * registra a informação e envia uma mensagem aos angariadores.
     *
     * @param imovel Objeto Imovel para conferência.
     */
    private void checkUpdateAndSendMessage(Imovel imovel) {
        WestPersistentManager.clear();

        if (imovel.getReferencia() != null) {

            String status = (String) formImovel.getPropertyValue("status");
            Double valor = (Double) formImovelEstru.getPropertyValue("valor");
            Boolean divulgar = (Boolean) formAnuncio.getPropertyValue("divulgar");

            List<Altera> listaAltera = new ArrayList<Altera>();

            if (!imovel.getValor().equals(valor)) {
                Altera altera = new Altera();
                altera.setImovel(imovel);
                altera.setDescricao((imovel.getValor() > valor) ? "menor" : "maior");
                AlteraDAO.save(altera);
                listaAltera.add(altera);
            }

            if (!imovel.getStatus().equals(status)) {
                Altera altera = new Altera();
                altera.setImovel(imovel);
                altera.setDescricao("status");
                AlteraDAO.save(altera);
                listaAltera.add(altera);
            }

            if (!imovel.getDivulgar().equals(divulgar)) {
                Altera altera = new Altera();
                altera.setImovel(imovel);
                altera.setDescricao("divulgar");
                AlteraDAO.save(altera);
                listaAltera.add(altera);
            }

            if (!listaAltera.isEmpty()) {
                Usuario usuarioSuper = UsuarioDAO.get(10l);

                for (Usuario usuario : imovel.getUsuarios()) {
                    for (Altera altera : listaAltera) {
                        JMSManager.enviarObjectMensagem(altera, usuario.getNome(), "imovel");
                        JMSManager.enviarObjectMensagem(altera, usuarioSuper.getNome(), "imovel");
                    }
                }

                imovel.setStatus(status);
                imovel.setValor(valor);
                imovel.setDivulgar(divulgar);

                sendEmailToSuper(imovel, listaAltera);
            }

            WestPersistentManager.clear();
        }
    }

    /**
     * Atribui uma lista de imóveis ao componente. Executa os métodos {@link #atualizaImovel()
     * } e {@link #atualizaTexto() }
     *
     * @param lista Lista de imóveis a ser exibida.
     */
    public void setLista(List<Imovel> lista) {
        this.listaImovel = new Imovel[lista.size()];
        int cont = 0;

        for (Iterator<Imovel> it = lista.iterator(); it.hasNext();) {
            Imovel imovel = it.next();
            this.listaImovel[cont] = imovel;
            cont++;
        }

        Usuario atual = (Usuario) DesktopSession.getInstance().getObject("usuario");

        btnSalvar.setEnabled(lista != null);
        btnFotos.setEnabled(lista != null);
        btnUsuario.setEnabled(lista != null && (atual.isSupervisor() || atual.getNivel() == Usuario.RECEPCAO));
        position = 0;
        enableAllForms(true);
        atualizaImovel();
        atualizaTexto();
    }

    /**
     * Habilita ou desabilita todos os forms do cadastro.
     *
     * @param enable : true para habilitar.
     */
    private void enableAllForms(boolean enable) {
        btnAtualizar.setEnabled(enable);
        formImovel.enabledAllComponents(enable);
        formImovelEstru.enabledAllComponents(enable);
        formLazer.enabledAllComponents(enable);
        formPagamento.enabledAllComponents(enable);
        formAnuncio.enabledAllComponents(enable);
        formPortaria.enabledAllComponents(enable);
    }

    private void sendEmailToSuper(final Imovel imovel, final List<Altera> lista) {
        try {
            EmailImovel imovelEmail = new EmailImovel(imovel, lista);
            EmailManager.sendEmail(imovelEmail);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void limparImovel() {
        enableAllForms(true);

        formAnuncio.cleanForm();
        formImovel.cleanForm();
        formImovelEstru.cleanForm();
        formPortaria.cleanForm();
        formPagamento.cleanForm();

        frmFotos = new FrmFotos(null);
        frmUsuario = new FrmUsuarioImovel(null, true, null);

        cleanForm();

        lblContador.setText("( 0 de 0 )");
        lblEndereco.setText("Endereço:");
        lblValor.setText(" ");
        lblTipo.setText(" ");
        btnFotos.setEnabled(true);
        btnSalvar.setEnabled(true);
        btnUsuario.setEnabled(true);
        btnLiberar.setEnabled(false);
        btnAnterior.setEnabled(false);
        btnPosterior.setEnabled(false);

        tip = new TipValores((JComponent) formImovelEstru.getComponent("valor"), "", new Imovel());

        position = 0;

        configPanel();
    }
}
