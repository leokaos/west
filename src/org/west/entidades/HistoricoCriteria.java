package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Historico}
 * @author WestGuerra Ltda.
 */
public class HistoricoCriteria extends AbstractCriteria{

    public HistoricoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Historico.class));
    }
    
    public HistoricoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Historico uniqueHistorico(){
        return (Historico) uniqueResult();
    }
    
    public UsuarioCriteria createUsuarioCriteria(){
        return new UsuarioCriteria(createCriteria("usuario"));
    }
    
    public List<Historico> listHistoricos(){
        List<Historico> listaHistorico = new ArrayList<Historico>();
        
        for(Object object: super.list())
            listaHistorico.add((Historico) object);
        
        return listaHistorico;
    }
}
