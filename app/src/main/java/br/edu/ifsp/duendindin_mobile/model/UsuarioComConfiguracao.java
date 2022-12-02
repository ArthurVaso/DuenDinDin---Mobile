package br.edu.ifsp.duendindin_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UsuarioComConfiguracao implements Serializable {

    private Integer id;
    private String nome;
    private String email;
    private String senha;
    @SerializedName("data_nascimento")
    private String dataNascimento;
    private String cep;
    private String cidade;
    private String estado;
    private boolean ativo;

    private Configuracao configuracao;
    //@SerializedName("id")
    //private Integer idConfiguracao;
    //private Integer usuarioId;
    //@SerializedName("renda_fixa")
    //private Double rendaFixa;
    //private Double limiteLazer;
    //private Double limiteContas;
    //private Double limiteInvestimento;

    public UsuarioComConfiguracao() {
    }

    //public UsuarioComConfiguracao(Integer id, String nome, String email, String senha, String dataNascimento, String cep, String cidade, String estado, Integer idConfiguracao, Integer usuarioId, Double rendaFixa, Double limiteLazer, Double limiteContas, Double limiteInvestimento) {
    public UsuarioComConfiguracao(Integer id, String nome, String email, String senha, String dataNascimento, String cep, String cidade, String estado, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
        //this.idConfiguracao = idConfiguracao;
        //this.usuarioId = usuarioId;
        //this.rendaFixa = rendaFixa;
        //this.limiteLazer = limiteLazer;
        //this.limiteContas = limiteContas;
        //this.limiteInvestimento = limiteInvestimento;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Configuracao getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(Configuracao configuracao) {
        this.configuracao = configuracao;
    }

    /*
    public Integer getIdConfiguracao() {
        return idConfiguracao;
    }

    public void setIdConfiguracao(Integer idConfiguracao) {
        this.idConfiguracao = idConfiguracao;
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
 */
}
