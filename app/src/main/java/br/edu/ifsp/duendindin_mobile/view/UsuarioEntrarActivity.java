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

public class UsuarioEntrarActivity extends AppCompatActivity {

    private Button btnEntrar;
    private ImageView imgSetaVoltar;
    private TextInputEditText txtEmail;
    private TextInputEditText txtSenha;
    private TextView txtEsqueceuSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_entrar);

        txtEmail = findViewById(R.id.txt_edit_usuario_entrar_email);
        txtSenha = findViewById(R.id.txt_edit_usuario_entrar_senha);

        btnEntrar = findViewById(R.id.btn_usuario_entrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (validate()) {
                    Toast.makeText(UsuarioEntrarActivity.this, "login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UsuarioEntrarActivity.this, HomeActivity.class);
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

        txtEsqueceuSenha = findViewById(R.id.txt_esqueceu_senha);
        txtEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UsuarioEntrarActivity.this, RecuperarSenhaEnviaEmailActivity.class);
                startActivity(intent);


            }
        });

    }

    private boolean validate() {
        boolean isValid = true;
        if (txtEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioEntrarActivity.this, "Preencha o campo Email!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
            Toast.makeText(UsuarioEntrarActivity.this, "Informe um Email válido!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtSenha.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioEntrarActivity.this, "Preencha o campo Senha!", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
