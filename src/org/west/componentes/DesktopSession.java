package org.west.componentes;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe que cria uma sessão, parecida com a sessão usada nas aplicações Web.
 * Utiliza o padrão Singleton.
 *
 * @author Leonardo Guerra.
 */
public class DesktopSession {

    private static DesktopSession desktopSession = null;
    private Map<String, Object> map = new HashMap<String, Object>();

    private DesktopSession() {
    }

    /**
     * Retorna uma instância da classe.
     *
     * @return desktopSession - instância única da sessão.
     */
    public static DesktopSession getInstance() {
        if (desktopSession == null) {
            desktopSession = new DesktopSession();
        }

        return desktopSession;
    }

    /**
     * Retorna o objeto armazenado anteriormente na sessão.
     *
     * @param propriedade nome de correspondência ao objecto.
     *
     * @return object - Objeto assossiado à propriedade.
     */
    public Object getObject(String propriedade) {
        return map.get(propriedade);
    }

    /**
     * Guarda o objeto na sessão e associa à propridade.
     *
     * @param propriedade nome da associação do objeto.
     * @param obj objeto à ser guardado na sessão.
     */
    public void setObject(String propriedade, Object obj) {
        map.put(propriedade, obj);
    }

    public <T> T getObjetoSessao(String propriedade) {
        return (T) map.get(propriedade);
    }
}