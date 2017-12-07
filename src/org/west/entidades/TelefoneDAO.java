package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Telefone}.
 * @author WestGuerra Ltda.
 */
public class TelefoneDAO {
    
    public static Telefone load(Long id){
        return (Telefone) WestPersistentManager.getSession().load(Telefone.class,id);
    }
    
    public static boolean delete(Telefone telefone){
        try{
            WestPersistentManager.delete(telefone);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Telefone telefone){
        try{
            WestPersistentManager.saveObject(telefone);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Telefone telefone){
        return lock(telefone, LockMode.NONE);
    }    
    
    public static boolean lock(Telefone telefone,LockMode lock){
        try{
            WestPersistentManager.lock(telefone, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Telefone loadTelefoneByQuery(String condition,String order){
        Telefone[] telefones = listTelefoneByQuery(condition, order);
        if (telefones != null && telefones.length > 0)
                return telefones[0];
        else
                return null;        
    }
    
    public static Telefone[] listTelefoneByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Telefone as Telefone");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Telefone[]) list.toArray(new Telefone[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}