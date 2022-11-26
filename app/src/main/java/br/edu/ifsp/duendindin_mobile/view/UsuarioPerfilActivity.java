package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.io.Serializable;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.model.UsuarioRetorno;
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
    private UsuarioRetorno usuarioRetorno = new UsuarioRetorno();
    private Usuario usuario = new Usuario();
    private String token = "";

    private TextView txtMsgHello;
    private TextView txtNome;
    private TextView txtDataNasc;
    private TextView txtCep;
    private TextView txtEstado;
    private TextView txtCidade;
    private TextView txtEmail;
    private TextView txtRendaFixa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        ImageButton btnEditUsuario = findViewById(R.id.img_usuario_perfil_edit);
        btnEditUsuario.setOnClickListener(view -> {
            Intent intent = new Intent(UsuarioPerfilActivity.this, UsuarioAlterarActivity.class);
            intent.putExtra("usuario", (Serializable) usuario);
            startActivity(intent);
        });

        TextView txtRedefinir = findViewById(R.id.msg_usuario_perfil_esqueceu_sua_senha);
        txtRedefinir.setOnClickListener(view -> {
            startActivity(new Intent(UsuarioPerfilActivity.this, RecuperarSenhaEnviaEmailActivity.class));
        });

        txtMsgHello = findViewById(R.id.msg_usario_perfil);
        txtNome = findViewById(R.id.txt_usuario_perfil_edit_nome_completo);
        txtDataNasc = findViewById(R.id.txt_usuario_perfil_edit_data_de_nascimento);
        txtCep = findViewById(R.id.txt_usuario_perfil_edit_CEP);
        txtEstado = findViewById(R.id.txt_usuario_perfil_edit_estado);
        txtCidade = findViewById(R.id.txt_usuario_perfil_edit_cidade);
        txtEmail = findViewById(R.id.txt_usuario_perfil_edit_email);
        txtRendaFixa = findViewById(R.id.txt_usuario_perfil_edit_salario);

        token = pref.getString("token", "");
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

        CustomProgressDialog progressDialog = new CustomProgressDialog(
                UsuarioPerfilActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        //instanciando a interface
        UsuarioService usuarioService = retrofitAPI.create(UsuarioService.class);

        //passando os dados para o serviço
        Call<UsuarioRetorno> call = usuarioService.consultarUsuario(token, 2);
        call.enqueue(new Callback<UsuarioRetorno>() {
            @Override
            public void onResponse(Call<UsuarioRetorno> call, Response<UsuarioRetorno> response) {
                if (response.isSuccessful()) {
                    usuarioRetorno = response.body();
                    usuario = usuarioRetorno.getUsuario();

                    String usuarioNome = usuario.getNome();
                    String usuarioDataNasc = usuario.getDataNascimento();
                    String usuarioCep = usuario.getCep();
                    String usuarioEstado = usuario.getEstado();
                    String usuarioCidade = usuario.getCidade();
                    String usuarioEmail = usuario.getEmail();
                    Double usuarioRenda = usuario.getRendaFixa();

                    txtMsgHello.setText("Olá, " + usuarioNome);
                    txtNome.setText(usuarioNome);
                    txtDataNasc.setText(usuarioDataNasc);
                    txtCep.setText(usuarioCep);
                    txtEstado.setText(usuarioEstado);
                    txtCidade.setText(usuarioCidade);
                    txtEmail.setText(usuarioEmail);
                    txtRendaFixa.setText("Salário");

                    progressDialog.dismiss();
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
                    progressDialog.dismiss();

                    Log.d("Usuario", "Usuário não encontrado.");
                    new CustomMessageDialog("Ocorreu um erro ao consultar os dados do Usuário.  \n" + msg.getMensagem(), UsuarioPerfilActivity.this);
                }
            }

            @Override
            public void onFailure(Call<UsuarioRetorno> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), UsuarioPerfilActivity.this);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}