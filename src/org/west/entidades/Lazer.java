package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *Classe de persistência referente à tabela tab_lazer.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_lazeres")
public class Lazer implements Serializable{
    
    @Id
    @Column(name="nome",nullable=false,length=100)
    private String nome;

    public Lazer() {
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
        
        if (!(obj instanceof Lazer))
            return false;
               
        Lazer lazer = (Lazer) obj;
        
        if ((getNome() != null && !getNome().equals(lazer.getNome())) || (getNome() == null && lazer.getNome() != null))
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