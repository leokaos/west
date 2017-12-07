package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Numero}.
 * @author WestGuerra Ltda.
 */
public class NumeroDAO {
    
    public static Numero load(Long id){
        return (Numero) WestPersistentManager.getSession().load(Numero.class,id);
    }
    
    public static boolean delete(Numero modifica){
        try{
            WestPersistentManager.delete(modifica);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Numero modifica){
        try{
            WestPersistentManager.saveObject(modifica);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Numero modifica){
        return lock(modifica, LockMode.NONE);
    }    
    
    public static boolean lock(Numero modifica,LockMode lock){
        try{
            WestPersistentManager.lock(modifica, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Numero loadNumeroByQuery(String condition,String order){
        Numero[] modificacoes = listNumeroByQuery(condition, order);
        if (modificacoes != null && modificacoes.length > 0)
                return modificacoes[0];
        else
                return null;        
    }
    
    public static Numero[] listNumeroByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Numero as Numero");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Numero[]) list.toArray(new Numero[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}