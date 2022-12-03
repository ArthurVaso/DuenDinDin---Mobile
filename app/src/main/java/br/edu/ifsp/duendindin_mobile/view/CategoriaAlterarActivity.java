package br.edu.ifsp.duendindin_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

public class CategoriaAlterarActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private Button btnAlterar;
    private ImageView imgSetaVoltar;
    private TextInputEditText edtNome;
    private TextInputEditText edtDescricao;

    private Categoria categoriaAtual;

    private Retrofit retrofitAPI;
    SharedPreferences pref;
    String token = "";
    int usuarioId = 0;

    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_categoria);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        edtNome = findViewById(R.id.edt_nome_categoria_alterar);
        edtDescricao = findViewById(R.id.edt_descricao_categoria_alterar);

        progressDialog = new CustomProgressDialog(
                CategoriaAlterarActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );

        btnAlterar = findViewById(R.id.btn_categoria_cadastro_alterar);
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    atualizarCategoria();

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
        categoriaAtual = (Categoria) getIntent().getSerializableExtra("categoria");
        edtNome.setText(categoriaAtual.getNome());
        edtDescricao.setText(categoriaAtual.getDescricao());
    }

    private void atualizarCategoria() {

        progressDialog.show();

        Categoria categoria = categoriaAtual;
        categoria.setNome(edtNome.getText().toString().trim());
        categoria.setDescricao(edtDescricao.getText().toString().trim());

        //instanciando a interface
        CategoriaService categoriaService = retrofitAPI.create(CategoriaService.class);

        //passando os dados para o serviço
        Call<Message> call = categoriaService.atualizarCategoriaUsuario(token, usuarioId, categoria.getId(), categoria);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), response.body().getMensagem(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CategoriaAlterarActivity.this, CategoriaListagemActivity.class);
                        startActivity(intent);

                    } else {
                        new CustomMessageDialog(response.body().getMensagem(), CategoriaAlterarActivity.this);
                    }

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
                    new CustomMessageDialog("Ocorreu um erro ao atualizar a categoria.  \n" + msg.getMensagem(), CategoriaAlterarActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), CategoriaAlterarActivity.this);
            }
        });

    }

    private boolean validate() {
        boolean isValid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            Toast.makeText(CategoriaAlterarActivity.this, "Preencha o campo Nome", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}