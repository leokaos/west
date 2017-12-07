package org.west.componentes;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperMultiplesFields implements ComponentWrapper{

    private String marcador;
    private String show;
    private String size;
    private JPanel panel;
    private List<JTextField> listFields;

    @Override
    public Object getValue() {
        String retorno = "";

        for (Iterator<JTextField> it = listFields.iterator(); it.hasNext();) {
            JTextField jTextField = it.next();
            if (!jTextField.getText().isEmpty())
                retorno+= jTextField.getText() + marcador;
        }

        if (!retorno.isEmpty())
            retorno = retorno.substring(0, retorno.length() - 1);

        return (retorno.isEmpty()?null:retorno);
    }

    @Override
    public void setValue(Object o) {
        String campo[] = o.toString().toLowerCase().split(marcador.toLowerCase());
        for(int x = 0;x < listFields.size();x++){
            if (x < campo.length)
                listFields.get(x).setText(campo[x]);
            else
                listFields.get(x).setText("");
        }
    }

    @Override
    public void cleanValue() {
        for (Iterator<JTextField> it = listFields.iterator(); it.hasNext();) {
            JTextField jTextField = it.next();
            jTextField.setText("");
        }
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public void setEnable(boolean bln) {
        for (JTextField jTextField : listFields)
            jTextField.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        Integer qt;
        listFields = new ArrayList<JTextField>();
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.anchor = GridBagConstraints.PAGE_START;
        cons.weightx = 1;
        cons.weighty = 0;
        cons.gridy = 0;
        int consx = 0;
        
        try{
            qt = Integer.parseInt(size);
        }
        catch(Exception ex){
            qt=1;
        }

        for(int x = 1;x <= qt;x++){

            cons.fill = GridBagConstraints.HORIZONTAL;
            JTextField txt = new JTextField();
            cons.gridx = consx;
            panel.add(txt,cons);
            listFields.add(txt);
            consx++;

            if (show.equals("true") && x<qt){
                cons.fill = GridBagConstraints.NONE;
                panel.add(new JLabel(marcador));
                consx++;
            }
        }
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMarcador() {
        return marcador;
    }

    public void setMarcador(String marcador) {
        this.marcador = marcador;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}