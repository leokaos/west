package org.west.componentes.tabelafilter;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class HeaderOrder extends JTableHeader {

    private Map<Integer, Integer> mapaOrder;
    private Map<Integer, PanelFilter> mapaFilter;
    private Map<Integer, List<String>> mapaFiltro;

    public HeaderOrder() {
    }

    public HeaderOrder(TableColumnModel model) {
        super(model);

        this.mapaOrder = new LinkedHashMap<Integer, Integer>();
        this.mapaFilter = new HashMap<Integer, PanelFilter>();
        this.mapaFiltro = new HashMap<Integer, List<String>>();

        configMouseListerner();
    }
    
    public void reset(){
        this.mapaOrder = new LinkedHashMap<Integer, Integer>();
        this.mapaFiltro = new HashMap<Integer, List<String>>();        
    }

    private void configMouseListerner() {
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {

                    int col = getTable().getColumnModel().getColumnIndexAtX(e.getX());

                    if (mapaOrder.containsKey(col)) {
                        if (mapaOrder.get(col) == 0) {
                            mapaOrder.remove(col);
                            mapaOrder.put(col, 1);
                        } else {
                            mapaOrder.remove(col);
                        }
                    } else {
                        mapaOrder.put(col, 0);
                    }

                    alteraLabel();
                    ordenaTabela();
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    criarMenu(table.getColumnModel().getColumnIndexAtX(e.getX()), e);
                }
            }
        });
    }

    private void ordenaTabela() {
        if (table.getModel().getClass().equals(ModelPortaria.class)) {

            ModelPortaria model = (ModelPortaria) table.getModel();
            model.ordenarListagem(mapaOrder);

            table.tableChanged(new TableModelEvent(model));
        }
    }

    private void alteraLabel() {
        JTable tabela = getTable();
        for (int x = 0; x < tabela.getColumnCount(); x++) {
            tabela.getColumnModel().getColumn(x).setHeaderRenderer(new HeaderRender(mapaOrder, mapaFiltro));
        }

        repaint();
    }

    @Override
    public void setTable(JTable table) {
        super.setTable(table);

        table.addPropertyChangeListener("model", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                constroiFiltros();
            }
        });
    }

    private void constroiFiltros() {
        for (int x = 0; x < getColumnModel().getColumnCount(); x++) {
            TreeSet lista = new TreeSet();

            for (int y = 0; y < table.getRowCount(); y++) {
                Object str = table.getModel().getValueAt(y, x);

                if (str != null) {
                    lista.add(str.toString());
                }
            }

            PanelFilter filter = new PanelFilter(new ArrayList(lista), mapaFiltro.get(x));
            mapaFilter.put(x, filter);
        }
    }

    public void criarMenu(final int coluna, MouseEvent evt) {

        JPopupMenu menu = new JPopupMenu();
        final PanelFilter panel = mapaFilter.get(coluna);
        JScrollPane jc = new JScrollPane(panel);
        menu.add(jc);

        menu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {

                ModelPortaria model = (ModelPortaria) table.getModel();

                if (panel.getSelecionados() == null) {
                    mapaFiltro.remove(coluna);
                    model.filterModel(mapaFiltro);

                    constroiFiltros();
                    alteraLabel();
                } else {
                    if ((mapaFiltro.get(coluna) == null || !mapaFiltro.get(coluna).equals(panel.getSelecionados())) && !panel.getSelecionados().isEmpty()) {

                        if (mapaFiltro.containsKey(coluna)) {
                            mapaFiltro.remove(coluna);
                        }

                        mapaFiltro.put(coluna, panel.getSelecionados());   

                        model.filterModel(mapaFiltro);

                        constroiFiltros();
                        alteraLabel();
                    }
                }
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent pme) {
            }
        });

        Rectangle rec = getHeaderRect(coluna);

        JScrollPane jc1 = (JScrollPane) getParent().getParent();

        menu.setPreferredSize(new Dimension(rec.width, 200));
        menu.show(table, rec.x, jc1.getVerticalScrollBar().getValue());
    }
}