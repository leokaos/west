package org.west.entidades;

import java.util.Date;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Classe responsável por encapsular a {@link Session} do Hibernate. Todas as
 * configurações da <i>Session</i> serão configuradas, bem como as classes
 * anotadas.
 *
 * @author West Guerra Ltda.
 */
public class WestPersistentManager {

    /**
     * <i>SessionFactory</i> responsável por criar a <i>Session</i>
     */
    private static final SessionFactory sessionFactory;
    /**
     * <i>Session</i> do Hibernate, sendo acessado via <i>SingleTon</i>
     */
    private static Session session;
    private static FlushMode flushMode;
    private static final ServiceRegistry serviceRegistry;

    static {
        flushMode = FlushMode.ALWAYS;

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Altera.class);
        configuration.addAnnotatedClass(Anuncio.class);
        configuration.addAnnotatedClass(Bairro.class);
        configuration.addAnnotatedClass(BairroKey.class);
        configuration.addAnnotatedClass(Cartao.class);
        configuration.addAnnotatedClass(Cep.class);
        configuration.addAnnotatedClass(Chave.class);
        configuration.addAnnotatedClass(Cliente.class);
        configuration.addAnnotatedClass(Contato.class);
        configuration.addAnnotatedClass(Email.class);
        configuration.addAnnotatedClass(Endereco.class);
        configuration.addAnnotatedClass(EstadoCivil.class);
        configuration.addAnnotatedClass(Evento.class);
        configuration.addAnnotatedClass(Foto.class);
        configuration.addAnnotatedClass(Frase.class);
        configuration.addAnnotatedClass(Frequencia.class);
        configuration.addAnnotatedClass(Historico.class);
        configuration.addAnnotatedClass(Imobiliaria.class);
        configuration.addAnnotatedClass(Imovel.class);
        configuration.addAnnotatedClass(Informacao.class);
        configuration.addAnnotatedClass(InformacaoKey.class);
        configuration.addAnnotatedClass(Lancamento.class);
        configuration.addAnnotatedClass(LancamentoPadrao.class);
        configuration.addAnnotatedClass(Lazer.class);
        configuration.addAnnotatedClass(Medidas.class);
        configuration.addAnnotatedClass(Modifica.class);
        configuration.addAnnotatedClass(Numero.class);
        configuration.addAnnotatedClass(Planta.class);
        configuration.addAnnotatedClass(Ponto.class);
        configuration.addAnnotatedClass(Portaria.class);
        configuration.addAnnotatedClass(Recado.class);
        configuration.addAnnotatedClass(Saldo.class);
        configuration.addAnnotatedClass(Servico.class);
        configuration.addAnnotatedClass(Tarefa.class);
        configuration.addAnnotatedClass(Telefone.class);
        configuration.addAnnotatedClass(Tipo.class);
        configuration.addAnnotatedClass(Usuario.class);
        configuration.addAnnotatedClass(Valor.class);
        configuration.addAnnotatedClass(Veiculo.class);
        configuration.addAnnotatedClass(Zap.class);
        configuration.addAnnotatedClass(TipoServico.class);
        configuration.addAnnotatedClass(Departamento.class);
        

        configuration.configure();

        serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();

        configuration.setSessionFactoryObserver(new SessionFactoryObserver() {
            @Override
            public void sessionFactoryCreated(SessionFactory sf) {
            }

            @Override
            public void sessionFactoryClosed(SessionFactory sf) {
                ServiceRegistryBuilder.destroy(serviceRegistry);
            }
        });

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        if (session == null || !session.isConnected()) {
            session = getSessionFactory().openSession();
            session.setFlushMode(flushMode);
        }

        return session;
    }

    public static FlushMode getFlushMode() {
        return flushMode;
    }

    public static void setFlushMode(FlushMode mode) {
        flushMode = mode;
    }

    public static void saveObject(Object obj) throws HibernateException {
        clear();
        getSession().beginTransaction();
        getSession().saveOrUpdate(obj);
        getSession().getTransaction().commit();
        getSession().flush();
        clear();
    }

    public static void delete(Object obj) throws HibernateException {
        clear();
        getSession().beginTransaction();
        getSession().delete(obj);
        getSession().getTransaction().commit();
        getSession().flush();
        clear();
    }

    public static void lock(Object obj, LockMode lock) throws HibernateException {
        clear();
        getSession().buildLockRequest(LockOptions.NONE).lock(obj);
    }

    public static void merge(Object obj) throws HibernateException {
        getSession().merge(obj);
    }

    public static void clear() {
        getSession().clear();
        getSession().close();
    }

    public static void refresh(Object obj) {
        clear();
        getSession().refresh(obj);
    }

    public static Date getDateServer() {
        try {
            return (Date) WestPersistentManager.getSession().createSQLQuery("Select now();").uniqueResult();
        } catch (Exception ex) {
            return null;
        }
    }
}