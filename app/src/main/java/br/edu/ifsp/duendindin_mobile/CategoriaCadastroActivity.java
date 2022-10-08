package br.edu.ifsp.duendindin_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class CategoriaCadastroActivity extends AppCompatActivity {

    private Button btnSalvar;
    private ImageView imgSetaVoltar;
    private TextView txtTipo;
    private TextView txtRecorrente;
    private TextInputEditText edtNome;
    private TextInputEditText edtDescricao;
    private TextInputEditText edtPeriodoRecorrencia;
    private Spinner spnTipo;
    private Spinner spnRecorrente;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro_categoria);
        edtNome = findViewById(R.id.edt_nome_categoria);
        edtDescricao = findViewById(R.id.edt_descricao_categoria);
        edtPeriodoRecorrencia = findViewById(R.id.edt_periodo_recorrencia);
        txtTipo = findViewById(R.id.txt_tipo_categoria);
        spnTipo = (Spinner) findViewById(R.id.sp_tipo_categoria);
        txtRecorrente = findViewById(R.id.txt_recorrente_categoria);
        spnRecorrente = (Spinner) findViewById(R.id.sp_recorrente_categoria);

        btnSalvar = findViewById(R.id.btn_categoria_cadastro_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO conferir obrigatoriedade dos campos
                if (validate()) {
                    Intent intent = new Intent(CategoriaCadastroActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO conferir obrigatoriedade dos campos

                Intent intent = new Intent(CategoriaCadastroActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        spnRecorrente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spnRecorrente.getSelectedItemPosition() == 1)
                {
                    spnTipo.setEnabled(true);
                    edtPeriodoRecorrencia.setEnabled(true);
                } else if(spnRecorrente.getSelectedItemPosition() == 2)
                {
                    spnTipo.setSelection(1);
                    spnTipo.setEnabled(false);
                    edtPeriodoRecorrencia.setEnabled(false);
                } else {
                    spnTipo.setEnabled(true);
                    edtPeriodoRecorrencia.setEnabled(false);
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

