package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Frequencia}
 * @author WestGuerra Ltda.
 */
public class FrequenciaCriteria extends AbstractCriteria{

    public FrequenciaCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Frequencia.class));
    }
    
    public FrequenciaCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Frequencia uniqueFrequencia(){
        return (Frequencia) uniqueResult();
    }
    
    public List<Frequencia> listFrequencias(){
        List<Frequencia> listaFrequencia = new ArrayList<Frequencia>();
        
        for(Object object: super.list())
            listaFrequencia.add((Frequencia) object);
        
        return listaFrequencia;
    }
}
