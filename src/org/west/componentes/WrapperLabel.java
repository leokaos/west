package org.west.componentes;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.text.MaskFormatter;
import org.swingBean.descriptor.look.LookProvider;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperLabel implements ComponentWrapper {
    
    private JLabel label;
    private Object object;
    
    private String mask = "";
    private String dateFormat = "";
    private String numberFormat = "";
    private String align = "";
    
    private MaskFormatter maskFormatter;
    private SimpleDateFormat dateFormatter;
    private DecimalFormat numberFormatter;

    @Override
    public Object getValue() {
        return object;
    }

    @Override
    public void setValue(Object obj) {
        this.object = obj;
        
        if (object instanceof Collection)
            if (((Collection) obj).size() > 0)
                this.object = ((Collection) obj).iterator().next();
                
        try{            
            if (dateFormatter != null && object instanceof Date)
                label.setText(dateFormatter.format((Date) object ));
            else
                if (numberFormatter != null && object instanceof Number)
                    label.setText(numberFormatter.format(object));
                else
                    if (maskFormatter != null && object instanceof String)
                        label.setText(maskFormatter.valueToString(object));
                    else
                        if (object == null || object.toString().isEmpty())
                            label.setText(" ");
                        else
                            label.setText(object.toString());
        }
        catch(Exception ex){ex.printStackTrace();}
    }

    @Override
    public void cleanValue() {
        label.setText(" ");
        setObj(null);
    }

    @Override
    public Component getComponent() {
        return label;
    }

    @Override
    public void setEnable(boolean bln) {
        label.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        label = new JLabel(" ");
        definiAlign();
        
        if (!dateFormat.isEmpty())
            dateFormatter = new SimpleDateFormat(dateFormat);
        
        if (!numberFormat.isEmpty())
            numberFormatter = new DecimalFormat(numberFormat);
        
        if (!mask.isEmpty())
            maskFormatter = new MaskFormatter(mask);

        label.setFont(LookProvider.getLook().getFieldsFont());
    }
    
    private void definiAlign(){
        if (align.equals("CENTER"))
            label.setHorizontalAlignment(SwingUtilities.CENTER);
        
        if (align.equals("LEFT"))
            label.setHorizontalAlignment(SwingUtilities.LEFT);
        
        if (align.equals("RIGHT"))
            label.setHorizontalAlignment(SwingUtilities.RIGHT);
    }

    public void setObj(Object obj) {
        this.object = obj;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public void setAlign(String align) {
        this.align = align;
    }
}