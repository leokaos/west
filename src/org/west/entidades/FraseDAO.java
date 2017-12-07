package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Frase}.
 * @author WestGuerra Ltda.
 */
public class FraseDAO {
    
    public static Frase load(Long id){
        return (Frase) WestPersistentManager.getSession().load(Frase.class,id);
    }
    
    public static boolean delete(Frase frase){
        try{
            WestPersistentManager.delete(frase);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Frase frase){
        try{
            WestPersistentManager.saveObject(frase);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Frase frase){
        return lock(frase, LockMode.NONE);
    }    
    
    public static boolean lock(Frase frase,LockMode lock){
        try{
            WestPersistentManager.lock(frase, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Frase loadFraseByQuery(String condition,String order){
        Frase[] Frases = listFraseByQuery(condition, order);
        if (Frases != null && Frases.length > 0)
                return Frases[0];
        else
                return null;        
    }
    
    public static Frase[] listFraseByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Frase as Frase");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Frase[]) list.toArray(new Frase[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}