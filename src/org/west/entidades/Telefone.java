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
import javax.swing.text.MaskFormatter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *Classe de persistência referente à tabela tab_telefone.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_telefone")
public class Telefone implements Serializable{
    
    @Id
    @Column(name="codigo",length=20, nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo; 
    
    @ManyToOne(targetEntity=Cliente.class,fetch= FetchType.LAZY)
    @JoinColumn(name="cliente",referencedColumnName="codigo")
    @Cascade(CascadeType.LOCK)
    private Cliente cliente;
    @Column(name="tipo",length=50,nullable=false)
    private String tipo; 
    @Column(name="telefone",length=20,nullable=false)
    private String telefone; 
    @Column(name="descricao",length=50,nullable=false)
    private String descricao;     

    public Telefone() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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
        
        if (!(obj instanceof Veiculo))
            return false;
               
        Telefone aTelefone = (Telefone) obj;
        
        if ((getCodigo() != null && !getCodigo().equals(aTelefone.getCodigo())) || (getCodigo() == null && aTelefone.getCodigo() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        String retorno = "";
        
        try{
            MaskFormatter mask = new MaskFormatter("(##) #### - ####");
            mask.setValueContainsLiteralCharacters(false);
            retorno = getTipo() + " " + mask.valueToString(getTelefone()) + ((getDescricao().isEmpty())? "":" (" + getDescricao() + ")");
        }
        catch(Exception ex){ex.printStackTrace();}
        
        return retorno;
    }    
}