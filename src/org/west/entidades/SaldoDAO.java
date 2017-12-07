package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Saldo}.
 * @author WestGuerra Ltda.
 */

public class SaldoDAO {
    
    public static Saldo load(String id){
        return (Saldo) WestPersistentManager.getSession().load(Saldo.class,id);
    }
    
    public static boolean delete(Saldo saldo){
        try{
            WestPersistentManager.delete(saldo);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Saldo saldo){
        try{
            WestPersistentManager.saveObject(saldo);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Saldo saldo){
        return lock(saldo, LockMode.NONE);
    }    
    
    public static boolean lock(Saldo saldo,LockMode lock){
        try{
            WestPersistentManager.lock(saldo, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Saldo loadSaldoByQuery(String condition,String order){
        Saldo[] saldos = listSaldoByQuery(condition, order);
        if (saldos != null && saldos.length > 0)
                return saldos[0];
        else
                return null;        
    }
    
    public static Saldo[] listSaldoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Saldo as Saldo");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Saldo[]) list.toArray(new Saldo[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}