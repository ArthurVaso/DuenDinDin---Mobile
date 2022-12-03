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
import br.edu.ifsp.duendindin_mobile.adapter.GanhosAdapter;
import br.edu.ifsp.duendindin_mobile.model.Ganho;
import br.edu.ifsp.duendindin_mobile.model.GanhoRetorno;
import br.edu.ifsp.duendindin_mobile.model.Gasto;
import br.edu.ifsp.duendindin_mobile.model.GastoRetorno;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.service.GanhoService;
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

public class GanhoListagemActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private RecyclerView rvGanhos;
    private TextView txtMsgUsuario;

    private Retrofit retrofitAPI;
    private SharedPreferences pref;
    private Usuario usuario = new Usuario();
    private String token = "";
    private int usuarioId;
    private List<GanhoRetorno> listGanhosRetorno = new ArrayList<>();
    private List<Ganho> listGanhos = new ArrayList<>();
    private GanhosAdapter ganhosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_ganho);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();
        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);

        rvGanhos = findViewById(R.id.rv_ganhos);
        txtMsgUsuario = findViewById(R.id.msg_usario_listagem_ganho);

        rvGanhos.setLayoutManager(new LinearLayoutManager(this));
        ganhosAdapter = new GanhosAdapter(this.getLayoutInflater(), (ArrayList<Ganho>) listGanhos);
        rvGanhos.setAdapter(ganhosAdapter);

        Button btnNovoRecebimento = findViewById(R.id.btn_ganho_listagem_novo_recebimento);
        btnNovoRecebimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GanhoListagemActivity.this, GanhoCadastroActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bnvGanho = findViewById(R.id.bnv_ganho);
        bnvGanho.setSelectedItemId(R.id.bottom_nav_menu_ganhos);
        bnvGanho.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                if ("Home".equals(title)) {
                    startActivity(new Intent(GanhoListagemActivity.this, HomeActivity.class));
                } else if ("Vencimentos".equals(title)) {
                    startActivity(new Intent(GanhoListagemActivity.this, GastoListagemActivity.class));
                } else if("Categorias".equals(title)){
                    startActivity(new Intent(GanhoListagemActivity.this, CategoriaListagemActivity.class));
                } else if ("Perfil".equals(title)) {
                    startActivity(new Intent(GanhoListagemActivity.this, UsuarioPerfilActivity.class));
                }
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        consultarUsuario();
        consultarGanhos();
    }

    public void consultarUsuario() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                GanhoListagemActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        UsuarioService usuarioService = retrofitAPI.create(UsuarioService.class);

        Call<Usuario> callUsuario = usuarioService.consultarUsuario(token, usuarioId);
        callUsuario.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()){
                    usuario = response.body();
                    txtMsgUsuario.setText("Olá, " + usuario.getNome());
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus dados.  \n" + msg.getMensagem(), GanhoListagemActivity.this);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), GanhoListagemActivity.this);
            }
        });

    }
    public void consultarGanhos() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                GanhoListagemActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();
        //instanciando a interface
        GanhoService ganhoService = retrofitAPI.create(GanhoService.class);

        Call<List<GanhoRetorno>> call = ganhoService.retornarGanhoUsuario(token, usuarioId);
        call.enqueue(new Callback<List<GanhoRetorno>>() {
            @Override
            public void onResponse(Call<List<GanhoRetorno>> call, Response<List<GanhoRetorno>> response) {
                if(response.isSuccessful()){
                    listGanhosRetorno = response.body();
                    List<Ganho> ganhos = new ArrayList<>();
                    for (GanhoRetorno ganhoRetorno : listGanhosRetorno){
                        ganhos = ganhoRetorno.getListGanhos();
                        for (Ganho g : ganhos){
                            listGanhos.add(g);
                        }
                    }
                    rvGanhos.setAdapter(new GanhosAdapter(GanhoListagemActivity.this.getLayoutInflater(), (ArrayList<Ganho>) listGanhos));
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus ganhos.  \n" + msg.getMensagem(), GanhoListagemActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<GanhoRetorno>> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), GanhoListagemActivity.this);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GanhoListagemActivity.this, HomeActivity.class));
        //super.onBackPressed();
    }


}