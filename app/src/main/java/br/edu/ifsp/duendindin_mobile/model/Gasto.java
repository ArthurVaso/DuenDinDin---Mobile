package br.edu.ifsp.duendindin_mobile.model;

import java.util.Date;

public class Gasto {

    private Integer id;
    private Integer categoriaId;
    private String nome;
    private Boolean recorrencia;
    private Date vencimento;
    private Double valor;
    private String descricao;
    private Boolean tipo;
    private Boolean pago;

    public Gasto() {
    }

    public Gasto(Integer id, Integer categoriaId, String nome, Boolean recorrencia, Date vencimento, Double valor, String descricao, Boolean tipo, Boolean pago) {
        this.id = id;
        this.categoriaId = categoriaId;
        this.nome = nome;
        this.recorrencia = recorrencia;
        this.vencimento = vencimento;
        this.valor = valor;
        this.descricao = descricao;
        this.tipo = tipo;
        this.pago = pago;
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

    public Boolean getRecorrencia() {
        return recorrencia;
    }

    public void setRecorrencia(Boolean recorrencia) {
        this.recorrencia = recorrencia;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
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

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }
}

