package br.edu.ifsp.duendindin_mobile.model;

import com.google.gson.annotations.SerializedName;

public class Ganho {

    private Integer id;
    @SerializedName("categoriaID")
    private Integer categoriaId;
    private String nome;
    private String data;
    private Double valor;
    private String descricao;
    @SerializedName("recorrente")
    private Boolean recorrencia;
    private String tipo;

    public Ganho() {

    }

    public Ganho(Integer id, Integer categoriaId, String nome, String data, Double valor, String descricao, Boolean recorrencia, String tipo) {
        this.id = id;
        this.categoriaId = categoriaId;
        this.nome = nome;
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;
        this.recorrencia = recorrencia;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }


    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getRecorrencia() {
        return recorrencia;
    }

    public void setRecorrencia(Boolean recorrencia) {
        this.recorrencia = recorrencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
