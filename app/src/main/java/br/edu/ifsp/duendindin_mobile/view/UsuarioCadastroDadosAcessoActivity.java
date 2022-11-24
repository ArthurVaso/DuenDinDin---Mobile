package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.model.UsuarioComToken;
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

public class UsuarioCadastroDadosAcessoActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private Button btnCadastrar;
    private ImageView imgSetaVoltar;
    private TextInputEditText txtEmail;
    private TextInputEditText txtSenha;
    private Retrofit retrofitAPI;
    private Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_dados_acesso);

        txtEmail = findViewById(R.id.txt_edit_usuario_cadastro_email);
        txtSenha = findViewById(R.id.txt_edit_senha);

        Intent intent = getIntent();
        usuario = (Usuario) intent.getExtras().getSerializable("Usuario");

        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        btnCadastrar = findViewById(R.id.btn_usuario_cadastro_cadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (validate()) {
                    cadastrarUsuario();
                }

            }
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

//função para teste do usuario sendo enviado entre activities
//    @Override
//    protected void onStart() {
//        super.onStart();
//        new CustomMessageDialog("Dados do usuario: \n " + usuario.getNome() + "\n" +
//                usuario.getDataNascimento() + "\n" +
//                usuario.getCep(),UsuarioCadastroDadosAcessoActivity.this);
//
//    }

    private void cadastrarUsuario() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                UsuarioCadastroDadosAcessoActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();
        usuario.setEmail(txtEmail.getText().toString().trim());
        usuario.setSenha(txtSenha.getText().toString().trim());

        //instanciando a interface
        UsuarioService usuarioService = retrofitAPI.create(UsuarioService.class);

        //passando os dados para o serviço
        Call<Usuario> call = usuarioService.cadastrarUsuario(usuario);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Usuario cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UsuarioCadastroDadosAcessoActivity.this, UsuarioEntrarActivity.class);
                    startActivity(intent);

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
                    new CustomMessageDialog("Ocorreu um erro ao realizar o cadastro.  \n" + msg.getMensagem(), UsuarioCadastroDadosAcessoActivity.this);
                    //Toast.makeText(getApplicationContext(), "Ocorreu um erro ao realizar login.  \n" + msg.getMensagem(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), UsuarioCadastroDadosAcessoActivity.this);
                Log.d("UsuarioEntrarActivity", t.getMessage());
                //Toast.makeText(getApplicationContext(), "Ocorreu outro erro ao fazer login." + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (txtEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, "Preencha o campo Email", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtEmail.getText().toString().trim().length() > 30) {
            Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, "O campo Email não deve ter mais de 30 caracteres!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
            Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, "Informe um email válido", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtSenha.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, "O campo Senha não pode estar vazio!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtSenha.getText().toString().trim().length() > 50) {
            Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, "O campo Senha não deve ter mais de 50 caracteres!", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
