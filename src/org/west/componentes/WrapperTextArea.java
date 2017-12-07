package org.west.componentes;

import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperTextArea implements ComponentWrapper {

    private JTextArea textarea;
    private JScrollPane js;
    
    private Boolean editable = Boolean.FALSE;
    private Integer rows = 10;

    @Override
    public Object getValue() {
        return this.textarea.getText();
    }

    @Override
    public void setValue(Object o) {
        this.textarea.setText(o.toString().trim());
    }

    @Override
    public void cleanValue() {
        this.textarea.setText("");
    }

    @Override
    public Component getComponent() {
        return this.js;
    }

    @Override
    public void setEnable(boolean bln) {
        this.textarea.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        this.textarea = new JTextArea();
        this.textarea.setEditable(editable);
        this.textarea.setRows(rows);
        this.textarea.setLineWrap(true);
        this.textarea.setWrapStyleWord(true);

        this.js = new JScrollPane(this.textarea);
    }

    public void setEditable(String editable) {
        this.editable = editable.equals("true");
    }

    public void setRows(String rows) {
        try{
            this.rows = new Integer(rows);
        }
        catch(Exception ex){
            this.rows = 10;
        }
    }
}