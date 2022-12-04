package br.edu.ifsp.duendindin_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AtualizarUsuario implements Serializable {

    private String nome;

    @SerializedName("data_nascimento")
    private String dataNascimento;

    private String cep;

    private String cidade;

    private String estado;

    @SerializedName("renda_fixa")
    private Double rendaFixa;


    public AtualizarUsuario() {
    }

    public AtualizarUsuario(String nome, String dataNascimento, String cep, String cidade, String estado, Double rendaFixa) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
        this.rendaFixa = rendaFixa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getRendaFixa() {
        return rendaFixa;
    }

    public void setRendaFixa(Double rendaFixa) {
        this.rendaFixa = rendaFixa;
    }
}
