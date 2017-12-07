package org.west.componentes;

import java.awt.Component;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperSet implements ComponentWrapper{
    
    private JComboBox combo;
    private Set value;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object o) {
        if (o instanceof Set){
            value = (Set) o;            
            combo.setModel(new DefaultComboBoxModel(value.toArray()));
        }
    }

    @Override
    public void cleanValue() {
        combo.setModel(new DefaultComboBoxModel());
    }

    @Override
    public Component getComponent() {
        return combo;
    }

    @Override
    public void setEnable(boolean bln) {
        combo.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        combo = new JComboBox();
        combo.setModel(new DefaultComboBoxModel());
    }    
}