package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 * Classe referente a especialização de Criteria referente à classe
 * {@link Imovel}
 *
 * @author WestGuerra Ltda.
 */
public class ImovelCriteria extends AbstractCriteria {

    public ImovelCriteria() {
        this(WestPersistentManager.getSession().createCriteria(Imovel.class));
    }

    public ImovelCriteria(Criteria criteria) {
        super(criteria);
    }

    public Imovel uniqueImovel() {
        return (Imovel) uniqueResult();
    }

    public UsuarioCriteria createUsuarioCriteria() {
        return new UsuarioCriteria(createCriteria("usuarios"));
    }

    public InformacaoCriteria createInformacaoCriteria() {
        return new InformacaoCriteria(createCriteria("informacoes"));
    }

    public TipoCriteria createTipoCriteria() {
        return new TipoCriteria(createCriteria("tipo"));
    }

    public BairroCriteria createBairroCriteria() {
        return new BairroCriteria(createCriteria("bairro"));
    }

    public CepCriteria createCepCriteria() {
        return new CepCriteria(createCriteria("cep"));
    }

    public FotoCriteria createFotoCriteria() {
        return new FotoCriteria(createCriteria("fotos"));
    }

    public AnuncioCriteria createAnuncioCriteria() {
        return new AnuncioCriteria(createCriteria("anuncios"));
    }

    public NumeroCriteria createNumeroCriteria() {
        return new NumeroCriteria(createCriteria("numeros"));
    }

    public List<Imovel> listImovels() {
        List<Imovel> listaImovel = new ArrayList<Imovel>();

        for (Object object : super.list()) {
            listaImovel.add((Imovel) object);
        }

        return listaImovel;
    }

    public MedidasCriteria createMedidasCriteria() {
        return new MedidasCriteria(createCriteria("medidass"));
    }
}