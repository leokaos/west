package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência {@link Informacao}.
 * @author WestGuerra Ltda.
 */
public class InformacaoDAO {
    
    public static Informacao load(Imovel imovel,Anuncio anuncio){
        try {
            InformacaoKey informacaoKey = new InformacaoKey();
            informacaoKey.setAnuncio(anuncio.getNome());
            informacaoKey.setReferencia(imovel.getReferencia());

            return (Informacao) WestPersistentManager.getSession().load(Informacao.class, informacaoKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Informacao get(Imovel imovel,Anuncio anuncio){
        try {
            InformacaoKey informacaoKey = new InformacaoKey();
            informacaoKey.setAnuncio(anuncio.getNome());
            informacaoKey.setReferencia(imovel.getReferencia());

            return (Informacao) WestPersistentManager.getSession().get(Informacao.class, informacaoKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }    
    
    public static boolean delete(Informacao informacao){
        try{
            WestPersistentManager.delete(informacao);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }
    
    public static boolean save(Informacao informacao){
        try{
            WestPersistentManager.saveObject(informacao);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static boolean lock(Informacao informacao){
        return lock(informacao, LockMode.NONE);
    }    
    
    public static boolean lock(Informacao informacao,LockMode lock){
        try{
            WestPersistentManager.lock(informacao, lock);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }        
    }    
    
    public static Informacao loadInformacaoByQuery(String condition,String order){
        Informacao[] informacoes = listInformacaoByQuery(condition, order);
        if (informacoes != null && informacoes.length > 0)
                return informacoes[0];
        else
                return null;        
    }
    
    public static Informacao[] listInformacaoByQuery(String condition,String order){
        StringBuilder sb = new StringBuilder("From Informacao as Informacao");
        
        if (condition != null)
                sb.append(" Where ").append(condition);
        if (order != null)
                sb.append(" Order By ").append(order);
        
        try {
                Query query = WestPersistentManager.getSession().createQuery(sb.toString());
                List list = query.list();
                return (Informacao[]) list.toArray(new Informacao[list.size()]);
        }
        catch (Exception e) {
                e.printStackTrace();
                return null;
        }               
    }
}