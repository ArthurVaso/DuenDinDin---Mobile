package br.edu.ifsp.duendindin_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UsuarioRetorno implements Serializable {
    @SerializedName("usuario")
    private Usuario usuario;

    public UsuarioRetorno() {
    }

    public UsuarioRetorno(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
