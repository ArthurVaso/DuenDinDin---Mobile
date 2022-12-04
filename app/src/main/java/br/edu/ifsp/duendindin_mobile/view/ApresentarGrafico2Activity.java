package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Ganho;
import br.edu.ifsp.duendindin_mobile.model.GanhoRetorno;
import br.edu.ifsp.duendindin_mobile.model.Gasto;
import br.edu.ifsp.duendindin_mobile.model.GastoRetorno;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.model.UsuarioComConfiguracao;
import br.edu.ifsp.duendindin_mobile.service.GanhoService;
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

public class ApresentarGrafico2Activity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private ImageView imgSetaVoltar;

    private BarChart barChart;

    private Retrofit retrofitAPI;
    private SharedPreferences pref;
    private Usuario usuario = new Usuario();
    private String token = "";
    private int usuarioId;

    private List<GanhoRetorno> listGanhosRetorno = new ArrayList<>();
    private List<Ganho> listGanhos = new ArrayList<>();
    private Double ganhosTotais = 0.0;

    private List<GastoRetorno> listGastosRetorno = new ArrayList<>();
    private List<Gasto> listGastos = new ArrayList<>();
    private Double gastosTotais = 0.0;

    private CustomProgressDialog progressDialog;
    private int mesInt;
    private String nomeMes;
    private String mesStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentar_grafico2);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        barChart = findViewById(R.id.barChart1);

        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressDialog = new CustomProgressDialog(
                ApresentarGrafico2Activity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );

        String meses[] = {"Janeiro", "Fevereiro",
                "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro",
                "Novembro", "Dezembro"};

        Calendar agora = Calendar.getInstance();
        mesInt = agora.get(Calendar.MONTH);
        mesStr = String.valueOf(mesInt+1);
        nomeMes = meses[agora.get(Calendar.MONTH)];

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);

        buscarGanhos();
    }

    private void buscarGanhos() {
        progressDialog.show();

        GanhoService ganhoService = retrofitAPI.create(GanhoService.class);
        Call<List<GanhoRetorno>> call = ganhoService.retornarGanhoUsuario(token, usuarioId);
        call.enqueue(new Callback<List<GanhoRetorno>>() {
            @Override
            public void onResponse(Call<List<GanhoRetorno>> call, Response<List<GanhoRetorno>> response) {
                if(response.isSuccessful()){
                    listGanhosRetorno = response.body();
                    for (GanhoRetorno ganhoRetorno : listGanhosRetorno){
                        String dataGanho = ganhoRetorno.getData();
                        String data[] = dataGanho.split("-");
                        String mesGanho = data[1];

                        if(mesGanho.equals(mesStr)){
                            for (Ganho g : ganhoRetorno.getListGanhos()){
                                ganhosTotais += g.getValor();
                            }
                        }
                    }
                    somarSalarioUsuario();
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus ganhos.  \n" + msg.getMensagem(), ApresentarGrafico2Activity.this);
                }
            }

            @Override
            public void onFailure(Call<List<GanhoRetorno>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void somarSalarioUsuario() {
        UsuarioService usuarioService = retrofitAPI.create(UsuarioService.class);
        Call<UsuarioComConfiguracao> call = usuarioService.consultarUsuarioComConfiguracao(token, usuarioId);
        call.enqueue(new Callback<UsuarioComConfiguracao>() {
            @Override
            public void onResponse(Call<UsuarioComConfiguracao> call, Response<UsuarioComConfiguracao> response) {
                if(response.isSuccessful()){
                    UsuarioComConfiguracao usuarioComConfiguracao = response.body();
                    ganhosTotais += usuarioComConfiguracao.getConfiguracao().getRendaFixa();
                    buscarGastos();
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar suas configurações.  \n" + msg.getMensagem(), ApresentarGrafico2Activity.this);
                }
            }

            @Override
            public void onFailure(Call<UsuarioComConfiguracao> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void buscarGastos() {
        GastoService gastoService = retrofitAPI.create(GastoService.class);
        Call<List<GastoRetorno>> call = gastoService.retornarGastoUsuario(token, usuarioId);
        call.enqueue(new Callback<List<GastoRetorno>>() {
            @Override
            public void onResponse(Call<List<GastoRetorno>> call, Response<List<GastoRetorno>> response) {
                if(response.isSuccessful()){
                    listGastosRetorno = response.body();
                    for (GastoRetorno gastoRetorno : listGastosRetorno){
                        String dataGasto = gastoRetorno.getData();
                        String data[] = dataGasto.split("-");
                        String mesGasto = data[1];
                        if(mesGasto.equals(mesStr)){
                            for (Gasto g : gastoRetorno.getListGastos()){
                                gastosTotais += g.getValor();
                            }
                        }
                    }
                    preencherBarChart();
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus ganhos.  \n" + msg.getMensagem(), ApresentarGrafico2Activity.this);
                }
            }

            @Override
            public void onFailure(Call<List<GastoRetorno>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void preencherBarChart() {
        if(ganhosTotais==0 && gastosTotais==0){
            Toast.makeText(this, "Nenhum dado disponível no momento.", Toast.LENGTH_SHORT).show();
            barChart.setNoDataText("Por enquanto, nenhum dado está disponível.");
        } else {
            ArrayList<BarEntry> entriesGanhos = new ArrayList<>();
            BarEntry barEntryGanhos = new BarEntry(1, ganhosTotais.floatValue());
            entriesGanhos.add(barEntryGanhos);

            ArrayList<BarEntry> entriesGastos = new ArrayList<>();
            BarEntry barEntryGastos = new BarEntry(2, gastosTotais.floatValue());
            entriesGastos.add(barEntryGastos);

            List<IBarDataSet> bars = new ArrayList<IBarDataSet>();

            BarDataSet datasetGanhos = new BarDataSet(entriesGanhos, "Ganhos");
            datasetGanhos.setColor(Color.BLUE);
            datasetGanhos.setValueTextSize(15f);
            bars.add(datasetGanhos);

            BarDataSet datasetGastos = new BarDataSet(entriesGastos, "Gastos");
            datasetGastos.setColor(Color.RED);
            datasetGastos.setValueTextSize(15f);
            bars.add(datasetGastos);

            BarData data = new BarData(bars);
            barChart.setData(data);
            barChart.getAxisRight().setDrawGridLines(false);
            barChart.getAxisLeft().setDrawGridLines(false);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.getXAxis().setDrawLabels(false);

            barChart.invalidate();
            barChart.getDescription().setEnabled(false);
        }
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ApresentarGrafico2Activity.this, HomeActivity.class));
    }
}