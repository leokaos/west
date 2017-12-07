package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Evento}
 * @author WestGuerra Ltda.
 */
public class EventoCriteria extends AbstractCriteria{

    public EventoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Evento.class));
    }
    
    public EventoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Evento uniqueEvento(){
        return (Evento) uniqueResult();
    }
    
    public List<Evento> listEventos(){
        List<Evento> listaEvento = new ArrayList<Evento>();
        
        for(Object object: super.list())
            listaEvento.add((Evento) object);
        
        return listaEvento;
    }
}
