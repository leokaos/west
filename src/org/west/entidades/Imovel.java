package org.west.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.west.collections.Collections3;
import org.west.collections.FilterUtils;
import static org.west.utilitarios.ValidadorUtil.not;

/**
 * Classe de persistência referente à tabela tab_imovel.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_imovel")
public class Imovel implements Serializable {

    private static final long serialVersionUID = 6432141882879157426L;
    public static String ATIVO = "Ativo";
    public static String SUSPENSO = "Suspenso";
    public static String PROPOSTA = "Proposta";
    public static String VENDIDO = "Vendido";
    @Id
    @Column(name = "referencia", nullable = false, length = 20)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long referencia;
    @ManyToOne(targetEntity = Chave.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "chaves", referencedColumnName = "nome")
    @Cascade(CascadeType.LOCK)
    private Chave chave;
    @ManyToOne(targetEntity = Tipo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo", referencedColumnName = "tipo")
    @Cascade(CascadeType.LOCK)
    private Tipo tipo;
    @ManyToOne(targetEntity = Portaria.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "portaria", referencedColumnName = "codigo")
    @Cascade(CascadeType.LOCK)
    private Portaria portaria;
    @ManyToOne(targetEntity = Bairro.class, fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "bairro", referencedColumnName = "nome"),
        @JoinColumn(name = "cidade", referencedColumnName = "cidade")})
    @Cascade(CascadeType.LOCK)
    private Bairro bairro;
    @ManyToOne(targetEntity = Cep.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "CEP", referencedColumnName = "CEP")
    @Cascade(CascadeType.REFRESH)
    private Cep cep;
    @Column(name = "bloco", nullable = true, length = 30)
    private String bloco;
    @Column(name = "divulgar", nullable = false)
    private Boolean divulgar;
    @Column(name = "imediacoes", nullable = false, length = 100)
    private String imediacoes;
    @Column(name = "proprietarios", nullable = false, length = 255)
    private String proprietarios;
    @Column(name = "numero", nullable = false, length = 10)
    private String numero;
    @Column(name = "dorms", nullable = false, length = 10)
    private Integer dorms;
    @Column(name = "garagens", nullable = false, length = 10)
    private Integer garagens;
    @Column(name = "salas", nullable = false, length = 10)
    private Integer salas;
    @Column(name = "banheiros", nullable = false, length = 10)
    private Integer banheiros;
    @Column(name = "suites", nullable = false, length = 10)
    private Integer suites;
    @Column(name = "valor", nullable = false)
    private Double valor;
    @Column(name = "privativa", nullable = true)
    private Double privativa;
    @Column(name = "construido", nullable = true)
    private Double construido;
    @Column(name = "terreno", nullable = true)
    private Double terreno;
    @Column(name = "apto", nullable = true, length = 30)
    private String apto;
    @Column(name = "status", nullable = false, length = 15)
    private String status;
    @Column(name = "medidas", nullable = true, length = 100)
    private String medidas;
    @Column(name = "descricao", nullable = false)
    private String descricao;
    @Column(name = "obs", nullable = false)
    private String obs;
    @Column(name = "pagamento", nullable = false)
    private String pagamento;
    @Column(name = "captacao", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date captacao;
    @Column(name = "atualizado", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date atualizado;
    @Column(name = "chaveObs", nullable = true, length = 255)
    private String chaveObs;
    @Column(name = "vago", nullable = false)
    private Boolean vago;
    @ManyToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER, cascade = javax.persistence.CascadeType.REFRESH)
    @JoinColumn(name = "atualizadoPor", referencedColumnName = "codigo")
    @Cascade(CascadeType.LOCK)
    private Usuario atualizadoPor;
    @Column(name = "cidade", nullable = false, length = 100, insertable = false, updatable = false)
    private String cidade;
    @Column(name = "proprietarios1", nullable = true, length = 255)
    private String proprietarios1;
    @Column(name = "andar", nullable = true, length = 10)
    private Integer andar;
    @Column(name = "cond", nullable = true)
    private Double condominio;
    @Column(name = "dataEntrada", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataEntrada;
    @Column(name = "destaque", nullable = true, length = 11)
    private Integer destaque;
    @Column(name = "edificio", nullable = true, length = 255)
    private String edificio;
    @Column(name = "reversivel", nullable = true)
    private Boolean reversivel;
    @Column(name = "aptoPorAndar", nullable = true, length = 11)
    private Integer aptoPorAndar;
    @Column(name = "andares", nullable = true, length = 11)
    private Integer andares;
    @Column(name = "locacao")
    private boolean locacao;
    @Column(name = "zona")
    @Enumerated(EnumType.STRING)
    private Zona zona;
    @OneToMany(mappedBy = "imovel", targetEntity = Foto.class)
    @Cascade({CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    @OrderBy(value = "principal desc,site desc,caminho")
    private Set<Foto> fotos = new HashSet<Foto>();
    @ManyToMany(targetEntity = Usuario.class)
    @Cascade({CascadeType.REFRESH})
    @JoinTable(name = "tab_imovel_usuario", joinColumns = {
        @JoinColumn(name = "referencia")}, inverseJoinColumns = {
        @JoinColumn(name = "usuario")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Usuario> usuarios = new HashSet<Usuario>();
    @OneToMany(mappedBy = "imovel", targetEntity = Informacao.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Cascade({CascadeType.LOCK})
    private Set<Informacao> informacoes = new HashSet<Informacao>();
    @ManyToMany(targetEntity = Anuncio.class, fetch = FetchType.EAGER)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JoinTable(name = "tab_imovel_anuncio", joinColumns = {
        @JoinColumn(name = "imovel")}, inverseJoinColumns = {
        @JoinColumn(name = "anuncio")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Anuncio> anuncios = new HashSet<Anuncio>();
    @OneToMany(mappedBy = "imovel", targetEntity = Numero.class, fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private List<Numero> numeros = new ArrayList<Numero>();
    @OneToOne(mappedBy = "imovel", targetEntity = Medidas.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK, CascadeType.DELETE})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Medidas medidass;
    @OneToMany(mappedBy = "imovel", targetEntity = Altera.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK, CascadeType.DELETE_ORPHAN})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Altera> alteracoes = new HashSet<Altera>();
    @OneToMany(mappedBy = "imovel", targetEntity = Valor.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK, CascadeType.DELETE_ORPHAN})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Valor> valores = new HashSet<Valor>();
    @Column(name = "iptu")
    private Double iptu;
    @Transient
    private String logradouro;
    @Transient
    private String endereco;

    public Imovel() {
        medidass = new Medidas(this);
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public Chave getChave() {
        return chave;
    }

    public void setChave(Chave chave) {
        this.chave = chave;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Portaria getPortaria() {
        return portaria;
    }

    public void setPortaria(Portaria portaria) {
        this.portaria = portaria;
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

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public Boolean getDivulgar() {
        return divulgar;
    }

    public void setDivulgar(Boolean divulgar) {
        this.divulgar = divulgar;
    }

    public String getImediacoes() {
        return imediacoes;
    }

    public void setImediacoes(String imediacoes) {
        this.imediacoes = imediacoes;
    }

    public String getProprietarios() {
        return proprietarios;
    }

    public void setProprietarios(String proprietarios) {
        this.proprietarios = proprietarios;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getDorms() {
        return dorms;
    }

    public void setDorms(Integer dorms) {
        this.dorms = dorms;
    }

    public Integer getGaragens() {
        return garagens;
    }

    public void setGaragens(Integer garagens) {
        this.garagens = garagens;
    }

    public Integer getSalas() {
        return salas;
    }

    public void setSalas(Integer salas) {
        this.salas = salas;
    }

    public Integer getBanheiros() {
        return banheiros;
    }

    public void setBanheiros(Integer banheiros) {
        this.banheiros = banheiros;
    }

    public Integer getSuites() {
        return suites;
    }

    public void setSuites(Integer suites) {
        this.suites = suites;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getPrivativa() {
        return privativa;
    }

    public void setPrivativa(Double privativa) {
        this.privativa = privativa;
    }

    public Double getConstruido() {
        return construido;
    }

    public void setConstruido(Double construido) {
        this.construido = construido;
    }

    public Double getTerreno() {
        return terreno;
    }

    public void setTerreno(Double terreno) {
        this.terreno = terreno;
    }

    public String getApto() {
        return apto;
    }

    public void setApto(String apto) {
        this.apto = apto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMedidas() {
        return medidas;
    }

    public void setMedidas(String medidas) {
        this.medidas = medidas;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public Date getCaptacao() {
        return captacao;
    }

    public void setCaptacao(Date captacao) {
        this.captacao = captacao;
    }

    public Date getAtualizado() {
        return atualizado;
    }

    public void setAtualizado(Date atualizado) {
        this.atualizado = atualizado;
    }

    public String getChaveObs() {
        return chaveObs;
    }

    public void setChaveObs(String chaveObs) {
        this.chaveObs = chaveObs;
    }

    public Boolean getVago() {
        return vago;
    }

    public void setVago(Boolean vago) {
        this.vago = vago;
    }

    public Usuario getAtualizadoPor() {
        return atualizadoPor;
    }

    public void setAtualizadoPor(Usuario atualizadoPor) {
        this.atualizadoPor = atualizadoPor;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getProprietarios1() {
        return proprietarios1;
    }

    public void setProprietarios1(String proprietarios1) {
        this.proprietarios1 = proprietarios1;
    }

    public Integer getAndar() {
        return andar;
    }

    public void setAndar(Integer andar) {
        this.andar = andar;
    }

    public Double getCondominio() {
        return condominio;
    }

    public void setCondominio(Double condominio) {
        this.condominio = condominio;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Integer getDestaque() {
        return destaque;
    }

    public void setDestaque(Integer destaque) {
        this.destaque = destaque;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public Boolean getReversivel() {
        return reversivel;
    }

    public void setReversivel(Boolean reversivel) {
        this.reversivel = reversivel;
    }

    public Integer getAptoPorAndar() {
        return aptoPorAndar;
    }

    public void setAptoPorAndar(Integer aptoPorAndar) {
        this.aptoPorAndar = aptoPorAndar;
    }

    public Integer getAndares() {
        return andares;
    }

    public void setAndares(Integer andares) {
        this.andares = andares;
    }

    public Set<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(Set<Foto> fotos) {
        this.fotos = fotos;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Set<Informacao> getInformacoes() {
        return informacoes;
    }

    public void setInformacoes(Set<Informacao> informacoes) {
        this.informacoes = informacoes;
    }

    public Set<Anuncio> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(Set<Anuncio> anuncios) {
        this.anuncios = anuncios;
    }

    public List<Numero> getNumeros() {
        return numeros;
    }

    public void setNumeros(List<Numero> numeros) {
        this.numeros = numeros;
    }

    public Medidas getMedidass() {
        return medidass;
    }

    public void setMedidass(Medidas medidass) {
        this.medidass = medidass;
        this.medidass.setImovel(this);
    }

    public Set<Altera> getAlteracoes() {
        return alteracoes;
    }

    public void setAlteracoes(Set<Altera> alteracoes) {
        this.alteracoes = alteracoes;
    }

    public Set<Valor> getValores() {
        return valores;
    }

    public void setValores(Set<Valor> valores) {
        this.valores = valores;
    }

    public Double getIptu() {
        return iptu;
    }

    public void setIptu(Double iptu) {
        this.iptu = iptu;
    }

    public String getLogradouro() {
        if (cep != null) {
            logradouro = cep.getTipo() + " " + cep.getDescricao();
        }
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public boolean isLocacao() {
        return locacao;
    }

    public void setLocacao(boolean locacao) {
        this.locacao = locacao;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public String getEndereco() {
        if (cep != null) {
            endereco = cep.getTipo() + " " + cep.getDescricao() + ", nº " + getNumero();
        }

        return endereco;
    }

    public String getEnderecoCompleto() {
        StringBuilder builder = new StringBuilder();

        builder.append(getEndereco());

        if (hasBloco()) {
            builder.append(" , Bloco ").append(getBloco());
        }

        if (hasApto()) {
            builder.append(" , Apto ").append(getApto());
        }

        return builder.toString();
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getStringNumero() {
        StringBuilder numeroStr = new StringBuilder();

        for (Iterator<Numero> it = getNumeros().iterator(); it.hasNext();) {
            Numero num = it.next();

            numeroStr.append(num.getNumeroPK().getNumero());

            if (it.hasNext()) {
                numeroStr.append(", ");
            }
        }

        return numeroStr.toString();
    }

    public boolean hasZona() {
        return zona != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Historico)) {
            return false;
        }

        Imovel imovel = (Imovel) obj;

        if ((getReferencia() != null && !getReferencia().equals(imovel.getReferencia())) || (getReferencia() == null && imovel.getReferencia() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.referencia != null ? this.referencia.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getReferencia().toString();
    }

    public boolean hasBloco() {
        return not(getBloco().equals("0")) && not(getBloco().isEmpty());
    }

    public boolean hasApto() {
        return not(getApto().equals("0")) && not(getApto().isEmpty());
    }

    public boolean isRemovido() {
        return getStatus().equals(SUSPENSO) || getStatus().equals(VENDIDO);
    }

    public boolean hasAnunciosLiberaveis() {
        for (Informacao info : informacoes) {
            if (info.getAnuncio().isLiberavel() && info.getLiberado() == null) {
                return true;
            }
        }

        return false;
    }

    public List<Informacao> getInformacoesALiberar() {
        return Collections3.extractByFilter(getInformacoes(), FilterUtils.filtroInformacoesALiberar());
    }
}