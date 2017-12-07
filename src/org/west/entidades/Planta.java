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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.west.utilitarios.ValidadorUtil;

/**
 * Classe de persistência referente à tabela tab_planta.
 *
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name = "tab_planta")
public class Planta implements Serializable {

    @Id
    @Column(name = "codigo", length = 20, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;
    @ManyToOne(targetEntity = Portaria.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "portaria", referencedColumnName = "codigo")
    @Cascade(CascadeType.LOCK)
    private Portaria portaria;
    @Column(name = "suites", length = 10, nullable = false)
    private Integer suites;
    @Column(name = "dorms", length = 10, nullable = false)
    private Integer dorms;
    @Column(name = "vagas", length = 10, nullable = false)
    private Integer vagas;
    @Column(name = "privativa", nullable = false)
    private Double privativa;
    @Column(name = "salas", length = 10, nullable = false)
    private Integer salas;
    @Column(name = "banheiros", length = 10, nullable = false)
    private Integer banheiros;
    @Column(name = "andares", length = 10, nullable = true)
    private Integer andares;
    @Column(name = "aptoPorAndar", length = 10, nullable = true)
    private Integer aptoPorAndar;
    @Column(name = "reversivel", length = 10, nullable = true)
    private Integer reversivel;

    public Planta() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Portaria getPortaria() {
        return portaria;
    }

    public void setPortaria(Portaria portaria) {
        this.portaria = portaria;
    }

    public Integer getSuites() {
        return suites;
    }

    public void setSuites(Integer suites) {
        this.suites = suites;
    }

    public Integer getDorms() {
        return dorms;
    }

    public void setDorms(Integer dorms) {
        this.dorms = dorms;
    }

    public Integer getVagas() {
        return vagas;
    }

    public void setVagas(Integer vagas) {
        this.vagas = vagas;
    }

    public Double getPrivativa() {
        return privativa;
    }

    public void setPrivativa(Double privativa) {
        this.privativa = privativa;
    }

    public Integer getSalas() {
        return salas;
    }

    public void setSalas(Integer salas) {
        this.salas = salas;
    }

    public Integer getBanheiros() {
        return banheiros;
    }

    public void setBanheiros(Integer banheiros) {
        this.banheiros = banheiros;
    }

    public Integer getAndares() {
        return andares;
    }

    public void setAndares(Integer andares) {
        this.andares = andares;
    }

    public Integer getAptoPorAndar() {
        return aptoPorAndar;
    }

    public void setAptoPorAndar(Integer aptoPorAndar) {
        this.aptoPorAndar = aptoPorAndar;
    }

    public Integer getReversivel() {
        return reversivel;
    }

    public void setReversivel(Integer reversivel) {
        this.reversivel = reversivel;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Modifica)) {
            return false;
        }

        Modifica Modifica = (Modifica) obj;

        if ((getCodigo() != null && !getCodigo().equals(Modifica.getCodigo())) || (getCodigo() == null && Modifica.getCodigo() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(dorms);
        sb.append(" Dorms, ");
        sb.append(suites == null ? 0 : suites);
        sb.append(" Suítes, ");
        sb.append(vagas);
        sb.append(" Vagas, ");
        sb.append(privativa);
        sb.append(" m² Privativa, ");
        sb.append(andares);
        sb.append(" Andares, ");
        sb.append(aptoPorAndar);
        sb.append(" Aptos. por Andar");

        if (ValidadorUtil.isNotNull(reversivel) && reversivel > 0) {
            sb.append(",");
            sb.append(reversivel);
            sb.append(" Rev.");
        } else {
            sb.append(".");
        }

        return sb.toString();
    }
}