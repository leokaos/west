package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Lancamento}.
 * @author WestGuerra Ltda.
 */
public class LancamentoDAO {
    
    public static Lancamento load(Long id){
        return (Lancamento) WestPersistentManager.getSession().load(Lancamento.class,id);
    }
    
    public static boolean delete(Lancamento lancamento){
        try{
            WestPersistentManager.delete(lancamento);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Lancamento lancamento){
        try{
            WestPersistentManager.saveObject(lancamento);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Lancamento lancamento){
        return lock(lancamento, LockMode.NONE);
    }    
    
    public static boolean lock(Lancamento Lancamento,LockMode lock){
        try{
            WestPersistentManager.lock(Lancamento, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Lancamento loadLancamentoByQuery(String condition,String order){
        Lancamento[] Lancamentos = listLancamentoByQuery(condition, order);
        if (Lancamentos != null && Lancamentos.length > 0)
                return Lancamentos[0];
        else
                return null;        
    }
    
    public static Lancamento[] listLancamentoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Lancamento as Lancamento");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Lancamento[]) list.toArray(new Lancamento[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}