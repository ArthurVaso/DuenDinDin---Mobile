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
import br.edu.ifsp.duendindin_mobile.adapter.GastosAdapter;
import br.edu.ifsp.duendindin_mobile.model.Gasto;
import br.edu.ifsp.duendindin_mobile.model.GastoRetorno;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.service.GastoService;
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

public class GastoListagemActivity extends AppCompatActivity {


    private final String URL_API = new URLAPI().baseUrl;

    private RecyclerView rvGastos;
    private TextView txtMsgUsuario;

    private Retrofit retrofitAPI;
    private SharedPreferences pref;
    private Usuario usuario = new Usuario();
    private String token = "";
    private int usuarioId;
    private List<GastoRetorno> listGastosRetorno = new ArrayList<>();
    private List<Gasto> listGastos = new ArrayList<>();
    private GastosAdapter gastosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_gasto);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        rvGastos = findViewById(R.id.rv_gasto);
        txtMsgUsuario = findViewById(R.id.msg_usario_listagem_gasto);

        rvGastos.setLayoutManager(new LinearLayoutManager(this));
        gastosAdapter = new GastosAdapter(this.getLayoutInflater(), (ArrayList<Gasto>) listGastos);
        rvGastos.setAdapter(gastosAdapter);

        Button btnNovoVencimento = findViewById(R.id.btn_gasto_listagem_novo_vencimento);
        btnNovoVencimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GastoListagemActivity.this, GastoCadastroActivity.class);
                startActivity(intent);
            }
        });

        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bnvGasto = findViewById(R.id.bnv_gasto);
        bnvGasto.setSelectedItemId(R.id.bottom_nav_menu_gastos);
        bnvGasto.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                if ("Home".equals(title)) {
                    startActivity(new Intent(GastoListagemActivity.this, HomeActivity.class));
                } else if ("Recebimento".equals(title)) {
                    startActivity(new Intent(GastoListagemActivity.this, GanhoListagemActivity.class));
                } else if("Categorias".equals(title)){
                    startActivity(new Intent(GastoListagemActivity.this, CategoriaListagemActivity.class));
                } else if ("Perfil".equals(title)) {
                    startActivity(new Intent(GastoListagemActivity.this, UsuarioPerfilActivity.class));
                }
                return false;
            }
        });
    }


    public void listarVencimentos() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                GastoListagemActivity.this,
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus dados.  \n" + msg.getMensagem(), GastoListagemActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), GastoListagemActivity.this);
            }
        });

        //instanciando a interface
        GastoService gastoService = retrofitAPI.create(GastoService.class);

        Call<List<GastoRetorno>> call = gastoService.retornarGastoUsuario(token, usuarioId);
        call.enqueue(new Callback<List<GastoRetorno>>() {
            @Override
            public void onResponse(Call<List<GastoRetorno>> call, Response<List<GastoRetorno>> response) {
                if(response.isSuccessful()){
                    listGastosRetorno = response.body();
                    List<Gasto> gastos = new ArrayList<>();
                    for (GastoRetorno gastoRetorno : listGastosRetorno){
                        gastos = gastoRetorno.getListGastos();
                        for (Gasto g : gastos){
                            listGastos.add(g);
                        }
                    }
                    rvGastos.setAdapter(new GastosAdapter(GastoListagemActivity.this.getLayoutInflater(), (ArrayList<Gasto>) listGastos));

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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus gastos.  \n" + msg.getMensagem(), GastoListagemActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<GastoRetorno>> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), GastoListagemActivity.this);

            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        listarVencimentos();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GastoListagemActivity.this, HomeActivity.class));
        //super.onBackPressed();
    }
}