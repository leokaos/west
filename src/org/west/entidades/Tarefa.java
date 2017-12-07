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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Classe de persistência referente à tabela tab_tarefa.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_tarefas")
public class Tarefa implements Serializable, Comparable<Tarefa> {

    @Id
    @Column(name = "codigo", length = 20, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;
    @Column(name = "dataInicio", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;
    @Column(name = "previsaoTermino", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date previsaoTermino;
    @Column(name = "aviso", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date aviso;
    @ManyToOne(targetEntity = Servico.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "servico", referencedColumnName = "codigo")
    @Cascade(CascadeType.LOCK)
    private Servico servico;
    @ManyToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario", referencedColumnName = "codigo")
    @Cascade(CascadeType.LOCK)
    private Usuario usuario;
    @Column(name = "terminado", nullable = false)
    private Boolean terminado;
    @Column(name = "arquivo", length = 255, nullable = true)
    private String arquivo;
    @Column(name = "descricao", nullable = false)
    private String descricao;
    @Column(name = "dataTermino", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTermino;
    @Column(name = "dataAtualizacao")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataAtualizacao;
    @Column(name = "valor", nullable = false)
    private Double valor;
    @OneToMany(mappedBy = "tarefa", fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Modifica> modifica = new HashSet<Modifica>();
    @Transient
    private String textoAdd;

    public Tarefa() {
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

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getPrevisaoTermino() {
        return previsaoTermino;
    }

    public void setPrevisaoTermino(Date previsaoTermino) {
        this.previsaoTermino = previsaoTermino;
    }

    public Date getAviso() {
        return aviso;
    }

    public void setAviso(Date aviso) {
        this.aviso = aviso;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getTerminado() {
        return terminado;
    }

    public void setTerminado(Boolean terminado) {
        this.terminado = terminado;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Set<Modifica> getModifica() {
        return modifica;
    }

    public void setModifica(Set<Modifica> modifica) {
        this.modifica = modifica;
    }

    public String getTextoAdd() {
        return textoAdd;
    }

    public void setTextoAdd(String textoAdd) {
        this.textoAdd = textoAdd;
    }

    public StatusTarefa getStatus() {
        return StatusTarefa.parseTarefa(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Tarefa)) {
            return false;
        }

        Tarefa tarefa = (Tarefa) obj;

        if ((getCodigo() != null && !getCodigo().equals(tarefa.getCodigo())) || (getCodigo() == null && tarefa.getCodigo() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo().toString() + " - " + getNome().toString();
    }

    @Override
    public int compareTo(Tarefa o) {
        return getStatus().compareTo(o.getStatus());
    }

    public boolean isNovo() {
        return codigo == null || codigo == 0;
    }
}
