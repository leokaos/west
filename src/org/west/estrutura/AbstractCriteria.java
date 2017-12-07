package org.west.estrutura;

import java.util.List;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;

/**
 *Classe que implementa {@link Criteria}.
 * @author WestGuerra Ltda.
 */
public class AbstractCriteria implements Criteria{
    
    private final Criteria criteria;

    public AbstractCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public String getAlias() {
        return criteria.getAlias();
    }

    @Override
    public Criteria setProjection(Projection prjctn) {
        return criteria.setProjection(prjctn);
    }

    @Override
    public Criteria add(Criterion crtrn) {
        return criteria.add(crtrn);
    }

    @Override
    public Criteria addOrder(Order order) {
        return criteria.addOrder(order);
    }

    @Override
    public Criteria setFetchMode(String string, FetchMode fm) throws HibernateException {
        return criteria.setFetchMode(string, fm);
    }

    @Override
    public Criteria setLockMode(LockMode lm) {
        return criteria.setLockMode(lm);
    }

    @Override
    public Criteria setLockMode(String string, LockMode lm) {
        return criteria.setLockMode(string, lm);
    }

    @Override
    public Criteria createAlias(String string, String string1) throws HibernateException {
        return criteria.createAlias(string, string1);
    }

    @Override
    public Criteria createAlias(String string, String string1, int i) throws HibernateException {
        return criteria.createAlias(string, string1, i);
    }

    @Override
    public Criteria createCriteria(String string) throws HibernateException {
        return criteria.createCriteria(string);
    }

    @Override
    public Criteria createCriteria(String string, int i) throws HibernateException {
        return createCriteria(string, i);
    }

    @Override
    public Criteria createCriteria(String string, String string1) throws HibernateException {
        return createCriteria(string, string1);
    }

    @Override
    public Criteria createCriteria(String string, String string1, int i) throws HibernateException {
        return createCriteria(string, string1, i);
    }

    @Override
    public Criteria setResultTransformer(ResultTransformer rt) {
        return criteria.setResultTransformer(rt);
    }

    @Override
    public Criteria setMaxResults(int i) {
        return criteria.setMaxResults(i);
    }

    @Override
    public Criteria setFirstResult(int i) {
        return criteria.setFirstResult(i);
    }

    @Override
    public Criteria setFetchSize(int i) {
        return criteria.setFetchSize(i);
    }

    @Override
    public Criteria setTimeout(int i) {
        return criteria.setTimeout(i);
    }

    @Override
    public Criteria setCacheable(boolean bln) {
        return criteria.setCacheable(bln);
    }

    @Override
    public Criteria setCacheRegion(String string) {
        return criteria.setCacheRegion(string);
    }

    @Override
    public Criteria setComment(String string) {
        return criteria.setComment(string);
    }

    @Override
    public Criteria setFlushMode(FlushMode fm) {
        return criteria.setFlushMode(fm);
    }

    @Override
    public Criteria setCacheMode(CacheMode cm) {
        return criteria.setCacheMode(cm);
    }

    @Override
    public List list() throws HibernateException {
        return criteria.list();
    }

    @Override
    public ScrollableResults scroll() throws HibernateException {
        return criteria.scroll();
    }

    @Override
    public ScrollableResults scroll(ScrollMode sm) throws HibernateException {
        return criteria.scroll(sm);
    }

    @Override
    public Object uniqueResult() throws HibernateException {
        return criteria.uniqueResult();
    }

    @Override
    public Criteria createAlias(String string, String string1, JoinType jt) throws HibernateException {
        return criteria.createAlias(string, string1, jt);
    }

    @Override
    public Criteria createAlias(String string, String string1, JoinType jt, Criterion crtrn) throws HibernateException {
        return criteria.createAlias(string, string1, jt, crtrn);
    }

    @Override
    public Criteria createAlias(String string, String string1, int i, Criterion crtrn) throws HibernateException {
        return criteria.createAlias(string, string1, i, crtrn);
    }

    @Override
    public Criteria createCriteria(String string, JoinType jt) throws HibernateException {
        return criteria.createCriteria(string, jt);
    }

    @Override
    public Criteria createCriteria(String string, String string1, JoinType jt) throws HibernateException {
        return criteria.createCriteria(string, string1, jt);
    }

    @Override
    public Criteria createCriteria(String string, String string1, JoinType jt, Criterion crtrn) throws HibernateException {
        return criteria.createCriteria(string, string1, jt, crtrn);
    }

    @Override
    public Criteria createCriteria(String string, String string1, int i, Criterion crtrn) throws HibernateException {
        return criteria.createCriteria(string, string1, i, crtrn);
    }

    @Override
    public boolean isReadOnlyInitialized() {
        return criteria.isReadOnlyInitialized();
    }

    @Override
    public boolean isReadOnly() {
        return criteria.isReadOnly();
    }

    @Override
    public Criteria setReadOnly(boolean bln) {
        return criteria.setReadOnly(bln);
    }
}