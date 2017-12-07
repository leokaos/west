package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Portaria}
 * @author WestGuerra Ltda.
 */
public class PortariaCriteria extends AbstractCriteria{

    public PortariaCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Portaria.class));
    }
    
    public PortariaCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Portaria uniquePortaria(){
        return (Portaria) uniqueResult();
    }
    
    public CepCriteria createCepCriteria(){
        return new CepCriteria(createCriteria("cep"));
    }
    
    public PlantaCriteria createPlantaCriteria() {
        return new PlantaCriteria(createCriteria("plantas"));
    }   
    
    public ImovelCriteria createImovelCriteria(){
        return new ImovelCriteria(createCriteria("imoveis"));
    }
    
    public List<Portaria> listPortarias(){
        List<Portaria> listaPortaria = new ArrayList<Portaria>();
        
        for(Object object: super.list())
            listaPortaria.add((Portaria) object);
        
        return listaPortaria;
    }
}