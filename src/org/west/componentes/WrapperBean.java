package org.west.componentes;

import org.west.entidades.WestPersistentManager;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.swingBean.descriptor.look.LookProvider;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperBean implements ComponentWrapper {

    private JComboBox combo;
    private String bean;
    private List lista;
    private String query;
    private String order;
    private DefaultComboBoxModel model;
    private String limit;
    private String join;

    @Override
    public Object getValue() {
        return this.combo.getModel().getSelectedItem();
    }

    @Override
    public void setValue(Object o) {
        this.combo.getModel().setSelectedItem(o);
    }

    @Override
    public void cleanValue() {
        if (this.combo.getModel() != null) {
            this.combo.getModel().setSelectedItem(null);
        }
    }

    @Override
    public Component getComponent() {
        return this.combo;
    }

    @Override
    public void setEnable(boolean bln) {
        this.combo.setEnabled(bln);
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public void reload() {
        try {

            Criteria criteria = WestPersistentManager.getSession().createCriteria(Class.forName(this.bean));

            if (query != null) {
                criteria.add(Restrictions.sqlRestriction(query));
            }

            if (order != null) {
                for (String str : order.split(";")) {
                    criteria.addOrder(Order.asc(str));
                }
            }

            if (limit != null) {
                criteria.setMaxResults(new Integer(limit));
            }

            if (join != null) {
                String joins[] = join.split(",");

                criteria.createAlias(joins[0].replaceAll(",", ""), joins[1].replaceAll(",", ""));
            }

            this.lista = criteria.list();
            model.removeAllElements();

            for (Iterator it = lista.iterator(); it.hasNext();) {
                Object object = it.next();
                model.addElement(object);
            }

            this.combo.getModel().setSelectedItem(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    @Override
    public void initComponent() throws Exception {
        this.combo = new JComboBox();

        this.combo.addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                reload();
            }
        });

        this.model = new DefaultComboBoxModel();
        this.combo.setModel(model);
        this.combo.setFont(LookProvider.getLook().getFieldsFont());
    }
}