package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Servico}.
 * @author WestGuerra Ltda.
 */
public class ServicoDAO {
    
    public static Servico load(Long id){
        return (Servico) WestPersistentManager.getSession().load(Servico.class,id);
    }
    
    public static boolean delete(Servico servico){
        try{
            WestPersistentManager.delete(servico);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean refresh(Servico servico){
        try{
            WestPersistentManager.refresh(servico);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean save(Servico servico){
        try{
            WestPersistentManager.saveObject(servico);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Servico servico){
        return lock(servico, LockMode.NONE);
    }    
    
    public static boolean lock(Servico servico,LockMode lock){
        try{
            WestPersistentManager.lock(servico, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Servico loadServicoByQuery(String condition,String order){
        Servico[] servicos = listServicoByQuery(condition, order);
        if (servicos != null && servicos.length > 0)
                return servicos[0];
        else
                return null;        
    }
    
    public static Servico[] listServicoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Servico as Servico");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Servico[]) list.toArray(new Servico[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}