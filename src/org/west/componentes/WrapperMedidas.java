package org.west.componentes;

import com.jgoodies.validation.formatter.EmptyNumberFormatter;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.swingBean.gui.wrappers.ComponentWrapper;
import org.west.entidades.Medidas;
import org.west.utilitarios.ValidadorUtil;

public class WrapperMedidas implements ComponentWrapper {

    private Medidas medidas;
    private JPanel panel;
    private JFormattedTextField frente;
    private JFormattedTextField esquerda;
    private JFormattedTextField traseira;
    private JFormattedTextField direita;

    @Override
    public Object getValue() {
        if (ValidadorUtil.isNull(medidas)) {
            medidas = new Medidas();
        }

        medidas.setFrente(converterParaDouble(frente));
        medidas.setEsquerda(converterParaDouble(esquerda));
        medidas.setTraseira(converterParaDouble(traseira));
        medidas.setDireita(converterParaDouble(direita));

        return medidas;
    }

    @Override
    public void setValue(Object o) {
        this.medidas = (Medidas) o;

        frente.setValue(medidas.getFrente());
        esquerda.setValue(medidas.getEsquerda());
        traseira.setValue(medidas.getTraseira());
        direita.setValue(medidas.getDireita());
    }

    @Override
    public void cleanValue() {
        setValue(new Medidas());
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public void setEnable(boolean bln) {
        this.panel.setEnabled(bln);

        for (Component comp : panel.getComponents()) {
            comp.setEnabled(bln);
        }
    }

    @Override
    public void initComponent() throws Exception {
        this.panel = new JPanel(new GridBagLayout());

        frente = new JFormattedTextField(new EmptyNumberFormatter(NumberFormat.getNumberInstance()));
        esquerda = new JFormattedTextField(new EmptyNumberFormatter(NumberFormat.getInstance()));
        traseira = new JFormattedTextField(new EmptyNumberFormatter(NumberFormat.getInstance()));
        direita = new JFormattedTextField(new EmptyNumberFormatter(NumberFormat.getInstance()));

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 0, 5, 0);
        cons.gridy = 0;
        cons.fill = GridBagConstraints.HORIZONTAL;
        int gridx = 0;

        cons.gridx = gridx++;
        cons.weightx = 1;
        this.panel.add(frente, cons);

        cons.gridx = gridx++;
        cons.weightx = 0;
        cons.insets = new Insets(5, 3, 5, 0);
        this.panel.add(new JLabel("X"), cons);

        cons.gridx = gridx++;
        cons.weightx = 1;
        this.panel.add(esquerda, cons);

        cons.gridx = gridx++;
        cons.weightx = 0;
        this.panel.add(new JLabel("X"), cons);

        cons.gridx = gridx++;
        cons.weightx = 1;
        this.panel.add(direita, cons);

        cons.gridx = gridx++;
        cons.weightx = 0;
        this.panel.add(new JLabel("X"), cons);

        cons.gridx = gridx++;
        cons.weightx = 1;
        this.panel.add(traseira, cons);

        frente.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    frente.commitEdit();
                    traseira.setValue(frente.getValue());
                } catch (ParseException ex) {
                }
            }
        });

        esquerda.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    esquerda.commitEdit();
                    direita.setValue(esquerda.getValue());
                } catch (ParseException ex) {
                }
            }
        });
    }

    private Double converterParaDouble(JFormattedTextField txt) {
        try {
            txt.commitEdit();

            if (ValidadorUtil.isNull(txt.getValue())) {
                return null;
            } else {
                Number valorNumber = (Number) txt.getValue();
                return valorNumber.doubleValue();
            }

        } catch (ParseException ex) {
            return null;
        }
    }
}
