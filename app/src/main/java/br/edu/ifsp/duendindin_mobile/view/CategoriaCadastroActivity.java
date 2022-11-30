package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Categoria;
import br.edu.ifsp.duendindin_mobile.service.CategoriaService;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
import br.edu.ifsp.duendindin_mobile.utils.CustomProgressDialog;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import br.edu.ifsp.duendindin_mobile.utils.URLAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriaCadastroActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;
    private Button btnSalvar;
    private ImageView imgSetaVoltar;
    private TextInputEditText edtNome;
    private TextInputEditText edtDescricao;
    private Retrofit retrofitAPI;
    SharedPreferences pref;
    String token = "";
    int usuarioId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        setContentView(R.layout.activity_cadastro_categoria);
        edtNome = findViewById(R.id.edt_nome_categoria);
        edtDescricao = findViewById(R.id.edt_descricao_categoria);

        btnSalvar = findViewById(R.id.btn_categoria_cadastro_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    cadastrarCategoria();
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
        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);
        //new CustomMessageDialog("Token: " + token + "\nUsuarioID: " + usuarioId, CategoriaCadastroActivity.this);
    }
    private void cadastrarCategoria() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                CategoriaCadastroActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        Categoria categoria = new Categoria();
        categoria.setUsuarioId(usuarioId);
        categoria.setNome(edtNome.getText().toString().trim());
        categoria.setDescricao(edtDescricao.getText().toString().trim());

        //instanciando a interface
        CategoriaService categoriaService = retrofitAPI.create(CategoriaService.class);
        
        //passando os dados para o serviço
        Call<Categoria> call = categoriaService.criarCategoria(token, categoria);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<Categoria>() {
            @Override
            public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Categoria cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();


                    Log.d("Categoria", "ID: " + response.body().getId().toString());
                    Log.d("Categoria", "UsuarioID: " + response.body().getUsuarioId().toString());
                    Log.d("Categoria", "Nome: " + response.body().getNome());
                    Log.d("Categoria", "Descrição: " + response.body().getDescricao());
                    Intent intent = new Intent(CategoriaCadastroActivity.this, HomeActivity.class);
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
                    new CustomMessageDialog("Ocorreu um erro ao cadastrar a categoria.  \n" + msg.getMensagem(), CategoriaCadastroActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Categoria> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), CategoriaCadastroActivity.this);
                Log.d("CategoriaCadastroActivity", t.getMessage());
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            Toast.makeText(CategoriaCadastroActivity.this, "Preencha o campo Nome", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

