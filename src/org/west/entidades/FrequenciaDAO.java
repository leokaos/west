package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Frequencia}.
 * @author WestGuerra Ltda.
 */
public class FrequenciaDAO {
    
    public static Frequencia load(Long id){
        return (Frequencia) WestPersistentManager.getSession().load(Frequencia.class,id);
    }
    
    public static boolean delete(Frequencia frequencia){
        try{
            WestPersistentManager.delete(frequencia);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Frequencia frequencia){
        try{
            WestPersistentManager.saveObject(frequencia);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Frequencia frequencia){
        return lock(frequencia, LockMode.NONE);
    }    
    
    public static boolean lock(Frequencia frequencia,LockMode lock){
        try{
            WestPersistentManager.lock(frequencia, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Frequencia loadFrequenciaByQuery(String condition,String order){
        Frequencia[] frequencias = listFrequenciaByQuery(condition, order);
        if (frequencias != null && frequencias.length > 0)
                return frequencias[0];
        else
                return null;        
    }
    
    public static Frequencia[] listFrequenciaByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Frequencia as Frequencia");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Frequencia[]) list.toArray(new Frequencia[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}