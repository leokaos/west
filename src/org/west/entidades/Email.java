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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *Classe de persistência referente à tabela tab_email.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_email")
public class Email implements Serializable{
    
    @Id
    @Column(name="codigo",length=20, nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    @Column(name="email",length=100,nullable=true)
    private String email;  
    @ManyToOne(targetEntity=Cliente.class,fetch= FetchType.LAZY)
    @JoinColumn(name="cliente",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Cliente cliente;

    public Email() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Veiculo))
            return false;
               
        Email aEmail = (Email) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(aEmail.getCodigo())) || (getCodigo() == null && aEmail.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getEmail();
    }
    
}