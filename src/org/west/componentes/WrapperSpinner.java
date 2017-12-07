package org.west.componentes;

import java.awt.Component;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperSpinner implements ComponentWrapper {

    private JSpinner spiner;
    private Integer inicial = 0;
    private Integer max = 100;
    private Integer min = 0;
    private Integer step = 1;

    @Override
    public Object getValue() {
        Number number = (Number) this.spiner.getValue();
        if (number.intValue() > 0) {
            return this.spiner.getValue();
        } else {
            return null;
        }
    }

    @Override
    public void setValue(Object o) {
        this.spiner.setValue(o);
    }

    @Override
    public void cleanValue() {
        this.spiner.setValue(inicial);
    }

    @Override
    public Component getComponent() {
        return this.spiner;
    }

    @Override
    public void setEnable(boolean bln) {
        this.setEnable(bln);
    }

    @Override
    public void initComponent() throws Exception {
        this.spiner = new JSpinner();
        SpinnerNumberModel model = new SpinnerNumberModel(inicial, min, max, step);
        this.spiner.setModel(model);
    }

    public void setInicial(String inicial) {
        this.inicial = (isNumber(inicial) ? new Integer(inicial) : 0);
    }

    public void setMax(String max) {
        this.max = (isNumber(max) ? new Integer(max) : 100);
    }

    public void setMin(String min) {
        this.min = (isNumber(min) ? new Integer(min) : 0);
    }

    public void setStep(String step) {
        this.step = (isNumber(step) ? new Integer(step) : 1);
    }

    private Boolean isNumber(String str) {
        Boolean retorno = false;

        if (str != null) {
            retorno = true;

            for (int x = 0; x < str.length(); x++) {
                if (!Character.isDigit(str.charAt(x))) {
                    retorno = false;
                    break;
                }
            }
        }

        return retorno;
    }
}