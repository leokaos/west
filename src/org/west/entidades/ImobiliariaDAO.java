package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Imobiliaria}.
 * @author WestGuerra Ltda.
 */
public class ImobiliariaDAO {
    
    public static Imobiliaria load(Long id){
        return (Imobiliaria) WestPersistentManager.getSession().load(Imobiliaria.class,id);
    }
    
    public static boolean delete(Imobiliaria imobiliaria){
        try{
            WestPersistentManager.delete(imobiliaria);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Imobiliaria imobiliaria){
        try{
            WestPersistentManager.saveObject(imobiliaria);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Imobiliaria imobiliaria){
        return lock(imobiliaria, LockMode.NONE);
    }    
    
    public static boolean lock(Imobiliaria imobiliaria,LockMode lock){
        try{
            WestPersistentManager.lock(imobiliaria, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Imobiliaria loadImobiliariaByQuery(String condition,String order){
        Imobiliaria[] Imobiliarias = listImobiliariaByQuery(condition, order);
        if (Imobiliarias != null && Imobiliarias.length > 0)
                return Imobiliarias[0];
        else
                return null;        
    }
    
    public static Imobiliaria[] listImobiliariaByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Imobiliaria as Imobiliaria");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Imobiliaria[]) list.toArray(new Imobiliaria[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
    
    public static void refresh(Imobiliaria imobiliaria){
        WestPersistentManager.refresh(imobiliaria);
    }
}