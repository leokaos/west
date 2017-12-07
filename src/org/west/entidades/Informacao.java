package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Classe de persistência referente à tabela tab_imovel_anuncio.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_informacoes")
public class Informacao implements Serializable {

    public static final Integer OFERTA_NORMAL_ZAP = 1;
    public static final Integer OFERTA_DESTAQUE_ZAP = 2;
    public static final Integer SUPER_DESTAQUE_ZAP = 3;
    public static final Integer NORMAL = 1;
    public static final Integer DESTAQUE = 2;
    @EmbeddedId
    private InformacaoKey informacaoKey = new InformacaoKey();
    @Column(name = "novo", nullable = true)
    private Boolean novo;
    @Column(name = "liberado", nullable = true)
    private Boolean liberado;
    @Column(name = "itens", nullable = true)
    private String itens;
    @Column(name = "textoAnuncio", nullable = true)
    private String textoAnuncio;
    @Column(name = "destaque", nullable = true)
    private Integer destaque;
    @Column(name = "valor", nullable = true)
    private Boolean mostrarValor;
    @ManyToOne(targetEntity = Imovel.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "referencia", referencedColumnName = "referencia", insertable = false, updatable = false)
    @Cascade(CascadeType.LOCK)
    private Imovel imovel;
    @ManyToOne(targetEntity = Anuncio.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "anuncio", referencedColumnName = "nome", insertable = false, updatable = false)
    @Cascade(CascadeType.LOCK)
    private Anuncio anuncio;
    @ManyToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "usuario", referencedColumnName = "codigo")
    @Cascade(CascadeType.LOCK)
    private Usuario usuario;
    @Column(name = "iptu")
    private boolean iptu;
    @Column(name = "condominio")
    private boolean condominio;
    @Column(name = "video")
    private String video;

    public Informacao() {
        imovel = new Imovel();
        anuncio = new Anuncio();
        informacaoKey = new InformacaoKey();
        usuario = new Usuario();
    }

    public Informacao(String anuncio, Long referencia) {
        informacaoKey.setAnuncio(anuncio);
        informacaoKey.setReferencia(referencia);
    }

    public Boolean getNovo() {
        return novo;
    }

    public void setNovo(Boolean novo) {
        this.novo = novo;
    }

    public Boolean getLiberado() {
        return liberado;
    }

    public void setLiberado(Boolean liberado) {
        this.liberado = liberado;
    }

    public String getItens() {
        return itens;
    }

    public void setItens(String itens) {
        this.itens = itens;
    }

    public String getTextoAnuncio() {
        return textoAnuncio;
    }

    public void setTextoAnuncio(String textoAnuncio) {
        this.textoAnuncio = textoAnuncio;
    }

    public Integer getDestaque() {
        return destaque;
    }

    public void setDestaque(Integer destaque) {
        this.destaque = destaque;
    }

    public Boolean getMostrarValor() {
        return mostrarValor;
    }

    public void setMostrarValor(Boolean mostrarValor) {
        this.mostrarValor = mostrarValor;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
        this.informacaoKey.setReferencia(imovel.getReferencia());
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
        this.informacaoKey.setAnuncio(anuncio.getNome());
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isIptu() {
        return iptu;
    }

    public void setIptu(boolean iptu) {
        this.iptu = iptu;
    }

    public boolean isCondominio() {
        return condominio;
    }

    public void setCondominio(boolean condominio) {
        this.condominio = condominio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Informacao)) {
            return false;
        }

        Informacao informacao = (Informacao) obj;

        if ((getImovel() != null && !getImovel().equals(informacao.getImovel())) || (getImovel() == null && informacao.getImovel() != null)) {
            return false;
        }

        if ((getAnuncio() != null && !getAnuncio().equals(informacao.getAnuncio())) || (getAnuncio() == null && informacao.getAnuncio() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.informacaoKey != null ? this.informacaoKey.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return informacaoKey.getReferencia() + " - " + informacaoKey.getAnuncio() + " - " + novo;
    }
}