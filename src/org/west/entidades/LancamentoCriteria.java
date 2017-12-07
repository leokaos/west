package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 * Classe referente a especialização de Criteria referente à classe
 * {@link Lancamento}
 *
 * @author WestGuerra Ltda.
 */
public class LancamentoCriteria extends AbstractCriteria {

    public LancamentoCriteria() {
        this(WestPersistentManager.getSession().createCriteria(Lancamento.class));
    }

    public LancamentoCriteria(Criteria criteria) {
        super(criteria);
    }

    public Lancamento uniqueLancamento() {
        return (Lancamento) uniqueResult();
    }

    public UsuarioCriteria createUsuarioCriteria() {
        return new UsuarioCriteria(createCriteria("usuario"));
    }

    public ServicoCriteria createServicoCriteria() {
        return new ServicoCriteria(createCriteria("servico"));
    }

    public List<Lancamento> listLancamentos() {
        List<Lancamento> listaLancamento = new ArrayList<Lancamento>();

        for (Object object : super.list()) {
            listaLancamento.add((Lancamento) object);
        }

        return listaLancamento;
    }
}