package org.west.componentes.tabelafilter;

import java.lang.reflect.Method;
import java.util.Comparator;

public class ObjectComparator implements Comparator{

    private String property;

    public ObjectComparator(String property) {
        this.property = property;
    }

    @Override
    public int compare(Object o1, Object o2) {
        
        try{
            Method metodo = o1.getClass().getDeclaredMethod(toGetter(property), new Class[0]);
            
            Object valor1 = metodo.invoke(o1, new Object[0]);
            Object valor2 = metodo.invoke(o2, new Object[0]);

            if(valor1 == null && valor2 == null)
                    return 0;
            if(valor1 != null && valor2 == null)
                    return -1;
            if(valor1 == null && valor2 != null)
                    return 1;
            if (valor1.getClass().equals(valor2.getClass())){
                if (valor1 instanceof Comparable)
                    return ((Comparable)valor1).compareTo(valor2);
                else
                    return valor1.toString().toLowerCase().compareTo(valor2.toString().toLowerCase());
            }
            else
                return 0;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }

    private String toGetter(String value){
        return "get" + value.substring(0,1).toUpperCase() + value.substring(1);
    }
}