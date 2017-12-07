package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Altera}.
 * @author WestGuerra Ltda.
 */

public class AlteraDAO {
    
    public static Altera load(String id){
        return (Altera) WestPersistentManager.getSession().load(Altera.class,id);
    }
    
    public static boolean delete(Altera Altera){
        try{
            WestPersistentManager.delete(Altera);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Altera Altera){
        try{
            WestPersistentManager.saveObject(Altera);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Altera Altera){
        return lock(Altera, LockMode.NONE);
    }    
    
    public static boolean lock(Altera Altera,LockMode lock){
        try{
            WestPersistentManager.lock(Altera, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Altera loadAlteraByQuery(String condition,String order){
        Altera[] Alteras = listAlteraByQuery(condition, order);
        if (Alteras != null && Alteras.length > 0)
                return Alteras[0];
        else
                return null;        
    }
    
    public static Altera[] listAlteraByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Altera as Altera");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Altera[]) list.toArray(new Altera[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}