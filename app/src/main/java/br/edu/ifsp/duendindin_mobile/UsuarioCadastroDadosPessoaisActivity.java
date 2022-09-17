package br.edu.ifsp.duendindin_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class UsuarioCadastroDadosPessoaisActivity extends AppCompatActivity {
    private Button btnContinuar;
    private ImageView imgSetaVoltar;
    private TextInputEditText txtNome;
    private TextInputEditText txtDataNasc;
    private TextInputEditText txtRendaFixa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_dados_pessoais);

        txtNome = findViewById(R.id.txt_edit_nome);
        txtDataNasc = findViewById(R.id.txt_edit_data_nasc);
        txtRendaFixa = findViewById(R.id.txt_edit_renda);

        btnContinuar = findViewById(R.id.btn_usuario_cadastro_continuar);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO conferir obrigatoriedade dos campos
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

    private boolean validate() {
        boolean isValid = true;
        if (txtNome.getText().toString().trim().isEmpty()) {
            txtNome.setError("Preencha o campo nome");
            isValid = false;
        } else {
            txtNome.setError(null);
        }

        return isValid;
    }
}
