package org.west.entidades;



import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *Classe de persistência referente à tabela tab_chaves.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_chaves")
public class Chave implements Serializable{
    
    @Id
    @Column(name="nome",nullable=false)
    private String nome;

    public Chave() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Chave))
            return false;
               
        Chave chave = (Chave) obj;
        
        if ((getNome() != null && !getNome().equals(chave.getNome())) || (getNome() == null && chave.getNome() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getNome();
    }
}