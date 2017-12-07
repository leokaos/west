package org.west.componentes.tabelafilter;

import org.west.entidades.Imovel;
import java.lang.reflect.Method;
import java.util.Comparator;

public class ImoveisComparator implements Comparator{

    private String sort;

    public ImoveisComparator(String sort) {
        this.sort = sort;
    }   

    @Override
    public int compare(Object o1, Object o2) {
        Imovel imo1 = (Imovel) o1;
        Imovel imo2 = (Imovel) o2;

        try{
            Method metodo = Imovel.class.getDeclaredMethod(toGetter(sort), new Class[0]);

            if (metodo == null)
                return 0;
            else{
                Object valor1 = metodo.invoke(imo1, new Object[0]);
                Object valor2 = metodo.invoke(imo2, new Object[0]);

                if (valor1.equals(valor2))
                    return 0;

                if (valor1 instanceof Number)
                    return ((Number) valor1).intValue() - ((Number) valor2).intValue();
                else
                    return valor1.toString().compareTo(valor2.toString());
            }            
        }
        catch(Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }

    private String toGetter(String valor){
        return "get" + valor.substring(0, 1).toUpperCase() + valor.substring(1);
    }
}