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
 *Classe de persistência referente à tabela tab_lancamento.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_lancamentos")
public class Lancamento implements Serializable{
    
    @Id
    @Column(name="codigo",length=20,nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    @Column(name="dataLancamento",nullable=false)
    @Temporal(TemporalType.DATE)
    private Date dataLancamento;    
    @Column(name="descricao",length=255,nullable=false)
    private String descricao;
    
    @ManyToOne(targetEntity=Servico.class,fetch= FetchType.EAGER)
    @JoinColumn(name="servico",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Servico servico;    
        
    @Column(name="entrada",nullable=false)
    private Double entrada;
    @Column(name="saida",nullable=false)
    private Double saida;    

    @ManyToOne(targetEntity=Usuario.class,fetch= FetchType.EAGER)
    @JoinColumn(name="usuario",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Usuario usuario;
    
    @Column(name="area",length=255,nullable=false)
    private String area;    
    
    public Lancamento() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Double getEntrada() {
        return entrada;
    }

    public void setEntrada(Double entrada) {
        this.entrada = entrada;
    }

    public Double getSaida() {
        return saida;
    }

    public void setSaida(Double saida) {
        this.saida = saida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Lancamento))
            return false;
               
        Lancamento lancamento = (Lancamento) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(lancamento.getCodigo())) || (getCodigo() == null && lancamento.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo().toString();
    }
}