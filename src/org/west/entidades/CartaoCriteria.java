package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Cartao}
 * @author WestGuerra Ltda.
 */
public class CartaoCriteria extends AbstractCriteria{

    public CartaoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Cartao.class));
    }
    
    public CartaoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Cartao uniqueCartao(){
        return (Cartao) uniqueResult();
    }
    
    public List<Cartao> listCartoes(){
        List<Cartao> listaCartao = new ArrayList<Cartao>();
        
        for(Object object: super.list())
            listaCartao.add((Cartao) object);
        
        return listaCartao;
    }
}