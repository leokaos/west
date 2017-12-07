package org.west.componentes;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperHidden implements ComponentWrapper{
    
    private Object obj;
    private JLabel label;

    public Object getValue() {
        return obj;
    }

    public void setValue(Object o) {
        this.obj = o;
    }

    public void cleanValue() {
        obj = null;
    }

    public Component getComponent() {
        return label;
    }

    public void setEnable(boolean bln) {
        label.setEnabled(bln);
    }

    public void initComponent() throws Exception {
        label = new JLabel();
        label.setPreferredSize(new Dimension(0, 0));
        label.setMinimumSize(new Dimension(0, 0));
        label.setMaximumSize(new Dimension(0, 0));
    }    
}