package org.west.utilitarios;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class ModelPeriodo extends DefaultComboBoxModel {

    private List<Date> listaObj;
    private List<String> listaString;

    public ModelPeriodo(){
        this(new Date());
    }

    public ModelPeriodo(Date data) {
        this.listaObj = new ArrayList();
        this.listaString = new ArrayList();

        Calendar calenda = new GregorianCalendar();

        calenda.setTime(data);
        calenda.add(Calendar.DAY_OF_YEAR, -10);
        Date atual = calenda.getTime();
        addElement(atual, "10 dias antes");

        calenda.setTime(data);
        calenda.add(Calendar.DAY_OF_YEAR, -7);
        atual = calenda.getTime();
        addElement(atual, "1 semana antes");
        
        calenda.setTime(data);
        calenda.add(Calendar.DAY_OF_YEAR, -4);
        atual = calenda.getTime();
        addElement(atual, "4 dias antes");        

        calenda.setTime(data);
        calenda.add(Calendar.DAY_OF_YEAR, -3);
        atual = calenda.getTime();
        addElement(atual, "3 dias antes");
        
        calenda.setTime(data);
        calenda.add(Calendar.DAY_OF_YEAR, -2);
        atual = calenda.getTime();
        addElement(atual, "2 dias antes");        

        calenda.setTime(data);
        calenda.add(Calendar.DAY_OF_YEAR, -1);
        atual = calenda.getTime();
        addElement(atual, "1 dia antes");

        calenda.setTime(data);
        calenda.add(Calendar.HOUR, -12);
        atual = calenda.getTime();
        addElement(atual, "12 horas antes");

        calenda.setTime(data);
        calenda.add(Calendar.HOUR, -6);
        atual = calenda.getTime();
        addElement(atual, "6 horas antes");

        calenda.setTime(data);
        calenda.add(Calendar.HOUR, -1);
        atual = calenda.getTime();
        addElement(atual, "1 hora antes");

        calenda.setTime(data);
        calenda.add(Calendar.MINUTE, -30);
        atual = calenda.getTime();
        addElement(atual, "30 minutos antes");
    }

    private void addElement(Date data, String string) {
        this.listaObj.add(data);
        this.listaString.add(string);
        addElement(string);
    }

    public int getIndex(Date data){
        return listaObj.indexOf(data);
    }

    public List getObsj(){
        return this.listaObj;
    }
}