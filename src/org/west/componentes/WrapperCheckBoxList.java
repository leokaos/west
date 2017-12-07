package org.west.componentes;

import java.awt.Component;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.swingBean.gui.wrappers.ArrayWrapper;

public class WrapperCheckBoxList extends ArrayWrapper{
    
    private Component comp;

    @Override
    public Object getValue() {
        Object[] obj = (Object[]) super.getValue();
        return new HashSet(Arrays.asList(obj));
    }

    @Override
    public void setValue(Object value) {
        Collection lista  = (Collection) value;       
        super.setValue(lista.toArray());
    }

    @Override
    public Component getComponent() {
        if (comp == null)
            comp = super.getComponent();
        
        return comp;
    }

    @Override
    public void initComponent() throws Exception {
        type = "CHECKBOX_LIST";
        super.initComponent();
    }
}