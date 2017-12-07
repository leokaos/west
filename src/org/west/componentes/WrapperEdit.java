package org.west.componentes;

import com.lowagie.text.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import org.swingBean.descriptor.look.LookProvider;
import org.swingBean.gui.EmptyNumberFormatterNoLetter;
import org.swingBean.gui.wrappers.ComponentWrapper;
import org.west.entidades.Usuario;

public class WrapperEdit implements ComponentWrapper {

    private JTextComponent text;
    private JLabel label;
    private JPanel panel;
    protected Object valor;
    private String mask = "";
    private String format = "";
    private String decorate = "";
    private MaskFormatter maskFormatter;
    private Boolean edit;

    @Override
    public Object getValue() {

        if (valor != null && !edit) {
            return valor;
        }

        try {
            if (text instanceof JFormattedTextField) {

                JFormattedTextField field = (JFormattedTextField) text;

                field.commitEdit();

                if (maskFormatter != null) {
                    return field.getValue();
                }

                Number number = (Number) field.getValue();

                if (format.equals("INT")) {
                    return number.intValue();
                }

                if (format.equals("DOUBLE")) {
                    return number.doubleValue();
                }

                if (format.equals("STRING")) {
                    return String.valueOf(field.getText());
                }
            } else {
                return ((JTextField) text).getText();
            }
        } catch (Exception ex) {
        }

        return null;
    }

    @Override
    public void setValue(Object o) {
        this.valor = o;

        if (valor != null) {
            this.edit = false;

            if (decorate.equals("true")) {
                label.setBorder(BorderFactory.createLineBorder(new Color(108, 140, 198)));
                label.setBackground(Color.white);
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }

            label.setFont(label.getFont().deriveFont(Font.BOLD));
            label.setOpaque(true);

            panel.removeAll();
            panel.add(label, BorderLayout.CENTER);

            Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

            if (usuario.isSupervisor()) {

                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                            panel.removeAll();
                            panel.add(text, BorderLayout.CENTER);

                            text.setText(valor.toString());

                            panel.revalidate();
                            panel.repaint();
                            edit = true;
                        }
                    }
                });
            }

            try {
                if (text instanceof JFormattedTextField) {
                    JFormattedTextField field = (JFormattedTextField) text;

                    if (maskFormatter != null) {
                        label.setText(maskFormatter.valueToString(valor));
                    }

                    if (format.equals("INT") || format.equals("STRING")) {
                        label.setText(field.getFormatter().valueToString(new Integer(valor.toString())));
                    }

                    if (format.equals("DOUBLE")) {
                        label.setText(field.getFormatter().valueToString(new Double(valor.toString())));
                    }
                } else {
                    label.setText(valor.toString());
                }
            } catch (Exception ex) {
                if (usuario.isSupervisor() || usuario.getNivel() == 1) {
                    label.setText(valor.toString());
                } else {
                    label.setText("");
                }
            }
        } else {
            if (text instanceof JFormattedTextField) {
                ((JFormattedTextField) text).setValue(valor);
            } else {
                text.setText("");
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void cleanValue() {
        valor = null;
        text.setText("");
        panel.removeAll();
        panel.add(text, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
        edit = true;
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public void setEnable(boolean bln) {
        text.setEnabled(bln);
        label.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {

        if (!mask.isEmpty()) {
            maskFormatter = new MaskFormatter(mask);
            maskFormatter.setValueContainsLiteralCharacters(false);
            text = new JFormattedTextField(maskFormatter);
        } else {
            if (!format.isEmpty()) {

                if (format.equals("INT") || format.equals("STRING")) {
                    text = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getIntegerInstance()));
                }

                if (format.equals("DOUBLE")) {
                    text = new JFormattedTextField(new EmptyNumberFormatterNoLetter(NumberFormat.getInstance()));
                }

                ((JFormattedTextField) text).setFocusLostBehavior(JFormattedTextField.COMMIT);

            } else {
                text = new JTextField();
            }
        }

        label = new JLabel();
        label.setFont(LookProvider.getLook().getFieldsFont());

        panel = new JPanel(new BorderLayout());
        panel.add(text, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(0, 23));

        edit = true;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setDecorate(String decorate) {
        this.decorate = decorate;
    }
}