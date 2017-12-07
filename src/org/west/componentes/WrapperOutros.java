package org.west.componentes;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperOutros implements ComponentWrapper{

    private JComboBox combo;
    private JPanel panel;
    private JTextField text;
    private JLabel label;

    public Object getValue() {
        if (text.isEnabled() && !text.getText().isEmpty())
            return text.getText();
        else
            return combo.getSelectedItem();
    }

    public void setValue(Object o) {
        combo.getModel().setSelectedItem(o);
    }

    public void cleanValue() {
        combo.setSelectedIndex(-1);
        text.setEnabled(false);
        text.setText("");
    }

    public Component getComponent() {
        return this.panel;
    }

    public void setEnable(boolean bln) {
        this.panel.setEnabled(bln);
    }

    public void initComponent() throws Exception {
        panel = new JPanel(new GridBagLayout());
        text = new JTextField();
        combo = new JComboBox();
        label = new JLabel("Outros: ");

        text.setEnabled(false);
        label.setEnabled(false);

        combo.setModel(getModel());
        combo.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED){
                    text.setEnabled(combo.getSelectedItem().equals("Outros"));
                    label.setEnabled(combo.getSelectedItem().equals("Outros"));
                }
            }
        });
        
        //definição do laytout
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weightx = 1;
        cons.weighty = 0;

        cons.gridx = 0;
        cons.gridy = 0;
        cons.gridwidth = 2;
        panel.add(combo,cons);

        cons.gridy = 1;
        cons.gridx = 0;
        cons.weightx = 0;
        cons.gridwidth = 1;
        panel.add(label,cons);
        
        cons.gridx = 1;
        cons.weightx = 1;
        panel.add(text,cons);
    }

    private ComboBoxModel getModel(){
        return ListModelFactory.getInstance().getModelLancamentos();
    }
}