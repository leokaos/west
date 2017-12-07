package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Recado}
 * @author WestGuerra Ltda.
 */
public class RecadoCriteria extends AbstractCriteria{

    public RecadoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Recado.class));
    }
    
    public RecadoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Recado uniqueRecado(){
        return (Recado) uniqueResult();
    }
    
    public List<Recado> listRecados(){
        List<Recado> listaRecado = new ArrayList<Recado>();
        
        for(Object object: super.list())
            listaRecado.add((Recado) object);
        
        return listaRecado;
    }
}
