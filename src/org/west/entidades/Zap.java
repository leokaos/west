package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *Classe de persistência referente à tabela tab_zap.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_zap")
public class Zap implements Serializable{
    
    @Id
    @Column(name = "tipo", nullable = true)    
    private String tipo;
    @Column(name = "apartamento", nullable = true)
    private Boolean apartamento;
    @Column(name = "apartamento_duplex", nullable = true)
    private Boolean apartamento_duplex;
    @Column(name = "casa", nullable = true)
    private Boolean casa;
    @Column(name = "casa_comercial", nullable = true)
    private Boolean casa_comercial;
    @Column(name = "casa_cond_fechado", nullable = true)
    private Boolean casa_cond_fechado;
    @Column(name = "cobertura", nullable = true)
    private Boolean cobertura;
    @Column(name = "conjunto_comercial", nullable = true)
    private Boolean conjunto_comercial;
    @Column(name = "flat", nullable = true)
    private Boolean flat;
    @Column(name = "galpao", nullable = true)
    private Boolean galpao;
    @Column(name = "loja", nullable = true)
    private Boolean loja;
    @Column(name = "predio", nullable = true)
    private Boolean predio;
    @Column(name = "predio_comercial", nullable = true)
    private Boolean predio_comercial;
    @Column(name = "sala_comercial", nullable = true)
    private Boolean sala_comercial;
    @Column(name = "sitio", nullable = true)
    private Boolean sitio;
    @Column(name = "sobrado", nullable = true)
    private Boolean sobrado;
    @Column(name = "sobrado_novo", nullable = true)
    private Boolean sobrado_novo;
    @Column(name = "terrea_nova", nullable = true)
    private Boolean terrea_nova;
    @Column(name = "terreno", nullable = true)
    private Boolean terreno;
    @Column(name = "tag", nullable = true)
    private String tag;

    public Zap() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getApartamento() {
        return apartamento;
    }

    public void setApartamento(Boolean apartamento) {
        this.apartamento = apartamento;
    }

    public Boolean getApartamento_duplex() {
        return apartamento_duplex;
    }

    public void setApartamento_duplex(Boolean apartamento_duplex) {
        this.apartamento_duplex = apartamento_duplex;
    }

    public Boolean getCasa() {
        return casa;
    }

    public void setCasa(Boolean casa) {
        this.casa = casa;
    }

    public Boolean getCasa_comercial() {
        return casa_comercial;
    }

    public void setCasa_comercial(Boolean casa_comercial) {
        this.casa_comercial = casa_comercial;
    }

    public Boolean getCasa_cond_fechado() {
        return casa_cond_fechado;
    }

    public void setCasa_cond_fechado(Boolean casa_cond_fechado) {
        this.casa_cond_fechado = casa_cond_fechado;
    }

    public Boolean getCobertura() {
        return cobertura;
    }

    public void setCobertura(Boolean cobertura) {
        this.cobertura = cobertura;
    }

    public Boolean getConjunto_comercial() {
        return conjunto_comercial;
    }

    public void setConjunto_comercial(Boolean conjunto_comercial) {
        this.conjunto_comercial = conjunto_comercial;
    }

    public Boolean getFlat() {
        return flat;
    }

    public void setFlat(Boolean flat) {
        this.flat = flat;
    }

    public Boolean getGalpao() {
        return galpao;
    }

    public void setGalpao(Boolean galpao) {
        this.galpao = galpao;
    }

    public Boolean getLoja() {
        return loja;
    }

    public void setLoja(Boolean loja) {
        this.loja = loja;
    }

    public Boolean getPredio() {
        return predio;
    }

    public void setPredio(Boolean predio) {
        this.predio = predio;
    }

    public Boolean getPredio_comercial() {
        return predio_comercial;
    }

    public void setPredio_comercial(Boolean predio_comercial) {
        this.predio_comercial = predio_comercial;
    }

    public Boolean getSala_comercial() {
        return sala_comercial;
    }

    public void setSala_comercial(Boolean sala_comercial) {
        this.sala_comercial = sala_comercial;
    }

    public Boolean getSitio() {
        return sitio;
    }

    public void setSitio(Boolean sitio) {
        this.sitio = sitio;
    }

    public Boolean getSobrado() {
        return sobrado;
    }

    public void setSobrado(Boolean sobrado) {
        this.sobrado = sobrado;
    }

    public Boolean getSobrado_novo() {
        return sobrado_novo;
    }

    public void setSobrado_novo(Boolean sobrado_novo) {
        this.sobrado_novo = sobrado_novo;
    }

    public Boolean getTerrea_nova() {
        return terrea_nova;
    }

    public void setTerrea_nova(Boolean terrea_nova) {
        this.terrea_nova = terrea_nova;
    }

    public Boolean getTerreno() {
        return terreno;
    }

    public void setTerreno(Boolean terreno) {
        this.terreno = terreno;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Zap))
            return false;
               
        Zap zap = (Zap) obj;
        
        if ((getTipo() != null && !getTipo().equals(zap.getTipo())) || (getTipo() == null && zap.getTipo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.tipo != null ? this.tipo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getTipo();
    }
}