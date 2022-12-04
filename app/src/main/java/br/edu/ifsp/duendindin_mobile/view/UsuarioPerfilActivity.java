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
import java.math.BigDecimal;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.UsuarioComConfiguracao;
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
    private UsuarioComConfiguracao usuarioComConfiguracao = new UsuarioComConfiguracao();
    private String token = "";
    private int usuarioId;

    private TextView txtMsgHello;
    private TextView txtNome;
    private TextView txtDataNasc;
    private TextView txtCep;
    private TextView txtEstado;
    private TextView txtCidade;
    private TextView txtEmail;
    private TextView txtRendaFixa;
    private TextView txtLimiteGasto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        ImageButton btnEditUsuario = findViewById(R.id.img_usuario_perfil_edit);
        btnEditUsuario.setOnClickListener(view -> {
            Intent intent = new Intent(UsuarioPerfilActivity.this, UsuarioAlterarActivity.class);
            intent.putExtra("usuario", (Serializable) usuarioComConfiguracao);
            startActivity(intent);
        });

        TextView txtRedefinir = findViewById(R.id.msg_usuario_perfil_esqueceu_sua_senha);
        txtRedefinir.setOnClickListener(view -> {
            startActivity(new Intent(UsuarioPerfilActivity.this, RecuperarSenhaEnviaEmailActivity.class));
        });

        txtMsgHello = findViewById(R.id.msg_usuario_perfil);
        txtNome = findViewById(R.id.txt_usuario_perfil_edit_nome_completo);
        txtDataNasc = findViewById(R.id.txt_usuario_perfil_edit_data_de_nascimento);
        txtCep = findViewById(R.id.txt_usuario_perfil_edit_CEP);
        txtEstado = findViewById(R.id.txt_usuario_perfil_edit_estado);
        txtCidade = findViewById(R.id.txt_usuario_perfil_edit_cidade);
        txtEmail = findViewById(R.id.txt_usuario_perfil_edit_email);
        txtRendaFixa = findViewById(R.id.txt_usuario_perfil_edit_salario);
        txtLimiteGasto = findViewById(R.id.txt_usuario_perfil_edit_limite_de_gasto);

        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);
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
        Call<UsuarioComConfiguracao> call = usuarioService.consultarUsuarioComConfiguracao(token, usuarioId);
        call.enqueue(new Callback<UsuarioComConfiguracao>() {
            @Override
            public void onResponse(Call<UsuarioComConfiguracao> call, Response<UsuarioComConfiguracao> response) {
                if (response.isSuccessful()) {
                    usuarioComConfiguracao = response.body();

                    String usuarioNome = usuarioComConfiguracao.getNome();
                    String usuarioDataNasc = usuarioComConfiguracao.getDataNascimento();
                    String data[] = usuarioDataNasc.split("-");
                    usuarioDataNasc = data[2] + "/" + data[1] + "/" + data[0];
                    String usuarioCep = usuarioComConfiguracao.getCep();
                    String usuarioEstado = usuarioComConfiguracao.getEstado();
                    String usuarioCidade = usuarioComConfiguracao.getCidade();
                    String usuarioEmail = usuarioComConfiguracao.getEmail();
                    BigDecimal usuarioRenda = new BigDecimal(usuarioComConfiguracao.getConfiguracao().getRendaFixa());
                    BigDecimal limiteLazer = new BigDecimal(usuarioComConfiguracao.getConfiguracao().getLimiteLazer());
                    BigDecimal limiteConta = new BigDecimal(usuarioComConfiguracao.getConfiguracao().getLimiteContas());
                    BigDecimal limiteInvestimento = new BigDecimal(usuarioComConfiguracao.getConfiguracao().getLimiteInvestimento());


                    txtMsgHello.setText("Olá, " + usuarioNome);
                    txtNome.setText(usuarioNome);
                    txtDataNasc.setText(usuarioDataNasc);
                    txtCep.setText(usuarioCep);
                    txtEstado.setText(usuarioEstado);
                    txtCidade.setText(usuarioCidade);
                    txtEmail.setText(usuarioEmail);
                    txtRendaFixa.setText("R$ " + usuarioRenda.setScale(2, BigDecimal.ROUND_HALF_DOWN));
                    txtLimiteGasto.setText("Padrão: " + limiteLazer + "% - " + limiteInvestimento + "% - " + limiteConta + "%");

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
            public void onFailure(Call<UsuarioComConfiguracao> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), UsuarioPerfilActivity.this);

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UsuarioPerfilActivity.this, HomeActivity.class));
        //super.onBackPressed();
    }
}