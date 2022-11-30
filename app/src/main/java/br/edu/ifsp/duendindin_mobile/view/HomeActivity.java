package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIColumn;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.core.HIChartView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import br.edu.ifsp.duendindin_mobile.R;

public class HomeActivity extends AppCompatActivity {

    private Button btnAdicionarRecebimentos;
    private Button btnAdicionarVencimentos;
    private Button btnAdicionarCategoria;
    private Button btnVerPerfil;

    private HIChartView hcGrafico;

    private BottomNavigationView bnvHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        title.setText("Gráfico 1");

        HIColumn series = new HIColumn();
        series.setData(new ArrayList<>(Arrays.asList(77.4, 63.2, 50.3, 48.5, 0.3)));

        HIOptions options = new HIOptions();
        options.setChart(chart);
        options.setTitle(title);
        options.setSeries(new ArrayList<>(Collections.singletonList(series)));
        hcGrafico.setOptions(options);

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
        super.onBackPressed();
    }
}