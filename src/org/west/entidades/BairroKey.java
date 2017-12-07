package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *Classe de persistência referente à Chave Primária da  tabela tab_bairro.
 * @author WestGuerra Ltda.
 */
@Embeddable
public class BairroKey implements Serializable {
    
    @Column(name="nome",length=255,nullable=false)
    private String nome;

    @Column(name="cidade",length=255,nullable=false)
    private String cidade;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof BairroKey))
            return false;
        
        BairroKey bairroKey = (BairroKey) obj;
        
        if ((getNome() != null && !getNome().equals(bairroKey.getNome())) || (getNome() == null && bairroKey.getNome() != null))
                return false;
        
        if ((getCidade() != null && !getCidade().equals(bairroKey.getCidade())) || (getCidade() == null && bairroKey.getCidade() != null))
                return false;        
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 59 * hash + (this.cidade != null ? this.cidade.hashCode() : 0);
        return hash;
    }
}