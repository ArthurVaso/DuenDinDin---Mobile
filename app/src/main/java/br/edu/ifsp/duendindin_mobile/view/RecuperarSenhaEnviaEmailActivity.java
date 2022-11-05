package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsp.duendindin_mobile.R;

public class RecuperarSenhaEnviaEmailActivity extends AppCompatActivity {

    private Button btnEnviar;
    private ImageView imgSetaVoltar;
    private TextInputEditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha_envia_email);

        txtEmail = findViewById(R.id.txt_edit_recuperar_senha_email);

        btnEnviar = findViewById(R.id.btn_recuperar_senha_enviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (validate()) {
                    Toast.makeText(RecuperarSenhaEnviaEmailActivity.this, "E-mail enviado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecuperarSenhaEnviaEmailActivity.this, RecuperarSenhaInformaCodigoActivity.class);
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
        if (txtEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(RecuperarSenhaEnviaEmailActivity.this, "Preencha o campo Email", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
            Toast.makeText(RecuperarSenhaEnviaEmailActivity.this, "Informe um Email válido", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
