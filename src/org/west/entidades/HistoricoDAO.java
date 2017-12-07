package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Historico}.
 * @author WestGuerra Ltda.
 */

public class HistoricoDAO {
    
    public static Historico load(Long id){
        return (Historico) WestPersistentManager.getSession().load(Historico.class,id);
    }
    
    public static boolean delete(Historico historico){
        try{
            WestPersistentManager.delete(historico);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Historico historico){
        try{
            WestPersistentManager.saveObject(historico);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Historico historico){
        return lock(historico, LockMode.NONE);
    }    
    
    public static boolean lock(Historico historico,LockMode lock){
        try{
            WestPersistentManager.lock(historico, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Historico loadHistoricoByQuery(String condition,String order){
        Historico[] historicos = listHistoricoByQuery(condition, order);
        if (historicos != null && historicos.length > 0)
                return historicos[0];
        else
                return null;        
    }
    
    public static Historico[] listHistoricoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Historico as Historico");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Historico[]) list.toArray(new Historico[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}