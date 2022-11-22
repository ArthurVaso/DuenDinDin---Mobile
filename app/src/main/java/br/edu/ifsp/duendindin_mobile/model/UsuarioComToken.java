package br.edu.ifsp.duendindin_mobile.model;

import com.google.gson.annotations.SerializedName;

public class UsuarioComToken {
    @SerializedName("jwt")
    private String token;
    @SerializedName("user")
    private Usuario usuario;

    public UsuarioComToken() {
    }

    public UsuarioComToken(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
