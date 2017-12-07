package org.west.entidades;



import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Informacao}
 * @author WestGuerra Ltda.
 */
public class InformacaoCriteria extends AbstractCriteria{

    public InformacaoCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Informacao.class));
    }
    
    public InformacaoCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public AnuncioCriteria createAnuncioCriteria(){
        return new AnuncioCriteria(createCriteria("anuncio"));
    }
    
    public ImovelCriteria createImovelCriteria(){
        return new ImovelCriteria(createCriteria("imovel"));
    }    
    
    public Informacao uniqueInformacoes(){
        return (Informacao) uniqueResult();
    }
    
    public List<Informacao> listInformacoes(){
        List<Informacao> listaInformacao = new ArrayList<Informacao>();
        
        for(Object object: super.list())
            listaInformacao.add((Informacao) object);
        
        return listaInformacao;
    }
}