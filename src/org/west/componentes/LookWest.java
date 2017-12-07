package org.west.componentes;

import java.awt.Font;
import javax.swing.JLabel;
import org.swingBean.descriptor.look.DefaultLookDescriptor;

public class LookWest extends DefaultLookDescriptor {

    @Override
    public JLabel createFormMandatoryLabel(String text) {
        JLabel lbl = super.createFormMandatoryLabel(text);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 12));
        lbl.setText("* " + text);
        return lbl;
    }

    @Override
    public Font getFieldsFont() {
        Font font = super.getFieldsFont();
        font = font.deriveFont(Font.PLAIN, 12);
        return font;
    }

    @Override
    public Font getTextFont() {
        Font font = new Font("Verdana", Font.PLAIN, 12);
        return font;
    }
}