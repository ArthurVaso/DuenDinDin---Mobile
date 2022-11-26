package br.edu.ifsp.duendindin_mobile.service;

import java.util.List;

import br.edu.ifsp.duendindin_mobile.model.Gasto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GastoService {

    //criar gasto
    @POST("expense/")
    Call<Gasto> criarGasto(@Header("x-access-token") String token,
                           @Body Gasto gasto);

    //consultar todas as gastos
    @GET("expense/")
    Call<List<Gasto>> retornarTodasGastos(@Header("x-access-token") String token);

    //consultar gasto por id
    @GET("expense/{gastoId}/")
    Call<Gasto> retornarGastoPorId(@Header("x-access-token") String token,
                                   @Path("gastoId") Integer id);

    //consultar gasto por usuario
    @GET("expense/user/{usuarioId}/")
    Call<Gasto> retornarGastoUsuario(@Header("x-access-token") String token,
                                     @Path("usuarioId") Integer id);

    //consultar gasto por categoria
    @GET("expense/category/{categoriaId}/")
    Call<Gasto> retornarGastoCategoria(@Header("x-access-token") String token,
                                       @Path("categoriaId") Integer id);

    //consultar gasto por id e por categoria
    @GET("expense/{gastoId}/{categoriaId}/")
    Call<Gasto> retornarGastoPorIdECategoria(@Header("x-access-token") String token,
                                             @Path("gastoId") Integer expenseId,
                                             @Path("categoriaId") Integer categoryId);

    //atualizar gasto por ID
    @PUT("expense/{gastoId}")
    Call<Gasto> atualizarGastoUsuario(@Header("x-access-token") String token,
                                      @Path("gastoId") Integer id,
                                      @Body Gasto gasto);

    //deletar gasto por ID
    @DELETE("expense/{gastoId}")
    Call<Gasto> deletarGastoUsuario(@Header("x-access-token") String token,
                                    @Path("gastoId") Integer id);
}
