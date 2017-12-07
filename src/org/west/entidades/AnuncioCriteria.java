package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Anuncio}
 * @author WestGuerra Ltda.
 */
public class AnuncioCriteria extends AbstractCriteria{

    public AnuncioCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Anuncio.class));
    }
    
    public AnuncioCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public Anuncio uniqueAnuncio(){
        return (Anuncio) uniqueResult();
    }
    
    public VeiculoCriteria createVeiculoCriteria(){
        return new VeiculoCriteria(createCriteria("veiculo"));
    }

    public List<Anuncio> listAnuncios(){
        List<Anuncio> listaAnuncio = new ArrayList<Anuncio>();
        
        for(Object object: super.list())
            listaAnuncio.add((Anuncio) object);
        
        return listaAnuncio;
    }
}