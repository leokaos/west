package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Usuario}.
 * @author WestGuerra Ltda.
 */
public class UsuarioDAO {
    
    public static Usuario load(Long id){
        return (Usuario) WestPersistentManager.getSession().load(Usuario.class,id);
    }
    
    public static boolean delete(Usuario usuario){
        try{
            WestPersistentManager.delete(usuario);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Usuario usuario){
        try{
            WestPersistentManager.saveObject(usuario);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Usuario usuario){
        return lock(usuario, LockMode.NONE);
    }    
    
    public static boolean lock(Usuario usuario,LockMode lock){
        try{
            WestPersistentManager.lock(usuario, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Usuario loadUsuarioByQuery(String condition,String order){
        Usuario[] usuarios = listUsuarioByQuery(condition, order);
        if (usuarios != null && usuarios.length > 0)
                return usuarios[0];
        else
                return null;        
    }
    
    public static Usuario[] listUsuarioByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Usuario as Usuario");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Usuario[]) list.toArray(new Usuario[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
    
    public static Usuario get(Long id){
        return (Usuario) WestPersistentManager.getSession().get(Usuario.class,id);
    }
}