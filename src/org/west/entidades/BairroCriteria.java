package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Bairro}
 * @author WestGuerra Ltda.
 */
public class BairroCriteria extends AbstractCriteria{

    public BairroCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Bairro.class));
    }
    
    public BairroCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Bairro uniqueBairro(){
        return (Bairro) uniqueResult();
    }
    
    public List<Bairro> listBairros(){
        List<Bairro> listaBairro = new ArrayList<Bairro>();
        
        for(Object object: super.list())
            listaBairro.add((Bairro) object);
        
        return listaBairro;
    }
}