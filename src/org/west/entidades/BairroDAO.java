package org.west.entidades;



import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Bairro}.
 * @author WestGuerra Ltda.
 */

public class BairroDAO {
    
    public static Bairro load(String nome,String cidade){
        try {
            BairroKey bairroKey = new BairroKey();
            bairroKey.setNome(nome);
            bairroKey.setCidade(cidade);

            return (Bairro) WestPersistentManager.getSession().load(Bairro.class, bairroKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean delete(Bairro Bairro){
        try{
            WestPersistentManager.delete(Bairro);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Bairro Bairro){
        try{
            WestPersistentManager.saveObject(Bairro);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Bairro Bairro){
        return lock(Bairro, LockMode.NONE);
    }    
    
    public static boolean lock(Bairro Bairro,LockMode lock){
        try{
            WestPersistentManager.lock(Bairro, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Bairro loadBairroByQuery(String condition,String order){
        Bairro[] Bairros = listBairroByQuery(condition, order);
        if (Bairros != null && Bairros.length > 0)
                return Bairros[0];
        else
                return null;        
    }
    
    public static Bairro[] listBairroByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Bairro as Bairro");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Bairro[]) list.toArray(new Bairro[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}