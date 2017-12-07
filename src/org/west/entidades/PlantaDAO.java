package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Planta}.
 * @author WestGuerra Ltda.
 */
public class PlantaDAO {
    
    public static Planta load(Long id){
        return (Planta) WestPersistentManager.getSession().load(Planta.class,id);
    }
    
    public static boolean delete(Planta planta){
        try{
            WestPersistentManager.delete(planta);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Planta planta){
        try{
            WestPersistentManager.saveObject(planta);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Planta planta){
        return lock(planta, LockMode.NONE);
    }    
    
    public static boolean lock(Planta planta,LockMode lock){
        try{
            WestPersistentManager.lock(planta, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Planta loadPlantaByQuery(String condition,String order){
        Planta[] Plantas = listPlantaByQuery(condition, order);
        if (Plantas != null && Plantas.length > 0)
                return Plantas[0];
        else
                return null;        
    }
    
    public static Planta[] listPlantaByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Planta as Planta");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Planta[]) list.toArray(new Planta[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}