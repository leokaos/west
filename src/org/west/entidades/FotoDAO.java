package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Foto}.
 * @author WestGuerra Ltda.
 */

public class FotoDAO {
    
    public static Foto load(Long id){
        return (Foto) WestPersistentManager.getSession().load(Foto.class,id);
    }
    
    public static boolean delete(Foto foto){
        try{
            WestPersistentManager.delete(foto);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Foto foto){
        try{
            WestPersistentManager.saveObject(foto);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Foto foto){
        return lock(foto, LockMode.NONE);
    }    
    
    public static boolean lock(Foto foto,LockMode lock){
        try{
            WestPersistentManager.lock(foto, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Foto loadFotoByQuery(String condition,String order){
        Foto[] Fotos = listFotoByQuery(condition, order);
        if (Fotos != null && Fotos.length > 0)
                return Fotos[0];
        else
                return null;        
    }
    
    public static Foto[] listFotoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Foto as Foto");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Foto[]) list.toArray(new Foto[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}