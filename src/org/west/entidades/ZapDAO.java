package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Zap}.
 * @author WestGuerra Ltda.
 */
public class ZapDAO {
    
    public static Zap load(String id){
        return (Zap) WestPersistentManager.getSession().load(Zap.class,id);
    }
    
    public static Zap get(String id){
        return (Zap) WestPersistentManager.getSession().get(Zap.class,id);
    }    
    
    public static boolean delete(Zap zap){
        try{
            WestPersistentManager.delete(zap);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Zap zap){
        try{
            WestPersistentManager.saveObject(zap);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Zap zap){
        return lock(zap, LockMode.NONE);
    }    
    
    public static boolean lock(Zap zap,LockMode lock){
        try{
            WestPersistentManager.lock(zap, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Zap loadZapByQuery(String condition,String order){
        Zap[] zaps = listZapByQuery(condition, order);
        if (zaps != null && zaps.length > 0)
                return zaps[0];
        else
                return null;        
    }
    
    public static Zap[] listZapByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Zap as Zap");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Zap[]) list.toArray(new Zap[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}