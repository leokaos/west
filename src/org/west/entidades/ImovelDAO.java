package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Imovel}.
 * @author WestGuerra Ltda.
 */
public class ImovelDAO {
    
    public static Imovel load(Long id){
        return (Imovel) WestPersistentManager.getSession().load(Imovel.class,id); 
    }
    
    public static Imovel get(Long id){
        return (Imovel) WestPersistentManager.getSession().get(Imovel.class, id);
    }
    
    public static boolean delete(Imovel imovel){
        try{
            WestPersistentManager.delete(imovel);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean refresh(Imovel imovel){
        try{
            WestPersistentManager.refresh(imovel);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean save(Imovel imovel){
        try{
            WestPersistentManager.saveObject(imovel);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Imovel imovel){
        return lock(imovel, LockMode.NONE);
    }    
    
    public static boolean lock(Imovel imovel,LockMode lock){
        try{
            WestPersistentManager.lock(imovel, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Imovel loadImovelByQuery(String condition,String order){
        Imovel[] Imovels = listImovelByQuery(condition, order);
        if (Imovels != null && Imovels.length > 0)
                return Imovels[0];
        else
                return null;        
    }
    
    public static Imovel[] listImovelByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Imovel as Imovel");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Imovel[]) list.toArray(new Imovel[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}