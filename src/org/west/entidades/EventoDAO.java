package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Evento}.
 * @author WestGuerra Ltda.
 */

public class EventoDAO {
    
    public static Evento load(Long id){
        return (Evento) WestPersistentManager.getSession().load(Evento.class,id);
    }
    
    public static boolean delete(Evento evento){
        try{
            WestPersistentManager.delete(evento);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Evento evento){
        try{
            WestPersistentManager.saveObject(evento);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Evento evento){
        return lock(evento, LockMode.NONE);
    }    
    
    public static boolean lock(Evento evento,LockMode lock){
        try{
            WestPersistentManager.lock(evento, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Evento loadEventoByQuery(String condition,String order){
        Evento[] eventos = listEventoByQuery(condition, order);
        if (eventos != null && eventos.length > 0)
                return eventos[0];
        else
                return null;        
    }
    
    public static Evento[] listEventoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Evento as Evento");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Evento[]) list.toArray(new Evento[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}