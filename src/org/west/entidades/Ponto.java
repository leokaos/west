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
 *Classe de persistência referente à tabela tab_ponto.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_ponto")
public class Ponto implements Serializable{
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="codigo",length=20,nullable=false)
    private Long codigo;
    @ManyToOne(targetEntity=Usuario.class,fetch= FetchType.LAZY)
    @JoinColumn(name="usuario",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)    
    private Usuario usuario;
    @Column(name="dataPonto",nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPonto;
    @Column(name="obs",length=255,nullable=false)
    private String obs;
    @Column(name="liberadoPor",length=255,nullable=true)
    private String liberadoPor;

    public Ponto() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataPonto() {
        return dataPonto;
    }

    public void setDataPonto(Date dataPonto) {
        this.dataPonto = dataPonto;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getLiberadoPor() {
        return liberadoPor;
    }

    public void setLiberadoPor(String liberadoPor) {
        this.liberadoPor = liberadoPor;
    }
    
@Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Ponto))
            return false;
               
        Ponto Ponto = (Ponto) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(Ponto.getCodigo())) || (getCodigo() == null && Ponto.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo().toString();
    }    
}