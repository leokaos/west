package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Lazer}
 * @author WestGuerra Ltda.
 */
public class LazerCriteria extends AbstractCriteria{

    public LazerCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Lazer.class));
    }
    
    public LazerCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Lazer uniqueLazer(){
        return (Lazer) uniqueResult();
    }
    
    public List<Lazer> listLazers(){
        List<Lazer> listaLazer = new ArrayList<Lazer>();
        
        for(Object object: super.list())
            listaLazer.add((Lazer) object);
        
        return listaLazer;
    }
}
