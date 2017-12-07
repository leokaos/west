package org.west.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import static org.west.utilitarios.ValidadorUtil.*;

/**
 * Classe de persistência referente à tabela tab_usuario.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_usuario")
public class Usuario implements Serializable, Comparable<Usuario> {

    //constantes de nivel
    public final static int RECEPCAO = 1;
    public final static int CORRETOR = 2;
    public final static int GESTOR_COMERCIAL = 3;
    public final static int ADMINISTRATIVO = 4;
    public final static int GESTOR_ADMINISTRATIVO = 5;
    @Id
    @Column(name = "codigo", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;
    private String nome = "";
    @Column(name = "senha", nullable = false, length = 40)
    private String senha = "";
    @Column(name = "nivel", nullable = false, length = 10)
    private int nivel;
    @Column(name = "status", nullable = false)
    private Boolean status;
    @Column(name = "grupo", nullable = true, length = 255)
    private String grupo;
    @Column(name = "nomeCompleto", nullable = true, length = 255)
    private String nomeCompleto;
    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado = false;
    @Column(name = "supervisor", nullable = false)
    private Boolean supervisor = false;
    @Column(name = "cor", nullable = true, length = 11)
    private String cor;
    @Column(name = "email", nullable = true, length = 50)
    private String email;
    @Column(name = "coordenador", nullable = true)
    private Boolean coordenador;
    @OneToOne(mappedBy = "usuario", targetEntity = Cartao.class)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Cartao cartao;
    @ManyToMany(mappedBy = "usuarios", targetEntity = Imovel.class)
    @Cascade({CascadeType.MERGE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Imovel> imoveis = new HashSet<Imovel>();
    @ManyToMany(mappedBy = "usuario", targetEntity = Imobiliaria.class)
    @Cascade({CascadeType.MERGE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Imobiliaria> imobiliaria = new HashSet<Imobiliaria>();
    @ManyToMany(targetEntity = Departamento.class)
    @Cascade({CascadeType.ALL})
    @JoinTable(name = "tab_departamento_usuario",
    joinColumns = {
        @JoinColumn(name = "usuario_codigo")},
    inverseJoinColumns = {
        @JoinColumn(name = "departamento_id")})
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Departamento> departamentos = new ArrayList<Departamento>();

    public Usuario() {
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public Boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Boolean isSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Boolean supervisor) {
        this.supervisor = supervisor;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public Set<Imovel> getImoveis() {
        return imoveis;
    }

    public void setImoveis(Set<Imovel> imoveis) {
        this.imoveis = imoveis;
    }

    public Set<Imobiliaria> getImobiliaria() {
        return imobiliaria;
    }

    public void setImobiliaria(Set<Imobiliaria> imobiliaria) {
        this.imobiliaria = imobiliaria;
    }

    public Boolean isCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Boolean coordenador) {
        this.coordenador = coordenador;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Usuario)) {
            return false;
        }

        Usuario usuario = (Usuario) obj;

        if ((getCodigo() != null && !getCodigo().equals(usuario.getCodigo())) || (getCodigo() == null && usuario.getCodigo() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getNome();
    }

    @Override
    public int compareTo(Usuario o) {
        return getNome().compareTo(o.getNome());
    }

    public boolean isDoGrupo(TipoServico tipoServico) {
        if (isNull(grupo) || isNull(tipoServico)) {
            return false;
        }

        return grupo.equals(tipoServico.getDescricao());
    }
}