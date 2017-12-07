package org.west.componentes;

import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;
import org.swingBean.gui.wrappers.ComponentWrapper;
import org.west.entidades.Usuario;
import org.west.utilitarios.DocumentNotRemove;

public class WrapperTextAdd implements ComponentWrapper{
    
    private DocumentNotRemove document;
    private JTextField field;

    @Override
    public Object getValue() {
        return field.getText();
    }

    @Override
    public void setValue(Object o) {
        String valor = o.toString();
        
        Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");
        if (!usuario.isSupervisor()){
            document = new DocumentNotRemove(valor);
            field.setDocument(document);
        }
        
        field.setText(valor);
    }

    @Override
    public void cleanValue() {
        field.setText("");
        field.setDocument(new PlainDocument());
    }

    @Override
    public Component getComponent() {
        return field;
    }

    @Override
    public void setEnable(boolean bln) {
        field.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        field = new JTextField();
    }
}