package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Planta}
 * @author WestGuerra Ltda.
 */
public class PlantaCriteria extends AbstractCriteria{

    public PlantaCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Planta.class));
    }
    
    public PlantaCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public PortariaCriteria createPortariaCriteria(){
        return new PortariaCriteria(createCriteria("portaria"));
    }
    
    public Planta uniquePlanta(){
        return (Planta) uniqueResult();
    }
    
    public List<Planta> listPlantas(){
        List<Planta> listaPlanta = new ArrayList<Planta>();
        
        for(Object object: super.list())
            listaPlanta.add((Planta) object);
        
        return listaPlanta;
    }
}