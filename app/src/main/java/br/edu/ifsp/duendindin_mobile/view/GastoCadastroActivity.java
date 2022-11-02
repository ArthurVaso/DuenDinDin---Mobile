package br.edu.ifsp.duendindin_mobile.view;

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
                txtDataVenc.setText(sdf.format(dataSelecionada.getTime()));
            }
        };
        txtDataVenc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(GastoCadastroActivity.this, date,dataSelecionada.get(Calendar.YEAR), dataSelecionada.get(Calendar.MONTH), dataSelecionada.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSalvar = findViewById(R.id.btn_gasto_cadastro_salvar);
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

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO conferir obrigatoriedade dos campos

                Intent intent = new Intent(GastoCadastroActivity.this, HomeActivity.class);
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
            edtNome.setError("");
            Toast.makeText(GastoCadastroActivity.this, "Preencha o campo nome", Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            edtNome.setError(null);
        }
        if (spnCategoria.getSelectedItemPosition() == 0) {
            txtCategoria.setError("");
            Toast.makeText(GastoCadastroActivity.this, "Selecione um tipo!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            txtCategoria.setError(null);
        }
        if (txtDataVenc.getText().toString().trim().isEmpty()) {
            txtDataVenc.setError("");
            Toast.makeText(GastoCadastroActivity.this, "Preencha o campo Data Vencimento", Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            txtDataVenc.setError(null);
        }
        if (edtValor.getText().toString().trim().isEmpty()) {
            edtValor.setError("");
            Toast.makeText(GastoCadastroActivity.this, "Preencha o campo Data Vencimento!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            edtValor.setError(null);
        }
        return isValid;
    }


}
