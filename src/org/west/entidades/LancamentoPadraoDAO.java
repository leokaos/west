package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link LancamentoPadrao}.
 * @author WestGuerra Ltda.
 */
public class LancamentoPadraoDAO {
    
    public static LancamentoPadrao load(String id){
        return (LancamentoPadrao) WestPersistentManager.getSession().load(LancamentoPadrao.class,id);
    }
    
    public static boolean delete(LancamentoPadrao lancamentoPadrao){
        try{
            WestPersistentManager.delete(lancamentoPadrao);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(LancamentoPadrao lancamentoPadrao){
        try{
            WestPersistentManager.saveObject(lancamentoPadrao);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(LancamentoPadrao lancamentoPadrao){
        return lock(lancamentoPadrao, LockMode.NONE);
    }    
    
    public static boolean lock(LancamentoPadrao lancamentoPadrao,LockMode lock){
        try{
            WestPersistentManager.lock(lancamentoPadrao, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static LancamentoPadrao loadLancamentoPadraoByQuery(String condition,String order){
        LancamentoPadrao[] LancamentoPadraos = listLancamentoPadraoByQuery(condition, order);
        if (LancamentoPadraos != null && LancamentoPadraos.length > 0)
                return LancamentoPadraos[0];
        else
                return null;        
    }
    
    public static LancamentoPadrao[] listLancamentoPadraoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From LancamentoPadrao as LancamentoPadrao");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (LancamentoPadrao[]) list.toArray(new LancamentoPadrao[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}