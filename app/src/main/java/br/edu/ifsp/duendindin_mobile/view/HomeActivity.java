package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIColumn;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HISubtitle;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Categoria;
import br.edu.ifsp.duendindin_mobile.model.Ganho;
import br.edu.ifsp.duendindin_mobile.model.GanhoRetorno;
import br.edu.ifsp.duendindin_mobile.model.Gasto;
import br.edu.ifsp.duendindin_mobile.model.GastoRetorno;
import br.edu.ifsp.duendindin_mobile.service.CategoriaService;
import br.edu.ifsp.duendindin_mobile.service.GanhoService;
import br.edu.ifsp.duendindin_mobile.service.GastoService;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
import br.edu.ifsp.duendindin_mobile.utils.CustomProgressDialog;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import br.edu.ifsp.duendindin_mobile.utils.URLAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;
    private Retrofit retrofitAPI;

    private Button btnAdicionarRecebimentos;
    private Button btnAdicionarVencimentos;
    private Button btnAdicionarCategoria;
    private Button btnVerPerfil;
    private Button btnApresentarGrafico;
    private Button btnApresentarGrafico2;
    private HIChartView hcGrafico;
    private BottomNavigationView bnvHome;

    private SharedPreferences pref;
    private String token = "";
    private int usuarioId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        btnAdicionarRecebimentos = findViewById(R.id.btn_home_adicionar_recebimentos);
        btnAdicionarVencimentos = findViewById(R.id.btn_home_adicionar_vencimentos);
        btnAdicionarCategoria = findViewById(R.id.btn_home_adicionar_categoria);
        btnVerPerfil = findViewById(R.id.btn_home_ver_perfil);
        btnApresentarGrafico = findViewById(R.id.btn_home_apresentar_grafico);
        btnApresentarGrafico2 = findViewById(R.id.btn_home_apresentar_grafico2);

        btnAdicionarRecebimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, GanhoCadastroActivity.class);
                startActivity(intent);
            }
        });
        btnAdicionarVencimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, GastoCadastroActivity.class);
                startActivity(intent);
            }
        });
        btnAdicionarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CategoriaCadastroActivity.class);
                startActivity(intent);
            }
        });
        btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UsuarioPerfilActivity.class);
                startActivity(intent);
            }
        });
        btnApresentarGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ApresentarGraficoActivity.class);
                startActivity(intent);
            }
        });
        btnApresentarGrafico2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ApresentarGrafico2Activity.class);
                startActivity(intent);
            }
        });

//        hcGrafico = findViewById(R.id.hc_graficos);
//        criarGraficos();

        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bnvHome = findViewById(R.id.bnv_home);
        bnvHome.setSelectedItemId(R.id.bottom_nav_menu_home);

        bnvHome.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                if ("Vencimentos".equals(title)) {
                    startActivity(new Intent(HomeActivity.this, GastoListagemActivity.class));
                } else if ("Recebimento".equals(title)) {
                    startActivity(new Intent(HomeActivity.this, GanhoListagemActivity.class));
                } else if("Categorias".equals(title)){
                    startActivity(new Intent(HomeActivity.this, CategoriaListagemActivity.class));
                } else if ("Perfil".equals(title)) {
                    startActivity(new Intent(HomeActivity.this, UsuarioPerfilActivity.class));
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}