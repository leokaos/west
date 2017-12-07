package org.west.componentes;

import java.awt.Component;
import java.awt.event.KeyEvent;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperSujestion implements ComponentWrapper{

    private JSugestion sugestion;

    private String entity;
    private String field;

    @Override
    public Object getValue() {
        return sugestion.getValue();
    }

    @Override
    public void setValue(Object o) {
        sugestion.setText(o.toString());
        sugestion.setValue(o);
    }

    @Override
    public void cleanValue() {
        sugestion.setText("");
        sugestion.setValue(null);
    }

    @Override
    public Component getComponent() {
        return sugestion;
    }

    @Override
    public void setEnable(boolean bln) {
        sugestion.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        sugestion = new JSugestion(entity, field);
        sugestion.enableTyping(false);
        sugestion.setKey(KeyEvent.VK_ENTER);
        sugestion.setOrder(field);
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setField(String field) {
        this.field = field;
    }
}