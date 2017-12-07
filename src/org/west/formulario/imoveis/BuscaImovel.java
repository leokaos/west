package org.west.formulario.imoveis;

import java.util.Date;
import org.west.entidades.Medidas;

public class BuscaImovel {

    private String referencia;
    private Object tipos;
    private String endereco;
    private Object bairro;
    private Object anuncios;
    private Boolean comFotos;
    private Boolean semFotos;
    private String edificio;
    private String proprietario;
    private Object dorms;
    private Object suites;
    private Object garagens;
    private Object areaPrivativa;
    private Object valor;
    private Date dataInicial;
    private Date dataFinal;
    private Date capInicial;
    private Date capFinal;
    private Object status;
    private String corretor;
    private Boolean vago;
    private String tamanho;
    private Boolean divulgar;
    private Integer destaque;
    private String descricao;
    private Medidas medidas;

    public BuscaImovel() {
        this.medidas = new Medidas();
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Object getTipos() {
        return tipos;
    }

    public void setTipos(Object tipos) {
        this.tipos = tipos;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Object getBairro() {
        return bairro;
    }

    public void setBairro(Object bairro) {
        this.bairro = bairro;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public Object getDorms() {
        return dorms;
    }

    public void setDorms(Object dorms) {
        this.dorms = dorms;
    }

    public Object getSuites() {
        return suites;
    }

    public void setSuites(Object suites) {
        this.suites = suites;
    }

    public Object getGaragens() {
        return garagens;
    }

    public void setGaragens(Object garagens) {
        this.garagens = garagens;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getAreaPrivativa() {
        return areaPrivativa;
    }

    public void setAreaPrivativa(Object areaPrivativa) {
        this.areaPrivativa = areaPrivativa;
    }

    public String getCorretor() {
        return corretor;
    }

    public void setCorretor(String corretor) {
        this.corretor = corretor;
    }

    public Object getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(Object anuncios) {
        this.anuncios = anuncios;
    }

    public Boolean getVago() {
        return vago;
    }

    public void setVago(Boolean vago) {
        this.vago = vago;
    }

    public Boolean getComFotos() {
        return comFotos;
    }

    public void setComFotos(Boolean comFotos) {
        this.comFotos = comFotos;
    }

    public Boolean getSemFotos() {
        return semFotos;
    }

    public void setSemFotos(Boolean semFotos) {
        this.semFotos = semFotos;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Boolean getDivulgar() {
        return divulgar;
    }

    public void setDivulgar(Boolean divulgar) {
        this.divulgar = divulgar;
    }

    public Date getCapInicial() {
        return capInicial;
    }

    public void setCapInicial(Date capInicial) {
        this.capInicial = capInicial;
    }

    public Date getCapFinal() {
        return capFinal;
    }

    public void setCapFinal(Date capFinal) {
        this.capFinal = capFinal;
    }

    public Integer getDestaque() {
        return destaque;
    }

    public void setDestaque(Integer destaque) {
        this.destaque = destaque;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Medidas getMedidas() {
        return medidas;
    }

    public void setMedidas(Medidas medidas) {
        this.medidas = medidas;
    }
}