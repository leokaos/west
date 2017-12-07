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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.SQLQuery;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import static org.west.utilitarios.ValidadorUtil.isEmpty;

/**
 * Classe de persistência referente à tabela tab_imobiliaria.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_servico")
public class Servico implements Serializable {

    @Id
    @Column(name = "codigo", length = 20, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;
    @Column(name = "nome", length = 255, nullable = true)
    private String nome;
    @ManyToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "responsavel", referencedColumnName = "codigo")
    @Cascade(CascadeType.LOCK)
    private Usuario responsavel;
    @ManyToOne(targetEntity = TipoServico.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo")
    private TipoServico tipoServico;
    @Column(name = "concluido", nullable = false, length = 10)
    private Integer concluido = 0;
    @ManyToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "token", referencedColumnName = "codigo")
    @Cascade(CascadeType.LOCK)
    private Usuario token;
    @Column(name = "dataToken", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataToken;
    @Column(name = "dataEntrada", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;
    @Column(name = "cliente", length = 100, nullable = false)
    private String cliente;
    @Column(name = "endereco", length = 100, nullable = false)
    private String endereco;
    @Column(name = "telefone", length = 255, nullable = false)
    private String telefone;
    @Column(name = "dataTermino", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTermino;
    @Column(name = "formaPagamento", nullable = false)
    private String formaPagamento;
    @Column(name = "obs", nullable = true, length = 255)
    private String obs;
    @Column(name = "pastaMae", nullable = true, length = 255)
    private String pastaMae;
    @OneToMany(mappedBy = "servico", targetEntity = Tarefa.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    @OrderBy(value = "codigo")
    private Set<Tarefa> tarefas = new HashSet<Tarefa>();
    @OneToMany(mappedBy = "servico", targetEntity = Lancamento.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Lancamento> lancamentos = new HashSet<Lancamento>();
    @Formula(value = "(select sum(T.valor) from tab_tarefas T where T.servico = codigo)")
    private Double valorBruto;
    @Formula(value = "(select sum(L.entrada) - sum(L.saida) from tab_lancamentos L where L.servico = codigo)")
    private Double valorLancamentos;
    @Formula(value = "(select T.previsaoTermino from tab_tarefas T where T.servico = codigo and T.terminado = 0 order by T.previsaoTermino limit 1)")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultimaPrevisaoTermino;
    @ManyToMany(targetEntity = Cliente.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @JoinTable(name = "tab_vendedor", joinColumns = {
        @JoinColumn(name = "servico")}, inverseJoinColumns = {
        @JoinColumn(name = "cliente")})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Cliente> vendedores = new HashSet<Cliente>();
    @ManyToMany(targetEntity = Cliente.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @JoinTable(name = "tab_comprador", joinColumns = {
        @JoinColumn(name = "servico")}, inverseJoinColumns = {
        @JoinColumn(name = "cliente")})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Cliente> compradores = new HashSet<Cliente>();
    @Transient
    private Boolean inativo;

    public Servico() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public Integer getConcluido() {
        return concluido;
    }

    public void setConcluido(Integer concluido) {
        this.concluido = concluido;
    }

    public Usuario getToken() {
        return token;
    }

    public void setToken(Usuario token) {
        this.token = token;
    }

    public Date getDataToken() {
        return dataToken;
    }

    public void setDataToken(Date dataToken) {
        this.dataToken = dataToken;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getPastaMae() {
        return pastaMae;
    }

    public void setPastaMae(String pastaMae) {
        this.pastaMae = pastaMae;
    }

    public Set<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(Set<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public Set<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(Set<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
    }

    public Double getValorBruto() {
        return this.valorBruto;
    }

    public void setValorBruto(Double valorBruto) {
        this.valorBruto = valorBruto;
    }

    public Double getValorLancamentos() {
        return valorLancamentos;
    }

    public void setValorLancamentos(Double valorLancamentos) {
        this.valorLancamentos = valorLancamentos;
    }

    public Date getUltimaPrevisaoTermino() {
        return ultimaPrevisaoTermino;
    }

    public void setUltimaPrevisaoTermino(Date ultimaPrevisaoTermino) {
        this.ultimaPrevisaoTermino = ultimaPrevisaoTermino;
    }

    public Set<Cliente> getVendedores() {
        return vendedores;
    }

    public void setVendedores(Set<Cliente> vendedores) {
        this.vendedores = vendedores;
    }

    public Set<Cliente> getCompradores() {
        return compradores;
    }

    public void setCompradores(Set<Cliente> compradores) {
        this.compradores = compradores;
    }

    public Boolean isInativo() {
        if (inativo == null) {
            String strQuery = "Select S.* from tab_servico S where S.concluido = 0 and "
                    + "(select count(*) from tab_tarefas T where T.servico = S.codigo and terminado = 0) = 0 "
                    + "and S.codigo = " + getCodigo();

            SQLQuery query = WestPersistentManager.getSession().createSQLQuery(strQuery);

            inativo = !query.list().isEmpty();
        }

        return inativo;
    }

    public void setInativo(Boolean inativo) {
        this.inativo = inativo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Servico)) {
            return false;
        }

        Servico servico = (Servico) obj;

        if ((getCodigo() != null && !getCodigo().equals(servico.getCodigo())) || (getCodigo() == null && servico.getCodigo() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        ServicoDAO.lock(this);
        return getCodigo().toString() + " - " + ((getCompradores().size() > 0) ? compradores.iterator().next().toString() : cliente);
    }

    public boolean isNovo() {
        return isEmpty(codigo);
    }
}