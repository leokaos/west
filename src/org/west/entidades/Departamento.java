package org.west.entidades;

import java.io.Serializable;
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
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "tab_departamento")
public class Departamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "nome")
    private String nome;
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({CascadeType.ALL})
    @JoinTable(name = "tab_departamento_usuario", joinColumns = {
        @JoinColumn(name = "departamento_id")}, inverseJoinColumns = {
        @JoinColumn(name = "usuario_codigo")})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Usuario> usuarios;
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({CascadeType.ALL})
    @JoinTable(name = "tab_departamento_tipo", joinColumns = {
        @JoinColumn(name = "departamento_id")}, inverseJoinColumns = {
        @JoinColumn(name = "tipo_id")})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<TipoServico> tipos;

    public Departamento() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Set<TipoServico> getTipos() {
        return tipos;
    }

    public void setTipos(Set<TipoServico> tipos) {
        this.tipos = tipos;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 97 * hash + (this.usuarios != null ? this.usuarios.hashCode() : 0);
        hash = 97 * hash + (this.tipos != null ? this.tipos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Departamento other = (Departamento) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.nome == null) ? (other.nome != null) : !this.nome.equals(other.nome)) {
            return false;
        }
        if (this.usuarios != other.usuarios && (this.usuarios == null || !this.usuarios.equals(other.usuarios))) {
            return false;
        }
        if (this.tipos != other.tipos && (this.tipos == null || !this.tipos.equals(other.tipos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNome();
    }
}