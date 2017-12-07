package org.west.componentes;

import org.west.entidades.Lazer;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperTwoLists implements ComponentWrapper{

    private JPanel panel;

    private JList disponivel;
    private JList conteudo;

    private List listaOriginal;

    private JButton btnAdd;
    private JButton btnRemove;

    private String classe;
    private String metodo;

    @Override
    public Object getValue() {
        Set valores = new HashSet();
        
        for (int i = 0; i < conteudo.getModel().getSize(); i++) {
            Lazer lazer = (Lazer) conteudo.getModel().getElementAt(i);
            valores.add(lazer);
        }
        
        return valores;
    }

    @Override
    public void setValue(Object o) {
        Set valores = new HashSet((Set) o);
        disponivel.setModel(getModel(listaOriginal.toArray()));
        conteudo.setModel(getModel(valores.toArray()));

        DefaultListModel model = (DefaultListModel) disponivel.getModel();
        
        for (Object object : valores) 
            model.removeElement(object);
    }

    @Override
    public void cleanValue() {
        conteudo.setModel(getModel(new Object[0]));
        disponivel.setModel(getModel(listaOriginal.toArray()));
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public void setEnable(boolean bln) {
        panel.setEnabled(bln);
    }

    private DefaultListModel getModel(Object[] elementos){
        DefaultListModel model  = new DefaultListModel();

        for (Object object : elementos)
            model.addElement(object);

        return model;
    }

    @Override
    public void initComponent() throws Exception {
        panel = new JPanel();

        Class facto = Class.forName(classe);

        disponivel = new JList();

        if (facto.getDeclaredMethod(metodo, new Class[0]) != null){
            Method met = facto.getDeclaredMethod(metodo, new Class[0]);
            disponivel.setModel( (DefaultListModel) met.invoke(facto.newInstance(), new Object[0]));
        }
      
        conteudo = new JList(new DefaultListModel());

        btnAdd = new JButton("  >>  ");
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                DefaultListModel modelC = (DefaultListModel) conteudo.getModel();
                DefaultListModel modelD = (DefaultListModel) disponivel.getModel();

                for (Object object : disponivel.getSelectedValues()) {
                    modelC.addElement(object);
                    modelD.removeElement(object);
                }
            }
        });
        
        btnRemove = new JButton("  << ");
        btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                DefaultListModel modelC = (DefaultListModel) conteudo.getModel();
                DefaultListModel modelD = (DefaultListModel) disponivel.getModel();

                for (Object object : conteudo.getSelectedValues()) {
                    modelC.removeElement(object);
                    modelD.addElement(object);
                }
            }
        });

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.weighty = 0;
        cons.weightx = 1;
        cons.fill = GridBagConstraints.HORIZONTAL;

        cons.gridx = 0;
        cons.gridy = 0;
        panel.add(new JLabel("Disponíveis:"),cons);

        cons.gridx = 2;
        panel.add(new JLabel("Possui:"),cons);

        cons.gridx = 0;
        cons.gridy = 1;
        cons.weighty = 1;
        cons.gridheight = 2;
        cons.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(disponivel),cons);

        cons.gridx = 1;
        cons.gridy = 1;
        cons.gridheight = 1;
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.PAGE_START;
        cons.weightx = 0;
        panel.add(btnAdd,cons);

        cons.gridy = 2;
        cons.anchor = GridBagConstraints.PAGE_END;
        panel.add(btnRemove,cons);

        cons.gridx = 2;
        cons.gridy = 1;
        cons.gridheight = 2;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(conteudo),cons);

    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}