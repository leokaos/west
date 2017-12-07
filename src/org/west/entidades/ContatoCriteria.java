package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Contato}
 * @author WestGuerra Ltda.
 */
public class ContatoCriteria extends AbstractCriteria{

    public ContatoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Contato.class));
    }
    
    public ContatoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Contato uniqueContato(){
        return (Contato) uniqueResult();
    }

    public List<Contato> listContatos(){
        List<Contato> listaContato = new ArrayList<Contato>();
        
        for(Object object: super.list())
            listaContato.add((Contato) object);
        
        return listaContato;
    }
}