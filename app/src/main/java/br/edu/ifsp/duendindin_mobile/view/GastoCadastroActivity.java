package br.edu.ifsp.duendindin_mobile.view;

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
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class GastoCadastroActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private Button btnSalvar;
    private ImageView imgSetaVoltar;

    private TextView txtTipo;
    private TextView txtRecorrente;
    private TextView txtCategoria;
    private TextView txtDataVenc;
    private TextInputEditText edtNome;
    private TextInputEditText edtDescricao;
    private TextInputEditText edtValor;
    private Spinner spnCategoria;
    private Spinner spnTipo;
    private Spinner spnRecorrente;


    private Retrofit retrofitAPI;
    SharedPreferences pref;
    String token = "";
    int usuarioId = 0;
    List<Categoria> listaCategoria = new ArrayList<Categoria>();
    List<String> nomeCategorias = new ArrayList<String>();

    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_gasto);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        inicializarComponentes();

        progressDialog = new CustomProgressDialog(
                GastoCadastroActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );

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
                new DatePickerDialog(GastoCadastroActivity.this, date, dataSelecionada.get(Calendar.YEAR), dataSelecionada.get(Calendar.MONTH), dataSelecionada.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    cadastrarGasto();
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

    }

    private void inicializarComponentes() {

        edtNome = findViewById(R.id.edt_nome_gasto);
        edtDescricao = findViewById(R.id.edt_descricao_gasto);
        edtValor = findViewById(R.id.edt_valor_gasto);
        txtCategoria = findViewById(R.id.txt_categoria_gasto);
        spnCategoria = (Spinner) findViewById(R.id.sp_categoria_gasto);
        txtTipo = findViewById(R.id.txt_tipo_gasto);
        spnTipo = (Spinner) findViewById(R.id.sp_tipo_gasto);
        txtRecorrente = findViewById(R.id.txt_recorrente_gasto);
        spnRecorrente = (Spinner) findViewById(R.id.sp_recorrente_gasto);
        txtDataVenc = findViewById(R.id.txt_data_venc_gasto);
        btnSalvar = findViewById(R.id.btn_gasto_cadastro_salvar);
        imgSetaVoltar = findViewById(R.id.seta_voltar);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);
        nomeCategorias.add("Selecione uma opção...");
        retornarCategoriasUsuario();
    }

    private void cadastrarGasto() {

        progressDialog.show();

        Gasto gasto = new Gasto();
        gasto.setCategoriaId(listaCategoria.get(spnCategoria.getSelectedItemPosition()-1).getId());
        gasto.setNome(edtNome.getText().toString().trim());
        String data[] = txtDataVenc.getText().toString().split("/");
        String dataVenc = data[2] + "-" + data[1] + "-" + data[0];
        gasto.setVencimento(dataVenc);
        gasto.setValor(Double.parseDouble(edtValor.getText().toString()));
        gasto.setDescricao(edtDescricao.getText().toString().trim());

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
        Call<Gasto> call = gastoService.criarGasto(token, gasto);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<Gasto>() {
            @Override
            public void onResponse(Call<Gasto> call, Response<Gasto> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Gasto cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(GastoCadastroActivity.this, HomeActivity.class);
                    startActivity(intent);
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
                    new CustomMessageDialog("Ocorreu um erro ao cadastrar o gasto.  \n" + msg.getMensagem(), GastoCadastroActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Gasto> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), GastoCadastroActivity.this);
                Log.d("GastoCadastroActivity", t.getMessage());
            }
        });
    }


    private void retornarCategoriasUsuario() {
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
                                GastoCadastroActivity.this, android.R.layout.simple_spinner_item, nomeCategorias);
                        spCategoriaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spnCategoria.setAdapter(spCategoriaAdapter);

                        //Toast.makeText(getApplicationContext(), "Categorias retornadas com sucesso!", Toast.LENGTH_LONG).show();

                    } else {
                        ArrayAdapter<String> spCategoriaAdapter = new ArrayAdapter<String>(
                                GastoCadastroActivity.this, android.R.layout.simple_spinner_item, nomeCategorias);
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
                    new CustomMessageDialog("Ocorreu um erro ao consultar as categorias do usuario.  \n" + msg.getMensagem(), GastoCadastroActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), GastoCadastroActivity.this);
                Log.d("GastoCadastroActivity", t.getMessage());
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            Toast.makeText(GastoCadastroActivity.this, "Preencha o campo Nome", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (spnCategoria.getSelectedItemPosition() == 0) {
            Toast.makeText(GastoCadastroActivity.this, "Selecione uma Categoria!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtDataVenc.getText().toString().trim().isEmpty()) {
            Toast.makeText(GastoCadastroActivity.this, "Preencha o campo Data Vencimento", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtValor.getText().toString().trim().isEmpty()) {
            Toast.makeText(GastoCadastroActivity.this, "Preencha o campo Valor!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (spnRecorrente.getSelectedItemPosition() == 0) {
            Toast.makeText(GastoCadastroActivity.this, "Escolha se o gasto é Recorrente!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (spnTipo.getSelectedItemPosition() == 0) {
            Toast.makeText(GastoCadastroActivity.this, "Selecione um Tipo!", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
