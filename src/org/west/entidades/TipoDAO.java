package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Tipo}.
 * @author WestGuerra Ltda.
 */

public class TipoDAO {
    
    public static Tipo load(String id){
        return (Tipo) WestPersistentManager.getSession().load(Tipo.class,id);
    }
    
    public static boolean delete(Tipo tipo){
        try{
            WestPersistentManager.delete(tipo);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Tipo tipo){
        try{
            WestPersistentManager.saveObject(tipo);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Tipo tipo){
        return lock(tipo, LockMode.NONE);
    }    
    
    public static boolean lock(Tipo tipo,LockMode lock){
        try{
            WestPersistentManager.lock(tipo, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Tipo loadTipoByQuery(String condition,String order){
        Tipo[] tipos = listTipoByQuery(condition, order);
        if (tipos != null && tipos.length > 0)
                return tipos[0];
        else
                return null;        
    }
    
    public static Tipo[] listTipoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Tipo as Tipo");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Tipo[]) list.toArray(new Tipo[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}