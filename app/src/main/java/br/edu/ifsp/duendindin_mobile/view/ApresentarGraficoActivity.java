package br.edu.ifsp.duendindin_mobile.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class ApresentarGraficoActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private ImageView imgSetaVoltar;
    private ImageButton imgInfo;
    private TextView txtSubtitulo;

    private PieChart pieChart;

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
    private Double gastosFixosTotais = 0.0;
    private Double gastosNaoRecorrentesTotais = 0.0;
    private Double gastosInvestimentoTotais = 0.0;
    private Double valorTotal = 0.0;
    private float percentualGastosFixos = 0;
    private float percentualGastosNaoRecorrentes = 0;
    private float percentualGastosInvestimentos = 0;
    private Double saldoTotal = 0.0;

    private CustomProgressDialog progressDialog;

    private int mesInt;
    private String nomeMes;
    private String mesStr;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentar_grafico);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgInfo = findViewById(R.id.img_btn_info);
        pieChart = findViewById(R.id.pieChart1);
        txtSubtitulo = findViewById(R.id.txt_subtitulo_grafico1);

        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resultado1 = String.format("%.2f", gastosFixosTotais);
                String resultado2 = String.format("%.2f", gastosNaoRecorrentesTotais);
                String resultado3 = String.format("%.2f", gastosInvestimentoTotais);

                String textoInformativo = "    O gráfico representa a situação do usuário no mês e ano especificado em relação às proporções "+ "\"ideais\"" + " de gastos durante o mês, sendo 50% para gastos fixos; 30% para gastos que não são recorrentes; e, 20% que sobram para investimento.\n";

                String dadosGrafico = "» Seus gastos recorrentes são R$ "+resultado1+" e correspondem à "+percentualGastosFixos+"%. \n" +
                        "» Seus gastos não recorrentes são R$ "+resultado2+" e correspondem à "+percentualGastosNaoRecorrentes+"%. \n" +
                        "» O restante para investimentos são R$ "+resultado3+" e correspondem à "+percentualGastosInvestimentos+"%. \n";

                new CustomMessageDialog(textoInformativo +"\n"+ dadosGrafico, ApresentarGraficoActivity.this);
            }
        });

        progressDialog = new CustomProgressDialog(
                ApresentarGraficoActivity.this,
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus ganhos.  \n" + msg.getMensagem(), ApresentarGraficoActivity.this);
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar suas configurações.  \n" + msg.getMensagem(), ApresentarGraficoActivity.this);
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
                                if (g.getRecorrencia()){
                                    gastosFixosTotais += g.getValor();
                                } else {
                                    gastosNaoRecorrentesTotais += g.getValor();
                                }
                            }
                        }
                    }

                    gastosInvestimentoTotais = ganhosTotais-gastosTotais;

                    saldoTotal = ganhosTotais-gastosTotais;

                    if (gastosInvestimentoTotais<0){
                        gastosInvestimentoTotais=0.0;
                    }

                    valorTotal = gastosFixosTotais+gastosNaoRecorrentesTotais+gastosInvestimentoTotais;

                    percentualGastosFixos = Math.round(gastosFixosTotais/valorTotal*100);
                    percentualGastosNaoRecorrentes = Math.round(gastosNaoRecorrentesTotais/valorTotal*100);
                    percentualGastosInvestimentos = Math.round(gastosInvestimentoTotais/valorTotal*100);

                    String textoSubtitulo = "";
                    String resultado = String.format("%.2f", saldoTotal);
                    if(saldoTotal<0) {
                        txtSubtitulo.setTextColor(Color.RED);
                        textoSubtitulo = txtSubtitulo.getText()+" - R$ "+resultado;
                    } else {
                        textoSubtitulo = txtSubtitulo.getText()+" R$ "+resultado;
                    }
                    txtSubtitulo.setText(textoSubtitulo);

                    preencherPieChart();
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar seus gastos.  \n" + msg.getMensagem(), ApresentarGraficoActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<GastoRetorno>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void preencherPieChart() {
        if(gastosFixosTotais==0 && gastosNaoRecorrentesTotais==0 && gastosInvestimentoTotais ==0){
            Toast.makeText(this, "Nenhum dado disponível no momento.", Toast.LENGTH_SHORT).show();
            pieChart.setNoDataText("Por enquanto, nenhum dado está disponível.");
        } else {
            ArrayList<PieEntry> entries = new ArrayList<>();

            PieEntry pieEntryInvestimento = new PieEntry(gastosInvestimentoTotais.floatValue(), "Investimento: " + percentualGastosInvestimentos + "% (20%)");
            PieEntry pieEntryNaoRecorrentes = new PieEntry(gastosNaoRecorrentesTotais.floatValue(), "Não Recorrentes: " + percentualGastosNaoRecorrentes + "% (30%)");
            PieEntry pieEntryRecorrentes = new PieEntry(Float.parseFloat(gastosFixosTotais.toString()), "Recorrentes: " + percentualGastosFixos + "% (50%)");

            if (gastosInvestimentoTotais!=0) {
                entries.add(pieEntryInvestimento);
            }
            entries.add(pieEntryNaoRecorrentes);
            entries.add(pieEntryRecorrentes);


            PieDataSet pieDataSet = new PieDataSet(entries, "");
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(16f);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.setHoleColor(Color.parseColor("#A1A39D"));
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("DuenDinDin");
            pieChart.invalidate();

            Legend legend = pieChart.getLegend();

            int[] colorClassArray = ColorTemplate.MATERIAL_COLORS;
            String[] legendName = {"Investimento", "Não Recorrentes", "Recorrentes"};

            LegendEntry[] legendEntries = new LegendEntry[3];
            for (int i=0; i<legendEntries.length; i++){
                LegendEntry entry = new LegendEntry();
                entry.formColor = colorClassArray[i];
                entry.label = legendName[i];
                legendEntries[i] = entry;
            }
            legend.setCustom(legendEntries);
        }
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ApresentarGraficoActivity.this, HomeActivity.class));
    }

}