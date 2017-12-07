package org.west.componentes;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperTelefone implements ComponentWrapper{

    private JFormattedTextField telefone;
    private JComboBox tipo;
    private JPanel panel;

    public Object getValue() {
        try{
            String texto = telefone.getText().replace("(","").replace(")","").replace("-","").replace(" ","");
            Integer tel = Integer.parseInt(texto);
            String retorno = "";
            retorno+= tipo.getSelectedItem();            
            retorno+= " " + tel.toString();
            return retorno;
        }
        catch(Exception ex){
            return null;
        }
    }

    public void setValue(Object o) {
        if (!o.toString().isEmpty()){
            String valores[] = o.toString().split(" ");
            tipo.setSelectedItem(valores[0]);
            telefone.setText(valores[1]);
        }
        else{
            tipo.setSelectedItem(null);
            telefone.setText("");
        }
    }

    public void cleanValue() {
        tipo.setSelectedIndex(-1);
        telefone.setText("");
    }

    public Component getComponent() {
        return panel;
    }

    public void setEnable(boolean bln) {
        tipo.setEnabled(bln);
        telefone.setEnabled(bln);
    }

    public void initComponent() throws Exception {
        MaskFormatter formato = new MaskFormatter("(##) ####-####");
        formato.setValidCharacters("0123456789");
        telefone = new JFormattedTextField(formato);

        String tipos[] = {"Celular","Residencial","Comercial","Fax","Outro"};
        tipo = new JComboBox(tipos);
        tipo.setSelectedIndex(-1);

        panel = new JPanel(new BorderLayout(10, 0));
        panel.add(tipo,BorderLayout.WEST);
        panel.add(telefone,BorderLayout.CENTER);
    }
}