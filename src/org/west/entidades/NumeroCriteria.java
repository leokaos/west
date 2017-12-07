package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Numero}
 * @author WestGuerra Ltda.
 */
public class NumeroCriteria extends AbstractCriteria{

    public NumeroCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Numero.class));
    }
    
    public NumeroCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Numero uniqueNumero(){
        return (Numero) uniqueResult();
    }
    
    public List<Numero> listNumeros(){
        List<Numero> listaNumero = new ArrayList<Numero>();
        
        for(Object object: super.list())
            listaNumero.add((Numero) object);
        
        return listaNumero;
    }
}
