package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Veiculo}
 * @author WestGuerra Ltda.
 */
public class VeiculoCriteria extends AbstractCriteria{

    public VeiculoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Veiculo.class));
    }
    
    public VeiculoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Veiculo uniqueVeiculo(){
        return (Veiculo) uniqueResult();
    }
    
    public List<Veiculo> listVeiculos(){
        List<Veiculo> listaVeiculo = new ArrayList<Veiculo>();
        
        for(Object object: super.list())
            listaVeiculo.add((Veiculo) object);
        
        return listaVeiculo;
    }
}
