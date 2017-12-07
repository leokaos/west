package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Email}
 * @author WestGuerra Ltda.
 */
public class EmailCriteria extends AbstractCriteria{

    public EmailCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Email.class));
    }
    
    public EmailCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Email uniqueEmail(){
        return (Email) uniqueResult();
    }
    
    public List<Email> listEmails(){
        List<Email> listaEmail = new ArrayList<Email>();
        
        for(Object object: super.list())
            listaEmail.add((Email) object);
        
        return listaEmail;
    }
}
