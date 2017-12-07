package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Lazer}.
 * @author WestGuerra Ltda.
 */
public class LazerDAO {
    
    public static Lazer load(String id){
        return (Lazer) WestPersistentManager.getSession().load(Lazer.class,id);
    }
    
    public static boolean delete(Lazer lazer){
        try{
            WestPersistentManager.delete(lazer);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Lazer lazer){
        try{
            WestPersistentManager.saveObject(lazer);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Lazer lazer){
        return lock(lazer, LockMode.NONE);
    }    
    
    public static boolean lock(Lazer lazer,LockMode lock){
        try{
            WestPersistentManager.lock(lazer, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Lazer loadLazerByQuery(String condition,String order){
        Lazer[] lazeres = listLazerByQuery(condition, order);
        if (lazeres != null && lazeres.length > 0)
                return lazeres[0];
        else
                return null;        
    }
    
    public static Lazer[] listLazerByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Lazer as Lazer");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Lazer[]) list.toArray(new Lazer[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}