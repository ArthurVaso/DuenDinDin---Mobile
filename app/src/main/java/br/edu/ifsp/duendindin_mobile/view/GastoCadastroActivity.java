package br.edu.ifsp.duendindin_mobile.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import br.edu.ifsp.duendindin_mobile.R;

public class GastoCadastroActivity extends AppCompatActivity {

    private Button btnSalvar;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_gasto);
        inicializarComponentes();

        //ArrayList<Categoria> listaCategoria = response.json()
        ArrayList<String> listaTeste = new ArrayList<String>();
        listaTeste.add(getString(R.string.spinner_selecione_uma_opcao));
        //for each item: listaCategoria
        //listaTeste.add(item.getNome());
        listaTeste.add("Salário");
        listaTeste.add("Bônus");
        //spnCategoria = (Spinner) findViewById(R.id.sp_categoria_ganho);
        ArrayAdapter<String> spCategoriaAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listaTeste);
        spCategoriaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnCategoria.setAdapter(spCategoriaAdapter);

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

                //TODO conferir obrigatoriedade dos campos
                if (validate()) {
                    Intent intent = new Intent(GastoCadastroActivity.this, HomeActivity.class);
                    startActivity(intent);
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
