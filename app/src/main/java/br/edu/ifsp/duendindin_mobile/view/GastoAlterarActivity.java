package br.edu.ifsp.duendindin_mobile.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import br.edu.ifsp.duendindin_mobile.model.Gasto;
import br.edu.ifsp.duendindin_mobile.service.CategoriaService;
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

public class GastoAlterarActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private Button btnAlterar;
    private ImageView imgSetaVoltar;

    private TextView txtTipo;
    private TextView txtRecorrente;
    private TextView txtCategoria;
    private TextView txtDataVenc;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText edtNome;
    private TextInputEditText edtDescricao;
    private TextInputEditText edtValor;
    private Spinner spnCategoria;
    private Spinner spnTipo;
    private Spinner spnRecorrente;

    private Gasto gastoAtual;

    private Retrofit retrofitAPI;
    SharedPreferences pref;
    String token = "";
    int usuarioId = 0;
    List<Categoria> listaCategoria = new ArrayList<Categoria>();
    List<String> nomeCategorias = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_gasto);

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

            private void updateCalendar() {
                String Format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.getDefault());
                txtDataVenc.setText(sdf.format(dataSelecionada.getTime()));
            }
        };

        txtDataVenc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(GastoAlterarActivity.this, date, dataSelecionada.get(Calendar.YEAR), dataSelecionada.get(Calendar.MONTH), dataSelecionada.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    atualizarGasto();

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
                if (spnRecorrente.getSelectedItemPosition() == 1) {
                    spnTipo.setEnabled(true);
                } else if (spnRecorrente.getSelectedItemPosition() == 2) {
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

        gastoAtual = (Gasto) getIntent().getSerializableExtra("gasto");

    }

    @Override
    protected void onStart() {
        super.onStart();
        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);
        nomeCategorias.add("Selecione uma opção...");

        retornarCategoriasUsuario();

        edtNome.setText(gastoAtual.getNome());
        String data[] = gastoAtual.getVencimento().split("-");
        String dataVenc = data[2] + "/" + data[1] + "/" + data[0];
        txtDataVenc.setText(dataVenc);
        edtDescricao.setText(gastoAtual.getDescricao());
        edtValor.setText(gastoAtual.getValor().toString());

        if(gastoAtual.getTipo() == null || gastoAtual.getTipo().isEmpty()) {
            spnTipo.setSelection(0);
        } else if (gastoAtual.getTipo().trim().equalsIgnoreCase("F")) {
            spnTipo.setSelection(1);
        } else if (gastoAtual.getTipo().trim().equalsIgnoreCase("V")) {
            spnTipo.setSelection(2);
        } else {
            spnTipo.setSelection(0);
        }

        if (gastoAtual.getRecorrencia() == null) {
            spnRecorrente.setSelection(0);
        } else if (gastoAtual.getRecorrencia()) {
            spnRecorrente.setSelection(1);
        } else {
            spnRecorrente.setSelection(2);
        }


    }

    private void inicializarComponentes() {

        edtNome = findViewById(R.id.edt_nome_gasto_alterar);
        edtDescricao = findViewById(R.id.edt_descricao_gasto_alterar);
        edtValor = findViewById(R.id.edt_valor_gasto_alterar);
        txtCategoria = findViewById(R.id.txt_categoria_gasto_alterar);
        spnCategoria = (Spinner) findViewById(R.id.sp_categoria_gasto_alterar);
        txtTipo = findViewById(R.id.txt_tipo_gasto_alterar);
        spnTipo = (Spinner) findViewById(R.id.sp_tipo_gasto_alterar);
        txtRecorrente = findViewById(R.id.txt_recorrente_gasto_alterar);
        spnRecorrente = (Spinner) findViewById(R.id.sp_recorrente_gasto_alterar);
        txtDataVenc = findViewById(R.id.txt_data_venc_gasto_alterar);
        btnAlterar = findViewById(R.id.btn_gasto_alterar);
        imgSetaVoltar = findViewById(R.id.seta_voltar);
    }


    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    private void atualizarGasto() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                GastoAlterarActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        Gasto gasto = gastoAtual;
        gasto.setCategoriaId(listaCategoria.get(spnCategoria.getSelectedItemPosition()-1).getId());
        gasto.setNome(edtNome.getText().toString().trim());
        String data[] = txtDataVenc.getText().toString().split("/");
        String dataVenc = data[2] + "-" + data[1] + "-" + data[0];
        gasto.setVencimento(dataVenc);
        gasto.setDescricao(edtDescricao.getText().toString().trim());
        gasto.setValor(Double.parseDouble(edtValor.getText().toString()));
        if (spnRecorrente.getSelectedItemPosition() == 1) {
            gasto.setRecorrencia(true);
        } else {
            gasto.setRecorrencia(false);
        }
        if (spnTipo.getSelectedItemPosition() == 1) {
            gasto.setTipo("F");
        } else if (spnTipo.getSelectedItemPosition() == 2){
            gasto.setTipo("V");
        } else {
            gasto.setTipo(null);
        }

        //instanciando a interface
        GastoService gastoService = retrofitAPI.create(GastoService.class);

        //passando os dados para o serviço
        Call<Message> call = gastoService.atualizarGastoUsuario(token, gasto.getId(), gasto);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), response.body().getMensagem(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(GastoAlterarActivity.this, GastoListagemActivity.class);
                        startActivity(intent);

                    } else {
                        new CustomMessageDialog(response.body().getMensagem(), GastoAlterarActivity.this);
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
                    new CustomMessageDialog("Ocorreu um erro ao atualizar o gasto.  \n" + msg.getMensagem(), GastoAlterarActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), GastoAlterarActivity.this);
            }
        });
    }

    private void retornarCategoriasUsuario() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                GastoAlterarActivity.this,
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
                                GastoAlterarActivity.this, android.R.layout.simple_spinner_item, nomeCategorias);
                        spCategoriaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spnCategoria.setAdapter(spCategoriaAdapter);
                        listaCategoria.forEach(categoria -> {
                            if (categoria.getId().equals(gastoAtual.getCategoriaId())) {
                                spnCategoria.setSelection(listaCategoria.indexOf(categoria) + 1);
                            }
                        });
                    } else {
                        ArrayAdapter<String> spCategoriaAdapter = new ArrayAdapter<String>(
                                GastoAlterarActivity.this, android.R.layout.simple_spinner_item, nomeCategorias);
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar as categorias do usuario.  \n" + msg.getMensagem(), GastoAlterarActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), GastoAlterarActivity.this);
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            new CustomMessageDialog("Preencha o campo Nome", GastoAlterarActivity.this);
            isValid = false;
        } else if (spnCategoria.getSelectedItemPosition() == 0) {
            new CustomMessageDialog("Selecione uma Categoria!", GastoAlterarActivity.this);
            isValid = false;
        } else if (txtDataVenc.getText().toString().trim().isEmpty()) {
            new CustomMessageDialog("Preencha o campo Data Vencimento", GastoAlterarActivity.this);
            isValid = false;
        } else if (edtValor.getText().toString().trim().isEmpty()) {
            new CustomMessageDialog("Preencha o campo Valor!", GastoAlterarActivity.this);
            isValid = false;
        } else if (spnRecorrente.getSelectedItemPosition() == 0) {
            new CustomMessageDialog("Escolha se o gasto é Recorrente!", GastoAlterarActivity.this);
            isValid = false;
        } else if (spnTipo.getSelectedItemPosition() == 0) {
            new CustomMessageDialog("Selecione um Tipo!", GastoAlterarActivity.this);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GastoAlterarActivity.this, GastoListagemActivity.class));
    }
}