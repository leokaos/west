package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe de persistência referente à tabela tab_tipo.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_tipo")
public class Tipo implements Serializable {

    @Id
    @Column(name = "tipo", length = 100, nullable = false)
    private String tipo;
    @Column(name = "coletivo", nullable = false)
    private Boolean coletivo;
    @Column(name = "exige_andar", nullable = false)
    private Boolean exigeAndar;

    public Tipo() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean isColetivo() {
        return coletivo;
    }

    public void setColetivo(Boolean coletivo) {
        this.coletivo = coletivo;
    }

    public Boolean getExigeAndar() {
        return exigeAndar;
    }

    public void setExigeAndar(Boolean exigeAndar) {
        this.exigeAndar = exigeAndar;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Tipo)) {
            return false;
        }

        Tipo aTipo = (Tipo) obj;

        if ((getTipo() != null && !getTipo().equals(aTipo.getTipo())) || (getTipo() == null && aTipo.getTipo() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.tipo != null ? this.tipo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getTipo();
    }
}