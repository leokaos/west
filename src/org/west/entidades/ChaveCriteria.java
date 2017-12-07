package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Chave}
 * @author WestGuerra Ltda.
 */
public class ChaveCriteria extends AbstractCriteria{

    public ChaveCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Chave.class));
    }
    
    public ChaveCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Chave uniqueChave(){
        return (Chave) uniqueResult();
    }
    
    public List<Chave> listChaves(){
        List<Chave> listaChave = new ArrayList<Chave>();
        
        for(Object object: super.list())
            listaChave.add((Chave) object);
        
        return listaChave;
    }
}
