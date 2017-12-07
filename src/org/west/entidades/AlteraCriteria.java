package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Altera}
 * @author WestGuerra Ltda.
 */
public class AlteraCriteria extends AbstractCriteria{

    public AlteraCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Altera.class));
    }
    
    public AlteraCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Altera uniqueAltera(){
        return (Altera) uniqueResult();
    }
    
    public ImovelCriteria createImovelCriteria(){
        return new ImovelCriteria(createCriteria("imovel"));
    }

    public List<Altera> listAlteras(){
        List<Altera> listaAltera = new ArrayList<Altera>();
        
        for(Object object: super.list())
            listaAltera.add((Altera) object);
        
        return listaAltera;
    }
}