package org.west.entidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
 *Classe de persistência referente à tabela tab_frequencia.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_frequencia")
public class Frequencia implements Serializable, Comparable<Frequencia> {
    
    @Id
    @Column(name="codigo",length=20,nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    @ManyToOne(targetEntity=Portaria.class,fetch= FetchType.EAGER)
    @JoinColumn(name="portaria",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Portaria portaria;
    @Column(name="dataVisita",nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVisita;
    @ManyToOne(targetEntity=Usuario.class,fetch= FetchType.EAGER)
    @JoinColumn(name="usuario",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Usuario usuario;

    public Frequencia() {
    }
    
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Portaria getPortaria() {
        return portaria;
    }

    public void setPortaria(Portaria portaria) {
        this.portaria = portaria;
    }

    public Date getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(Date dataVisita) {
        this.dataVisita = dataVisita;
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
        
        if (!(obj instanceof Frequencia))
            return false;
               
        Frequencia frequencia = (Frequencia) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(frequencia.getCodigo())) || (getCodigo() == null && frequencia.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(dataVisita);
    }

    @Override
    public int compareTo(Frequencia o) {
        return getDataVisita().compareTo(o.getDataVisita());
    }
}