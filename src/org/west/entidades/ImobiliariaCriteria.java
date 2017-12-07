package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 * Classe referente a especialização de Criteria referente à classe
 * {@link Imobiliaria}
 *
 * @author WestGuerra Ltda.
 */
public class ImobiliariaCriteria extends AbstractCriteria {

    public ImobiliariaCriteria() {
        this(WestPersistentManager.getSession().createCriteria(Imobiliaria.class));
    }

    public ImobiliariaCriteria(Criteria criteria) {
        super(criteria);
    }

    public Imobiliaria uniqueImobiliaria() {
        return (Imobiliaria) uniqueResult();
    }

    public ClienteCriteria createClienteCriteria() {
        return new ClienteCriteria(createCriteria("cliente"));
    }

    public UsuarioCriteria createUsuarioCriteria() {
        return new UsuarioCriteria(createCriteria("usuario"));
    }

    public VeiculoCriteria createVeiculoCriteria() {
        return new VeiculoCriteria(createCriteria("veiculo"));
    }

    public AnuncioCriteria createAnuncioCriteria() {
        return new AnuncioCriteria(createCriteria("anuncio"));
    }

    public HistoricoCriteria createHistoricoCriteria() {
        return new HistoricoCriteria(createCriteria("historicos"));
    }

    public List<Imobiliaria> listImobiliarias() {
        List<Imobiliaria> listaImobiliaria = new ArrayList<Imobiliaria>();

        for (Object object : super.list()) {
            listaImobiliaria.add((Imobiliaria) object);
        }

        return listaImobiliaria;
    }
}
