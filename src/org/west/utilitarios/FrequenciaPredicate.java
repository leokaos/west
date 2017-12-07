package org.west.utilitarios;

import org.west.entidades.Frequencia;
import org.west.entidades.Portaria;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.commons.collections.Predicate;

public class FrequenciaPredicate implements Predicate{
    
    private Integer index;
    private List<String> valores;

    public FrequenciaPredicate(Integer index, List<String> valores) {
        this.index = index;
        this.valores = valores;
    }

    @Override
    public boolean evaluate(Object o) {
        Portaria portaria = (Portaria) o;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        
        List<Frequencia> lista = portaria.getFrequenciaPeriodo();
        
        String obj = "";
        
        if (lista != null && lista.size() > 0 && index < lista.size()){
            Frequencia frequencia = portaria.getFrequenciaPeriodo().get(index);

            obj = formato.format(frequencia.getDataVisita());
        }

        return valores.contains(obj);
    }    
}