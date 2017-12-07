package org.west.utilitarios;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.Predicate;

public class ListPredicate implements Predicate{

    private List listaValores;
    private String atributo;

    public ListPredicate(List listaValores, String atributo) {
        this.listaValores = listaValores;
        this.atributo = atributo;
   }

    @Override
    public boolean evaluate(Object o) {
        if (listaValores != null && !listaValores.isEmpty()){

            try{
                Method metodo = o.getClass().getDeclaredMethod(toGetter(atributo), new Class[0]);

                if (metodo != null){
                    Object retorno = metodo.invoke(o, new Object[0]);
                    
                    if (retorno instanceof Date){
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        retorno = format.format((Date) retorno);
                    }
                    
                    if (retorno == null)
                        retorno = "";
                    
                    if (listaValores.contains(retorno.toString()))
                        return true;
                    else
                        return false;
                }
            }
            catch(Exception ex){ex.printStackTrace();}
        }

        return true;
    }

    private String toGetter(String value){
        return "get" + value.substring(0, 1).toUpperCase() + value.substring(1);
    }
}