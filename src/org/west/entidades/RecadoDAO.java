package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Recado}.
 * @author WestGuerra Ltda.
 */
public class RecadoDAO {
    
    public static Recado load(Long id){
        return (Recado) WestPersistentManager.getSession().load(Recado.class,id);
    }
    
    public static boolean delete(Recado recado){
        try{
            WestPersistentManager.delete(recado);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Recado recado){
        try{
            WestPersistentManager.saveObject(recado);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Recado recado){
        return lock(recado, LockMode.NONE);
    }    
    
    public static boolean lock(Recado recado,LockMode lock){
        try{
            WestPersistentManager.lock(recado, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Recado loadRecadoByQuery(String condition,String order){
        Recado[] recados = listRecadoByQuery(condition, order);
        if (recados != null && recados.length > 0)
                return recados[0];
        else
                return null;        
    }
    
    public static Recado[] listRecadoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Recado as Recado");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Recado[]) list.toArray(new Recado[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}