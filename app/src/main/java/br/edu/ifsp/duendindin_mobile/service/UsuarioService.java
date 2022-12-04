package br.edu.ifsp.duendindin_mobile.service;

import java.util.List;

import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.model.UsuarioComConfiguracao;
import br.edu.ifsp.duendindin_mobile.model.AtualizarUsuario;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsuarioService {

    //criar usuario
    @POST("user/")
    Call<Usuario> cadastrarUsuario(@Body Usuario usuario);

    //consultar usuario
    @GET("user/{usuarioId}/")
    Call<Usuario> consultarUsuario(@Header("x-access-token") String token,
                                          @Path("usuarioId") Integer id);//consultar usuario

    //consultar usuario com configuração
    @GET("user/getSettings/{usuarioId}/")
    Call<UsuarioComConfiguracao> consultarUsuarioComConfiguracao(@Header("x-access-token") String token,
                                                                 @Path("usuarioId") Integer id);

    //consultar todos os usuarios
    @GET("user/getAll/")
    Call<List<Usuario>> consultarTodosUsuarios(@Header("x-access-token") String token);

    //atualizar usuário
    @PUT("user/full/{usuarioId}")
    Call<Message> atualizarUsuario(@Header("x-access-token") String token,
                                   @Path("usuarioId") Integer id,
                                   @Body AtualizarUsuario atualizarUsuario);

}
