package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Endereco}
 * @author WestGuerra Ltda.
 */
public class EnderecoCriteria extends AbstractCriteria{

    public EnderecoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Endereco.class));
    }
    
    public EnderecoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public ClienteCriteria createClienteCriteria(){
        return new ClienteCriteria(createCriteria("cliente"));
    }
    
    public Endereco uniqueEndereco(){
        return (Endereco) uniqueResult();
    }
    
    public List<Endereco> listEnderecos(){
        List<Endereco> listaEndereco = new ArrayList<Endereco>();
        
        for(Object object: super.list())
            listaEndereco.add((Endereco) object);
        
        return listaEndereco;
    }
}
