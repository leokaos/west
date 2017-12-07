package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Servico}
 * @author WestGuerra Ltda.
 */
public class ServicoCriteria extends AbstractCriteria{

    public ServicoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Servico.class));
    }
    
    public ServicoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Servico uniqueServico(){
        return (Servico) uniqueResult();
    }
    
    public List<Servico> listServicos(){
        List<Servico> listaServico = new ArrayList<Servico>();
        
        for(Object object: super.list())
            listaServico.add((Servico) object);
        
        return listaServico;
    }
}
