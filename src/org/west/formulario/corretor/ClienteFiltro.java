package org.west.formulario.corretor;

import java.util.Date;
import org.west.utilitarios.Util;
import org.west.utilitarios.ValidadorUtil;

public class ClienteFiltro {

    private String nome;
    private String telefone;
    private String imovel;
    private Date inicio;
    private Date fim;
    private String corretor;
    private Double codigo;
    private TipoAcompanhado tipoAcompanhado;
    private Double atendimento;
    private String historico;

    public ClienteFiltro() {
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getImovel() {
        return this.imovel;
    }

    public void setImovel(String imovel) {
        this.imovel = imovel;
    }

    public Date getInicio() {
        return this.inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = Util.corrigirDate(inicio, Util.INICIO);
    }

    public Date getFim() {
        return this.fim;
    }

    public void setFim(Date fim) {
        this.fim = Util.corrigirDate(fim, Util.FIM);
    }

    public String getCorretor() {
        return this.corretor;
    }

    public void setCorretor(String corretor) {
        this.corretor = corretor;
    }

    public Double getCodigo() {
        return codigo;
    }

    public void setCodigo(Double codigo) {
        this.codigo = codigo;
    }

    public TipoAcompanhado getTipoAcompanhado() {
        return tipoAcompanhado;
    }

    public void setTipoAcompanhado(TipoAcompanhado tipoAcompanhado) {
        this.tipoAcompanhado = tipoAcompanhado;
    }

    public Double getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Double atendimento) {
        this.atendimento = atendimento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public boolean hasCodigoCliente() {
        return ValidadorUtil.isNotEmpty(this.codigo);
    }

    public boolean hasImovel() {
        return ValidadorUtil.isNotEmpty(imovel);
    }

    public boolean hasNomeCliente() {
        return ValidadorUtil.isNotEmpty(nome);
    }

    public boolean hasTelefone() {
        return ValidadorUtil.isNotEmpty(telefone);
    }

    public boolean hasTipoAcompanhado() {
        if (ValidadorUtil.isNotNull(tipoAcompanhado)) {
            return ValidadorUtil.isNotNull(tipoAcompanhado.getValue());
        }

        return false;
    }

    public boolean hasHistorico() {
        return ValidadorUtil.isNotEmpty(historico);
    }
}