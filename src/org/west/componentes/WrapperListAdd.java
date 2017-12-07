package org.west.componentes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperListAdd implements ComponentWrapper{
    
    private JPanel panel;
    private JList lista;
    private JSugestion sugestion;
    private HashSet listagem;
    
    private String query;
    private String entity;

    @Override
    public Object getValue() {
        return listagem;
    }

    @Override
    public void setValue(Object o) {
        if (o instanceof Collection)
            listagem = new HashSet((Collection)o);
        
        if (o.getClass().isArray())
            listagem.addAll((Collection) Arrays.asList((Object[]) o));
    }

    @Override
    public void cleanValue() {
        lista.setModel(new DefaultListModel());
        listagem.clear();
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public void setEnable(boolean bln) {
        panel.setEnabled(bln);
        sugestion.setEnabled(bln);
        lista.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        panel = new JPanel(new BorderLayout(3, 3));
        sugestion = new JSugestion(entity, query);
        lista = new JList();
        
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(new EmptyBorder(3, 3, 3, 3));
        
        sugestion.setBorder(new EmptyBorder(3, 3, 3, 3));
        
        panel.add(scroll,BorderLayout.CENTER);
        panel.add(sugestion,BorderLayout.SOUTH);
        
        listagem = new HashSet();
        
        sugestion.addPropertyChangeListener("selecionado", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                listagem.add(sugestion.getValue());
                reload();
            }
        });
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }    
    
    private void reload(){
        DefaultListModel model = new DefaultListModel();
        for(Object obj : listagem)
            model.addElement(obj);
        lista.setModel(model);        
    }
}