package org.west.collections;

import org.west.entidades.Informacao;
import org.west.entidades.TipoServico;

import static org.west.utilitarios.ValidadorUtil.isNull;

public final class FilterUtils {

    public static FilterByValue<TipoServico, String> filtroTipoServicoDescricao() {
        return new FilterByValue<TipoServico, String>() {

            @Override
            public String getValue(TipoServico t) {
                return t.getDescricao();
            }
        };
    }

    public static Filtro<Informacao> filtroInformacoesALiberar() {
        return new Filtro<Informacao>() {

            @Override
            public boolean accept(Informacao t) {
                return isNull(t.getLiberado());
            }
        };
    }
}
