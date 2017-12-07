package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Modifica}.
 * @author WestGuerra Ltda.
 */
public class ModificaDAO {
    
    public static Modifica load(Long id){
        return (Modifica) WestPersistentManager.getSession().load(Modifica.class,id);
    }
    
    public static boolean delete(Modifica modifica){
        try{
            WestPersistentManager.delete(modifica);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Modifica modifica){
        try{
            WestPersistentManager.saveObject(modifica);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Modifica modifica){
        return lock(modifica, LockMode.NONE);
    }    
    
    public static boolean lock(Modifica modifica,LockMode lock){
        try{
            WestPersistentManager.lock(modifica, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Modifica loadModificaByQuery(String condition,String order){
        Modifica[] modificacoes = listModificaByQuery(condition, order);
        if (modificacoes != null && modificacoes.length > 0)
                return modificacoes[0];
        else
                return null;        
    }
    
    public static Modifica[] listModificaByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Modifica as Modifica");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Modifica[]) list.toArray(new Modifica[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}