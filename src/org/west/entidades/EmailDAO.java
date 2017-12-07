package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Email}.
 * @author WestGuerra Ltda.
 */

public class EmailDAO {
    
    public static Email load(Long id){
        return (Email) WestPersistentManager.getSession().load(Email.class,id);
    }
    
    public static boolean delete(Email email){
        try{
            WestPersistentManager.delete(email);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Email email){
        try{
            WestPersistentManager.saveObject(email);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Email email){
        return lock(email, LockMode.NONE);
    }    
    
    public static boolean lock(Email email,LockMode lock){
        try{
            WestPersistentManager.lock(email, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Email loadEmailByQuery(String condition,String order){
        Email[] emails = listEmailByQuery(condition, order);
        if (emails != null && emails.length > 0)
                return emails[0];
        else
                return null;        
    }
    
    public static Email[] listEmailByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Email as Email");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Email[]) list.toArray(new Email[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}