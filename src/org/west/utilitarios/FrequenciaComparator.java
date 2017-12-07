package org.west.utilitarios;

import java.util.Comparator;
import org.west.componentes.tabelafilter.ModelPortaria;
import org.west.entidades.Frequencia;
import org.west.entidades.Portaria;

/**
 * Classe que compara dois objetos {@link Frequencia}.
 * Na classe {@link ModelPortaria}, existe a possibilidade de ordenar a tabela por vários campos.
 * Este comparator compara duas instancias de frequëncia, obedecendo a ordem de ascendente de data de visita.
 * @author West Guerra Ltda.
 */
public class FrequenciaComparator implements Comparator{
    
    /**
     * Indice da coluna que foi clicada.
     */
    private Integer columnIndex;

    /**
     * Construtor que recebe o índice da coluna clicada.
     * Para a construção do {@link ModelPortaria}, ele utiliza a quantidade máxima existente de visitas como limitador.
     * Portanto, nen toda portaria tem a quantidade máxima de visitas, para efeitos de ordenação é necessário selecionar a {@link Frequencia}
     * correta para então efetuar a ordenação.
     * @param columnIndex índice da coluna clicada.
     */
    public FrequenciaComparator(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    /**
     * Compara dois objetos {@link Portaria}, comparando as datas de visitas.
     * @param o1 Primeiro objeto {@link Portaria} a ser comparado.
     * @param o2 Segundo objeto {@link Portaria} a ser comparado.
     * @return 
     */
    @Override
    public int compare(Object o1, Object o2) {
        
        Portaria p1 = (Portaria) o1;
        Portaria p2 = (Portaria) o2;
        
        Frequencia f1 = null;
        Frequencia f2 = null;
        
        if (columnIndex < p1.getFrequenciaPeriodo().size())
            f1 = p1.getFrequenciaPeriodo().get(columnIndex);
        
        if (columnIndex < p2.getFrequenciaPeriodo().size())
            f2 = p2.getFrequenciaPeriodo().get(columnIndex);        
        
        if(f1 == null && f2 == null)
                return 0;
        if(f1 != null && f2 == null)
                return -1;
        if(f1 == null && f2 != null)
                return 1;
        
        return f1.getDataVisita().compareTo(f2.getDataVisita());
    }
}