package br.edu.ifsp.duendindin_mobile.service;

import java.util.List;


import br.edu.ifsp.duendindin_mobile.model.Categoria;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoriaService {

    //criar categoria
    @POST("category/")
    Call<Categoria> criarCategoria(@Header("x-access-token") String token,
                                     @Body Categoria categoria);

    //consultar categoria por usuario
    @GET("category/all/{usuarioId}/")
    Call<List<Categoria>> retornarCategoriaUsuario(@Header("x-access-token") String token,
                                   @Path("usuarioId") Integer id);

    //consultar todas as categorias
    @GET("category/all/")
    Call<List<Categoria>> retornarTodasCategorias(@Header("x-access-token") String token);

    //atualizar categoria
    @PATCH("category/{usuarioId}/{categoriaId}")
    Call<Message> atualizarCategoriaUsuario(@Header("x-access-token") String token,
                                            @Path("usuarioId") Integer userId,
                                            @Path("categoriaId") Integer categoryId,
                                            @Body Categoria categoria);

    //deletar categoria
    @DELETE("category/{usuarioId}/{categoriaId}")
    Call<Message> deletarCategoriaUsuario(@Header("x-access-token") String token,
                                             @Path("usuarioId") Integer userId,
                                             @Path("categoriaId") Integer categoryId);

}
