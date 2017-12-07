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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Classe de persistência referente à tabela tab_cliente.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_cliente")
public class Cliente implements Serializable {

    @Id
    @Column(name = "codigo", nullable = false, length = 20)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;
    @Column(name = "nome", nullable = true, length = 200)
    private String nome;
    @Column(name = "telefone", nullable = true, length = 100)
    private String telefone;
    @Column(name = "endereco", nullable = true, length = 100)
    private String endereco;
    @Column(name = "CPF", nullable = true, length = 14)
    private String CPF;
    @Column(name = "CNPJ", nullable = true, length = 14)
    private String CNPJ;
    @Column(name = "RG", nullable = true, length = 20)
    private String RG;
    @Column(name = "email", nullable = true, length = 50)
    private String email;
    @Column(name = "dataNascimento", nullable = true, length = 200)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataNascimento;
    @ManyToOne(targetEntity = Cliente.class, fetch = FetchType.LAZY)
    @Cascade(CascadeType.LOCK)
    @JoinColumns({
        @JoinColumn(name = "conjuge", referencedColumnName = "codigo")})
    private Cliente conjuge;
    @Column(name = "profissao", nullable = true, length = 50)
    private String profissao;
    @OneToMany(mappedBy = "cliente", targetEntity = Email.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Email> emails = new HashSet<Email>();
    @OneToMany(mappedBy = "cliente", targetEntity = Endereco.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Endereco> enderecos = new HashSet<Endereco>();
    @OneToMany(mappedBy = "cliente", targetEntity = Telefone.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Telefone> telefones = new HashSet<Telefone>();
    @OneToMany(mappedBy = "cliente", targetEntity = Imobiliaria.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    @OrderBy(value = "dataEntrada desc")
    private Set<Imobiliaria> imobiliarias = new HashSet<Imobiliaria>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_civil_id")
    private EstadoCivil estadoCivil;

    public Cliente() {
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Cliente getConjuge() {
        return conjuge;
    }

    public void setConjuge(Cliente conjuge) {
        this.conjuge = conjuge;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public Set<Email> getEmails() {
        return emails;
    }

    public void setEmails(Set<Email> emails) {
        this.setEmails(emails);
    }

    public Set<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }

    public Set<Imobiliaria> getImobiliarias() {
        return imobiliarias;
    }

    public void setImobiliarias(Set<Imobiliaria> imobiliarias) {
        this.imobiliarias = imobiliarias;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Cliente)) {
            return false;
        }

        Cliente cliente = (Cliente) obj;

        if ((getCodigo() != null && !getCodigo().equals(cliente.getCodigo())) || (getCodigo() == null && cliente.getCodigo() != null)) {
            return false;
        }

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
        return getNome();
    }
}