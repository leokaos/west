package org.west.componentes;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperColorLabel implements ComponentWrapper{

    private JLabel label;
    private String color;
    private Map<String,Color> map;
    private String original;

    @Override
    public Object getValue() {
        return this.original;
    }

    @Override
    public void setValue(Object o) {
        this.original = o.toString();
        this.label.setForeground(map.get(this.original));
        this.label.setText(this.original.toUpperCase());
    }

    @Override
    public void cleanValue() {
        this.label.setText("");
    }

    @Override
    public Component getComponent() {
        return this.label;
    }

    public void setColor(String color){
        this.color = color;
    }

    @Override
    public void setEnable(boolean bln) {
        this.label.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        this.label = new JLabel();

        if (color != null)
            montaMap(color);
    }

    private void montaMap(String color) {
        String[] valores = color.split(";");

        if (valores.length % 2 == 0){
            this.map = new HashMap<String, Color>();

            for(int x = 0; x < valores.length;x+= 2)
                map.put(valores[x], getColor(valores[x+1]));
        }
    }

    private Color getColor(String cor){

        if (cor.equals("verde"))
            return Color.green;
        if (cor.equals("amarelo"))
            return new Color(255,204,51);
        if (cor.equals("vermelho"))
            return Color.red;
        if (cor.equals("cinza"))
            return Color.gray;

        return Color.black;
    }
}