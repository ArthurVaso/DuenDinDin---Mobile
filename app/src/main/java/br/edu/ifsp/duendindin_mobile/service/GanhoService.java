package br.edu.ifsp.duendindin_mobile.service;

import java.util.List;

import br.edu.ifsp.duendindin_mobile.model.Ganho;
import br.edu.ifsp.duendindin_mobile.model.GanhoRetorno;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GanhoService {

    //criar ganho
    @POST("gain/")
    Call<Ganho> criarGanho(@Header("x-access-token") String token,
                           @Body Ganho ganho);

    //consultar todas as ganhos
    @GET("gain/")
    Call<List<Ganho>> retornarTodasGanhos(@Header("x-access-token") String token);

    //consultar ganho por id
    @GET("gain/{ganhoId}/")
    Call<Ganho> retornarGanhoPorId(@Header("x-access-token") String token,
                                 @Path("ganhoId") Integer id);

    //consultar ganho por usuario
    @GET("gain/user/{usuarioId}/")
    Call<List<GanhoRetorno>> retornarGanhoUsuario(@Header("x-access-token") String token,
                                                  @Path("usuarioId") Integer id);

    //consultar ganho por categoria
    @GET("gain/category/{categoriaId}/")
    Call<Ganho> retornarGanhoCategoria(@Header("x-access-token") String token,
                                     @Path("categoriaId") Integer id);

    //consultar ganho por id e por categoria
    @GET("gain/{ganhoId}/{categoriaId}/")
    Call<Ganho> retornarGanhoPorIdECategoria(@Header("x-access-token") String token,
                                             @Path("ganhoId") Integer gainId,
                                             @Path("categoriaId") Integer categoryId);

    //atualizar ganho por ID
    @PUT("gain/{ganhoId}")
    Call<Ganho> atualizarGanhoUsuario(@Header("x-access-token") String token,
                                              @Path("ganhoId") Integer id,
                                              @Body Ganho ganho);

    //deletar ganho por ID
    @DELETE("gain/{ganhoId}")
    Call<String> deletarGanhoUsuario(@Header("x-access-token") String token,
                                            @Path("ganhoId") Integer id);
}
