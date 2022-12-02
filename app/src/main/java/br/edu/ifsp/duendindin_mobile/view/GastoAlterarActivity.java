package br.edu.ifsp.duendindin_mobile.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Categoria;
import br.edu.ifsp.duendindin_mobile.model.Gasto;

public class GastoAlterarActivity extends AppCompatActivity {

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

    private Gasto gastoListagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_gasto);
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
                new DatePickerDialog(GastoAlterarActivity.this, date, dataSelecionada.get(Calendar.YEAR), dataSelecionada.get(Calendar.MONTH), dataSelecionada.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO conferir obrigatoriedade dos campos
                if (validate()) {
                    Intent intent = new Intent(GastoAlterarActivity.this, HomeActivity.class);
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

        gastoListagem = (Gasto) getIntent().getSerializableExtra("gasto");
        edtNome.setText(gastoListagem.getNome());
        edtDescricao.setText(gastoListagem.getDescricao());


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

    private boolean validate() {
        boolean isValid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            Toast.makeText(GastoAlterarActivity.this, "Preencha o campo Nome", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (spnCategoria.getSelectedItemPosition() == 0) {
            Toast.makeText(GastoAlterarActivity.this, "Selecione uma Categoria!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtDataVenc.getText().toString().trim().isEmpty()) {
            Toast.makeText(GastoAlterarActivity.this, "Preencha o campo Data Vencimento", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtValor.getText().toString().trim().isEmpty()) {
            Toast.makeText(GastoAlterarActivity.this, "Preencha o campo Valor!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (spnRecorrente.getSelectedItemPosition() == 0) {
            Toast.makeText(GastoAlterarActivity.this, "Escolha se o gasto é Recorrente!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (spnTipo.getSelectedItemPosition() == 0) {
            Toast.makeText(GastoAlterarActivity.this, "Selecione um Tipo!", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}