package org.west.formulario.corretor;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanPanel;
import org.west.componentes.DesktopSession;
import org.west.componentes.TipValores;
import org.west.entidades.Anuncio;
import org.west.entidades.AnuncioDAO;
import org.west.entidades.Imovel;
import org.west.entidades.ImovelCriteria;
import org.west.entidades.ImovelDAO;
import org.west.entidades.Informacao;
import org.west.entidades.InformacaoCriteria;
import org.west.entidades.InformacaoDAO;
import org.west.entidades.Usuario;
import org.west.entidades.WestPersistentManager;
import org.west.formulario.imoveis.FrmInfoAnuncios;
import static org.west.utilitarios.ValidadorUtil.*;

public class FrmAnuncios extends javax.swing.JDialog {

    private JBeanPanel<Imovel> formImovelDetalhes;
    private TipValores tip = null;
    private Usuario usuario;

    public FrmAnuncios(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        setSize(800, 600);
        setLocationRelativeTo(null);

        GenericFieldDescriptor descriptor = XMLDescriptorFactory.getFieldDescriptor(Imovel.class, "/org/west/xml/imovelDetalhes.xml", "imovelDetalhes");
        formImovelDetalhes = new JBeanPanel<Imovel>(Imovel.class, "Detalhes do Imóvel", descriptor);

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridx = 0;
        cons.gridy = 4;
        cons.gridwidth = 4;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weightx = 1;
        cons.weighty = 0;

        this.add(formImovelDetalhes, cons);

        formImovelDetalhes.getComponent("valor").addMouseListener(new MouseAdapter() {

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

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                carregaListaImoveis(true);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaAnuncios = new javax.swing.JTable();
        tabelaAnuncios.setAutoCreateRowSorter(true);
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        todosImoveis = new javax.swing.JRadioButton();
        meusImoveis = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Anúncios");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 16));
        jLabel1.setText("Anúncios");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        lblStatus.setFont(new java.awt.Font("Tahoma", 2, 12));
        lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/load_small.gif"))); // NOI18N
        lblStatus.setText("Carregando Imóveis...");
        lblStatus.setIconTextGap(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblStatus, gridBagConstraints);

        tabelaAnuncios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tabelaAnuncios);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jButton1.setText("Gravar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButton1, gridBagConstraints);

        jButton2.setText("Anúncios...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButton2, gridBagConstraints);

        buttonGroup1.add(todosImoveis);
        todosImoveis.setText("Todos Imóveis");
        todosImoveis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todosImoveisActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        getContentPane().add(todosImoveis, gridBagConstraints);

        buttonGroup1.add(meusImoveis);
        meusImoveis.setSelected(true);
        meusImoveis.setText("Meu Imóveis");
        meusImoveis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                meusImoveisActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        getContentPane().add(meusImoveis, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        TableModelAnuncios model = (TableModelAnuncios) tabelaAnuncios.getModel();

        if (model.getImoveisAlterados().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum imóvel foi alterado!", "Informação", JOptionPane.INFORMATION_MESSAGE);
        } else {
            boolean ok = true;

            WestPersistentManager.clear();

            for (Imovel imovel : model.getImoveisAlterados()) {

                for (Anuncio anuncio : imovel.getAnuncios()) {

                    if (!anuncio.getTravado()) {
                        Informacao info = InformacaoDAO.get(imovel, anuncio);

                        if (info == null) {
                            info = new Informacao(anuncio.getNome(), imovel.getReferencia());
                            info.setDestaque(Informacao.NORMAL);
                        }

                        info.setUsuario(usuario);
                        info.setMostrarValor(true);
                        info.setLiberado(Boolean.TRUE);
                        info.setNovo(!existeAnuncio(imovel, anuncio));

                        InformacaoDAO.save(info);
                    }
                }

                if (!ImovelDAO.save(imovel)) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                JOptionPane.showMessageDialog(null, "Informações Gravadas com Sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Gravar Informações!");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (tabelaAnuncios.getSelectedRow() != -1) {
            TableModelAnuncios model = (TableModelAnuncios) tabelaAnuncios.getModel();
            Imovel imovel = model.getImovelAt(tabelaAnuncios.convertRowIndexToModel(tabelaAnuncios.getSelectedRow()));

            FrmInfoAnuncios frmInfoAnuncios = new FrmInfoAnuncios(null, true, imovel);
            frmInfoAnuncios.setVisible(true);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void todosImoveisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_todosImoveisActionPerformed
        carregaListaImoveis(false);
    }//GEN-LAST:event_todosImoveisActionPerformed

    private void meusImoveisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meusImoveisActionPerformed
        carregaListaImoveis(true);
    }//GEN-LAST:event_meusImoveisActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JRadioButton meusImoveis;
    private javax.swing.JTable tabelaAnuncios;
    private javax.swing.JRadioButton todosImoveis;
    // End of variables declaration//GEN-END:variables

    private void setSizes() {
        TableColumnModel model = tabelaAnuncios.getColumnModel();
        Integer tamanhos[] = {50, 300};

        for (int x = 0; x < model.getColumnCount(); x++) {
            tabelaAnuncios.getColumnModel().getColumn(x).setHeaderValue(tabelaAnuncios.getModel().getColumnName(x));

            if (x < tamanhos.length) {
                model.getColumn(x).setPreferredWidth(tamanhos[x]);
            } else {
                model.getColumn(x).setPreferredWidth(120);
            }
        }

        tabelaAnuncios.getTableHeader().repaint();
    }

    private Boolean existeAnuncio(Imovel imovel, Anuncio anuncio) {
        String query = "select * from tab_imovel_anuncio where imovel=" + imovel.getReferencia() + " and anuncio='" + anuncio.getNome() + "'";
        return WestPersistentManager.getSession().createSQLQuery(query).list().size() == 1;
    }

    private void carregaListaImoveis(final Boolean meusImoveis) {
        lblStatus.setFont(new Font("Tahoma", Font.ITALIC, 12));
        lblStatus.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/load_small.gif")));
        lblStatus.setText("Carregando Imóveis...");
        lblStatus.setIconTextGap(10);

        WestPersistentManager.clear();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (tabelaAnuncios.getModel() instanceof TableModelAnuncios) {
                        ((TableModelAnuncios) tabelaAnuncios.getModel()).clear();
                    }

                    ImovelCriteria criteria = new ImovelCriteria();
                    criteria.add(Restrictions.eq("status", "Ativo"));

                    if (meusImoveis) {
                        criteria.createUsuarioCriteria().add(Restrictions.eq("codigo", usuario.getCodigo()));
                    }

                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

                    List<Imovel> listaImovel = criteria.list();

                    TableModelAnuncios model = new TableModelAnuncios(listaImovel);
                    tabelaAnuncios.setModel(model);
                    setSizes();

                    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);

                    sorter.setComparator(1, new Comparator<String>() {

                        @Override
                        public int compare(String o1, String o2) {

                            Collator collator = Collator.getInstance(new Locale("pt", "BR"));
                            collator.setStrength(Collator.PRIMARY);

                            String real1 = o1.substring(o1.indexOf(" "), o1.indexOf(","));
                            String real2 = o2.substring(o2.indexOf(" "), o2.indexOf(","));

                            return collator.compare(real1, real2);
                        }
                    });

                    tabelaAnuncios.setRowSorter(sorter);

                    lblStatus.setIcon(null);
                    lblStatus.setText("Imóveis disponíveis: " + listaImovel.size());
                    lblStatus.setFont(lblStatus.getFont().deriveFont(Font.PLAIN));

                    tabelaAnuncios.setSelectionModel(new DefaultListSelectionModel());

                    tabelaAnuncios.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                            if (tabelaAnuncios.getSelectedRow() != -1) {
                                TableModelAnuncios model = (TableModelAnuncios) tabelaAnuncios.getModel();
                                Imovel imovel = model.getImovelAt(tabelaAnuncios.convertRowIndexToModel(tabelaAnuncios.getSelectedRow()));

                                formImovelDetalhes.setBean(imovel);
                                tip = new TipValores((JComponent) formImovelDetalhes.getComponent("valor"), "", imovel);
                                tip.getStyle().flip(true, false);
                            }
                        }
                    });

                    tabelaAnuncios.getModel().addTableModelListener(new TableModelListener() {

                        @Override
                        public void tableChanged(TableModelEvent e) {
                            if (e.getColumn() > 1) {
                                Boolean marcar = (Boolean) tabelaAnuncios.getValueAt(e.getFirstRow(), e.getColumn());

                                tabelaAnuncios.getTableHeader().getColumnModel().getColumn(e.getColumn()).
                                        setHeaderValue(tabelaAnuncios.getModel().getColumnName(e.getColumn()));

                                tabelaAnuncios.getTableHeader().repaint();

                                if (marcar) {
                                    Long imovel = (Long) tabelaAnuncios.getValueAt(e.getFirstRow(), 0);
                                    String anuncio = tabelaAnuncios.getColumnName(e.getColumn());

                                    anuncio = anuncio.substring(0, anuncio.indexOf(" ("));

                                    Informacao info = InformacaoDAO.loadInformacaoByQuery("imovel=" + imovel
                                            + " and anuncio='" + anuncio + "'", "referencia");

                                    if (info == null || info.getTextoAnuncio() == null || info.getTextoAnuncio().isEmpty()) {
                                        FrmInfoAnuncios frmInfoAnuncios = new FrmInfoAnuncios(null, true, ImovelDAO.get(imovel));
                                        frmInfoAnuncios.setAnuncioInicial(AnuncioDAO.load(anuncio));
                                        frmInfoAnuncios.setVisible(true);
                                    }
                                }
                            }
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
//    private void consolidarImovelAnunciados(Anuncio anuncio) {
//        InformacaoCriteria criteria = new InformacaoCriteria();
//        criteria.add(Restrictions.eq("usuario", usuario));
//        criteria.add(Restrictions.eq("anuncio", anuncio));
//
//        for (Informacao info : criteria.listInformacoes()) {
//            if (not(info.getLiberado())) {
//                info.getImovel().getAnuncios().remove(anuncio);
//                ImovelDAO.save(info.getImovel());
//            }
//        }
//    }
}
