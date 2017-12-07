package org.west.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.west.utilitarios.ValidadorUtil;

/**
 *Classe de persistência referente à tabela tab_portaria.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_portaria")
public class Portaria implements Serializable {

    @Id
    @Column(name="codigo",nullable=false,length=20)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    
    @ManyToOne(targetEntity=Usuario.class,fetch= FetchType.EAGER)
    @JoinColumn(name="usuario",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Usuario usuario;

    @ManyToOne(targetEntity = Bairro.class, fetch = FetchType.EAGER)
    @JoinColumns({@JoinColumn(name = "bairro", referencedColumnName = "nome"),@JoinColumn(name = "cidade", referencedColumnName = "cidade")})
    @Cascade(CascadeType.LOCK)
    private Bairro bairro;
    
    @ManyToOne(targetEntity=Cep.class,fetch= FetchType.EAGER)
    @JoinColumn(name="CEP",referencedColumnName="CEP")
    @Cascade(CascadeType.LOCK)
    private Cep cep;    
	 	 	
    @Column(name="numero",nullable=false,length=20)
    private String numero;
    @Column(name="edificio",nullable=true,length=30)
    private String edificio;    
    @Column(name="zelador",nullable=false,length=255)
    private String zelador;
    @Column(name="telefone",nullable=false,length=255)
    private String telefone;    
    @Column(name="prioridade",nullable=false)
    private Boolean prioridade;	 	 	 	 	 	 	
    @Column(name="cidade",nullable=false,length=100,insertable=false,updatable=false)
    private String cidade;
    @Column(name="porteiro",nullable=false,length=30)
    private String porteiro;    
    
    @OneToMany(mappedBy="portaria",targetEntity=Planta.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    @OrderBy(value="dorms,suites")
    private Set<Planta> plantas = new HashSet<Planta>();
    
    @OneToMany(mappedBy="portaria",targetEntity=Imovel.class)
    @Cascade({CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Imovel> imoveis = new HashSet<Imovel>();    
    
    @ManyToMany(targetEntity=Lazer.class)
    @Cascade({CascadeType.ALL})
    @JoinTable(name="tab_lazeres_portaria",
            joinColumns={@JoinColumn(name="portaria")},
            inverseJoinColumns={@JoinColumn(name="lazer")})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Lazer> lazer = new HashSet<Lazer>();

    //Atributo Transientes
    @Transient
    private String quantidadeImovel;
    @Transient
    private boolean visitado;
    @Transient
    private String endereco = "";
    @Transient
    private Date ultimaVisita;
    @Transient
    private String retornoFicha;
    @Transient
    private List<Frequencia> frequenciaPeriodo;
    @Transient
    private String foto = "";    

    public Portaria() {
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

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public Cep getCep() {
        return cep;
    }

    public void setCep(Cep cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getZelador() {
        return zelador;
    }

    public void setZelador(String zelador) {
        this.zelador = zelador;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Boolean prioridade) {
        this.prioridade = prioridade;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getPorteiro() {
        return porteiro;
    }

    public void setPorteiro(String porteiro) {
        this.porteiro = porteiro;
    }

    public Set<Planta> getPlantas() {
        return plantas;
    }

    public void setPlantas(Set<Planta> plantas) {
        this.plantas = plantas;
    }
    
    public Set<Imovel> getImoveis() {
        return imoveis;
    }

    public void setImoveis(Set<Imovel> imoveis) {
        this.imoveis = imoveis;
    }    
    
    public Set<Lazer> getLazer() {
        return lazer;
    }

    public void setLazer(Set<Lazer> lazer) {
        this.lazer = lazer;
    }    
    
    //Métodos das Propriedades Transientes
    public String getQuantidadeImovel() {
        return quantidadeImovel;
    }

    public void setQuantidadeImovel(String quantidadeImovel) {
        this.quantidadeImovel = quantidadeImovel;
    }

    public boolean isVisitado() {
        return visitado;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }

    public String getEndereco() {
        if (cep != null)
            this.endereco = this.cep.getTipo() + " " + this.cep.getDescricao();
        
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Date getUltimaVisita() {
        return ultimaVisita;
    }

    public void setUltimaVisita(Date ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }

    public String getRetornoFicha() {
        return retornoFicha;
    }

    public void setRetornoFicha(String retornoFicha) {
        this.retornoFicha = retornoFicha;
    }

    public List<Frequencia> getFrequenciaPeriodo() {
        return frequenciaPeriodo;
    }

    public void setFrequenciaPeriodo(List<Frequencia> frequenciaPeriodo) {
        this.frequenciaPeriodo = frequenciaPeriodo;
    }

    public String getFoto() {
        if (foto.isEmpty())
            foto = "cod " + codigo + ".jpg";
        
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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
        int hash = 7;
        hash = 97 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return String.valueOf(getCep().getTipo() + " " + getCep().getDescricao() + ", nº " + getNumero());
    }

    public boolean isNovo() {
        return ValidadorUtil.isEmpty(codigo);
    }
}