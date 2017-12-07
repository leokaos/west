package org.west.entidades;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * Classe no modelo <i>Data Access Object</i> para a classe de persistência
 * {@link Departamento}.
 *
 * @author WestGuerra Ltda.
 */
public class DepartamentoDAO {

    public static Departamento load(String id) {
        return (Departamento) WestPersistentManager.getSession().load(Departamento.class, id);
    }

    public static boolean delete(Departamento Departamento) {
        try {
            WestPersistentManager.delete(Departamento);
            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean save(Departamento Departamento) {
        try {
            WestPersistentManager.saveObject(Departamento);
            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean lock(Departamento Departamento) {
        return lock(Departamento, LockMode.NONE);
    }

    public static boolean lock(Departamento Departamento, LockMode lock) {
        try {
            WestPersistentManager.lock(Departamento, lock);
            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static Departamento loadDepartamentoByQuery(String condition, String order) {
        Departamento[] Departamentos = listDepartamentoByQuery(condition, order);
        if (Departamentos != null && Departamentos.length > 0) {
            return Departamentos[0];
        } else {
            return null;
        }
    }

    public static Departamento[] listDepartamentoByQuery(String condition, String order) {
        StringBuilder sb = new StringBuilder("From Departamento as Departamento");

        if (condition != null) {
            sb.append(" Where ").append(condition);
        }
        if (order != null) {
            sb.append(" Order By ").append(order);
        }

        try {
            Query query = WestPersistentManager.getSession().createQuery(sb.toString());
            List list = query.list();
            return (Departamento[]) list.toArray(new Departamento[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}