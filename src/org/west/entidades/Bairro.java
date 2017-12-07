package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Classe de persistência referente à tabela tab_bairro.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_bairro")
public class Bairro implements Serializable, Comparable<Bairro> {

    @EmbeddedId
    private BairroKey bairroKey;
    @Column(name = "nome", nullable = false, insertable = false, updatable = false)
    private String nome;
    @Column(name = "cidade", nullable = false, insertable = false, updatable = false)
    private String cidade;
    @Column(name = "usado", nullable = false)
    private Boolean usado;
    @Column(name = "uf", nullable = false)
    private String uf;

    public Bairro() {
    }

    public Boolean isUsado() {
        return usado;
    }

    public void setUsado(Boolean usado) {
        this.usado = usado;
    }

    public String getNome() {
        return bairroKey.getNome();
    }

    public void setNome(String nome) {
        this.bairroKey.setNome(nome);
    }

    public String getCidade() {
        return bairroKey.getCidade();
    }

    public void setCidade(String cidade) {
        this.bairroKey.setCidade(cidade);
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Bairro)) {
            return false;
        }

        Bairro bairro = (Bairro) obj;

        if ((getNome() != null && !getNome().equals(bairro.getNome())) || (getNome() == null && bairro.getNome() != null)) {
            return false;
        }

        if ((getCidade() != null && !getCidade().equals(bairro.getCidade())) || (getCidade() == null && bairro.getCidade() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.bairroKey != null ? this.bairroKey.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getNome();
    }

    @Override
    public int compareTo(Bairro o) {
        return getNome().compareTo(o.getNome());
    }
}
