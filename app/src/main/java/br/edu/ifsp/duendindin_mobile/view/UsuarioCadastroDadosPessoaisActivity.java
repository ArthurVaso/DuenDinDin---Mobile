package br.edu.ifsp.duendindin_mobile.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import br.edu.ifsp.duendindin_mobile.R;

public class UsuarioCadastroDadosPessoaisActivity extends AppCompatActivity {
    private Button btnContinuar;
    private ImageView imgSetaVoltar;
    private TextInputEditText edtNome;
    private TextView txtDataNasc;
    private TextView txtEsqueciCEP;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText edtRendaFixa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_dados_pessoais);
        final Calendar c = Calendar.getInstance();
        int anoAtual = c.get(Calendar.YEAR);
        int mesAtual = c.get(Calendar.MONTH);
        int diaAtual = c.get(Calendar.DAY_OF_MONTH);
        edtNome = findViewById(R.id.edt_nome_usuario);
        edtRendaFixa = findViewById(R.id.edt_renda_usuario);
        txtDataNasc = findViewById(R.id.txt_data_nasc_usuario);
        txtDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(UsuarioCadastroDadosPessoaisActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int ano,
                                                  int mes, int dia) {
                                Calendar dataSelecionada = Calendar.getInstance();
                                dataSelecionada.set(ano, mes, dia);
                                calcularIdade(dia, mes, ano, diaAtual, mesAtual, anoAtual);
                            }
                        }, anoAtual, mesAtual, diaAtual);
                datePickerDialog.show();
            }
        });

        txtEsqueciCEP = findViewById(R.id.txt_esqueci_cep);
        txtEsqueciCEP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri parsedUrl = Uri.parse("https://buscacepinter.correios.com.br/app/endereco/index.php");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(parsedUrl);
                startActivity(intent);
            }
        });

        btnContinuar = findViewById(R.id.btn_usuario_cadastro_continuar);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Intent intent = new Intent(UsuarioCadastroDadosPessoaisActivity.this, UsuarioCadastroDadosAcessoActivity.class);
                    startActivity(intent);
                }
            }
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO conferir obrigatoriedade dos campos

                Intent intent = new Intent(UsuarioCadastroDadosPessoaisActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });

    }

    private void calcularIdade(int diaS, int mesS, int anoS, int diaAtual, int mesAtual, int anoAtual) {
        int idade = 0;
        idade = anoAtual - anoS;
        if (mesS > mesAtual) {
            idade--;
        } else if (mesAtual == mesS) {
            if (diaS > diaAtual) {
                idade--;
            }
        }
        if (idade > 110 || idade < 10) {
            txtDataNasc.setError(getString(R.string.msg_data_nasc_invalida));
            txtDataNasc.setText("");
            Toast.makeText(UsuarioCadastroDadosPessoaisActivity.this, R.string.msg_data_nasc_invalida, Toast.LENGTH_LONG).show();
            return;
        }
        txtDataNasc.setError(null);
        txtDataNasc.setText(diaS + "/" + (mesS + 1) + "/" + anoS);

    }

    private boolean validate() {
        boolean isValid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            edtNome.setError("");
            Toast.makeText(UsuarioCadastroDadosPessoaisActivity.this, "O campo Nome deve ser preenchido!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtNome.getText().toString().trim().length() > 30) {
            edtNome.setError("");
            Toast.makeText(UsuarioCadastroDadosPessoaisActivity.this, "O campo Nome não deve ter mais de 30 caracteres!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            edtNome.setError(null);
        }
        return isValid;
    }
}
