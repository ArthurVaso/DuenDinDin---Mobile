package br.edu.ifsp.duendindin_mobile.api;

import br.edu.ifsp.duendindin_mobile.model.CEP;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RESTService {

    //consultar CEP no webservice do ViaCEP
    @GET("{cep}/json/")
    Call<CEP> consultarCEP(@Path("cep") String cep);

    @GET("usuarios/{usuarioID}/json/")
    Call<CEP> consultarUsuario(@Path("usuarioId") String cep);

}
