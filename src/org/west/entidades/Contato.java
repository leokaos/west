package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *Classe de persistência referente à tabela tab_contato.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_contatos")
public class Contato implements Serializable{
    
    @Id
    @Column(name="codigo",length=20,nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    @Column(name="nome",nullable=false)
    private String nome;
    @Column(name="telefone1",length=50,nullable=true)
    private String telefone1;
    @Column(name="telefone2",length=50,nullable=true)
    private String telefone2;
    @Column(name="telefone3",length=50,nullable=true)
    private String telefone3;
    @Column(name="telefone4",length=50,nullable=true)
    private String telefone4; 
    @Column(name="telefone5",length=50,nullable=true)
    private String telefone5; 
    @Column(name="email",length=50,nullable=true)
    private String email;  
    @Column(name="obs",nullable=true)
    private String obs;

    public Contato() {
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

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getTelefone3() {
        return telefone3;
    }

    public void setTelefone3(String telefone3) {
        this.telefone3 = telefone3;
    }

    public String getTelefone4() {
        return telefone4;
    }

    public void setTelefone4(String telefone4) {
        this.telefone4 = telefone4;
    }

    public String getTelefone5() {
        return telefone5;
    }

    public void setTelefone5(String telefone5) {
        this.telefone5 = telefone5;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Contato))
            return false;
               
        Contato contato = (Contato) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(contato.getCodigo())) || (getCodigo() == null && contato.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getCodigo().toString();
    }    
}