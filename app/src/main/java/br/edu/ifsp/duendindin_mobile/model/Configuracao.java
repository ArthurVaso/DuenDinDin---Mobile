package br.edu.ifsp.duendindin_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Configuracao implements Serializable {

    private Integer id;
    @SerializedName("usuarioID")
    private Integer usuarioId;
    @SerializedName("renda_fixa")
    private Double rendaFixa;
    @SerializedName("limite_lazer")
    private Double limiteLazer;
    @SerializedName("limite_contas")
    private Double limiteContas;
    @SerializedName("limite_investimento")
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
