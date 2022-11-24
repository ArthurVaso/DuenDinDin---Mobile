package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.model.UsuarioComToken;
import br.edu.ifsp.duendindin_mobile.service.LoginService;
import br.edu.ifsp.duendindin_mobile.service.UsuarioService;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
import br.edu.ifsp.duendindin_mobile.utils.CustomProgressDialog;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import br.edu.ifsp.duendindin_mobile.utils.URLAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioPerfilActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private Retrofit retrofitAPI;
    private SharedPreferences pref;
    private Usuario usuario = new Usuario();
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        ImageButton btnEditUsuario = findViewById(R.id.img_usuario_perfil_edit);
        btnEditUsuario.setOnClickListener(view -> {
            startActivity(new Intent(UsuarioPerfilActivity.this, UsuarioCadastroDadosPessoaisActivity.class));
        });

        TextView txtRedefinir = findViewById(R.id.msg_usuario_perfil_esqueceu_sua_senha);
        txtRedefinir.setOnClickListener(view -> {
            startActivity(new Intent(UsuarioPerfilActivity.this, RecuperarSenhaEnviaEmailActivity.class));
        });

        token = pref.getString("token", "");
//        new CustomMessageDialog("Token: " + token, UsuarioPerfilActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bnvPerfil = findViewById(R.id.bnv_perfil);
        bnvPerfil.setSelectedItemId(R.id.bottom_nav_menu_profile);
        bnvPerfil.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                if ("Home".equals(title)) {
                    startActivity(new Intent(UsuarioPerfilActivity.this, HomeActivity.class));
                } else if ("Vencimentos".equals(title)) {
                    startActivity(new Intent(UsuarioPerfilActivity.this, GastoListagemActivity.class));
                } else if ("Recebimento".equals(title)) {
                    startActivity(new Intent(UsuarioPerfilActivity.this, GanhoListagemActivity.class));
                } else if("Categorias".equals(title)){
                    startActivity(new Intent(UsuarioPerfilActivity.this, CategoriaListagemActivity.class));
                }
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        CustomProgressDialog dialog = new CustomProgressDialog(
                UsuarioPerfilActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        dialog.show();

        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        //instanciando a interface
        UsuarioService usuarioService = retrofitAPI.create(UsuarioService.class);

        //passando os dados para o serviço
        Call<Usuario> call = usuarioService.consultarUsuario(token, 2);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    usuario = response.body();
                    Log.d("Usuario", "Usuario: " + usuario);

                    Log.d("Usuario", "Nome: " + response.body().getNome());
                    Log.d("Usuario","Email: " + response.body().getEmail());
                    Log.d("Usuario","DataNasc: " + response.body().getDataNascimento());

                    dialog.dismiss();
                } else {
                    String errorBody = null;
                    Message msg = new Message();
                    try {
                        errorBody = response.errorBody().string();
                        Gson gson = new Gson(); // conversor
                        msg = gson.fromJson(errorBody, Message.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();

                    Log.d("Usuario", "Usuário não encontrado.");
                    new CustomMessageDialog("Ocorreu um erro ao consultar os dados do Usuário.  \n" + msg.getMensagem(), UsuarioPerfilActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                dialog.dismiss();
                new CustomMessageDialog("Ocorreu outro erro ao consultar os dados do Usuário.", UsuarioPerfilActivity.this);
                Log.d("Usuario", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}