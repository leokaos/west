package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Zap}
 * @author WestGuerra Ltda.
 */
public class ZapCriteria extends AbstractCriteria{

    public ZapCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Zap.class));
    }
    
    public ZapCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Zap uniqueZap(){
        return (Zap) uniqueResult();
    }
    
    public List<Zap> listZaps(){
        List<Zap> listaZap = new ArrayList<Zap>();
        
        for(Object object: super.list())
            listaZap.add((Zap) object);
        
        return listaZap;
    }
}