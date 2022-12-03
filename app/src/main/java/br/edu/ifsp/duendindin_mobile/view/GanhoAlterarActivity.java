package br.edu.ifsp.duendindin_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Categoria;
import br.edu.ifsp.duendindin_mobile.model.Ganho;
import br.edu.ifsp.duendindin_mobile.service.CategoriaService;
import br.edu.ifsp.duendindin_mobile.service.GanhoService;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
import br.edu.ifsp.duendindin_mobile.utils.CustomProgressDialog;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import br.edu.ifsp.duendindin_mobile.utils.URLAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GanhoAlterarActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private Button btnAlterar;
    private ImageView imgSetaVoltar;

    private TextView txtTipo;
    private TextView txtRecorrente;
    private TextView txtCategoria;
    private TextView txtDataReceb;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText edtNome;
    private TextInputEditText edtDescricao;
    private TextInputEditText edtValor;
    private Spinner spnCategoria;
    private Spinner spnTipo;
    private Spinner spnRecorrente;

    private Ganho ganhoAtual;

    private Retrofit retrofitAPI;
    SharedPreferences pref;
    String token = "";
    int usuarioId = 0;
    List<Categoria> listaCategoria = new ArrayList<Categoria>();
    List<String> nomeCategorias = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_ganho);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        inicializarComponentes();

        Calendar dataSelecionada = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                dataSelecionada.set(Calendar.YEAR, ano);
                dataSelecionada.set(Calendar.MONTH, mes);
                dataSelecionada.set(Calendar.DAY_OF_MONTH, dia);

                updateCalendar();
            }

            private void updateCalendar(){
                String Format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.getDefault());
                txtDataReceb.setText(sdf.format(dataSelecionada.getTime()));
            }
        };
        txtDataReceb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(GanhoAlterarActivity.this, date,dataSelecionada.get(Calendar.YEAR), dataSelecionada.get(Calendar.MONTH), dataSelecionada.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    atualizarGanho();
                }
            }
        });

        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        spnRecorrente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spnRecorrente.getSelectedItemPosition() == 1)
                {
                    spnTipo.setEnabled(true);
                } else if(spnRecorrente.getSelectedItemPosition() == 2)
                {
                    spnTipo.setSelection(1);
                    spnTipo.setEnabled(false);
                } else {
                    spnTipo.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ganhoAtual = (Ganho) getIntent().getSerializableExtra("ganho");
    }

    @Override
    protected void onStart() {
        super.onStart();
        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);
        nomeCategorias.add("Selecione uma opção...");

        retornarCategoriasUsuario();

        edtNome.setText(ganhoAtual.getNome());
        String data[] = ganhoAtual.getData().split("-");
        String dataReceb = data[2] + "/" + data[1] + "/" + data[0];
        txtDataReceb.setText(dataReceb);
        edtDescricao.setText(ganhoAtual.getDescricao());
        edtValor.setText(ganhoAtual.getValor().toString());

        if(ganhoAtual.getTipo() == null || ganhoAtual.getTipo().isEmpty()) {
            spnTipo.setSelection(0);
        } else if (ganhoAtual.getTipo().trim().equalsIgnoreCase("F")) {
            spnTipo.setSelection(1);
        } else if (ganhoAtual.getTipo().trim().equalsIgnoreCase("V")) {
            spnTipo.setSelection(2);
        } else {
            spnTipo.setSelection(0);
        }

        if (ganhoAtual.getRecorrencia() == null) {
            spnRecorrente.setSelection(0);
        } else if (ganhoAtual.getRecorrencia()) {
            spnRecorrente.setSelection(1);
        } else {
            spnRecorrente.setSelection(2);
        }


    }

    private void atualizarGanho() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                GanhoAlterarActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        Ganho ganho = ganhoAtual;
        ganho.setCategoriaId(listaCategoria.get(spnCategoria.getSelectedItemPosition()-1).getId());
        ganho.setNome(edtNome.getText().toString().trim());
        String data[] = txtDataReceb.getText().toString().split("/");
        String dataReceb = data[2] + "-" + data[1] + "-" + data[0];
        ganho.setData(dataReceb);
        ganho.setDescricao(edtDescricao.getText().toString().trim());
        ganho.setValor(Double.parseDouble(edtValor.getText().toString()));
        if (spnRecorrente.getSelectedItemPosition() == 1) {
            ganho.setRecorrencia(true);
        } else {
            ganho.setRecorrencia(false);
        }
        if (spnTipo.getSelectedItemPosition() == 1) {
            ganho.setTipo("F");
        } else if (spnTipo.getSelectedItemPosition() == 2){
            ganho.setTipo("V");
        } else {
            ganho.setTipo(null);
        }

        //instanciando a interface
        GanhoService ganhoService = retrofitAPI.create(GanhoService.class);

        //passando os dados para o serviço
        Call<Message> call = ganhoService.atualizarGanhoUsuario(token, ganho.getId(), ganho);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), response.body().getMensagem(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(GanhoAlterarActivity.this, GanhoListagemActivity.class);
                        startActivity(intent);

                    } else {
                        new CustomMessageDialog(response.body().getMensagem(), GanhoAlterarActivity.this);
                    }

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
                    new CustomMessageDialog("Ocorreu um erro ao atualizar o ganho.  \n" + msg.getMensagem(), GanhoAlterarActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), GanhoAlterarActivity.this);
            }
        });
    }

    private void inicializarComponentes() {
        edtNome = findViewById(R.id.edt_nome_ganho_alterar);
        edtDescricao = findViewById(R.id.edt_descricao_ganho_alterar);
        edtValor = findViewById(R.id.edt_valor_ganho_alterar);
        txtCategoria = findViewById(R.id.txt_categoria_ganho_alterar);
        spnCategoria = (Spinner) findViewById(R.id.sp_categoria_ganho_alterar);
        txtTipo = findViewById(R.id.txt_tipo_ganho_alterar);
        spnTipo = (Spinner) findViewById(R.id.sp_tipo_ganho_alterar);
        txtRecorrente = findViewById(R.id.txt_recorrente_ganho_alterar);
        spnRecorrente = (Spinner) findViewById(R.id.sp_recorrente_ganho_alterar);
        txtDataReceb = findViewById(R.id.txt_data_receb_ganho_alterar);
        btnAlterar = findViewById(R.id.btn_ganho_alterar);
        imgSetaVoltar = findViewById(R.id.seta_voltar);
    }

    private void retornarCategoriasUsuario() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                GanhoAlterarActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );

        progressDialog.show();

        //instanciando a interface
        CategoriaService categoriaService = retrofitAPI.create(CategoriaService.class);

        //passando os dados para o serviço
        Call<List<Categoria>> call = categoriaService.retornarCategoriaUsuario(token, usuarioId);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {

                if (response.isSuccessful()) {

                    if (response.body().size() > 0) {
                        listaCategoria.addAll(response.body());
                        listaCategoria.forEach(categoria -> {
                            nomeCategorias.add(categoria.getNome());

                        });
                        //populando o array
                        ArrayAdapter<String> spCategoriaAdapter = new ArrayAdapter<String>(
                                GanhoAlterarActivity.this, android.R.layout.simple_spinner_item, nomeCategorias);
                        spCategoriaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spnCategoria.setAdapter(spCategoriaAdapter);
                        listaCategoria.forEach(categoria -> {
                            if (categoria.getId().equals(ganhoAtual.getCategoriaId())) {
                                spnCategoria.setSelection(listaCategoria.indexOf(categoria) + 1);
                            }
                        });
                    } else {
                        ArrayAdapter<String> spCategoriaAdapter = new ArrayAdapter<String>(
                                GanhoAlterarActivity.this, android.R.layout.simple_spinner_item, nomeCategorias);
                        spCategoriaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spnCategoria.setAdapter(spCategoriaAdapter);
                        //Toast.makeText(getApplicationContext(), "nenhuma categoria a ser retornada!", Toast.LENGTH_LONG).show();
                    }

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
                    new CustomMessageDialog("Ocorreu um erro ao consultar as categorias do usuario.  \n"
                            + msg.getMensagem(),
                            GanhoAlterarActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor),
                        GanhoAlterarActivity.this);
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            Toast.makeText(GanhoAlterarActivity.this, "Preencha o campo Nome", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (spnCategoria.getSelectedItemPosition() == 0) {
            Toast.makeText(GanhoAlterarActivity.this, "Selecione uma Categoria!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtDataReceb.getText().toString().trim().isEmpty()) {
            Toast.makeText(GanhoAlterarActivity.this, "Preencha o campo Data Recebimento", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtValor.getText().toString().trim().isEmpty()) {
            Toast.makeText(GanhoAlterarActivity.this, "Preencha o campo Valor", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (spnRecorrente.getSelectedItemPosition() == 0) {
            Toast.makeText(GanhoAlterarActivity.this, "Escolha se o ganho é Recorrente!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (spnTipo.getSelectedItemPosition() == 0) {
            Toast.makeText(GanhoAlterarActivity.this, "Selecione um Tipo!", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}