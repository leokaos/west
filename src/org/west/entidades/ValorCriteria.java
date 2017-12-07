package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Valor}
 * @author WestGuerra Ltda.
 */
public class ValorCriteria extends AbstractCriteria{

    public ValorCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Valor.class));
    }
    
    public ValorCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Valor uniqueValor(){
        return (Valor) uniqueResult();
    }
    
    public List<Valor> listValors(){
        List<Valor> listaValor = new ArrayList<Valor>();
        
        for(Object object: super.list())
            listaValor.add((Valor) object);
        
        return listaValor;
    }
}