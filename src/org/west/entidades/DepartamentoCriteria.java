package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 * Classe referente a especialização de Criteria referente à classe
 * {@link Departamento}
 *
 * @author WestGuerra Ltda.
 */
public class DepartamentoCriteria extends AbstractCriteria {

    public DepartamentoCriteria() {
        this(WestPersistentManager.getSession().createCriteria(Departamento.class));
    }

    public DepartamentoCriteria(Criteria criteria) {
        super(criteria);
    }

    public Departamento uniqueDepartamento() {
        return (Departamento) uniqueResult();
    }

    public UsuarioCriteria createUsuarioCriteria() {
        return new UsuarioCriteria(createCriteria("usuarios"));
    }

    public List<Departamento> listDepartamentos() {
        List<Departamento> listaDepartamento = new ArrayList<Departamento>();

        for (Object object : super.list()) {
            listaDepartamento.add((Departamento) object);
        }

        return listaDepartamento;
    }
}