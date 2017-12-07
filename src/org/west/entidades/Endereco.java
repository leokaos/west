package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *Classe de persistência referente à tabela tab_endereco.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_endereco")
public class Endereco implements Serializable {

    @Id
    @Column(name = "codigo", length = 20, nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    
    @ManyToOne(targetEntity = Cliente.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente", referencedColumnName = "codigo")
    @Cascade(CascadeType.LOCK)
    private Cliente cliente;
    @ManyToOne(targetEntity = Cep.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "CEP", referencedColumnName = "CEP")
    @Cascade(CascadeType.LOCK)
    private Cep cep;
    @Column(name = "numero", length = 10, nullable = false)
    private String numero;
    @Column(name = "complemento", length = 50, nullable = false)
    private String complemento;
    @Column(name = "bairro", length = 100, nullable = false)
    private String bairro;
    @Column(name = "cidade", length = 100, nullable = false)
    private String cidade;
    @Column(name = "uf", length = 2, nullable = false)
    private String uf;
    
    @Transient
    private String logradouro;

    public Endereco() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
    
    public String getLogradouro() {
        if (cep != null){
            logradouro = cep.getTipo() + " " + cep.getDescricao() + ", nº " + numero;
        }
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Endereco)) {
            return false;
        }

        Endereco endereco = (Endereco) obj;

        if ((getCodigo() != null && !getCodigo().equals(endereco.getCodigo())) || (getCodigo() == null && endereco.getCodigo() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCep().toString() + ", nº " +numero;
    }
}