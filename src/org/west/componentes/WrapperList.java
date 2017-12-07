package org.west.componentes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperList implements ComponentWrapper{

    private JList lista;
    private JScrollPane scroll;
    private List listagem;
    private JPanel panel;

    @Override
    public Object getValue() {
        return (Collection) this.listagem;
    }

    @Override
    public void setValue(Object o) {
        if (o.getClass().isArray())
            listagem = Arrays.asList((Object[])o);
        else
            listagem = new ArrayList((Collection) o);

        DefaultListModel model = new DefaultListModel();

        for(Object obj: listagem)
            model.addElement(obj);

        this.lista.setModel(model);
    }

    @Override
    public void cleanValue() {
        ((DefaultListModel)lista.getModel()).clear();
    }

    @Override
    public Component getComponent() {
        return this.scroll;
    }

    @Override
    public void setEnable(boolean bln) {
        this.lista.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        this.lista = new JList();
        this.scroll = new JScrollPane(this.lista);

        this.lista.setMinimumSize(new Dimension(0, 75));
        this.scroll.setMinimumSize(new Dimension(0, 75));

        this.panel = new JPanel(new BorderLayout(20, 20));

        this.panel.add(scroll,BorderLayout.CENTER);

        this.panel.setMinimumSize(new Dimension(0, 75));

        lista.setModel(new DefaultListModel());
        lista.setCellRenderer(new DefaultListCellRenderer(){

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component comp =  super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                comp.setFont(comp.getFont().deriveFont(Font.PLAIN));
                return comp;
            }
        });
    }
}