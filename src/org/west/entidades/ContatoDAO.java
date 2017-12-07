package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Contato}.
 * @author WestGuerra Ltda.
 */
public class ContatoDAO {
    
    public static Contato load(String id){
        return (Contato) WestPersistentManager.getSession().load(Contato.class,id);
    }
    
    public static boolean delete(Contato contato){
        try{
            WestPersistentManager.delete(contato);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Contato contato){
        try{
            WestPersistentManager.saveObject(contato);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Contato contato){
        return lock(contato, LockMode.NONE);
    }    
    
    public static boolean lock(Contato contato,LockMode lock){
        try{
            WestPersistentManager.lock(contato, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Contato loadContatoByQuery(String condition,String order){
        Contato[] contatos = listContatoByQuery(condition, order);
        if (contatos != null && contatos.length > 0)
                return contatos[0];
        else
                return null;        
    }
    
    public static Contato[] listContatoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Contato as Contato");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Contato[]) list.toArray(new Contato[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}