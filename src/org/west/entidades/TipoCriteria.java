package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Tipo}
 * @author WestGuerra Ltda.
 */
public class TipoCriteria extends AbstractCriteria{

    public TipoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Tipo.class));
    }
    
    public TipoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Tipo uniqueTipo(){
        return (Tipo) uniqueResult();
    }
    
    public List<Tipo> listTipos(){
        List<Tipo> listaTipo = new ArrayList<Tipo>();
        
        for(Object object: super.list())
            listaTipo.add((Tipo) object);
        
        return listaTipo;
    }
}