package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsp.duendindin_mobile.R;

public class CategoriaCadastroActivity extends AppCompatActivity {

    private Button btnSalvar;
    private ImageView imgSetaVoltar;
    private TextInputEditText edtNome;
    private TextInputEditText edtDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro_categoria);
        edtNome = findViewById(R.id.edt_nome_categoria);
        edtDescricao = findViewById(R.id.edt_descricao_categoria);

        btnSalvar = findViewById(R.id.btn_categoria_cadastro_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Intent intent = new Intent(CategoriaCadastroActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO conferir obrigatoriedade dos campos

                Intent intent = new Intent(CategoriaCadastroActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean validate() {
        boolean isValid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            edtNome.setError("");
            Toast.makeText(CategoriaCadastroActivity.this, "Preencha o campo nome", Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            edtNome.setError(null);
        }
        return isValid;
    }
}

