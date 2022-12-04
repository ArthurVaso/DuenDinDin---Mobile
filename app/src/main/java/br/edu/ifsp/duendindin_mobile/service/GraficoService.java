package br.edu.ifsp.duendindin_mobile.service;

import br.edu.ifsp.duendindin_mobile.model.Usuario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GraficoService {

    //consultar gráfico 1
    @GET("user/{usuarioId}/")
    Call<Usuario> consultarGrafico1(@Header("x-access-token") String token,
                                   @Path("usuarioId") Integer id);

    //consultar gráfico 2
    @GET("user/{usuarioId}/")
    Call<Usuario> consultarGrafico2(@Header("x-access-token") String token,
                                   @Path("usuarioId") Integer id);

    //consultar gráfico 3
    @GET("user/{usuarioId}/")
    Call<Usuario> consultarGrafico3(@Header("x-access-token") String token,
                                   @Path("usuarioId") Integer id);

    //consultar gráfico 4
    @GET("user/{usuarioId}/")
    Call<Usuario> consultarGrafico4(@Header("x-access-token") String token,
                                   @Path("usuarioId") Integer id);
}
