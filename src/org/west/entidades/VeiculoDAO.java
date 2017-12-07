package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Veiculo}.
 * @author WestGuerra Ltda.
 */

public class VeiculoDAO {
    
    public static Veiculo load(String id){
        return (Veiculo) WestPersistentManager.getSession().load(Veiculo.class,id);
    }
    
    public static boolean delete(Veiculo veiculo){
        try{
            WestPersistentManager.delete(veiculo);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Veiculo veiculo){
        try{
            WestPersistentManager.saveObject(veiculo);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Veiculo veiculo){
        return lock(veiculo, LockMode.NONE);
    }    
    
    public static boolean lock(Veiculo veiculo,LockMode lock){
        try{
            WestPersistentManager.lock(veiculo, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Veiculo loadVeiculoByQuery(String condition,String order){
        Veiculo[] veiculos = listVeiculoByQuery(condition, order);
        if (veiculos != null && veiculos.length > 0)
                return veiculos[0];
        else
                return null;        
    }
    
    public static Veiculo[] listVeiculoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Veiculo as Veiculo");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Veiculo[]) list.toArray(new Veiculo[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}