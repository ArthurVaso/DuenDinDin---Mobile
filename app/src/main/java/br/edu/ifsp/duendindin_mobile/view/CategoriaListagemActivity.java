package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.adapter.CategoriasAdapter;
import br.edu.ifsp.duendindin_mobile.model.Categoria;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.service.CategoriaService;
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

public class CategoriaListagemActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private RecyclerView rvCategorias;
    private TextView txtMsgUsuario;

    private Retrofit retrofitAPI;
    private SharedPreferences pref;
    private Usuario usuario = new Usuario();
    private String token = "";
    private int usuarioId;
    private List<Categoria> listCategorias = new ArrayList<>();
    private CategoriasAdapter categoriasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_categoria);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        rvCategorias = findViewById(R.id.rv_categorias);
        txtMsgUsuario = findViewById(R.id.msg_usuario_categoria);

        rvCategorias.setLayoutManager(new LinearLayoutManager(this));
        categoriasAdapter = new CategoriasAdapter(this.getLayoutInflater(), (ArrayList<Categoria>) listCategorias);
        rvCategorias.setAdapter(categoriasAdapter);

        Button btnNovaCategoria = findViewById(R.id.btn_categoria_listagem_nova_categoria);
        btnNovaCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoriaListagemActivity.this, CategoriaCadastroActivity.class);
                startActivity(intent);
            }
        });

        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bnvCategoria = findViewById(R.id.bnv_categoria);
        bnvCategoria.setSelectedItemId(R.id.bottom_nav_menu_category);
        bnvCategoria.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                if ("Home".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, HomeActivity.class));
                } else if ("Vencimentos".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, GastoListagemActivity.class));
                } else if ("Recebimento".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, GanhoListagemActivity.class));
                } else if ("Perfil".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, UsuarioPerfilActivity.class));
                }
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        CustomProgressDialog progressDialog = new CustomProgressDialog(
                CategoriaListagemActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        UsuarioService usuarioService = retrofitAPI.create(UsuarioService.class);

        Call<Usuario> callUsuario = usuarioService.consultarUsuario(token, usuarioId);
        callUsuario.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()){
                    usuario = response.body();
                    txtMsgUsuario.setText("Olá, "+usuario.getNome());
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus dados.  \n" + msg.getMensagem(), CategoriaListagemActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });

        //instanciando a interface
        CategoriaService categoriaService = retrofitAPI.create(CategoriaService.class);

        Call<List<Categoria>> call = categoriaService.retornarCategoriaUsuario(token, usuarioId);
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if(response.isSuccessful()){
                    listCategorias = response.body();
                    rvCategorias.setAdapter(new CategoriasAdapter(CategoriaListagemActivity.this.getLayoutInflater(), (ArrayList<Categoria>) listCategorias));
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar suas categorias.  \n" + msg.getMensagem(), CategoriaListagemActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}