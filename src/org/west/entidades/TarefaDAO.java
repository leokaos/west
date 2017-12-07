package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Tarefa}.
 * @author WestGuerra Ltda.
 */

public class TarefaDAO {
    
    public static Tarefa load(Long id){
        return (Tarefa) WestPersistentManager.getSession().load(Tarefa.class,id);
    }
    
    public static boolean delete(Tarefa tarefa){
        try{
            WestPersistentManager.delete(tarefa);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean refresh(Tarefa tarefa){
        try{
            WestPersistentManager.refresh(tarefa);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean save(Tarefa tarefa){
        try{
            WestPersistentManager.saveObject(tarefa);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Tarefa tarefa){
        return lock(tarefa, LockMode.NONE);
    }    
    
    public static boolean lock(Tarefa tarefa,LockMode lock){
        try{
            WestPersistentManager.lock(tarefa, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Tarefa loadTarefaByQuery(String condition,String order){
        Tarefa[] tarefas = listTarefaByQuery(condition, order);
        if (tarefas != null && tarefas.length > 0)
                return tarefas[0];
        else
                return null;        
    }
    
    public static Tarefa[] listTarefaByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Tarefa as Tarefa");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Tarefa[]) list.toArray(new Tarefa[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}