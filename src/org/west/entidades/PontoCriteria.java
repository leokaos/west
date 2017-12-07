package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Ponto}
 * @author WestGuerra Ltda.
 */
public class PontoCriteria extends AbstractCriteria{

    public PontoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Ponto.class));
    }
    
    public PontoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public UsuarioCriteria createUsuarioCriteria(){
        return new UsuarioCriteria(createCriteria("usuario"));
    }
    
    public Ponto uniquePonto(){
        return (Ponto) uniqueResult();
    }
    
    public List<Ponto> listPontos(){
        List<Ponto> listaPonto = new ArrayList<Ponto>();
        
        for(Object object: super.list())
            listaPonto.add((Ponto) object);
        
        return listaPonto;
    }
}
