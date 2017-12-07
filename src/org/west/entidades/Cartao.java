package org.west.entidades;



import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *Classe de persistência referente à tabela tab_cartao.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_cartao")
public class Cartao implements Serializable{
    
    @PrimaryKeyJoinColumn
    @OneToOne(targetEntity=Usuario.class,fetch= FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE,CascadeType.LOCK})
    @JoinColumns(@JoinColumn(name="usuario"))
    private Usuario usuario;
    
    @Id
    @GeneratedValue(generator="usuarioCartao")
    @GenericGenerator(name="usuarioCartao",strategy="foreign",parameters={@Parameter(name="property",value="usuario")})
    @Column(name="usuario",nullable=false)
    private Long usuarioID;
    
    @Column(name="quantidade",length=20,nullable=false)
    private Long quantidade;

    public Cartao() {
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Long usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
    
    @Override
    public String toString() {
        return usuario.toString() + " (" + quantidade.toString() + ")";
    }
}