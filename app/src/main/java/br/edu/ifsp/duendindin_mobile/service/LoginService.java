package br.edu.ifsp.duendindin_mobile.service;

import java.util.Observable;

import br.edu.ifsp.duendindin_mobile.model.Login;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.model.UsuarioComToken;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("user/login")
    Call<UsuarioComToken> login (@Body Login login);

}
