package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Chave}.
 * @author WestGuerra Ltda.
 */

public class ChaveDAO {
    
    public static Chave load(String id){
        return (Chave) WestPersistentManager.getSession().load(Chave.class,id);
    }
    
    public static boolean delete(Chave Chave){
        try{
            WestPersistentManager.delete(Chave);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Chave Chave){
        try{
            WestPersistentManager.saveObject(Chave);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Chave Chave){
        return lock(Chave, LockMode.NONE);
    }    
    
    public static boolean lock(Chave Chave,LockMode lock){
        try{
            WestPersistentManager.lock(Chave, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Chave loadChaveByQuery(String condition,String order){
        Chave[] Chaves = listChaveByQuery(condition, order);
        if (Chaves != null && Chaves.length > 0)
                return Chaves[0];
        else
                return null;        
    }
    
    public static Chave[] listChaveByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Chave as Chave");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Chave[]) list.toArray(new Chave[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}