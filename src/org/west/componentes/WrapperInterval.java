package org.west.componentes;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.swingBean.gui.EmptyNumberFormatterNoLetter;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperInterval implements ComponentWrapper {

    private JFormattedTextField minValue;
    private JFormattedTextField maxValue;
    private JPanel panel;

    @Override
    public Object getValue() {
        if (minValue.getText().isEmpty() && maxValue.getText().isEmpty()) {
            return null;
        } else {
            Object value[] = new Object[2];
            value[0] = minValue.getValue();
            value[1] = maxValue.getValue();
            return value;
        }
    }

    @Override
    public void setValue(Object o) {
        Object value[] = new Object[2];
        value = (Object[]) o;

        if (value[0] != null) {
            minValue.setText(value[0].toString());
        } else {
            minValue.setText("");
        }

        if (value[1] != null) {
            maxValue.setText(value[1].toString());
        } else {
            maxValue.setText("");
        }
    }

    @Override
    public void cleanValue() {
        minValue.setText("");
        maxValue.setText("");
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public void setEnable(boolean bln) {
        minValue.setEnabled(bln);
        maxValue.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        EmptyNumberFormatterNoLetter format = new EmptyNumberFormatterNoLetter(NumberFormat.getNumberInstance());

        minValue = new JFormattedTextField(format);
        maxValue = new JFormattedTextField(format);
        panel = new JPanel(new GridBagLayout());

        GridBagConstraints cons = new GridBagConstraints();
        cons.anchor = GridBagConstraints.CENTER;
        cons.fill = GridBagConstraints.BOTH;
        cons.insets = new Insets(0, 1, 0, 1);
        cons.gridy = 0;

        cons.gridx = 0;
        cons.weightx = 1;
        cons.weighty = 1;
        panel.add(minValue, cons);

        cons.gridx = 1;
        cons.weightx = 0;
        panel.add(new JLabel("até"), cons);

        cons.weightx = 1;
        cons.gridx = 2;
        panel.add(maxValue, cons);
    }
}