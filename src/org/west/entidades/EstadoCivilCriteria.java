package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 * Classe referente a especialização de Criteria referente à classe
 * {@link EstadoCivil}
 *
 * @author WestGuerra Ltda.
 */
public class EstadoCivilCriteria extends AbstractCriteria {

    public EstadoCivilCriteria() {
        this(WestPersistentManager.getSession().createCriteria(EstadoCivil.class));
    }

    public EstadoCivilCriteria(Criteria criteria) {
        super(criteria);
    }

    public EstadoCivil uniqueEstadoCivil() {
        return (EstadoCivil) uniqueResult();
    }

    public List<EstadoCivil> listEstadoCivils() {
        List<EstadoCivil> listaEstadoCivil = new ArrayList<EstadoCivil>();

        for (Object object : super.list()) {
            listaEstadoCivil.add((EstadoCivil) object);
        }

        return listaEstadoCivil;
    }
}
