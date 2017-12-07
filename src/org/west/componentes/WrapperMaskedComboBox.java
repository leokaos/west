package org.west.componentes;

import org.west.utilitarios.ModelPeriodo;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperMaskedComboBox implements ComponentWrapper {
    
    private String masked;
    private String values;
    private String objetos;
    private List visua;
    private List objs;
    private JComboBox comboBox;
    private ComboBoxModel model;

    public void setMasked(String masked) {
        this.masked = masked;
    }

    public void setValues(String values) {
        this.values = values;
    }

    @Override
    public Object getValue() {
        if (this.comboBox.getSelectedIndex() == -1)
            return null;
        else
            return this.objs.get(this.comboBox.getSelectedIndex());
    }

    @Override
    public void setValue(Object o) {
        if (comboBox.getModel() instanceof ModelPeriodo){
            ModelPeriodo modelIn = (ModelPeriodo) comboBox.getModel();
            Date data = new Date(((Timestamp)o).getTime());
            comboBox.setSelectedIndex(modelIn.getIndex(data));
        }
        else
            comboBox.setSelectedIndex(objs.indexOf(o.toString()));
    }

    @Override
    public void cleanValue() {
        this.comboBox.setSelectedIndex(-1);
    }

    @Override
    public Component getComponent() {
        return this.comboBox;
    }

    @Override
    public void setEnable(boolean bln) {
        this.comboBox.setEnabled(bln);
    }

    public void setObjetos(String objetos){
        this.objetos = objetos;
    }

    public void setModel(String model){
        try{
            this.model = (ComboBoxModel) Class.forName(model).newInstance();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void initComponent() throws Exception {

        this.comboBox = new JComboBox();

        this.comboBox.addPropertyChangeListener("model", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                carregaObjetos();
            }
        });
        
        this.visua = new ArrayList();
        this.objs = new ArrayList();

        if (this.values != null && this.masked != null) {
            this.visua = Arrays.asList(this.masked.split(";"));
            this.objs = Arrays.asList(this.values.split(";"));
            this.comboBox.setModel(new DefaultComboBoxModel(this.visua.toArray()));
        }

        if (this.model != null && this.objetos != null){
            this.comboBox.setModel(model);
            this.model.setSelectedItem(null);
        }
        
        this.comboBox.setSelectedIndex(-1);
    }

    public void carregaObjetos(){
        model = comboBox.getModel();
        if (objetos != null){
            try{
                Method metodo = model.getClass().getDeclaredMethod(objetos, new Class[0]);
                if (metodo != null)
                    this.objs = (ArrayList) metodo.invoke(model, new Object[0]);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}