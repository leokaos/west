package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Saldo}
 * @author WestGuerra Ltda.
 */
public class SaldoCriteria extends AbstractCriteria{

    public SaldoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Saldo.class));
    }
    
    public SaldoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Saldo uniqueSaldo(){
        return (Saldo) uniqueResult();
    }
    
    public List<Saldo> listSaldos(){
        List<Saldo> listaSaldo = new ArrayList<Saldo>();
        
        for(Object object: super.list())
            listaSaldo.add((Saldo) object);
        
        return listaSaldo;
    }
}
