package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Endereco}.
 * @author WestGuerra Ltda.
 */

public class EnderecoDAO {
    
    public static Endereco load(String id){
        return (Endereco) WestPersistentManager.getSession().load(Endereco.class,id);
    }
    
    public static boolean delete(Endereco endereco){
        try{
            WestPersistentManager.delete(endereco);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Endereco endereco){
        try{
            WestPersistentManager.saveObject(endereco);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Endereco endereco){
        return lock(endereco, LockMode.NONE);
    }    
    
    public static boolean lock(Endereco endereco,LockMode lock){
        try{
            WestPersistentManager.lock(endereco, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Endereco loadEnderecoByQuery(String condition,String order){
        Endereco[] Enderecos = listEnderecoByQuery(condition, order);
        if (Enderecos != null && Enderecos.length > 0)
                return Enderecos[0];
        else
                return null;        
    }
    
    public static Endereco[] listEnderecoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Endereco as Endereco");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Endereco[]) list.toArray(new Endereco[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}