package org.west.entidades;

import java.util.HashMap;
import java.util.Map;

public class TipoAlteracao {

    public static final TipoAlteracao DIVULGAR = new TipoAlteracao("divulgar");
    public static final TipoAlteracao MAIOR = new TipoAlteracao("maior");
    public static final TipoAlteracao MENOR = new TipoAlteracao("menor");
    public static final TipoAlteracao STATUS = new TipoAlteracao("status");
    private static final Map<String, TipoAlteracao> possiveisValores;

    static {
        possiveisValores = new HashMap<String, TipoAlteracao>();

        possiveisValores.put(DIVULGAR.getTexto(), DIVULGAR);
        possiveisValores.put(MAIOR.getTexto(), MAIOR);
        possiveisValores.put(MENOR.getTexto(), MENOR);
        possiveisValores.put(STATUS.getTexto(), STATUS);
    }
    private final String texto;

    private TipoAlteracao(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public static TipoAlteracao parse(String texto) {
        if (possiveisValores.containsKey(texto)) {
            return possiveisValores.get(texto);
        } else {
            return null;
        }
    }
}
