package org.west.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *Classe de persistência referente à tabela tab_eventos.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_eventos")
public class Evento implements Serializable {

    @Id
    @Column(name = "codigo", length = 20, nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long codigo;
    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;
    @Column(name = "dataStart", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataStart;
    @Column(name = "dataEnd", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataEnd;

    public Evento() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getDataStart() {
        return dataStart;
    }

    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }

    public Date getDataEnd() {
        return dataEnd;
    }

    public void setDataEnd(Date dataEnd) {
        this.dataEnd = dataEnd;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Evento)) {
            return false;
        }

        Evento evento = (Evento) obj;

        if ((getCodigo() != null && !getCodigo().equals(evento.getCodigo())) || (getCodigo() == null && evento.getCodigo() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getTitulo();
    }    
}