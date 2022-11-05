package br.edu.ifsp.duendindin_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.highsoft.highcharts.common.hichartsclasses.*;
import com.highsoft.highcharts.core.*;

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
    public void onBackPressed() {
        super.onBackPressed();
    }
}