package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência
 * {@link EstadoCivil}.
 *
 * @author WestGuerra Ltda.
 */
public class EstadoCivilDAO {

    public static EstadoCivil load(Long id) {
        return (EstadoCivil) WestPersistentManager.getSession().load(EstadoCivil.class, id);
    }

    public static boolean delete(EstadoCivil EstadoCivil) {
        try {
            WestPersistentManager.delete(EstadoCivil);
            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean save(EstadoCivil EstadoCivil) {
        try {
            WestPersistentManager.saveObject(EstadoCivil);
            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean lock(EstadoCivil EstadoCivil) {
        return lock(EstadoCivil, LockMode.NONE);
    }

    public static boolean lock(EstadoCivil EstadoCivil, LockMode lock) {
        try {
            WestPersistentManager.lock(EstadoCivil, lock);
            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static EstadoCivil loadEstadoCivilByQuery(String condition, String order) {
        EstadoCivil[] EstadoCivils = listEstadoCivilByQuery(condition, order);
        if (EstadoCivils != null && EstadoCivils.length > 0) {
            return EstadoCivils[0];
        } else {
            return null;
        }
    }

    public static EstadoCivil[] listEstadoCivilByQuery(String condition, String order) {
        StringBuilder sb = new StringBuilder("From EstadoCivil as EstadoCivil");

        if (condition != null) {
            sb.append(" Where ").append(condition);
        }
        if (order != null) {
            sb.append(" Order By ").append(order);
        }

        try {
            Query query = WestPersistentManager.getSession().createQuery(sb.toString());
            List list = query.list();
            return (EstadoCivil[]) list.toArray(new EstadoCivil[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static EstadoCivil get(Long id) {
        return (EstadoCivil) WestPersistentManager.getSession().get(EstadoCivil.class, id);
    }
}
