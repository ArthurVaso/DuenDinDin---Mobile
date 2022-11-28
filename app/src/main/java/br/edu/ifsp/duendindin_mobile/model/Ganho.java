package br.edu.ifsp.duendindin_mobile.model;

import java.util.Date;

public class Ganho {

    private Integer id;
    private Integer categoriaId;
    private String nome;
    private Date data;
    private Double valor;
    private String descricao;
    private Boolean recorrencia;
    private Boolean tipo;

    public Ganho() {

    }

    public Ganho(Integer id, Integer categoriaId, String nome, Date data, Double valor, String descricao, Boolean recorrencia, Boolean tipo) {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
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

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }
}