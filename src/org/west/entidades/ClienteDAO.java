package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Cliente}.
 * @author WestGuerra Ltda.
 */
public class ClienteDAO {
    
    public static Cliente load(Long id){
        return (Cliente) WestPersistentManager.getSession().load(Cliente.class,id);
    }
    
    public static boolean delete(Cliente cliente){
        try{
            WestPersistentManager.delete(cliente);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Cliente cliente){
        try{
            WestPersistentManager.saveObject(cliente);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Cliente cliente){
        return lock(cliente, LockMode.NONE);
    }    
    
    public static boolean lock(Cliente cliente,LockMode lock){
        try{
            WestPersistentManager.lock(cliente, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static void refresh(Cliente cliente){
        WestPersistentManager.refresh(cliente);
    }
    
    public static Cliente loadClienteByQuery(String condition,String order){
        Cliente[] clientes = listClienteByQuery(condition, order);
        if (clientes != null && clientes.length > 0)
                return clientes[0];
        else
                return null;        
    }
    
    public static Cliente[] listClienteByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Cliente as Cliente");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Cliente[]) list.toArray(new Cliente[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}