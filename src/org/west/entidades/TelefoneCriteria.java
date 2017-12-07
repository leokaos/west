package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Telefone}
 * @author WestGuerra Ltda.
 */
public class TelefoneCriteria extends AbstractCriteria{

    public TelefoneCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Telefone.class));
    }
    
    public TelefoneCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Telefone uniqueTelefone(){
        return (Telefone) uniqueResult();
    }
    
    public List<Telefone> listTelefones(){
        List<Telefone> listaTelefone = new ArrayList<Telefone>();
        
        for(Object object: super.list())
            listaTelefone.add((Telefone) object);
        
        return listaTelefone;
    }
}
