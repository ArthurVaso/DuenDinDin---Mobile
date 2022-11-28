package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.hichartsclasses.HIBar;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.adapter.CategoriasAdapter;
import br.edu.ifsp.duendindin_mobile.adapter.GanhosAdapter;
import br.edu.ifsp.duendindin_mobile.adapter.GastosAdapter;
import br.edu.ifsp.duendindin_mobile.model.Categoria;
import br.edu.ifsp.duendindin_mobile.model.Ganho;
import br.edu.ifsp.duendindin_mobile.model.Gasto;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;

public class HomeActivity extends AppCompatActivity {

    private Button btnAdicionarRecebimentos;
    private Button btnAdicionarVencimentos;
    private Button btnAdicionarCategoria;
    private Button btnVerPerfil;
    private HIChartView hcGrafico;
    private BottomNavigationView bnvHome;
    private SharedPreferences pref;

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

        preencherRecyclerViews();

        String token = pref.getString("token", "");
        new CustomMessageDialog("Token: " + token, HomeActivity.this);

    }

    private void preencherRecyclerViews() {
        RecyclerView rvVencimentosProximos = findViewById(R.id.rv_vencimentos_proximos);
        ArrayList<Gasto> listVencimentosPróximos = new ArrayList();
        Gasto vp1 = new Gasto(1, 5, "Academia Outubro", true, new Date(2022, 10, 30), 100.00, "Paguei a academia de outubro.", false, true);
        listVencimentosPróximos.add(vp1);
        rvVencimentosProximos.setLayoutManager(new LinearLayoutManager(this));
        GastosAdapter vencimentosProximosAdapter = new GastosAdapter(this.getLayoutInflater(), listVencimentosPróximos);
        rvVencimentosProximos.setAdapter(vencimentosProximosAdapter);

        RecyclerView rvGastos = findViewById(R.id.rv_outras_opcoes_gastos);
        ArrayList<Gasto> listGastos = new ArrayList();
        Gasto g1 = new Gasto(1, 5, "Academia Outubro", true, new Date(2022, 10, 30), 100.00, "Paguei a academia de outubro.", false, true);
        Gasto g2 = new Gasto(2, 5, "Presente Cunhado", false, new Date(2022, 11, 19), 120.00, "Comprei o presente do meu cunhado.", true, false);
        Gasto g3 = new Gasto(3, 5, "Uber", false, new Date(2022, 11, 27), 20.00, "Paguei o uber pra rodoviária.", false, true);
        listGastos.add(g1);
        listGastos.add(g2);
        listGastos.add(g3);
        rvGastos.setLayoutManager(new LinearLayoutManager(this));
        GastosAdapter gastosAdapter = new GastosAdapter(this.getLayoutInflater(), listGastos);
        rvGastos.setAdapter(gastosAdapter);

        RecyclerView rvGanhos = findViewById(R.id.rv_outras_opcoes_ganhos);
        ArrayList<Ganho> listGanhos = new ArrayList();
        Ganho gn1 = new Ganho(1, 4, "Salário Outubro", new Date(2022, 10, 5), 1000.00, "Esse é meu salário de outubro", true, true);
        Ganho gn2 = new Ganho(2, 1, "Presente do meu avô", new Date(2022, 11, 15), 50.00, "Meu avô me ajudou na vaquinha", false, false);
        Ganho gn3 = new Ganho(3, 2, "Freela Tio", new Date(2022, 10, 27), 500.00, "Fiz um Freela pra empresa do meu tio", false, true);
        listGanhos.add(gn1);
        listGanhos.add(gn2);
        listGanhos.add(gn3);
        rvGanhos.setLayoutManager(new LinearLayoutManager(this));
        GanhosAdapter ganhosAdapter = new GanhosAdapter(this.getLayoutInflater(), listGanhos);
        rvGanhos.setAdapter(ganhosAdapter);

        RecyclerView rvCategorias = findViewById(R.id.rv_outras_opcoes_categorias);
        ArrayList<Categoria> listCategorias = new ArrayList();
        Categoria c1 = new Categoria(1, 1, "Vaquinha", "Vou guardar para ajudar uma família no Natal.");
        Categoria c2 = new Categoria(2, 1, "Reserva de Emergência", "Essa é minha reserva de emergência :).");
        Categoria c3 = new Categoria(3, 1, "Viagem Final de Ano", "Conhecer alguma cidade nova no final de ano.");
        Categoria c4 = new Categoria(4, 1, "Meus Salários", "Onde deixo meu salário todo mês!");
        Categoria c5 = new Categoria(5, 1, "Meus Gastos", "Onde deixo meus gastos todo mês!");
        listCategorias.add(c1);
        listCategorias.add(c2);
        listCategorias.add(c3);
        listCategorias.add(c4);
        listCategorias.add(c5);
        rvCategorias.setLayoutManager(new LinearLayoutManager(this));
        CategoriasAdapter categoriasAdapter = new CategoriasAdapter(this.getLayoutInflater(), listCategorias);
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
    public void onBackPressed() {
        //pref.edit().clear().commit();
        super.onBackPressed();

    }
}