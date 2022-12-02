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
import java.util.Date;
import java.util.List;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.adapter.CategoriasAdapter;
import br.edu.ifsp.duendindin_mobile.adapter.GanhosAdapter;
import br.edu.ifsp.duendindin_mobile.adapter.GastosAdapter;
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
    private HIChartView hcGrafico;
    private BottomNavigationView bnvHome;

    private RecyclerView rvGanhos;
    private List<GanhoRetorno> listGanhosRetorno = new ArrayList<>();
    private List<Ganho> listGanhos = new ArrayList<>();
    private GanhosAdapter ganhosAdapter;

    private RecyclerView rvGastos;
    private List<GastoRetorno> listGastosRetorno = new ArrayList<>();
    private List<Gasto> listGastos = new ArrayList<>();
    private GastosAdapter gastosAdapter;

    private RecyclerView rvVencimentosProximos;
    private List<GastoRetorno> listProximosGastosRetorno = new ArrayList<>();
    private List<Gasto> listProximosGastos = new ArrayList<>();
    private GastosAdapter proximosGastosAdapter;

    private RecyclerView rvCategorias;
    private List<Categoria> listCategorias = new ArrayList<>();
    private CategoriasAdapter categoriasAdapter;

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

        hcGrafico = findViewById(R.id.hc_graficos);
        criarGraficos();

        preencherRecyclerViews();

        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);
    }

    private void criarGraficos () {
        HIChart chart = new HIChart();
        chart.setType("column");

        HITitle title = new HITitle();
        title.setText("Seu Balanço Mensal");

        HISubtitle subtitle = new HISubtitle();
        subtitle.setText("Out - Nov");

        final HIYAxis hiyAxis = new HIYAxis();
        hiyAxis.setMin(0);
        hiyAxis.setTitle(new HITitle());
        hiyAxis.getTitle().setText("Valores (R$)");

        final HIXAxis hixAxis = new HIXAxis();
        ArrayList categories = new ArrayList<>();
        categories.add("Gastos x Ganhos");
        hixAxis.setCategories(categories);

        HITooltip tooltip = new HITooltip();
        tooltip.setHeaderFormat("<span style=\"font-size:15px\">{point.key}</span><table>");
        tooltip.setPointFormat("<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>" + "<td style=\"padding:0\"><b>{point.y}</b></td></tr>");
        tooltip.setFooterFormat("</talble>");
        tooltip.setShared(true);
        tooltip.setUseHTML(true);

        HIPlotOptions plotOptions = new HIPlotOptions();
        plotOptions.setColumn(new HIColumn());
        plotOptions.getColumn().setPointPadding(0.2);
        plotOptions.getColumn().setBorderWidth(0);

        HIColumn seriesGastos = new HIColumn();
        HIColumn seriesGanhos = new HIColumn();

        seriesGastos.setColor(HIColor.initWithRGB(255, 0, 0)); // red
        seriesGastos.setName("Gastos totais");
        ArrayList gastosData = new ArrayList<>();
        gastosData.add(250);
        seriesGastos.setData(gastosData);

        seriesGanhos.setColor(HIColor.initWithRGB(0, 0, 255)); // blue
        seriesGanhos.setName("Ganhos totais");
        ArrayList ganhosData = new ArrayList<>();
        ganhosData.add(1550);
        seriesGanhos.setData(ganhosData);

        ArrayList series = new ArrayList<>();
        series.add(seriesGastos);
        series.add(seriesGanhos);

        HIOptions options = new HIOptions();
        options.setChart(chart);
        options.setTitle(title);
        options.setSubtitle(subtitle);
        options.setYAxis(new ArrayList(){{add(hiyAxis);}});
        options.setXAxis(new ArrayList(){{add(hixAxis);}});
        options.setTooltip(tooltip);
        options.setPlotOptions(plotOptions);
        options.setSeries(series);
        hcGrafico.setOptions(options);
    }

    private void preencherRecyclerViews() {
        rvVencimentosProximos = findViewById(R.id.rv_vencimentos_proximos);
        rvVencimentosProximos.setLayoutManager(new LinearLayoutManager(this));
        proximosGastosAdapter = new GastosAdapter(this.getLayoutInflater(), (ArrayList<Gasto>) listProximosGastos);
        rvVencimentosProximos.setAdapter(proximosGastosAdapter);

        rvGastos = findViewById(R.id.rv_outras_opcoes_gastos);
        rvGastos.setLayoutManager(new LinearLayoutManager(this));
        gastosAdapter = new GastosAdapter(this.getLayoutInflater(), (ArrayList<Gasto>) listGastos);
        rvGastos.setAdapter(gastosAdapter);

        rvGanhos = findViewById(R.id.rv_outras_opcoes_ganhos);
        rvGanhos.setLayoutManager(new LinearLayoutManager(this));
        ganhosAdapter = new GanhosAdapter(this.getLayoutInflater(), (ArrayList<Ganho>) listGanhos);
        rvGanhos.setAdapter(ganhosAdapter);

        rvCategorias = findViewById(R.id.rv_outras_opcoes_categorias);
        rvCategorias.setLayoutManager(new LinearLayoutManager(this));
        categoriasAdapter = new CategoriasAdapter(this.getLayoutInflater(), (ArrayList<Categoria>) listCategorias);
        rvCategorias.setAdapter(categoriasAdapter);
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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        CustomProgressDialog progressDialog = new CustomProgressDialog(
                HomeActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        //instanciando a interface
        GanhoService ganhoService = retrofitAPI.create(GanhoService.class);

        Call<List<GanhoRetorno>> callGanho = ganhoService.retornarGanhoUsuario(token, usuarioId);
        callGanho.enqueue(new Callback<List<GanhoRetorno>>() {
            @Override
            public void onResponse(Call<List<GanhoRetorno>> callGanho, Response<List<GanhoRetorno>> response) {
                if(response.isSuccessful()){
                    listGanhosRetorno = response.body();
                    List<Ganho> ganhos = new ArrayList<>();
                    for (GanhoRetorno ganhoRetorno : listGanhosRetorno){
                        ganhos = ganhoRetorno.getListGanhos();
                        for (Ganho g : ganhos){
                            listGanhos.add(g);
                        }
                    }
                    rvGanhos.setAdapter(new GanhosAdapter(HomeActivity.this.getLayoutInflater(), (ArrayList<Ganho>) listGanhos));
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus ganhos.  \n" + msg.getMensagem(), HomeActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<GanhoRetorno>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

        //instanciando a interface
        GastoService gastoService = retrofitAPI.create(GastoService.class);

        Call<List<GastoRetorno>> callGasto = gastoService.retornarGastoUsuario(token, usuarioId);
        callGasto.enqueue(new Callback<List<GastoRetorno>>() {
            @Override
            public void onResponse(Call<List<GastoRetorno>> callGasto, Response<List<GastoRetorno>> response) {
                if(response.isSuccessful()){
                    listGastosRetorno = response.body();
                    List<Gasto> gastos = new ArrayList<>();
                    for (GastoRetorno gastoRetorno : listGastosRetorno){
                        gastos = gastoRetorno.getListGastos();
                        for (Gasto g : gastos){
                            listGastos.add(g);
                        }
                    }
                    rvGastos.setAdapter(new GastosAdapter(HomeActivity.this.getLayoutInflater(), (ArrayList<Gasto>) listGastos));
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus gastos.  \n" + msg.getMensagem(), HomeActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<GastoRetorno>> call, Throwable t) {
                progressDialog.dismiss();
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
                    rvCategorias.setAdapter(new CategoriasAdapter(HomeActivity.this.getLayoutInflater(), (ArrayList<Categoria>) listCategorias));
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar suas categorias.  \n" + msg.getMensagem(), HomeActivity.this);
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
        //pref.edit().clear().commit();
        super.onBackPressed();

    }
}