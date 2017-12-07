package org.west.converters;

import org.west.entidades.Imovel;

/**
 * Interface que tem como finalidade converter um Imóvel para um formato específico.
 * @author WestGuerra Ltda.
 */
public interface ImovelConverter {
    
    /**
     * Converte um Imóvel para o formato específico. Esse formato pode ser de qualquer tipo, desde que o {@link ControlExport} 
     * conheça.
     * @param imovel Imóvel a ser convertido.
     * @return obj : Objeto já convertido para o formato desejado.
     */       
    public Object toFormato(Imovel imovel);
}