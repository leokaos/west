package org.west.componentes;

import com.toedter.calendar.JDateChooser;
import org.west.entidades.Tarefa;
import org.west.entidades.TarefaCriteria;
import org.west.entidades.Usuario;
import org.west.entidades.WestPersistentManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.EdgedBalloonStyle;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.swingBean.gui.JBeanPanel;
import org.west.entidades.Cliente;
import org.west.formulario.cliente.FrmCliente;
import org.west.utilitarios.Util;

/**
 *Para facilitar o acompanhamento das tarefas incubidas ao usuário, esta classe cria um modelo de agenda
 * que mostra todas as tarefas do usuário no dia ou semana selecionada, mostrando a data de início, término ou de aviso.
 * @author WestGuerra Ltda.
 */
public class PanelAgenda extends JPanel implements ActionListener {
    
    public static Integer INICIO = 1;
    public static Integer TERMINO = 2;
    public static Integer AVISO = 3;

    private List<Tarefa> tarefas;
    private JScrollPane scroll;
    private Date dataAtual;
    private JButton btnEsquerda;
    private JButton btnDireita;
    private Usuario usuario;
    private Boolean semana;
    private JPanel panelTarefas;
    private JPanel panelData;
    private JRadioButton radioSemana;
    private JRadioButton radioDia;
    private JBeanPanel<Tarefa> panelTarefa;
    private JDateChooser irPara;

    /**
     * <p>Construtor padrão, que recebe um {@link JBeanPanel} do tipo {@link Tarefa}. Este componente é utilizado
     * para exibir a descrição da tarefa ao clicar nela no painel da agenda.</p>
     * <p>Utiliza o usuário logado atualmente para selecionar 
     * as tarefas atuais.</p>
     * @param panelTarefa {@link JBeanPanel} que exibirá os dados da Tarefa.
     */
    public PanelAgenda(JBeanPanel<Tarefa> panelTarefa) {
        this(panelTarefa, (Usuario) DesktopSession.getInstance().getObject("usuario"));
    }

    /**
     * <p>Construtor que recebe um {@link JBeanPanel} do tipo {@link Tarefa} e um {@link Usuario}. O parâmetro é utilizado
     * para exibir a descrição da tarefa ao clicar nela no painel da agenda e o usuário é utilizado para exibir tarefas dos diferentes
     * usuários, para uso gerencial.</p>
     * @param panelTarefa {@link JBeanPanel} que exibirá os dados da Tarefa.
     * @param usuario {@link Usuario} que se deseja exibir as tarefas.
     */
    public PanelAgenda(JBeanPanel<Tarefa> panelTarefa,Usuario usuario){
        setLayout(new BorderLayout(20, 10));
        this.usuario = usuario;
        this.semana = false;
        this.panelTarefa = panelTarefa;
    }

    /**
     * Defini o usuário das tarefas a serem exibidas.
     * @param usuario {@link Usuario} que se deseja exibir as tarefas.
     */
    public void setUsuario(Usuario usuario){
        this.usuario = usuario;               
    }

    /**
     * Constroi a agenda em si. Depois de alterações no modelo, deve-se chamar este método com a finalidade de atualizar a
     * exibição.
     * @param data indica a data das tarefas que serão exibidas na agenda. 
     */
    public void construirPanel(Date data) {
        if (getComponentCount() != 0) {
            removeAll();
            validate();
        }

        this.dataAtual = data;

        this.panelData = new JPanel(new BorderLayout(20, 20));

        ImageIcon iconeCalendario = new ImageIcon(super.getClass().getResource("/org/west/imagens/calenda.png"));
        JLabel lblData = new JLabel(getTextoData(), iconeCalendario, 2);
        lblData.setFont(new Font("Verdana", 0, 14));
        lblData.setIconTextGap(15);

        ImageIcon iconeEsquerda = new ImageIcon(super.getClass().getResource("/org/west/imagens/seta_esquerda.png"));
        this.btnEsquerda = new JButton(iconeEsquerda);
        this.btnEsquerda.setBorder(null);
        this.btnEsquerda.setContentAreaFilled(false);
        this.btnEsquerda.addActionListener(this);

        ImageIcon iconeDireita = new ImageIcon(super.getClass().getResource("/org/west/imagens/seta_direita.png"));
        this.btnDireita = new JButton(iconeDireita);
        this.btnDireita.setBorder(null);
        this.btnDireita.setContentAreaFilled(false);
        this.btnDireita.addActionListener(this);

        JPanel panelBaixo = new JPanel(new BorderLayout(20, 0));

        irPara = new JDateChooser(data);
        irPara.setPreferredSize(new Dimension(125, 23));

        irPara.addPropertyChangeListener("date", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                construirPanel(irPara.getDate());
            }
        });

        JPanel panelIr = new JPanel(new FlowLayout(0, 20, 0));
        panelIr.add(new JLabel("Ir para a data:"));
        panelIr.add(irPara);

        panelBaixo.add(panelIr, BorderLayout.WEST);

        JPanel panelExibi = new JPanel(new FlowLayout(2, 20, 0));

        ButtonGroup grupo = new ButtonGroup();

        this.radioSemana = new JRadioButton("Semana");
        this.radioDia = new JRadioButton("Dia");

        this.radioSemana.addActionListener(this);
        this.radioDia.addActionListener(this);

        if (isSemana())
            this.radioSemana.setSelected(true);
        else
            this.radioDia.setSelected(true);
        
        grupo.add(this.radioSemana);
        grupo.add(this.radioDia);

        panelExibi.add(new JLabel("Exibir por:"));
        panelExibi.add(this.radioSemana);
        panelExibi.add(this.radioDia);

        panelBaixo.add(panelExibi, BorderLayout.EAST);

        this.panelData.add(panelBaixo, BorderLayout.SOUTH);
        this.panelData.add(this.btnEsquerda, BorderLayout.WEST);
        this.panelData.add(this.btnDireita, BorderLayout.EAST);
        this.panelData.add(lblData, BorderLayout.CENTER);

        add(this.panelData, BorderLayout.NORTH);

        this.panelTarefas = new JPanel(new BorderLayout());

        if (!isSemana().booleanValue()) {
            this.panelTarefas.add(construirDia(data), BorderLayout.CENTER);
            this.panelTarefas.add(createHeader(data), BorderLayout.NORTH);
        } else {
            this.panelTarefas.add(createHeaderSemana(data), BorderLayout.NORTH);
            this.panelTarefas.add(contruirSemana(data), BorderLayout.CENTER);
        }

        add(this.panelTarefas, BorderLayout.CENTER);

        validate();
        repaint();

        getBar().setValue(Math.round(getBar().getMaximum() * new GregorianCalendar().get(11) / 24));
    }

    /**
     * Recupera a data selecionada pelo usuário ou pelo sistema.
     * @return data : Data do componente {@link JDateChooser} na parte de cima da agenda.
     */
    public Date getDataAtual(){
        return irPara.getDate();
    }

    /**
     * Retorna o texto utilizado para exibir a data ou a semana atual.
     * @return textoData : se caso a agenda mostra o dia retorna o dia por extenso, caso semana mostra os extremos da semana.
     */
    private String getTextoData() {
        String retorno = "";

        if (isSemana().booleanValue()) {
            SimpleDateFormat sp = new SimpleDateFormat("dd/MM");
            retorno = "Semana de " + sp.format(getComecoSemana(this.dataAtual)) + " a " + sp.format(Util.addDias(getComecoSemana(this.dataAtual), Integer.valueOf(5)));
        } else {
            retorno = dataPorExtenso(this.dataAtual);
        }
        return retorno;
    }

    /**
     * Retorna o cabeçalho de cada dia da agenda.
     * @param data data referente ao dia de exibição.
     * @return header : retorna um {@link JPanel} referente ao cabeçalho.
     */
    private Component createHeaderSemana(Date data) {
        JPanel header = new JPanel(new GridBagLayout());
        data = getComecoSemana(data);

        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = 1;
        cons.weightx = 1.0D;
        cons.weighty = 1.0D;
        cons.gridy = 0;

        for (int x = 1; x <= 6; ++x) {
            cons.gridx = (x - 1);
            if (x == 1) {
                cons.insets = new Insets(0, 50, 0, 0);
            } else if (x == 6) {
                cons.insets = new Insets(0, 0, 0, 20);
            } else {
                cons.insets = new Insets(0, 0, 0, 0);
            }
            header.add(createHeader(data), cons);
            data = Util.addDias(data, Integer.valueOf(1));
        }

        return header;
    }

    /**
     * Retorna se a agenda está no modo semana ou dia.
     * @return semana : retorna true se estiver no modo semana, false caso contrário.
     */
    public Boolean isSemana() {
        return this.semana;
    }

    /**
     * Defini se a agenda está no modo semana ou no modo dia.
     * @param value : true para definir como semana, e false para dia.
     */
    public void setSemana(Boolean value) {
        this.semana = value;
    }

    /**
     * Data um {@link Date}, retorna o início da semana.
     * @param data a data ser computada.
     * @return comecoSemana : data do primeiro dia da semana.
     */
    private Date getComecoSemana(Date data) {
        return Util.addDias(data, Integer.valueOf(-(Util.getCampo(data, 7) - 2)));
    }

    /**
     * Retorna a data por extenso.
     * @param data data a ser escrita por extenso.
     * @return dataPorExtenso : data escrita por extenso.
     */
    private String dataPorExtenso(Date data) {
        String dataPorExtenso = "";
        String[] meses = {"janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};

        GregorianCalendar calendario = new GregorianCalendar();
        calendario.setTime(data);

        dataPorExtenso = dataPorExtenso + String.valueOf(calendario.get(5)) + " de ";

        dataPorExtenso = dataPorExtenso + meses[calendario.get(2)] + " de ";

        dataPorExtenso = dataPorExtenso + String.valueOf(calendario.get(1)) + ".";

        return dataPorExtenso;
    }

    /**
     * Retorna a {@link JScrollBar} da agenda. Este método é utilizado para rolar a agenda até o horário atual.
     * @return jScrollBar : jscrollbar referente ao {@link JPanel} da agenda.
     */
    public JScrollBar getBar() {
        return this.scroll.getVerticalScrollBar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.btnEsquerda) {
            if (isSemana().booleanValue()) {
                this.dataAtual = Util.addDias(this.dataAtual, Integer.valueOf(-7));
            } else {
                this.dataAtual = Util.addDias(this.dataAtual, Integer.valueOf(-1));
            }
        }
        if (e.getSource() == this.btnDireita) {
            if (isSemana().booleanValue()) {
                this.dataAtual = Util.addDias(this.dataAtual, Integer.valueOf(7));
            } else {
                this.dataAtual = Util.addDias(this.dataAtual, Integer.valueOf(1));
            }
        }
        if (e.getSource() == this.radioDia) {
            setSemana(Boolean.valueOf(!this.radioDia.isSelected()));
        }
        if (e.getSource() == this.radioSemana) {
            setSemana(Boolean.valueOf(this.radioSemana.isSelected()));
        }
        construirPanel(this.dataAtual);
    }

    /**
     * Método que constroi o painel da agenda, utilizando a data passada.
     * @param data data do painel.
     * @return panelDia : painel já construido referente ao dia.
     */
    private Component construirDia(Date data) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createLegenda(), BorderLayout.WEST);

        panel.add(getPanelDia(data),BorderLayout.CENTER);

        this.scroll = new JScrollPane(panel);
        this.scroll.getVerticalScrollBar().setUnitIncrement(100);
        return this.scroll;
    }
    
    /**
     * Método que constroi o painel da agenda, utilizando a data passada.
     * @param data data do painel.
     * @return panelDia : painel já construido referente à semana.
     */
    private Component contruirSemana(Date data) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createLegenda(), "West");

        data = getComecoSemana(data);

        JPanel dias = new JPanel(new GridLayout(1, 6));

        for (int x = 2; x < 8; ++x) {
            dias.add(getPanelDia(data));
            data = Util.addDias(data, Integer.valueOf(1));
        }

        panel.add(dias, "Center");

        this.scroll = new JScrollPane(panel);
        return this.scroll;
    }

    /**
     * Dada uma certa data, retorna o painel, com as tarefas da data já incluídas.
     * @param data Data do painel.
     * @return panelDia : {@link JPanel} já populado com as tarefas.
     */
    private JPanel getPanelDia(Date data) {
        JPanel panelDia = new JPanel(new GridBagLayout());

        GridBagConstraints cons = new GridBagConstraints();
        cons.weightx = 1.0D;
        cons.weighty = 1.0D;
        cons.insets = new Insets(0, 0, 0, 0);
        cons.ipadx = 0;
        cons.ipady = 0;
        cons.fill = 1;
        
        this.tarefas = getListEmData(data);

        for (int y = 0; y < 24; ++y) {
            JPanel novo = getNovoPanel();
            cons.gridy = y;
            preencherPanel(novo, getListaHorario(data,y), Integer.valueOf(y));
            panelDia.add(novo, cons);
        }

        return panelDia;
    }

    /**
     * Retorna a string que representa um dia da semana.
     * @param valor o dia da semana.
     * @return diaSemana : dia da semana por extenso.
     */
    private String getDiaSemana(int valor) {
        Map mapa = new HashMap();
        mapa.put(Integer.valueOf(1), "Domingo");
        mapa.put(Integer.valueOf(2), "Segunda-feira");
        mapa.put(Integer.valueOf(3), "Terça-feira");
        mapa.put(Integer.valueOf(4), "Quarta-feira");
        mapa.put(Integer.valueOf(5), "Quinta-feira");
        mapa.put(Integer.valueOf(6), "Sexta-feira");
        mapa.put(Integer.valueOf(7), "Sábado");

        return String.valueOf(mapa.get(Integer.valueOf(valor)));
    }

    /**
     * Retorna uma lista de tarefas que possuem data de início, de término ou de aviso na data informada.
     * @param data a data de referência.
     * @return listaTarefas : lista de tarefas que coincide com o critério.
     */
    private List<Tarefa> getListEmData(Date data) {
        List lista = new ArrayList();
        try {
            Date inicio = Util.corrigirDate(data, Util.INICIO);
            Date fim = Util.corrigirDate(data, Util.FIM);

            TarefaCriteria criteria = new TarefaCriteria();

            Disjunction dis = Restrictions.disjunction();
            dis.add(Restrictions.between("previsaoTermino", inicio, fim));
            dis.add(Restrictions.between("dataInicio", inicio, fim));
            dis.add(Restrictions.between("aviso", inicio, fim));

            criteria.add(dis);
            criteria.add(Restrictions.eq("usuario", usuario));
            criteria.add(Restrictions.eq("terminado",false));
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            criteria.addOrder(Order.asc("dataInicio"));
            criteria.addOrder(Order.asc("previsaoTermino"));

            lista = criteria.list();

            WestPersistentManager.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    /**
     * Preenche o panel, utilizando a lista de tarefas.
     * @param novo painel a ser preenchido.
     * @param listaHorario lista de tarefas que coincide com o horário.
     * @param y indica a hora que o panel representa.
     */
    private void preencherPanel(JPanel novo, List<Tarefa> listaHorario, Integer y) {
        
        for (Iterator it = listaHorario.iterator(); it.hasNext();) {
            Tarefa tarefa = (Tarefa) it.next();
            
            Calendar calenda = new GregorianCalendar(); 
            
            /*calenda.setTime(tarefa.getDataInicio());  
            if (Util.isMesmoDia(dataAtual,tarefa.getDataInicio()) && y==calenda.get(Calendar.HOUR_OF_DAY))
                novo.add(getLabelTarefa(tarefa, INICIO));
            
            calenda.setTime(tarefa.getPrevisaoTermino());
            if (Util.isMesmoDia(dataAtual,tarefa.getPrevisaoTermino()) && y==calenda.get(Calendar.HOUR_OF_DAY))
                novo.add(getLabelTarefa(tarefa, TERMINO));*/
            
            calenda.setTime(tarefa.getAviso());
            if (Util.isMesmoDia(dataAtual,tarefa.getAviso()) && y==calenda.get(Calendar.HOUR_OF_DAY))
                novo.add(getLabelTarefa(tarefa, AVISO));   
        }
    }
    
    /**
     * Retorna um JLabel preechido com o ícone e texto de uma determinada tarefa.
     * @param tarefa Tarefa a ser colocada no JLabel.
     * @param tipo se a data é a de início, término ou de aviso.
     * @return label : JLabel já configurado.
     */
    private JLabel getLabelTarefa(final Tarefa tarefa,Integer tipo){
               
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
        Font ftLabel = new Font("Verdana", Font.PLAIN, 10);
        
        JLabel lblNovo = new JLabel();
        lblNovo.setBorder(new EmptyBorder(3, 3, 3, 3));
        
        lblNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblNovo.setFont(ftLabel);  
        lblNovo.setPreferredSize(new Dimension(30, 30));
        
        final BalloonTip tip = getBallon(tarefa, lblNovo);

        lblNovo.addMouseListener(new MouseAdapter() {
            
            private Boolean clicado = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                
                if (e.getClickCount() == 1){
                    tip.setVisible(true);
                    clicado = !clicado;
                }
                
                if (e.getClickCount() == 2)
                    panelTarefa.setBean(tarefa);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                Double largura = Toolkit.getDefaultToolkit().getScreenSize().width * 0.9;
                Double altura = Toolkit.getDefaultToolkit().getScreenSize().height * 0.9;

                if (e.getYOnScreen() + tip.getHeight() > altura.intValue()) {
                    EdgedBalloonStyle style = (EdgedBalloonStyle) tip.getStyle();
                    style.flipY(false);
                } else {
                    EdgedBalloonStyle style = (EdgedBalloonStyle) tip.getStyle();
                    style.flipY(true);
                }

                if (e.getXOnScreen() + tip.getWidth() > largura.intValue()) {
                    EdgedBalloonStyle style = (EdgedBalloonStyle) tip.getStyle();
                    style.flipX(true);
                } else {
                    EdgedBalloonStyle style = (EdgedBalloonStyle) tip.getStyle();
                    style.flipX(false);
                }
                
                if (!tip.isVisible())
                    clicado = false;

                tip.refreshLocation();
                tip.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                
                if (!clicado)
                    tip.setVisible(false);
            }
        });     
              
        if (tipo == INICIO){
            lblNovo.setText(formato.format(tarefa.getDataInicio()) + ": " + tarefa.getNome());
            lblNovo.setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/data_inicial.png")));      
        }
        
        if (tipo == TERMINO){
            lblNovo.setText(formato.format(tarefa.getPrevisaoTermino()) + ": " + tarefa.getNome());
            lblNovo.setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/data_final.png")));            
        }
        
        if (tipo == AVISO){
            lblNovo.setText(formato.format(tarefa.getAviso()) + ": " + tarefa.getNome());
            lblNovo.setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/data_aviso.png")));            
        }
        
        return lblNovo;
    }

    /**
     * Retorna todas as tarefas que tem a data de início, término ou de aviso na hora indicada.
     * @param data data do panel de origem.
     * @param y hora a ser considerada.
     * @return listaTarefas : tarefas que estão no critério.
     */
    private List<Tarefa> getListaHorario(Date data,Integer y) {
        List atual = new ArrayList();

        Calendar calenda = new GregorianCalendar();

        for (Iterator it = this.tarefas.iterator(); it.hasNext();) {
            Tarefa tarefa = (Tarefa) it.next();

            calenda.setTime(tarefa.getDataInicio());
            if (Util.isMesmoDia(data,tarefa.getDataInicio()) && y==calenda.get(Calendar.HOUR_OF_DAY))
                atual.add(tarefa);
            else{
                calenda.setTime(tarefa.getPrevisaoTermino());
                if (Util.isMesmoDia(data,tarefa.getPrevisaoTermino()) && y==calenda.get(Calendar.HOUR_OF_DAY))
                    atual.add(tarefa);
                else{
                    calenda.setTime(tarefa.getAviso());
                    if (Util.isMesmoDia(data,tarefa.getAviso()) && y==calenda.get(Calendar.HOUR_OF_DAY))
                        atual.add(tarefa);                     
                }               
            }             
        }
        return atual;
    }

    /**
     * Retorna um novo panel da agenda.
     * @return panel : panel da agenda.
     */
    private JPanel getNovoPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 0));
        Dimension tamanho = new Dimension(0, 100);
        panel.setMinimumSize(tamanho);
        panel.setPreferredSize(tamanho);
        panel.setMaximumSize(tamanho);

        panel.setBorder(BorderFactory.createEtchedBorder(0));
        return panel;
    }

    /**
     * Retorna um {@link JLabel} com a hora indicada.
     * @param hora hora indicada.
     * @return labelNovo : JLabel já preenchido.
     */
    private JLabel getLabel(Integer hora) {
        JLabel novo = new JLabel(String.valueOf(hora));
        return novo;
    }

    /**
     * Cria o label da legenda dos horários.
     * @return labelHorario : JLabel com o horario.
     */
    private JPanel createLegenda() {
        JPanel labelHorario = new JPanel(new GridBagLayout());

        GridBagConstraints cons = new GridBagConstraints();
        cons.weightx = 1.0D;
        cons.weighty = 1.0D;
        cons.insets = new Insets(0, 0, 0, 0);
        cons.ipadx = 0;
        cons.ipady = 0;
        cons.fill = 1;

        Font font = new Font("Verdana", 1, 16);

        for (int y = 0; y < 24; ++y) {
            cons.gridy = y;
            JLabel hora = getLabel(Integer.valueOf(y));
            hora.setPreferredSize(new Dimension(50, 50));
            hora.setVerticalAlignment(0);
            hora.setHorizontalTextPosition(0);
            hora.setFont(font);
            labelHorario.add(hora, cons);
        }

        return labelHorario;
    }

    /**
     * Cria o header do painel do dia com as tarefas.
     * @param data data a que se refere o cabeçalho.
     * @return header : painel já configurado.
     */
    private JPanel createHeader(Date data) {
        JPanel header = new JPanel(new BorderLayout());
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM");
        JLabel label = new JLabel(getDiaSemana(Util.getCampo(data, 7)) + " (" + sp.format(data) + ")");
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setPreferredSize(new Dimension(100, 20));
        label.setHorizontalAlignment(0);
        header.add(label, "Center");
        return header;
    }

    /**
     * Cria um {@link BalloonTip} com as informações da tarefa.
     * @param tarefa Tarefa a ser exibida.
     * @param label Componente em que estará atrelado o ballontip.
     * @return tip : BallonTip preenchido.
     */
    private BalloonTip getBallon(Tarefa tarefa, JLabel label) {
        BalloonTip tip = new BalloonTip(label,"");
        tip.setVisible(false);
        tip.enableClickToHide(true);
        tip.removeAll();

        tip.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        cons.gridx = 0;
        cons.gridy = 0;
        cons.insets = new Insets(10,10,10,10);
        cons.anchor = GridBagConstraints.LINE_START;

        tip.add(new JLabel("Data de Término: " + format.format(tarefa.getPrevisaoTermino())), cons);        

        cons.gridy = GridBagConstraints.RELATIVE;
        
        tip.add(new JLabel("Serviço: " + tarefa.getServico().toString()),cons);
        
        tip.add(new JLabel("Compradores:"),cons);
        
        for(Cliente cliente : tarefa.getServico().getCompradores())
            tip.add(getLabelClientes(cliente),cons);
        
        tip.add(new JLabel("Vendedores"),cons);
        
        for(Cliente cliente : tarefa.getServico().getVendedores())
            tip.add(getLabelClientes(cliente),cons);

        tip.setStyle(new EdgedBalloonStyle(new Color(236, 241, 140),new Color(161, 241, 140)));

        return tip;
    }
    
    private JLabel getLabelClientes(final Cliente cliente){
        JLabel label = new JLabel(cliente.getNome());
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setForeground(new Color(110,133,184));
        
        label.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1){
                    FrmCliente frmCliente = new FrmCliente(cliente);
                    frmCliente.setVisible(true);
                }
            }
            
        });
        
        return label;
    }
}