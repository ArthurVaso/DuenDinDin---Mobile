package br.edu.ifsp.duendindin_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class GanhoCadastroActivity extends AppCompatActivity {

    private Button btnSalvar;
    private ImageView imgSetaVoltar;

    private TextView txtCategoria;
    private TextView txtDataReceb;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText edtNome;
    private TextInputEditText edtDescricao;
    private TextInputEditText edtValor;
    private Spinner spnCategoria;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ganho);
        edtNome = findViewById(R.id.edt_nome_ganho);
        edtDescricao = findViewById(R.id.edt_descricao_ganho);
        edtValor = findViewById(R.id.edt_valor_ganho);
        txtCategoria = findViewById(R.id.txt_tipo_categoria);
        spnCategoria = (Spinner) findViewById(R.id.sp_tipo_categoria);

        final Calendar c = Calendar.getInstance();
        int anoAtual = c.get(Calendar.YEAR);
        int mesAtual = c.get(Calendar.MONTH);
        int diaAtual = c.get(Calendar.DAY_OF_MONTH);
        txtDataReceb = findViewById(R.id.txt_data_receb_ganho);
        txtDataReceb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(GanhoCadastroActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int ano,
                                                  int mes, int dia) {
                                Calendar dataSelecionada = Calendar.getInstance();
                                dataSelecionada.set(ano, mes, dia);

                                validarData(dia, mes, ano, diaAtual, mesAtual, anoAtual);

                            }
                        }, anoAtual, mesAtual, diaAtual);
                datePickerDialog.show();
            }


        });

        btnSalvar = findViewById(R.id.btn_ganho_cadastro_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO conferir obrigatoriedade dos campos
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
        return isValid;
    }

    private void validarData(int diaS, int mesS, int anoS, int diaAtual, int mesAtual, int anoAtual) {
        /*
        int idade = 0;
        idade = anoAtual - anoS;
        if (mesS > mesAtual) {
            idade--;
        } else if (mesAtual == mesS) {
            if (diaS > diaAtual) {
                idade--;
            }
        }
        */
        if (false) {
            txtDataReceb.setError(getString(R.string.msg_data_receb_invalida));
            txtDataReceb.setText("");
            Toast.makeText(GanhoCadastroActivity.this, R.string.msg_data_receb_invalida, Toast.LENGTH_LONG).show();
            return;
        }
        txtDataReceb.setError(null);
        txtDataReceb.setText(diaS + "/" + (mesS + 1) + "/" + anoS);

    }
}
