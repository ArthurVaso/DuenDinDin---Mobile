package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsp.duendindin_mobile.R;

public class RecuperarSenhaInformaCodigoActivity extends AppCompatActivity {

    private Button btnProximo;
    private ImageView imgSetaVoltar;
    private TextInputEditText txtCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha_informa_codigo);

        txtCodigo = findViewById(R.id.txt_edit_recuperar_senha_codigo);

        btnProximo = findViewById(R.id.btn_recuperar_senha_proximo);
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (validate()) {
                    Toast.makeText(RecuperarSenhaInformaCodigoActivity.this, "Código validado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecuperarSenhaInformaCodigoActivity.this, RecuperarSenhaNovaSenhaActivity.class);
                    startActivity(intent);
                }

            }
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private boolean validate() {
        boolean isValid = true;
        if (txtCodigo.getText().toString().trim().isEmpty()) {
            Toast.makeText(RecuperarSenhaInformaCodigoActivity.this, "Preencha o campo Email!", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
