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
 *Classe de persistência referente à tabela tab_historico.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_historico")
public class Historico implements Serializable{
    
    @Id
    @Column(name="codigo",nullable=false,length=20)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    @ManyToOne(targetEntity=Usuario.class,fetch= FetchType.EAGER)
    @JoinColumn(name="usuario",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Usuario usuario;
    @Column(name="descricao",nullable=false)
    private String descricao;
    @Column(name="dataEntrada",nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;
    @ManyToOne(targetEntity=Imobiliaria.class,fetch= FetchType.EAGER)
    @JoinColumn(name="imobiliaria",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Imobiliaria imobiliaria;

    public Historico() {
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Imobiliaria getImobiliaria() {
        return imobiliaria;
    }

    public void setImobiliaria(Imobiliaria imobiliaria) {
        this.imobiliaria = imobiliaria;
    } 	 	 	 	 	 	

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Historico))
            return false;
               
        Historico historico = (Historico) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(historico.getCodigo())) || (getCodigo() == null && historico.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo().toString();
    }    

}