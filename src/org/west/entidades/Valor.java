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
 *Classe de persistência referente à tabela tab_Valor.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_valor")
public class Valor implements Serializable{
    
    @Id
    @Column(name="codigo",length=20,nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    
    @ManyToOne(targetEntity=Imovel.class,fetch= FetchType.EAGER)
    @JoinColumn(name="imovel",referencedColumnName="referencia")
    @Cascade(CascadeType.LOCK)    
    private Imovel imovel;
    
    @Column(name="dataAltera",nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAltera;    
    @Column(name="valor",nullable=false)
    private Double valor;

    public Valor() {
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

    public Date getDataAltera() {
        return dataAltera;
    }

    public void setDataAltera(Date dataAltera) {
        this.dataAltera = dataAltera;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Valor))
            return false;
               
        Valor aValor = (Valor) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(aValor.getCodigo())) || (getCodigo() == null && aValor.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo().toString();
    }    
}