package org.west.componentes;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.Queue;
import java.io.Serializable;
import java.util.Map;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

public class JMSManager {

    public static void enviarObjectMensagem(Object obj, String dest, String tipo) {
        try {
            ConnectionFactory fabrica = getFabrica();
            Connection cone = fabrica.createConnection();
            Session session = cone.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destino = new Queue(dest);

            MessageProducer produtor = session.createProducer(destino);

            ObjectMessage mensagem = session.createObjectMessage();
            mensagem.setObject((Serializable) obj);
            mensagem.setStringProperty("tipo", tipo);

            produtor.send(mensagem);

            session.close();
            cone.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void enviarObjectMensagem(Object obj, String dest, String tipo, Map<String, String> mapa) {
        try {
            ConnectionFactory fabrica = getFabrica();
            Connection cone = fabrica.createConnection();
            Session session = cone.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destino = new Queue(dest);

            MessageProducer produtor = session.createProducer(destino);

            ObjectMessage mensagem = session.createObjectMessage();
            mensagem.setObject((Serializable) obj);
            mensagem.setStringProperty("tipo", tipo);

            for (String chave : mapa.keySet()) {
                mensagem.setStringProperty(chave, mapa.get(chave));
            }

            produtor.send(mensagem);

            session.close();
            cone.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ConnectionFactory getFabrica() {
        ConnectionFactory fabrica = null;
        try {
            fabrica = new ConnectionFactory();
            fabrica.setProperty(ConnectionConfiguration.imqAddressList, "weststorage:7676, broker2:5000, broker3:9999");
            fabrica.setProperty(ConnectionConfiguration.imqReconnectEnabled, "true");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fabrica;
    }
}
