package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link LancamentoPadrao}
 * @author WestGuerra Ltda.
 */
public class LancamentoPadraoCriteria extends AbstractCriteria{

    public LancamentoPadraoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(LancamentoPadrao.class));
    }
    
    public LancamentoPadraoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public LancamentoPadrao uniqueLancamentoPadrao(){
        return (LancamentoPadrao) uniqueResult();
    }
    
    public List<LancamentoPadrao> listLancamentoPadraos(){
        List<LancamentoPadrao> listaLancamentoPadrao = new ArrayList<LancamentoPadrao>();
        
        for(Object object: super.list())
            listaLancamentoPadrao.add((LancamentoPadrao) object);
        
        return listaLancamentoPadrao;
    }
}
