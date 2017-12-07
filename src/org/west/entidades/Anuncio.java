package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Classe de persistência referente à tabela tab_anuncio.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_anuncio")
public class Anuncio implements Serializable {

    @Id
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;
    @ManyToOne(targetEntity = Veiculo.class, fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "veiculo", referencedColumnName = "nome")})
    @Cascade(CascadeType.LOCK)
    private Veiculo veiculo;
    @Column(name = "travado", length = 20, nullable = false)
    private boolean travado;
    @Column(name = "usado", length = 20, nullable = false)
    private boolean usado;
    @Column(name = "liberavel")
    private boolean liberavel;
    @Column(name = "informacaoAdicional")
    private boolean informacaoAdicional;
    @Column(name = "exige_informacao")
    private boolean exigeInformacao;
    @Column(name = "modelo_destaque")
    private String modeloDestaque;
    @Column(name = "permite_video")
    private boolean permiteVideo;
    @Transient
    private Integer quantidade = 0;

    public Anuncio() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public boolean getTravado() {
        return travado;
    }

    public void setTravado(boolean travado) {
        this.travado = travado;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isLiberavel() {
        return liberavel;
    }

    public void setLiberavel(boolean liberavel) {
        this.liberavel = liberavel;
    }

    public boolean isInformacaoAdicional() {
        return informacaoAdicional;
    }

    public void setInformacaoAdicional(boolean informacaoAdicional) {
        this.informacaoAdicional = informacaoAdicional;
    }

    public boolean isExigeInformacao() {
        return exigeInformacao;
    }

    public void setExigeInformacao(boolean exigeInformacao) {
        this.exigeInformacao = exigeInformacao;
    }

    public String getModeloDestaque() {
        return modeloDestaque;
    }

    public void setModeloDestaque(String modeloDestaque) {
        this.modeloDestaque = modeloDestaque;
    }

    public boolean isPermiteVideo() {
        return permiteVideo;
    }

    public void setPermiteVideo(boolean permiteVideo) {
        this.permiteVideo = permiteVideo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Anuncio)) {
            return false;
        }

        Anuncio anuncio = (Anuncio) obj;

        if ((getNome() != null && !getNome().equals(anuncio.getNome())) || (getNome() == null && anuncio.getNome() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getNome();
    }
}