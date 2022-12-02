package br.edu.ifsp.duendindin_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import br.edu.ifsp.duendindin_mobile.model.Ganho;

public class GanhoAlterarActivity extends AppCompatActivity {

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

    private Ganho ganhoListagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_ganho);

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

        //ArrayList<Categoria> listaCategoria = response.json()
        ArrayList<String> listaTeste = new ArrayList<>();
        listaTeste.add(getString(R.string.spinner_selecione_uma_opcao));
        //for each item: listaCategoria
        //listaTeste.add(item.getNome());
        listaTeste.add("Salário");
        listaTeste.add("Bônus");

        //spnCategoria = (Spinner) findViewById(R.id.sp_categoria_ganho);
        ArrayAdapter<String> spCategoriaAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listaTeste);
        spCategoriaAdapter.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line );

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
                    Intent intent = new Intent(GanhoAlterarActivity.this, GanhoListagemActivity.class);
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

        ganhoListagem = (Ganho) getIntent().getSerializableExtra("ganho");
        edtNome.setText(ganhoListagem.getNome());
//        spnCategoria.setSelection(ganhoListagem.getCategoriaId()); // ALTERAR ISSO AQUI - TOTALMENTE ERRADO
        edtDescricao.setText(ganhoListagem.getDescricao());
        txtDataReceb.setText(ganhoListagem.getData());
        edtValor.setText(ganhoListagem.getValor().toString());
        // spnRecorrente.setSelection(ganhoListagem.getRecorrencia()); // ALTERAR ISSO AQUI - TOTALMENTE ERRADO
        // spnTipo.setSelection(ganhoListagem.getTipo()); // ALTERAR ISSO AQUI - TOTALMENTE ERRADO

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