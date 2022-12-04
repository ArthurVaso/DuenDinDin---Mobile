package br.edu.ifsp.duendindin_mobile.model;

import com.google.gson.annotations.SerializedName;

public class AtualizarSenha {

    private String email;

    @SerializedName("nova_senha")
    private String novaSenha;

    @SerializedName("senha_atual")
    private String senhaAtual;


    public AtualizarSenha() {
    }

    public AtualizarSenha(String email, String novaSenha, String senhaAtual) {
        this.email = email;
        this.novaSenha = novaSenha;
        this.senhaAtual = senhaAtual;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }
}
