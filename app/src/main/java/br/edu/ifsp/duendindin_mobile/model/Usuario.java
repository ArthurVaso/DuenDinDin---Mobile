package br.edu.ifsp.duendindin_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Usuario {

    private Integer id;
    private String nome;
    private String email;
    private String senha;
    @SerializedName("data_nascimento")
    private Date dataNascimento;
    private String cep;
    private String cidade;
    private String estado;
    @SerializedName("jwt")
    private String token;


    public Usuario() {
    }

    public Usuario(Integer id, String nome, String email, String senha, Date dataNascimento, String cep, String cidade, String estado, String token) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
