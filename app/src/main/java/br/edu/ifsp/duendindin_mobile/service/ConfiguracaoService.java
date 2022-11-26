package br.edu.ifsp.duendindin_mobile.service;

import java.util.List;

import br.edu.ifsp.duendindin_mobile.model.Configuracao;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ConfiguracaoService {


    //consultar configuracao por usuario
    @GET("setting/{configuracaoId}/")
    Call<Configuracao> retornarConfiguracaoUsuario(@Header("x-access-token") String token,
                                                @Path("configuracaoId") Integer id);

    //consultar todas as configuracaos
    @GET("setting/")
    Call<List<Configuracao>> retornarTodasConfiguracaos(@Header("x-access-token") String token);

    //atualizar configuracao
    @PUT("setting/{configuracaoId}")
    Call<Configuracao> atualizarConfiguracaoUsuario(@Header("x-access-token") String token,
                                              @Path("configuracaoId") Integer configId,
                                              @Body Configuracao configuracao);

  

}
