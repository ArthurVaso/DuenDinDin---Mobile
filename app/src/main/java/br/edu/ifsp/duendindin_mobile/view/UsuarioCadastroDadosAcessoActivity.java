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

public class UsuarioCadastroDadosAcessoActivity extends AppCompatActivity {

    private Button btnCadastrar;
    private ImageView imgSetaVoltar;
    private TextInputEditText txtEmail;
    private TextInputEditText txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_dados_acesso);

        txtEmail = findViewById(R.id.txt_edit_usuario_cadastro_email);
        txtSenha = findViewById(R.id.txt_edit_senha);

        btnCadastrar = findViewById(R.id.btn_usuario_cadastro_cadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (validate()) {
                    Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, R.string.msg_cadastrado_sucesso, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UsuarioCadastroDadosAcessoActivity.this, UsuarioEntrarActivity.class);
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
            Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, "Preencha o campo Email", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtEmail.getText().toString().trim().length() > 30) {
            Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, "O campo Email n??o deve ter mais de 30 caracteres!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
            Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, "Informe um email v??lido", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtSenha.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, "O campo Senha n??o pode estar vazio!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (txtSenha.getText().toString().trim().length() > 50) {
            Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, "O campo Senha n??o deve ter mais de 50 caracteres!", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
