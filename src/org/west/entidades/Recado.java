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
 *Classe de persistência referente à tabela tab_recado.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_recados")
public class Recado implements Serializable{
    
    @Id
    @Column(name="codigo",length=20,nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    @Column(name="descricao",nullable=false)
    private String descricao;
    @Column(name="entrada",nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date entrada;
    @Column(name="leitura",nullable=true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date leitura;       

    @ManyToOne(targetEntity=Usuario.class,fetch= FetchType.EAGER)
    @JoinColumn(name="usuario",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)    
    private Usuario usuario;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getLeitura() {
        return leitura;
    }

    public void setLeitura(Date leitura) {
        this.leitura = leitura;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Historico))
            return false;
               
        Portaria portaria = (Portaria) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(portaria.getCodigo())) || (getCodigo() == null && portaria.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo().toString();
    }
}