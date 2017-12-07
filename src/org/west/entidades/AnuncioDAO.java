package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Anuncio}.
 * @author WestGuerra Ltda.
 */

public class AnuncioDAO {
    
    public static Anuncio load(String id){
        return (Anuncio) WestPersistentManager.getSession().load(Anuncio.class,id);
    }
    
    public static boolean delete(Anuncio Anuncio){
        try{
            WestPersistentManager.delete(Anuncio);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Anuncio Anuncio){
        try{
            WestPersistentManager.saveObject(Anuncio);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Anuncio Anuncio){
        return lock(Anuncio, LockMode.NONE);
    }    
    
    public static boolean lock(Anuncio Anuncio,LockMode lock){
        try{
            WestPersistentManager.lock(Anuncio, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Anuncio loadAnuncioByQuery(String condition,String order){
        Anuncio[] Anuncios = listAnuncioByQuery(condition, order);
        if (Anuncios != null && Anuncios.length > 0)
                return Anuncios[0];
        else
                return null;        
    }
    
    public static Anuncio[] listAnuncioByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Anuncio as Anuncio");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Anuncio[]) list.toArray(new Anuncio[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}