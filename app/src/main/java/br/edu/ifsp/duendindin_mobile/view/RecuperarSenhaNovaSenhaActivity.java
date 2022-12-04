package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.AtualizarSenha;
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

public class RecuperarSenhaNovaSenhaActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;
    private Retrofit retrofitAPI;
    private SharedPreferences pref;

    private UsuarioService _usuarioService;

    private String _email;
    private String token;

    private Button btnFinalizar;
    private Button btnVoltar;
    private ImageView imgSetaVoltar;
    private TextInputEditText txtSenhaAtual;
    private TextInputEditText txtNovaSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha_nova_senha);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        txtNovaSenha = findViewById(R.id.txt_edit_recuperar_senha_confirmar_senha);
        txtSenhaAtual = findViewById(R.id.txt_edit_recuperar_senha_nova_senha);

        btnFinalizar = findViewById(R.id.btn_recuperar_senha_finalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (validate()) {
                    atualizarSenha();
                    pref.edit().clear();
                    Intent intent = new Intent(RecuperarSenhaNovaSenhaActivity.this, UsuarioEntrarActivity.class);
                    startActivity(intent);
                }

            }
        });

        btnVoltar = findViewById(R.id.btn_recuperar_senha_voltar);
        btnVoltar.setOnClickListener((view) -> {
                Intent intent = new Intent(RecuperarSenhaNovaSenhaActivity.this, UsuarioPerfilActivity.class);
                startActivity(intent);
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        token = pref.getString("token", "");
        _email = pref.getString("email", "");

        //instanciando a interface
        _usuarioService = retrofitAPI.create(UsuarioService.class);
    }

    private void atualizarSenha() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                RecuperarSenhaNovaSenhaActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );


        Call<Message> call = _usuarioService.atualizarSenha(token, new AtualizarSenha(_email, txtNovaSenha.getText().toString(), txtSenhaAtual.getText().toString() ));
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful()) {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.body().getMensagem(), Toast.LENGTH_LONG).show();

                } else {

                    String errorBody = null;
                    Message msg = new Message();
                    try {
                        errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    new CustomMessageDialog("Ocorreu um erro ao realizar a atualização.  \n" + errorBody, RecuperarSenhaNovaSenhaActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), RecuperarSenhaNovaSenhaActivity.this);

            }
        });

    }

    private boolean validate() {
        boolean isValid = true;
        if (txtSenhaAtual.getText().toString().trim().isEmpty()) {
            Toast.makeText(RecuperarSenhaNovaSenhaActivity.this, "Por favor, preencha o campo de Senha Atual", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtNovaSenha.getText().toString().trim().isEmpty()) {
            Toast.makeText(RecuperarSenhaNovaSenhaActivity.this, "Por favor, preencha o campo de Nova Senha", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
