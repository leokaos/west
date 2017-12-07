package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Foto}
 * @author WestGuerra Ltda.
 */
public class FotoCriteria extends AbstractCriteria{

    public FotoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Foto.class));
    }
    
    public FotoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public ImovelCriteria createImovelCriteria(){
        return new ImovelCriteria(createCriteria("imovel"));
    }
    
    public Foto uniqueFoto(){
        return (Foto) uniqueResult();
    }
        
    public List<Foto> listFotos(){
        List<Foto> listaFoto = new ArrayList<Foto>();
        
        for(Object object: super.list())
            listaFoto.add((Foto) object);
        
        return listaFoto;
    }
}