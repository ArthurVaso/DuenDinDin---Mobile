package br.edu.ifsp.duendindin_mobile.model;

import java.util.Date;

public class Configuracao {

    private Integer id;
    private Integer usuarioId;
    private Double rendaFixa;
    private Double limiteLazer;
    private Double limiteContas;
    private Double limiteInvestimento;


    public Configuracao() {
    }

    public Configuracao(Integer id, Integer usuarioId, Double rendaFixa, Double limiteLazer, Double limiteContas, Double limiteInvestimento) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.rendaFixa = rendaFixa;
        this.limiteLazer = limiteLazer;
        this.limiteContas = limiteContas;
        this.limiteInvestimento = limiteInvestimento;
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

    public Double getRendaFixa() {
        return rendaFixa;
    }

    public void setRendaFixa(Double rendaFixa) {
        this.rendaFixa = rendaFixa;
    }

    public Double getLimiteLazer() {
        return limiteLazer;
    }

    public void setLimiteLazer(Double limiteLazer) {
        this.limiteLazer = limiteLazer;
    }

    public Double getLimiteContas() {
        return limiteContas;
    }

    public void setLimiteContas(Double limiteContas) {
        this.limiteContas = limiteContas;
    }

    public Double getLimiteInvestimento() {
        return limiteInvestimento;
    }

    public void setLimiteInvestimento(Double limiteInvestimento) {
        this.limiteInvestimento = limiteInvestimento;
    }
}
