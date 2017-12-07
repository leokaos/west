package org.west.componentes;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.west.entidades.WestPersistentManager;

/**
 * Componente que extende JTextField e cria um sugestion com os dados do banco
 * de dados mapeado através do Hibernate.
 */
public class JSugestion extends JTextField {

    private JList lista;
    private int key;
    private String entity;
    private List<String> queryFields;
    private Boolean type;
    private JLayeredPane panel;
    private JScrollPane scroll;
    private boolean selecionado;
    private Object obj;
    private String order = "";

    /**
     * Construtor que recebe duas string que representam a entidade e o campo de
     * busca.
     *
     * @param entity : Entidade mapeada no hibernate pela qual será feita a
     * busca.
     * @param queryField: Campo pelo qual será feita a busca.
     */
    public JSugestion(String entity, String queryField) {
        this.key = KeyEvent.VK_ENTER;
        this.queryFields = new ArrayList<String>();
        this.queryFields.add(queryField);
        this.entity = entity;
        this.type = false;

        this.init();
    }

    /**
     * Define qual tecla irá efetuar a busca.
     *
     * @param key : a tecla obtida através da classe KeyEvent.
     */
    public void setKey(int key) {
        this.key = key;
    }

    /**
     * Recupera a tecla definida para efetuar a busca.
     *
     * @return key : Inteiro que representa uma tecla.
     */
    public int getKey() {
        return this.key;
    }

    /**
     * Define por qual entidade será feita a busca.
     *
     * @param entity : a entidade mapeada no hibernate.
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * Recupera a entidade.
     *
     * Essa entidade deve estar mapeada no hibernate.
     *
     * @return entity : Entidade do hibernate.
     */
    public String getEntity() {
        return this.entity;
    }

    /**
     * Define por qual campo será feita a busca.
     *
     * @param queryField : o campo pelo qual será feita a busca.
     */
    public void setQueryField(String queryField) {
        this.queryFields.clear();
        this.queryFields.add(queryField);
    }

    /**
     * Adiciona um campo na pesquisa que será feita no banco de dados.
     *
     * @param queryField nome do campo a ser pesquisado.
     */
    public void addQueryField(String queryField) {
        queryFields.add(queryField);
    }

    /**
     * Define por qual campo será feita a ordernação.
     *
     * @param order : nome do campo para ordernação.
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Retorna o campo que será utilizado como ordernação.
     *
     * @return order : campo de ordenação.
     */
    public String getOrder() {
        return this.order;
    }

    /**
     * Habilita ou desabilita a busca feita ao escrever.
     *
     * @param bln true ou false para a busca ao escrever.
     */
    public void enableTyping(boolean bln) {
        this.type = bln;
    }

    /**
     * Metódo de inicialização para definição dos componentes.
     */
    private void init() {

        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (getText().isEmpty()) {
                    if (panel != null && lista != null) {
                        scroll.setVisible(false);
                        lista.setVisible(false);
                    }
                } else {
                    if (type) {
                        showList();
                    } else {
                        if (e.getKeyCode() == key) {
                            showList();
                        }
                    }
                }
            }
        });
    }

    /**
     * Metódo responsavel por fazer a busca no banco e mostrar as opções.
     */
    private void showList() {
        try {
            defineRootPane();
            if (this.lista == null) {
                this.lista = new JList();

                lista.addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {

                        if (lista.getSelectedValue() != null) {
                            setText(lista.getSelectedValue().toString());
                            setValue(lista.getSelectedValue());
                            scroll.setVisible(false);
                            firePropertyChange("selecionado", selecionado, !selecionado);
                        }
                    }
                });
            }

            if (this.scroll == null) {
                scroll = new JScrollPane(this.lista);
                this.panel.add(scroll, JLayeredPane.POPUP_LAYER);
            }

            Criteria criteria = getSession().createCriteria(Class.forName(entity));

            Disjunction disjun = Restrictions.disjunction();

            for (String string : queryFields) {

                if (string.indexOf(".") > -1) {
                    String prop = string.substring(0, string.indexOf("."));
                    criteria.createCriteria(prop, prop);
                }

                disjun.add(Restrictions.ilike(string, getText(), MatchMode.ANYWHERE));
            }

            criteria.add(disjun);

            if (!order.isEmpty()) {
                criteria.addOrder(Order.asc(order));
            }

            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            DefaultListModel model = new DefaultListModel();

            for (Iterator it = criteria.list().iterator(); it.hasNext();) {
                Object object = it.next();
                model.addElement(object);
            }

            this.lista.setModel(model);

            if (model.getSize() > 4) {
                scroll.setSize(new Dimension(getWidth(), 76));
            } else {
                scroll.setSize(new Dimension(getWidth(), model.size() * 20));
            }

            int x = getLocationOnScreen().x - panel.getLocationOnScreen().x;
            int y = getLocationOnScreen().y - panel.getLocationOnScreen().y + getHeight();

            scroll.setLocation(x, y);

            scroll.setVisible(true);
            lista.setVisible(true);

            panel.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retorna uma session do hibernate.
     *
     * @return Session session do hibernate.
     */
    private Session getSession() {
        try {
            return WestPersistentManager.getSession();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Acha o layeredPane.
     */
    private void defineRootPane() {
        Container cont = getParent();

        while (true) {
            if (cont instanceof RootPaneContainer) {
                this.panel = ((RootPaneContainer) cont).getLayeredPane();
                break;
            } else {
                cont = cont.getParent();
            }
        }
    }

    /**
     * Método utilizado para alterar o objeto selecionado no componente.
     *
     * @param obj : Valor novo da propriedade Value;
     */
    public void setValue(Object obj) {
        this.obj = obj;
    }

    /**
     * Método utilizado para recuperar o objeto selecionado no componente.
     *
     * @return obj: Valor atual da propriedade Value
     */
    public Object getValue() {
        return this.obj;
    }
}