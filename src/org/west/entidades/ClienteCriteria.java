package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Cliente}
 * @author WestGuerra Ltda.
 */
public class ClienteCriteria extends AbstractCriteria{

    public ClienteCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Cliente.class));
    }
    
    public ClienteCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public EmailCriteria createEmailCriteria(){
        return new EmailCriteria(createCriteria("emails"));
    }
        
    public Cliente uniqueCliente(){
        return (Cliente) uniqueResult();
    }
    
    public List<Cliente> listClientes(){
        List<Cliente> listaCliente = new ArrayList<Cliente>();
        
        for(Object object: super.list())
            listaCliente.add((Cliente) object);
        
        return listaCliente;
    }
}
