package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Tarefa}
 * @author WestGuerra Ltda.
 */
public class TarefaCriteria extends AbstractCriteria{

    public TarefaCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Tarefa.class));
    }
    
    public TarefaCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public ServicoCriteria createServicoCriteria(){
        return new ServicoCriteria(createCriteria("servico"));
    }
    
    public UsuarioCriteria createUsuarioCriteria(){
        return new UsuarioCriteria(createCriteria("usuario"));
    }
    
    public Tarefa uniqueTarefa(){
        return (Tarefa) uniqueResult();
    }
    
    public List<Tarefa> listTarefas(){
        List<Tarefa> listaTarefa = new ArrayList<Tarefa>();
        
        for(Object object: super.list())
            listaTarefa.add((Tarefa) object);
        
        return listaTarefa;
    }
}