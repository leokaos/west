package org.west.componentes;

import org.west.entidades.PlantaCriteria;
import org.west.entidades.PortariaCriteria;
import org.west.entidades.PortariaDAO;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.west.componentes.tabelafilter.HeaderOrder;
import org.west.componentes.tabelafilter.HeaderRender;
import org.west.componentes.tabelafilter.ModelPortaria;
import org.west.entidades.ImovelDAO;
import org.west.entidades.Portaria;
import org.west.entidades.Usuario;
import org.west.entidades.WestPersistentManager;
import org.west.formulario.portaria.FrmPortaria;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.EdgedBalloonStyle;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.swingBean.gui.EmptyNumberFormatterNoLetter;
import org.west.entidades.ImovelCriteria;
import org.west.utilitarios.RenderListaPortaria;
import org.west.utilitarios.Util;

public class PanelPortaria extends javax.swing.JPanel {

    private PanelImovel panelImovel;

    public PanelPortaria(PanelImovel panel) {
        initComponents();

        this.panelImovel = panel;

        tabela.getTableHeader().setDefaultRenderer(new HeaderRender(new HashMap<Integer, Integer>(), new HashMap<Integer, List<String>>()));
        tabela.setDefaultRenderer(Object.class, new RenderListaPortaria(null));

        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabela.repaint();
        tabela.getTableHeader().setReorderingAllowed(false);

        tabela.addMouseMotionListener(new MouseMotionAdapter() {
            private BalloonTip tip = null;
            private int linha = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                int coluna = tabela.columnAtPoint(e.getPoint());

                if (coluna == 0) {
                    tabela.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    int linhaAtual = tabela.rowAtPoint(e.getPoint());

                    if (tip == null) {
                        tip = new BalloonTip(tabela, "");
                        tip.setStyle(new EdgedBalloonStyle(new Color(236, 241, 140), new Color(161, 241, 140)));
                    }

                    tip = configTip(linhaAtual, tip);

                    if (linha != linhaAtual) {

                        linha = linhaAtual;
                        Point p = e.getLocationOnScreen();
                        p.y -= 30;
                        Point s = jScrollPane1.getLocationOnScreen();

                        EdgedBalloonStyle style = (EdgedBalloonStyle) tip.getStyle();

                        if ((p.y - s.y) + tip.getHeight() > jScrollPane1.getHeight()) {
                            p.y -= tip.getHeight() + 30;
                            style.flipY(false);
                        } else {
                            style.flipY(true);
                        }

                        tip.setLocation(p);
                    }

                    tip.setVisible(true);
                } else {
                    tabela.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                    if (tip != null) {
                        tip.setVisible(false);
                    }
                }
            }
        });

        final JPopupMenu popupMenu = new JPopupMenu("Ações");

        JMenuItem itemPrioridade = new JMenuItem("Marcar como prioridade", new ImageIcon(getClass().getResource("/org/west/imagens/exclamation.png")));
        itemPrioridade.setIconTextGap(5);

        itemPrioridade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Portaria portaria = (Portaria) ((ModelPortaria) tabela.getModel()).getRow(tabela.getSelectedRow());

                Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

                if (portaria.getUsuario().equals(usuario)) {
                    portaria.setPrioridade(true);
                    PortariaDAO.save(portaria);
                } else {
                    JOptionPane.showMessageDialog(null, "Essa portaria não pertence ao usuário atual!");
                }
            }
        });

        JMenuItem itemNaoPrioridade = new JMenuItem("Marcar como normal");
        itemNaoPrioridade.setIconTextGap(5);

        itemNaoPrioridade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Portaria portaria = (Portaria) ((ModelPortaria) tabela.getModel()).getRow(tabela.getSelectedRow());

                Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

                if (portaria.getUsuario().equals(usuario)) {
                    portaria.setPrioridade(false);
                    PortariaDAO.save(portaria);
                } else {
                    JOptionPane.showMessageDialog(null, "Essa portaria não pertence ao usuário atual!");
                }
            }
        });

        JMenuItem itemContem = new JMenuItem("Contém...");
        itemContem.setIconTextGap(5);

        itemContem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscaContem();
            }
        });

        JMenuItem itemIncluir = new JMenuItem("Incluir...");
        itemIncluir.setIconTextGap(5);

        itemIncluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmPortaria frm = new FrmPortaria(null, true, null);
                frm.setVisible(true);
            }
        });

        Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        itemIncluir.setEnabled((usuario.getNivel() == 1) || (usuario.getNivel() == 2 && usuario.isSupervisor()));

        JMenuItem itemEditar = new JMenuItem("Editar", new ImageIcon(getClass().getResource("/org/west/imagens/edit.png")));
        itemEditar.setIconTextGap(5);

        itemEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelPortaria model = (ModelPortaria) tabela.getModel();
                FrmPortaria frm = new FrmPortaria(null, true, model.getRow(tabela.getSelectedRow()));
                frm.setVisible(true);
            }
        });

        itemEditar.setEnabled((usuario.getNivel() == 1) || (usuario.getNivel() == 2 && usuario.isSupervisor()));

        JMenuItem itemImprimir = new JMenuItem("Imprimir...", new ImageIcon(getClass().getResource("/org/west/imagens/print.png")));
        itemImprimir.setIconTextGap(5);

        itemImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelPortaria model = (ModelPortaria) tabela.getModel();
                List lista = new ArrayList();

                for (int index : tabela.getSelectedRows()) {
                    lista.add(model.getRow(index));
                }

                try {
                    JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/org/west/relatorios/relatorio_portaria.jasper"));
                    JRHibernateSource source = new JRHibernateSource(lista);
                    JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), source);
                    JasperViewer.viewReport(print, false);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        popupMenu.add(itemPrioridade);
        popupMenu.add(itemNaoPrioridade);
        popupMenu.addSeparator();
        popupMenu.add(itemContem);
        popupMenu.addSeparator();
        popupMenu.add(itemIncluir);
        popupMenu.add(itemEditar);
        popupMenu.addSeparator();
        popupMenu.add(itemImprimir);

        tabela.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (e.getKeyCode() == KeyEvent.VK_F && e.getModifiers() == KeyEvent.CTRL_MASK) {
                    buscaContem();
                }
            }
        });

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getButton() == 3) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }

                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    panelImovel.exibeDetalhes(Arrays.asList(ImovelDAO.listImovelByQuery(
                            "portaria = " + tabela.getValueAt(tabela.getSelectedRows()[0], 0), "status,atualizado desc")));
                }
            }
        });

        dtInicio.setDate(Util.getExtremidadesMes(new Date(), Util.INICIO));
        dtFinal.setDate(Util.getExtremidadesMes(new Date(), Util.FIM));
    }

    private void buscaContem() {
        final Object obj = JOptionPane.showInputDialog(null, "Digite a busca:", "Busca", JOptionPane.INFORMATION_MESSAGE);

        if (obj != null && !obj.toString().isEmpty()) {
            ModelPortaria model = ((ModelPortaria) tabela.getModel());
            model.filterContains(obj.toString());
            tabela.setDefaultRenderer(Object.class, new RenderListaPortaria(obj.toString()));
        }
    }

    private void setSizes() {
        int[] tamanhos = {90, 50, 360, 100, 200, 110, 110, 150, 110, 165, 200, 160, 165};
        for (int x = 0; x < tabela.getColumnCount(); x++) {
            if (tabela.getColumnName(x).equals("Visita")) {
                tabela.getColumnModel().getColumn(x).setPreferredWidth(105);
            } else {
                ModelPortaria modelo = (ModelPortaria) tabela.getModel();
                if (x <= 7) {
                    tabela.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
                }

                if (x > 7 + modelo.getMaxVisitas()) {
                    tabela.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x - modelo.getMaxVisitas()]);
                }
            }
        }
    }

    private PlantaCriteria getRestri(JFormattedTextField f1, JFormattedTextField f2, String property, PlantaCriteria criteria) {

        if (f1.getText().isEmpty() && !f2.getText().isEmpty()) {
            criteria.add(Restrictions.le(property, new Integer(f2.getValue().toString())));
        }

        if (!f1.getText().isEmpty() && f2.getText().isEmpty()) {
            criteria.add(Restrictions.ge(property, new Integer(f1.getValue().toString())));
        }

        if (!f1.getText().isEmpty() && !f2.getText().isEmpty()) {
            criteria.add(Restrictions.between(property, new Integer(f1.getValue().toString()), new Integer(f2.getValue().toString())));
        }

        return criteria;
    }

    private BalloonTip configTip(int linha, BalloonTip tip) {

        tip.removeAll();
        tip.setLayout(new GridBagLayout());
        tip.setSize(620, 300);

        String codigo = tabela.getValueAt(linha, 0).toString();
        Portaria portaria = ((ModelPortaria) tabela.getModel()).getRow(linha);

        WestPersistentManager.clear();
        PortariaDAO.lock(portaria);

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.weightx = 1;
        cons.weighty = 0;

        //LABEL CÓDIGO
        cons.gridx = 0;
        cons.gridy = 0;
        cons.gridwidth = 2;
        cons.fill = GridBagConstraints.HORIZONTAL;
        tip.add(new JLabel("Código de Portaria: " + codigo), cons);

        //LABEL FACHADA
        cons.gridx = 0;
        cons.gridy = 1;
        cons.weightx = 0;
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;
        cons.anchor = GridBagConstraints.LINE_START;
        tip.add(new JLabel("Fachada"), cons);

        //IMAGEM FACHADA
        cons.gridy = 2;
        cons.weighty = 1;
        cons.gridheight = 3;
        cons.weightx = 0.5;
        String pasta = Util.getPropriedade("west.foto_dir");
        pasta = pasta + "\\Portaria\\cod " + codigo + ".jpg";
        ImageIcon icon = new ImageIcon(pasta);
        Dimension d = getSizeIcon(icon);
        icon = new ImageIcon(icon.getImage().getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH));
        JLabel labelIcon = new JLabel(icon);
        tip.add(labelIcon, cons);

        //LABEL LAZERES
        cons.weighty = 0;
        cons.gridheight = 1;
        cons.gridx = 1;
        cons.gridy = 1;
        cons.weightx = 1;
        cons.fill = GridBagConstraints.HORIZONTAL;
        tip.add(new JLabel("Lazeres"), cons);

        //LISTA DE LAZERES
        cons.gridy = 2;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
        tip.add(new JScrollPane(new JList(portaria.getLazer().toArray())), cons);

        //LABEL PLANTAS
        cons.gridx = 1;
        cons.gridy = 3;
        cons.weighty = 0;
        cons.fill = GridBagConstraints.HORIZONTAL;
        tip.add(new JLabel("Plantas"), cons);

        //LISTA DE PLANTAS
        cons.gridy = 4;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
        tip.add(new JScrollPane(new JList(portaria.getPlantas().toArray())), cons);

        tip.revalidate();
        tip.repaint();

        return tip;
    }

    private Dimension getSizeIcon(ImageIcon icon) {

        Double proporcao = new Double((double) icon.getIconWidth() / icon.getIconHeight());
        Integer largura = 0;
        Integer altura = 0;

        if (icon.getIconWidth() > icon.getIconHeight()) {
            largura = 200;
            altura = new Double(largura / proporcao).intValue();
        } else {
            altura = 200;
            largura = new Double(altura * proporcao).intValue();
        }

        return new Dimension(largura, altura);
    }

    private PlantaCriteria addImovelCriteria(JFormattedTextField f1, JFormattedTextField f2, PlantaCriteria plantaCriteria) {

        if (!f1.getText().isEmpty() && !f2.getText().isEmpty()) {
            ImovelCriteria criteria = plantaCriteria.createPortariaCriteria().createImovelCriteria();

            if (f1.getText().isEmpty() && !f2.getText().isEmpty()) {
                criteria.add(Restrictions.le("valor", new Double(f2.getValue().toString())));
            }

            if (!f1.getText().isEmpty() && f2.getText().isEmpty()) {
                criteria.add(Restrictions.ge("valor", new Double(f1.getValue().toString())));
            }

            if (!f1.getText().isEmpty() && !f2.getText().isEmpty()) {
                criteria.add(Restrictions.between("valor", new Double(f1.getValue().toString()), new Double(f2.getValue().toString())));
            }

            Date agora = new Date();
            criteria.add(Restrictions.gt("atualizado", Util.addAnos(agora, -1)));
        }

        return plantaCriteria;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtDorms1 = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getIntegerInstance()));
        txtDorms2 = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getIntegerInstance()));
        jLabel1 = new javax.swing.JLabel();
        txtSuites2 = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getIntegerInstance()));
        txtSuites1 = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getIntegerInstance()));
        jLabel5 = new javax.swing.JLabel();
        txtVagas1 = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getIntegerInstance()));
        txtVagas2 = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getIntegerInstance()));
        jLabel6 = new javax.swing.JLabel();
        txtPrivativa1 = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getInstance()));
        txtPrivativa2 = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getInstance()));
        btnBuscar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtPreco1 = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getInstance()));
        txtPreco2 = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getInstance()));
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dtInicio = new com.toedter.calendar.JDateChooser();
        dtFinal = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Busca de Plantas"));
        jPanel1.setMinimumSize(new java.awt.Dimension(362, 85));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 85));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Dormitórios:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel4, gridBagConstraints);

        txtDorms1.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(txtDorms1, gridBagConstraints);

        txtDorms2.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(txtDorms2, gridBagConstraints);

        jLabel1.setText("Suítes:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel1, gridBagConstraints);

        txtSuites2.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(txtSuites2, gridBagConstraints);

        txtSuites1.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(txtSuites1, gridBagConstraints);

        jLabel5.setText("Vagas:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel5, gridBagConstraints);

        txtVagas1.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(txtVagas1, gridBagConstraints);

        txtVagas2.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(txtVagas2, gridBagConstraints);

        jLabel6.setText("Área Privativa:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel6, gridBagConstraints);

        txtPrivativa1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtPrivativa1.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(txtPrivativa1, gridBagConstraints);

        txtPrivativa2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtPrivativa2.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(txtPrivativa2, gridBagConstraints);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(btnBuscar, gridBagConstraints);

        jLabel7.setText("Preço:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel7, gridBagConstraints);

        txtPreco1.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(txtPreco1, gridBagConstraints);

        txtPreco2.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(txtPreco2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Busca Por Período"));
        jPanel2.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 70));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Inicial:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Final:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(jLabel3, gridBagConstraints);

        dtInicio.setPreferredSize(new java.awt.Dimension(114, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(dtInicio, gridBagConstraints);

        dtFinal.setPreferredSize(new java.awt.Dimension(114, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(dtFinal, gridBagConstraints);

        jButton1.setText("Buscar");
        jButton1.setPreferredSize(new java.awt.Dimension(52, 25));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(jButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jPanel2, gridBagConstraints);

        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabela.setRowHeight(30);
        tabela.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabela.setTableHeader(new HeaderOrder(tabela.getColumnModel()));
        jScrollPane1.setViewportView(tabela);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jScrollPane1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        Util.load(jScrollPane1);
        tabela.setVisible(false);
        tabela.getTableHeader().setVisible(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PortariaCriteria criteria = new PortariaCriteria();
                    PlantaCriteria plantaCriteria = criteria.createPlantaCriteria();

                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

                    plantaCriteria = getRestri(txtDorms1, txtDorms2, "dorms", plantaCriteria);
                    plantaCriteria = getRestri(txtSuites1, txtSuites2, "suites", plantaCriteria);
                    plantaCriteria = getRestri(txtVagas1, txtVagas2, "vagas", plantaCriteria);

                    if (txtPrivativa1.getText().isEmpty() && !txtPrivativa2.getText().isEmpty()) {
                        plantaCriteria.add(Restrictions.le("privativa", new Double(txtPrivativa2.getValue().toString())));
                    }

                    if (!txtPrivativa1.getText().isEmpty() && txtPrivativa2.getText().isEmpty()) {
                        plantaCriteria.add(Restrictions.ge("privativa", new Double(txtPrivativa1.getValue().toString())));
                    }

                    if (!txtPrivativa1.getText().isEmpty() && !txtPrivativa2.getText().isEmpty()) {
                        plantaCriteria.add(Restrictions.between("privativa", new Double(txtPrivativa1.getValue().toString()), new Double(txtPrivativa2.getValue().toString())));
                    }

                    plantaCriteria = addImovelCriteria(txtPreco1, txtPreco2, plantaCriteria);

                    List lista = criteria.list();

                    setPortarias(lista);

                    Util.unload(jScrollPane1);
                    tabela.setVisible(true);
                    tabela.getTableHeader().setVisible(true);

                    WestPersistentManager.clear();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (dtFinal.getDate().before(dtInicio.getDate())) {
            JOptionPane.showMessageDialog(null, "Período Inválido!");
        } else {
            Util.load(jScrollPane1);
            tabela.setVisible(false);
            tabela.getTableHeader().setVisible(false);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    ModelPortaria model = new ModelPortaria(Arrays.asList(PortariaDAO.listPortariaByQuery("codigo>0", "codigo")), dtInicio.getDate(), dtFinal.getDate());

                    tabela.setModel(model);
                    setSizes();

                    HeaderOrder header = (HeaderOrder) tabela.getTableHeader();
                    header.reset();

                    Util.unload(jScrollPane1);
                    tabela.setVisible(true);
                    tabela.getTableHeader().setVisible(true);

                    WestPersistentManager.clear();
                }
            }).start();
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private com.toedter.calendar.JDateChooser dtFinal;
    private com.toedter.calendar.JDateChooser dtInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabela;
    private javax.swing.JFormattedTextField txtDorms1;
    private javax.swing.JFormattedTextField txtDorms2;
    private javax.swing.JFormattedTextField txtPreco1;
    private javax.swing.JFormattedTextField txtPreco2;
    private javax.swing.JFormattedTextField txtPrivativa1;
    private javax.swing.JFormattedTextField txtPrivativa2;
    private javax.swing.JFormattedTextField txtSuites1;
    private javax.swing.JFormattedTextField txtSuites2;
    private javax.swing.JFormattedTextField txtVagas1;
    private javax.swing.JFormattedTextField txtVagas2;
    // End of variables declaration//GEN-END:variables

    public void setPortarias(List<Portaria> lista) {
        ModelPortaria model = new ModelPortaria(lista);
        tabela.setModel(model);
        setSizes();
        tabela.getTableHeader().setDefaultRenderer(new HeaderRender(new HashMap<Integer, Integer>(), new HashMap<Integer, List<String>>()));
    }

    public void setPortaria(Portaria portaria) {
        List<Portaria> lista = new ArrayList<Portaria>();
        lista.add(portaria);
        setPortarias(lista);
    }
}
