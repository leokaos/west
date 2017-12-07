package org.west.entidades;

import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Ponto}.
 * @author WestGuerra Ltda.
 */
public class PontoDAO {
    
    public static Ponto load(Long id){
        return (Ponto) WestPersistentManager.getSession().load(Ponto.class,id);
    }
    
    public static boolean delete(Ponto ponto){
        try{
            WestPersistentManager.delete(ponto);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Ponto ponto){
        try{
            WestPersistentManager.saveObject(ponto);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Ponto Ponto){
        return lock(Ponto, LockMode.NONE);
    }    
    
    public static boolean lock(Ponto ponto,LockMode lock){
        try{
            WestPersistentManager.lock(ponto, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Ponto loadPontoByQuery(String condition,String order){
        Ponto[] Pontos = listPontoByQuery(condition, order);
        if (Pontos != null && Pontos.length > 0)
                return Pontos[0];
        else
                return null;        
    }
    
    public static Ponto[] listPontoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Ponto as Ponto");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Ponto[]) list.toArray(new Ponto[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }

    public static Date getDateServer() {
        try{
            return (Date) WestPersistentManager.getSession().createSQLQuery("Select now();").uniqueResult();
        }
        catch(Exception ex){
            ex.printStackTrace();
            return null;
        }        
    }
}