package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Cep}.
 * @author WestGuerra Ltda.
 */

public class CepDAO {
    
    public static Cep load(String id){
        return (Cep) WestPersistentManager.getSession().load(Cep.class,id);
    }
    
    public static Cep get(String id){
        return (Cep) WestPersistentManager.getSession().get(Cep.class,id);
    }    
    
    public static boolean delete(Cep Cep){
        try{
            WestPersistentManager.delete(Cep);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Cep Cep){
        try{
            WestPersistentManager.saveObject(Cep);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Cep Cep){
        return lock(Cep, LockMode.NONE);
    }    
    
    public static boolean lock(Cep Cep,LockMode lock){
        try{
            WestPersistentManager.lock(Cep, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Cep loadCepByQuery(String condition,String order){
        Cep[] Ceps = listCepByQuery(condition, order);
        if (Ceps != null && Ceps.length > 0)
                return Ceps[0];
        else
                return null;        
    }
    
    public static Cep[] listCepByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Cep as Cep");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Cep[]) list.toArray(new Cep[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}