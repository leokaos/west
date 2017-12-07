package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Cartao}.
 * @author WestGuerra Ltda.
 */

public class CartaoDAO {
       
    public static boolean delete(Cartao Cartao){
        try{
            WestPersistentManager.delete(Cartao);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Cartao Cartao){
        try{
            WestPersistentManager.saveObject(Cartao);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Cartao Cartao){
        return lock(Cartao, LockMode.NONE);
    }    
    
    public static boolean lock(Cartao Cartao,LockMode lock){
        try{
            WestPersistentManager.lock(Cartao, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Cartao loadCartaoByQuery(String condition,String order){
        Cartao[] cartoes = listCartaoByQuery(condition, order);
        if (cartoes != null && cartoes.length > 0)
                return cartoes[0];
        else
                return null;        
    }
    
    public static Cartao[] listCartaoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Cartao as Cartao");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Cartao[]) list.toArray(new Cartao[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}