package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Cep}
 * @author WestGuerra Ltda.
 */
public class CepCriteria extends AbstractCriteria{

    public CepCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Cep.class));
    }
    
    public CepCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Cep uniqueCep(){
        return (Cep) uniqueResult();
    }
    
    public List<Cep> listCeps(){
        List<Cep> listaCep = new ArrayList<Cep>();
        
        for(Object object: super.list())
            listaCep.add((Cep) object);
        
        return listaCep;
    }
}