package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Medidas}.
 * @author WestGuerra Ltda.
 */
public class MedidasDAO {
    
    public static Medidas load(String id){
        return (Medidas) WestPersistentManager.getSession().load(Medidas.class,id);
    }
    
    public static boolean delete(Medidas medidas){
        try{
            WestPersistentManager.delete(medidas);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Medidas medidas){
        try{
            WestPersistentManager.saveObject(medidas);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Medidas medidas){
        return lock(medidas, LockMode.NONE);
    }    
    
    public static boolean lock(Medidas medidas,LockMode lock){
        try{
            WestPersistentManager.lock(medidas, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Medidas loadMedidasByQuery(String condition,String order){
        Medidas[] medidases = listMedidasByQuery(condition, order);
        if (medidases != null && medidases.length > 0)
                return medidases[0];
        else
                return null;        
    }
    
    public static Medidas[] listMedidasByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Medidas as Medidas");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Medidas[]) list.toArray(new Medidas[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}