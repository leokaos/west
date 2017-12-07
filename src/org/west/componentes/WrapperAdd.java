package org.west.componentes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperAdd implements ComponentWrapper{

    private JPanel panel;
    private JList list;
    private JComboBox combo;
    private JButton btnAdd;
    private JButton btnRemove;
    private List listagem;
    private String classe;
    private String metodo;
    
    public void setClasse(String classe) {
        this.classe = classe;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    @Override
    public Object getValue() {
        return (Collection) listagem;
    }

    @Override
    public void setValue(Object o) {
        if (o instanceof Collection){
            listagem = new ArrayList( (Collection) o);
            
            DefaultListModel model = new DefaultListModel();
            
            for (Object object : listagem)
                model.addElement(object);
            
            list.setModel(model);
        }
    }

    @Override
    public void cleanValue() {
        list.setModel(new DefaultListModel());
        listagem = new ArrayList();
    }

    @Override
    public Component getComponent() {
        return panel;   }

    @Override
    public void setEnable(boolean bln) {
        panel.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        panel = new JPanel(new BorderLayout(20, 20));
        combo = new JComboBox();
        list = new JList(new DefaultListModel());
        btnAdd = new JButton("Adicionar");
        btnRemove = new JButton("Remover");
        listagem = new ArrayList();

        panel.add(new JScrollPane(list),BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new BorderLayout(20, 20));
        panelBotoes.add(combo,BorderLayout.LINE_START);
        panelBotoes.add(btnAdd,BorderLayout.CENTER);
        panelBotoes.add(btnRemove,BorderLayout.LINE_END);
        panel.add(panelBotoes,BorderLayout.SOUTH);      
        
        combo.setModel(getModel());
        
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (combo.getSelectedItem() != null && !listagem.contains(combo.getSelectedItem())){
                    listagem.add(combo.getSelectedItem());
                    
                    DefaultListModel model = new DefaultListModel();
                    
                    for (Object object : listagem)
                        model.addElement(object);
                    
                    list.setModel(model);
                }
            }
        });
        
        btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (list.getSelectedIndex() > -1){
                    
                    DefaultListModel model = (DefaultListModel) list.getModel();
                    Object obj = list.getSelectedValue();

                    model.removeElement(obj);                    
                    listagem.remove(obj);
                }
                else
                    JOptionPane.showMessageDialog(null, "Não foi selecionado nenhum item!");
            }
        });
    }
    
    private ComboBoxModel getModel(){
        if (classe != null && !classe.isEmpty() && metodo != null && !metodo.isEmpty()){
            
            ComboBoxModel model = new DefaultComboBoxModel();
            
            try{
                Class comboClass = Class.forName(classe);
                
                if (comboClass != null){
                    
                    Object objCombo = comboClass.newInstance();
                    
                    Method metodoCombo = objCombo.getClass().getDeclaredMethod(metodo, new Class[0]);
                    
                    if (metodoCombo != null){
                        model = (ComboBoxModel) metodoCombo.invoke(objCombo, new Object[0]);
                    }
                }
            }
            catch(Exception ex){ex.printStackTrace();}
            
            return model;
        }
        else
            return new DefaultComboBoxModel();
    }
}