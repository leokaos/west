package org.west.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *Classe de persistência referente à tabela tab_foto.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_fotos")
public class Foto implements Serializable{
    
    @Id
    @Column(name="codigo",length=20,nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    @ManyToOne(targetEntity=Imovel.class,fetch= FetchType.LAZY)
    @JoinColumn(name="imovel",referencedColumnName="referencia")
    @Cascade(CascadeType.LOCK)
    private Imovel imovel;
    @Column(name="caminho",length=255,nullable=false)
    private String caminho;
    @Column(name="descricao",length=255,nullable=false)
    private String descricao;
    @Column(name="site",nullable=false)
    private Boolean site;
    @Column(name="principal",nullable=false)
    private Boolean principal;
    @Column(name="dataEntrada",nullable=false)
    @Temporal(TemporalType.DATE)
    private Date dataEntrada;
    
    public Foto() {
    }
    
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean isSite() {
        return site;
    }

    public void setSite(Boolean site) {
        this.site = site;
    }

    public Boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }
    
    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }    

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Veiculo))
            return false;
               
        Foto foto = (Foto) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(foto.getCodigo())) || (getCodigo() == null && foto.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo().toString();
    }   
}