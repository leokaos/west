package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Valor}.
 * @author WestGuerra Ltda.
 */
public class ValorDAO {
    
    public static Valor load(Long id){
        return (Valor) WestPersistentManager.getSession().load(Valor.class,id);
    }
    
    public static boolean delete(Valor valor){
        try{
            WestPersistentManager.delete(valor);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Valor valor){
        try{
            WestPersistentManager.saveObject(valor);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Valor valor){
        return lock(valor, LockMode.NONE);
    }    
    
    public static boolean lock(Valor valor,LockMode lock){
        try{
            WestPersistentManager.lock(valor, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Valor loadValorByQuery(String condition,String order){
        Valor[] valores = listValorByQuery(condition, order);
        if (valores != null && valores.length > 0)
                return valores[0];
        else
                return null;        
    }
    
    public static Valor[] listValorByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Valor as Valor");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Valor[]) list.toArray(new Valor[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}