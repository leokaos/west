package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 * Classe referente a especialização de Criteria referente à classe
 * {@link Medidas}
 *
 * @author WestGuerra Ltda.
 */
public class MedidasCriteria extends AbstractCriteria {

    public MedidasCriteria() {
        this(WestPersistentManager.getSession().createCriteria(Medidas.class));
    }

    public MedidasCriteria(Criteria criteria) {
        super(criteria);
    }

    public Medidas uniqueMedidas() {
        return (Medidas) uniqueResult();
    }

    public List<Medidas> listMedidass() {
        List<Medidas> listaMedidas = new ArrayList<Medidas>();

        for (Object object : super.list()) {
            listaMedidas.add((Medidas) object);
        }

        return listaMedidas;
    }
}
