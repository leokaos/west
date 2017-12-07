package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Modifica}
 * @author WestGuerra Ltda.
 */
public class ModificaCriteria extends AbstractCriteria{

    public ModificaCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Modifica.class));
    }
    
    public ModificaCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Modifica uniqueModifica(){
        return (Modifica) uniqueResult();
    }
    
    public List<Modifica> listModificas(){
        List<Modifica> listaModifica = new ArrayList<Modifica>();
        
        for(Object object: super.list())
            listaModifica.add((Modifica) object);
        
        return listaModifica;
    }
}
