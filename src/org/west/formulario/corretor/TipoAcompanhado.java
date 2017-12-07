package org.west.formulario.corretor;

import java.util.ArrayList;
import java.util.List;

public enum TipoAcompanhado {

    TODOS(null, "Todos"), ACOMPANHADO(Boolean.TRUE, "Acompanhado"), NAO_ACOMPANHADO(Boolean.FALSE, "Não Acompanhado");
    private Boolean value;
    private String descricao;

    private TipoAcompanhado(Boolean value, String descricao) {
        this.value = value;
        this.descricao = descricao;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static List<TipoAcompanhado> getTodosValores() {
        List<TipoAcompanhado> listaValores = new ArrayList<TipoAcompanhado>();

        listaValores.add(TODOS);
        listaValores.add(ACOMPANHADO);
        listaValores.add(NAO_ACOMPANHADO);

        return listaValores;
    }
}
