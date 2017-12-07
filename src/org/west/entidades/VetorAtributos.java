package org.west.entidades;

import java.math.BigDecimal;

public class VetorAtributos {

    private final BigDecimal valor;
    private final Integer dormitorios;
    private final String bairro;

    public VetorAtributos(Imovel imovel) {

        this.valor = new BigDecimal(imovel.getValor());

        this.dormitorios = imovel.getDorms();

        this.bairro = imovel.getBairro().getNome();

    }

    public BigDecimal getValor() {
        return valor;
    }

    public Integer getDormitorios() {
        return dormitorios;
    }

    public String getBairro() {
        return bairro;
    }
}
