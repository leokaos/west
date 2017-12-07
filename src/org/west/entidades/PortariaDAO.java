package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Portaria}.
 * @author WestGuerra Ltda.
 */

public class PortariaDAO {
    
    public static Portaria load(Long id){
        return (Portaria) WestPersistentManager.getSession().load(Portaria.class,id);
    }
    
    public static Portaria get(Long id){
        return (Portaria) WestPersistentManager.getSession().get(Portaria.class, id);
    }
    
    public static boolean delete(Portaria portaria){
        try{
            WestPersistentManager.delete(portaria);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Portaria portaria){
        try{
            WestPersistentManager.saveObject(portaria);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Portaria portaria){
        return lock(portaria, LockMode.NONE);
    }    
    
    public static boolean lock(Portaria portaria,LockMode lock){
        try{
            WestPersistentManager.lock(portaria, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Portaria loadPortariaByQuery(String condition,String order){
        Portaria[] portarias = listPortariaByQuery(condition, order);
        if (portarias != null && portarias.length > 0)
                return portarias[0];
        else
                return null;        
    }
    
    public static Portaria[] listPortariaByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Portaria as Portaria");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Portaria[]) list.toArray(new Portaria[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
    
    public static void refresh(Portaria portaria){
        WestPersistentManager.refresh(portaria);
    }
}