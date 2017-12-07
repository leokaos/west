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
 *Classe de persistência referente à tabela tab_modifica.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_modifica")
public class Modifica implements Serializable{
    
    @Id
    @Column(name="codigo",length=20,nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    
    @ManyToOne(targetEntity=Tarefa.class,fetch= FetchType.LAZY)
    @JoinColumn(name="tarefa",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Tarefa tarefa;
    @ManyToOne(targetEntity=Usuario.class,fetch= FetchType.LAZY)
    @JoinColumn(name="usuario",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Usuario usuario;    
    
    @Column(name="dataModificacao",nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataModificacao;
    @Column(name="dataAnterior",nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAnterior;       
    
    public Modifica() {
    }
 
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(Date dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public Date getDataAnterior() {
        return dataAnterior;
    }

    public void setDataAnterior(Date dataAnterior) {
        this.dataAnterior = dataAnterior;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Modifica))
            return false;
               
        Modifica Modifica = (Modifica) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(Modifica.getCodigo())) || (getCodigo() == null && Modifica.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo().toString();
    }    
}