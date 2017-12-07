package org.west.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *Classe de persistência referente à tabela tab_imobiliaria.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_imobiliaria")
public class Imobiliaria implements Serializable{
    
    //constantes
    public final static int SEM_CIENCIA = 1;
    public final static int SEM_CONTATO = 2;
    public final static int SEM_RETORNO = 3;
    public final static int ATENDIDO = 4;    
    
    @Id
    @Column(name="codigo",length=20,nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    @Column(name="imovel",length=45,nullable=true)
    private String imovel;
    @Column(name="valor",nullable=true)
    private Double valor;
    
    @ManyToOne(targetEntity=Cliente.class,fetch= FetchType.EAGER)
    @JoinColumn(name="cliente",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Cliente cliente;
    
    @Column(name="dataEntrada",nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;
    
    @ManyToOne(targetEntity=Veiculo.class,fetch= FetchType.EAGER)
    @JoinColumn(name="veiculo",referencedColumnName="nome")
    @Cascade(CascadeType.LOCK)    
    private Veiculo veiculo;
        
    @ManyToOne(targetEntity=Anuncio.class,fetch= FetchType.EAGER)
    @JoinColumn(name="anuncio",referencedColumnName="nome")
    @Cascade(CascadeType.LOCK)    
    private Anuncio anuncio; 
    
    @ManyToOne(targetEntity=Usuario.class,fetch= FetchType.EAGER)
    @JoinColumn(name="usuario",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)    
    private Usuario usuario;     

    @Column(name="status",length=10,nullable=false)
    private Integer status;
    @Column(name="prioridade",length=1,nullable=false)
    private Boolean prioridade = false;
    @Column(name="acompanhado",length=1,nullable=false)
    private Boolean acompanhado = false; 	

    @OneToMany(mappedBy="imobiliaria",targetEntity=Historico.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    @OrderBy(value="dataEntrada")
    private Set<Historico> historicos = new HashSet<Historico>();

    public Imobiliaria() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getImovel() {
        return imovel;
    }

    public void setImovel(String imovel) {
        this.imovel = imovel;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }    

    public Boolean getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Boolean prioridade) {
        this.prioridade = prioridade;
    }

    public Boolean isAcompanhado() {
        return acompanhado;
    }

    public void setAcompanhado(Boolean acompanhado) {
        this.acompanhado = acompanhado;
    }

    public Set<Historico> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(Set<Historico> historicos) {
        this.historicos = historicos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Imobiliaria))
            return false;
               
        Imobiliaria imobiliaria = (Imobiliaria) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(imobiliaria.getCodigo())) || (getCodigo() == null && imobiliaria.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo().toString();
    }
}