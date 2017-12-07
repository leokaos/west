package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Frase}
 * @author WestGuerra Ltda.
 */
public class FraseCriteria extends AbstractCriteria{

    public FraseCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Frase.class));
    }
    
    public FraseCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Frase uniqueFrase(){
        return (Frase) uniqueResult();
    }
    
    public List<Frase> listFrases(){
        List<Frase> listaFrase = new ArrayList<Frase>();
        
        for(Object object: super.list())
            listaFrase.add((Frase) object);
        
        return listaFrase;
    }
}