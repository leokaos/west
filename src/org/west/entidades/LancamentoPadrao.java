package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *Classe de persistência referente à tabela tab_lancamento_padrao.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_lancamentos_padrao")
public class LancamentoPadrao implements Serializable{
    
    @Id
    @Column(name="codigo",length=20,nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo; 
    @Column(name="descricao",length=255,nullable=false)
    private String descricao;

    public LancamentoPadrao() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof LancamentoPadrao))
            return false;
               
        LancamentoPadrao LancamentoPadrao = (LancamentoPadrao) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(LancamentoPadrao.getCodigo())) || (getCodigo() == null && LancamentoPadrao.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getDescricao();
    }
}