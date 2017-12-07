package org.west.entidades;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *Classe de persistência referente à tabela tab_veiculo.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_veiculo")
public class Veiculo implements Serializable{
    
    @Id
    @Column(name="nome",nullable=false,length=100)
    private String nome;
    
    @OneToMany(mappedBy="veiculo",fetch= FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Anuncio> anuncios = new HashSet<Anuncio>();
    

    public Veiculo() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Set<Anuncio> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(Set<Anuncio> anuncios) {
        this.anuncios = anuncios;
    }    

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Veiculo))
            return false;
               
        Veiculo veiculo = (Veiculo) obj;
        
        if ((getNome() != null && !getNome().equals(veiculo.getNome())) || (getNome() == null && veiculo.getNome() != null))
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