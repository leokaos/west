package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *Classe de persistência referente à tabela tab_saldo.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_saldo")
public class Saldo implements Serializable{
    
    @Id
    @Column(name="area",nullable=false,length=100)
    private String area;
    @Column(name="saldo",nullable=false)
    private Double saldo;    

    public Saldo() {
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Saldo))
            return false;
               
        Saldo aSaldo = (Saldo) obj;
        
        if ((getArea() != null && !getArea().equals(aSaldo.getArea())) || (getArea() == null && aSaldo.getArea() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.area != null ? this.area.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getArea();
    }    
}