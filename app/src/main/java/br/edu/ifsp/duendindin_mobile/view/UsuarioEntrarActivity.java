package br.edu.ifsp.duendindin_mobile.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.CEP;
import br.edu.ifsp.duendindin_mobile.model.Login;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.model.UsuarioComToken;
import br.edu.ifsp.duendindin_mobile.service.CEPService;
import br.edu.ifsp.duendindin_mobile.service.LoginService;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioEntrarActivity extends AppCompatActivity {

    private final String URL_API = "http://192.168.0.106:5011/";

    private Button btnEntrar;
    private ImageView imgSetaVoltar;
    private TextInputEditText txtEmail;
    private TextInputEditText txtSenha;
    private TextView txtEsqueceuSenha;
    private Retrofit retrofitAPI;
    private UsuarioComToken usuario = new UsuarioComToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_entrar);

        txtEmail = findViewById(R.id.txt_edit_usuario_entrar_email);
        txtSenha = findViewById(R.id.txt_edit_usuario_entrar_senha);

        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        btnEntrar = findViewById(R.id.btn_usuario_entrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (validate()) {
                    realizarLogin();
                    //Toast.makeText(UsuarioEntrarActivity.this, "login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UsuarioEntrarActivity.this, HomeActivity.class);
                    startActivity(intent);
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

        txtEsqueceuSenha = findViewById(R.id.txt_esqueceu_senha);
        txtEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UsuarioEntrarActivity.this, RecuperarSenhaEnviaEmailActivity.class);
                startActivity(intent);


            }
        });

    }

    private void realizarLogin() {

        Login login = new Login();
        login.setEmail(txtEmail.getText().toString().trim());
        login.setSenha(txtSenha.getText().toString().trim());

        //instanciando a interface
        LoginService loginService = retrofitAPI.create(LoginService.class);

        //passando os dados para o serviço
        Call<UsuarioComToken> call = loginService.login(login);

        //colocando a requisição na fila para execução
        call.enqueue(new Callback<UsuarioComToken>() {
            @Override
            public void onResponse(Call<UsuarioComToken> call, Response<UsuarioComToken> response) {

                if (response.isSuccessful()) {

                    usuario.setUsuario(response.body().getUsuario());
                    usuario.setToken(response.body().getToken());
                    Toast.makeText(getApplicationContext(), "Login efetuado com sucesso \n"
                            + "\nEmail: " + usuario.getUsuario().getEmail()
                            + "\nCep: " + usuario.getUsuario().getCep()
                            + "\nToken: " + usuario.getToken(), Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro ao realizar login." + response.errorBody(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioComToken> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreu outro erro ao fazer login." + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private boolean validate() {
        boolean isValid = true;
        if (txtEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioEntrarActivity.this, "Preencha o campo Email!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
            Toast.makeText(UsuarioEntrarActivity.this, "Informe um Email válido!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtSenha.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioEntrarActivity.this, "Preencha o campo Senha!", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
