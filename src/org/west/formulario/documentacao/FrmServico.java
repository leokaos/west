package org.west.formulario.documentacao;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
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
import org.west.componentes.DesktopSession;
import org.west.componentes.JRCapaServico;
import org.west.componentes.JSugestion;
import org.west.entidades.Cliente;
import org.west.entidades.PontoDAO;
import org.west.entidades.Servico;
import org.west.entidades.ServicoDAO;
import org.west.entidades.Tarefa;
import org.west.entidades.TarefaDAO;
import org.west.entidades.TipoServico;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioDAO;
import org.west.entidades.WestPersistentManager;
import org.west.formulario.cliente.FrmCliente;

public class FrmServico extends javax.swing.JDialog {

    private FrmDocumentos origem;
    private JBeanPanel formServico;
    private Servico servicoAtual;
    private JSugestion comprador;
    private BeanTableModel<Cliente> modelComprador;
    private JBeanTable tabelaComprador;
    private JSugestion vendedor;
    private BeanTableModel<Cliente> modelVendedor;
    private JBeanTable tabelaVendedor;

    public FrmServico(java.awt.Frame parent, boolean modal, Servico servico) {
        super(parent, modal);
        initComponents();

        setSize(900, 600);
        setLocationRelativeTo(null);

        this.servicoAtual = servico;
        this.origem = ((FrmDocumentos) parent);

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.weightx = 0;
        cons.weighty = 1;

        GenericFieldDescriptor descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Servico.class, "org/west/xml/servicoFormConstrutora.xml", "servico");
        formServico = new JBeanPanel(Servico.class, "Informações de Serviço", descriptorForm);
        formServico.setBorder(null);

        Usuario usuario = DesktopSession.getInstance().getObjetoSessao("usuario");

        if (servicoAtual.isNovo()) {
            servicoAtual.setResponsavel(usuario);

            formServico.setEnable("responsavel", false);

            if (usuario.isCoordenador()) {
                formServico.setEnable("responsavel", true);
                formServico.setEnable("tipo", true);
            }
        } else {
            formServico.setEnable("responsavel", false);
            formServico.setEnable("tipo", false);
        }

        cons.gridx = 0;
        cons.gridy = 0;
        cons.gridheight = 4;
        cons.fill = GridBagConstraints.BOTH;
        this.add(formServico, cons);

        cons.gridx = 1;
        cons.gridheight = 1;
        cons.weighty = 0;
        cons.weightx = 0;
        cons.fill = GridBagConstraints.BOTH;
        this.add(new JLabel("Compradores:"), cons);

        cons.gridx = 2;
        cons.weightx = 1;
        cons.fill = GridBagConstraints.HORIZONTAL;
        comprador = new JSugestion("org.west.entidades.Cliente", "CPF");
        comprador.addQueryField("nome");
        comprador.addQueryField("telefones.telefone");
        comprador.setOrder("nome");
        this.add(comprador, cons);

        comprador.addPropertyChangeListener("selecionado", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Cliente cliente = (Cliente) comprador.getValue();
                servicoAtual.getCompradores().add(cliente);
                modelComprador.setBeanList(new ArrayList(servicoAtual.getCompradores()));
            }
        });

        TableFieldDescriptor descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Servico.class, "org/west/xml/clienteServicos.xml", "servicoComprador");
        modelComprador = new BeanTableModel<Cliente>(descriptorTable);
        tabelaComprador = new JBeanTable(modelComprador);

        tabelaComprador.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                if (tabelaComprador.getSelectedRow() > -1) {
                    FrmCliente frmCliente = new FrmCliente(modelComprador.getBeanAt(tabelaComprador.getSelectedRow()));
                    frmCliente.setVisible(true);
                }
            }
        });

        cons.gridx = 1;
        cons.gridy = 1;
        cons.weighty = 1;
        cons.weightx = 1;
        cons.fill = GridBagConstraints.BOTH;
        cons.gridwidth = 2;
        this.add(new JScrollPane(tabelaComprador), cons);

        cons.gridx = 1;
        cons.gridy = 2;
        cons.gridheight = 1;
        cons.weighty = 0;
        cons.weightx = 0;
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.NONE;
        this.add(new JLabel("Vendedores:"), cons);

        cons.gridx = 2;
        cons.weightx = 1;
        cons.fill = GridBagConstraints.HORIZONTAL;
        vendedor = new JSugestion("org.west.entidades.Cliente", "CPF");
        vendedor.addQueryField("nome");
        vendedor.setOrder("nome");
        this.add(vendedor, cons);

        vendedor.addPropertyChangeListener("selecionado", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Cliente cliente = (Cliente) vendedor.getValue();
                servicoAtual.getVendedores().add(cliente);
                modelVendedor.setBeanList(new ArrayList(servicoAtual.getVendedores()));
            }
        });

        descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Servico.class, "org/west/xml/clienteServicos.xml", "servicoVendedor");
        modelVendedor = new BeanTableModel<Cliente>(descriptorTable);
        tabelaVendedor = new JBeanTable(modelVendedor);

        tabelaVendedor.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                if (tabelaVendedor.getSelectedRow() > -1) {
                    FrmCliente frmCliente = new FrmCliente(modelVendedor.getBeanAt(tabelaVendedor.getSelectedRow()));
                    frmCliente.setVisible(true);
                }
            }
        });

        cons.gridx = 1;
        cons.gridy = 3;
        cons.weighty = 1;
        cons.weightx = 1;
        cons.fill = GridBagConstraints.BOTH;
        cons.gridwidth = 2;
        this.add(new JScrollPane(tabelaVendedor), cons);

        cons.gridx = 0;
        cons.gridy = 4;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.gridwidth = 3;
        cons.weightx = 1;
        cons.weighty = 0;

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.add(panelBotoes, cons);

        ApplicationAction gravaServico = new ApplicationAction() {

            @Override
            public void execute() {

                formServico.populateBean(servicoAtual);
                servicoAtual.setCompradores(new HashSet<Cliente>(modelComprador.getCompleteList()));
                servicoAtual.setVendedores(new HashSet<Cliente>(modelVendedor.getCompleteList()));

                if (servicoAtual.getCodigo() == null) {
                    servicoAtual.setDataEntrada(new Date());
                }

                if (servicoAtual.getCompradores().size() <= 0) {
                    JOptionPane.showMessageDialog(null, "Deve haver pelo menos 1 comprador associado ao Serviço!");
                    return;
                }

                servicoAtual.setPastaMae(servicoAtual.getPastaMae().toUpperCase());

                if (ServicoDAO.save(servicoAtual)) {

                    // Início do teste

                    if (servicoAtual.getTipoServico().isGeraTarefaComissao()) {
                        Tarefa tarefa = new Tarefa();

                        Date dataAtual = PontoDAO.getDateServer();
                        Date dataFim = DateUtil.add(dataAtual, 5, Calendar.DAY_OF_YEAR);
                        Date dataAviso = DateUtil.add(dataFim, -30, Calendar.MINUTE);

                        tarefa.setDataInicio(dataAtual);
                        tarefa.setDataAtualizacao(dataAtual);
                        tarefa.setPrevisaoTermino(dataFim);
                        tarefa.setAviso(dataAviso);

                        tarefa.setDescricao("Verificar comissão referente ao serviço " + servicoAtual.getCodigo().toString());

                        tarefa.setUsuario(UsuarioDAO.get(56L));
                        tarefa.setNome("Comissão");
                        tarefa.setValor(new Double(0.0));
                        tarefa.setServico(servicoAtual);
                        tarefa.setTerminado(false);

                        TarefaDAO.save(tarefa);
                    }

                    // Fim do teste


                    ServicoDAO.refresh(servicoAtual);
                    formServico.setBean(servicoAtual);
                    JOptionPane.showMessageDialog(null, "Serviço atualizado com sucesso!");

                    int resp = JOptionPane.showOptionDialog(null, "Deseja imprimir a capa do serviço?", "Impressão",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sim", "Não"}, null);

                    if (resp == 0) {
                        gerarRelatorio();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao atualizar serviço!");
                }

                WestPersistentManager.clear();

                if (origem != null) {
                    origem.carregaTarefas(origem.getListaTarefas(""));
                    origem.reload();
                }
            }
        };

        ValidationAction servicoAction = new ValidationAction(formServico);

        ApplicationAction clienteValida = ActionChainFactory.createChainActions(servicoAction, gravaServico);

        JActButton btnGravar = new JActButton("Gravar", clienteValida);
        btnGravar.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/save.png")));

        panelBotoes.add(btnGravar);

        JActButton btnLimpar = new JActButton("Limpar", new ApplicationAction() {

            @Override
            public void execute() {
                formServico.cleanForm();
            }
        });

        btnLimpar.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/limpar.png")));
        panelBotoes.add(btnLimpar);

        JActButton btnImprimir = new JActButton("Imprimir", new ApplicationAction() {

            @Override
            public void execute() {
                gerarRelatorio();
            }
        });
        btnImprimir.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/print.png")));
        panelBotoes.add(btnImprimir);

        JActButton btnNovoCliente = new JActButton("Novo Cliente...", new ApplicationAction() {

            @Override
            public void execute() {
                FrmCliente frmCliente = new FrmCliente();
                frmCliente.setVisible(true);
            }
        });
        btnNovoCliente.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/add_cliente.png")));
        panelBotoes.add(btnNovoCliente);

        formServico.setBean(servicoAtual);

        if (!servicoAtual.isNovo()) {
            ServicoDAO.lock(servico);
            modelComprador.setBeanList(new ArrayList<Cliente>(servicoAtual.getCompradores()));
            modelVendedor.setBeanList(new ArrayList<Cliente>(servicoAtual.getVendedores()));
        }
    }

    private void gerarRelatorio() {
        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/org/west/relatorios/relatorio_capa.jasper"));

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Serviço");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}