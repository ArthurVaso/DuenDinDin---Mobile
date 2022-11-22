package br.edu.ifsp.duendindin_mobile.service;

import br.edu.ifsp.duendindin_mobile.model.Login;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("login")
    Call<Usuario> login (@Body Login login);
}
