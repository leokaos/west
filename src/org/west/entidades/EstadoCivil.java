package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe de persistência referente à tabela tab_estado_civil.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_estado_civil")
public class EstadoCivil implements Serializable {

    @Id
    @Column(name = "codigo", nullable = false, length = 20)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;
    @Column(name = "descricao", nullable = true)
    private String descricao;

    public EstadoCivil() {
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
    public String toString() {
        return descricao;
    }
}
