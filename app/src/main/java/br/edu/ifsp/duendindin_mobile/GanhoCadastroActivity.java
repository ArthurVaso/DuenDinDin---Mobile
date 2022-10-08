package br.edu.ifsp.duendindin_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Calendar;
import java.util.Locale;

public class GanhoCadastroActivity extends AppCompatActivity {

    private Button btnSalvar;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ganho);
        edtNome = findViewById(R.id.edt_nome_ganho);
        edtDescricao = findViewById(R.id.edt_descricao_ganho);
        edtValor = findViewById(R.id.edt_valor_ganho);
        txtCategoria = findViewById(R.id.txt_categoria_ganho);
        spnCategoria = (Spinner) findViewById(R.id.sp_categoria_ganho);
        txtTipo = findViewById(R.id.txt_tipo_ganho);
        spnTipo = (Spinner) findViewById(R.id.sp_tipo_ganho);
        txtRecorrente = findViewById(R.id.txt_recorrente_ganho);
        spnRecorrente = (Spinner) findViewById(R.id.sp_recorrente_ganho);

        txtDataReceb = findViewById(R.id.txt_data_receb_ganho);
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
                new DatePickerDialog(GanhoCadastroActivity.this, date,dataSelecionada.get(Calendar.YEAR), dataSelecionada.get(Calendar.MONTH), dataSelecionada.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSalvar = findViewById(R.id.btn_ganho_cadastro_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    Intent intent = new Intent(GanhoCadastroActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO conferir obrigatoriedade dos campos

                Intent intent = new Intent(GanhoCadastroActivity.this, MainActivity.class);
                startActivity(intent);
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

    }

    private boolean validate() {
        boolean isValid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            edtNome.setError("Preencha o campo nome");
            isValid = false;
        } else {
            edtNome.setError(null);
        }
        if (spnCategoria.getSelectedItemPosition() == 0) {
            txtCategoria.setError("Selecione um tipo!");
            isValid = false;
        } else {
            txtCategoria.setError(null);
        }
        if (txtDataReceb.getText().toString().trim().isEmpty()) {
            txtDataReceb.setError("Preencha o campo Data Recebimento");
            isValid = false;
        } else {
            txtDataReceb.setError(null);
        }
        if (edtValor.getText().toString().trim().isEmpty()) {
            edtValor.setError("Preencha o campo Data Recebimento");
            isValid = false;
        } else {
            edtValor.setError(null);
        }
        if (spnTipo.getSelectedItemPosition() == 0) {
            txtTipo.setError("Selecione um tipo!");
            isValid = false;
        } else {
            txtTipo.setError(null);
        }
        if (spnRecorrente.getSelectedItemPosition() == 0) {
            txtRecorrente.setError("Informe se a categoria é recorrente!");
            isValid = false;
        } else {
            txtRecorrente.setError(null);
        }
        return isValid;
    }

}
