package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe de persistência referente à tabela tab_tipo_servico.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_tipo_servico")
public class TipoServico implements Serializable, Comparable<TipoServico> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "icone")
    private String icone;
    @Column(name = "gera_tarefa_comissao")
    private boolean geraTarefaComissao;
    @Column(name = "ativo")
    private boolean ativo;

    public TipoServico() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public boolean isGeraTarefaComissao() {
        return geraTarefaComissao;
    }

    public void setGeraTarefaComissao(boolean geraTarefaComissao) {
        this.geraTarefaComissao = geraTarefaComissao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 71 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
        hash = 71 * hash + (this.icone != null ? this.icone.hashCode() : 0);
        hash = 71 * hash + (this.geraTarefaComissao ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoServico other = (TipoServico) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.descricao == null) ? (other.descricao != null) : !this.descricao.equals(other.descricao)) {
            return false;
        }
        if ((this.icone == null) ? (other.icone != null) : !this.icone.equals(other.icone)) {
            return false;
        }
        if (this.geraTarefaComissao != other.geraTarefaComissao) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(TipoServico o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public String toString() {
        return getDescricao();
    }
}
