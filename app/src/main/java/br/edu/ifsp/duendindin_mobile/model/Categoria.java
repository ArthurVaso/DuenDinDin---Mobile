package br.edu.ifsp.duendindin_mobile.model;

import java.util.Objects;

public class Categoria {
    private Integer id;
    private Integer usuarioId;
    private String nome;
    private String descricao;
    private Double gastoFixo;
    private Double ganhoFixo;
    private Double gastoVariavel;
    private Double ganhoVariavel;
    private Double valor;

    public Categoria() {
    }

    public Categoria(Integer id, Integer usuarioId, String nome, String descricao, Double gastoFixo, Double ganhoFixo, Double gastoVariavel, Double ganhoVariavel, Double valor) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.descricao = descricao;
        this.gastoFixo = gastoFixo;
        this.ganhoFixo = ganhoFixo;
        this.gastoVariavel = gastoVariavel;
        this.ganhoVariavel = ganhoVariavel;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getGastoFixo() {
        return gastoFixo;
    }

    public void setGastoFixo(Double gastoFixo) {
        this.gastoFixo = gastoFixo;
    }

    public Double getGanhoFixo() {
        return ganhoFixo;
    }

    public void setGanhoFixo(Double ganhoFixo) {
        this.ganhoFixo = ganhoFixo;
    }

    public Double getGastoVariavel() {
        return gastoVariavel;
    }

    public void setGastoVariavel(Double gastoVariavel) {
        this.gastoVariavel = gastoVariavel;
    }

    public Double getGanhoVariavel() {
        return ganhoVariavel;
    }

    public void setGanhoVariavel(Double ganhoVariavel) {
        this.ganhoVariavel = ganhoVariavel;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
