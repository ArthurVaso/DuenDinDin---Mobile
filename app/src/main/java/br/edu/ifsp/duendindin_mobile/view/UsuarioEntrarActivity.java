package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Observable;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Login;
import br.edu.ifsp.duendindin_mobile.model.UsuarioComToken;
import br.edu.ifsp.duendindin_mobile.service.LoginService;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
import br.edu.ifsp.duendindin_mobile.utils.CustomProgressDialog;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import br.edu.ifsp.duendindin_mobile.utils.URLAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioEntrarActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private Button btnEntrar;
    private ImageView imgSetaVoltar;
    private TextInputEditText txtEmail;
    private TextInputEditText txtSenha;
    private Retrofit retrofitAPI;
    private UsuarioComToken usuario = new UsuarioComToken();
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_entrar);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

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

    @Override
    protected void onStart() {
        super.onStart();
        String token = pref.getString("token", "");
        new CustomMessageDialog("Token: " + token, UsuarioEntrarActivity.this);
    }

    private void realizarLogin() {
        CustomProgressDialog dialog = new CustomProgressDialog(
                UsuarioEntrarActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        dialog.show();


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

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", usuario.getToken());
                    editor.commit();

//                    Toast.makeText(getApplicationContext(), "Login efetuado com sucesso \n"
//                            + "\nEmail: " + usuario.getUsuario().getEmail()
//                            + "\nToken: " + usuario.getToken(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();


                    Intent intent = new Intent(UsuarioEntrarActivity.this, HomeActivity.class);
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
                    dialog.dismiss();
                    new CustomMessageDialog("Ocorreu um erro ao realizar login.  \n" + msg.getMensagem(), UsuarioEntrarActivity.this);
                    //Toast.makeText(getApplicationContext(), "Ocorreu um erro ao realizar login.  \n" + msg.getMensagem(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioComToken> call, Throwable t) {
                dialog.dismiss();
                new CustomMessageDialog("Ocorreu outro erro ao fazer login.", UsuarioEntrarActivity.this);
                Log.d("UsuarioEntrarActivity", t.getMessage());
                //Toast.makeText(getApplicationContext(), "Ocorreu outro erro ao fazer login." + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (txtEmail.getText().toString().trim().isEmpty()) {
            new CustomMessageDialog("Preencha o campo Email!", UsuarioEntrarActivity.this);
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
            new CustomMessageDialog("Informe um Email válido!", UsuarioEntrarActivity.this);
           isValid = false;
        } else if (txtSenha.getText().toString().trim().isEmpty()) {
            new CustomMessageDialog("Preencha o campo Senha!", UsuarioEntrarActivity.this);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
