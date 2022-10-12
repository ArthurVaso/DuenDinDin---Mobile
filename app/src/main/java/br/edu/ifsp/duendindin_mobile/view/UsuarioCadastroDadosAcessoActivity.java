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

        txtEmail = findViewById(R.id.txt_edit_email);
        txtSenha = findViewById(R.id.txt_edit_senha);

        btnCadastrar = findViewById(R.id.btn_usuario_cadastro_cadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (validate()) {
                    Toast.makeText(UsuarioCadastroDadosAcessoActivity.this, R.string.msg_cadastrado_sucesso, Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO conferir obrigatoriedade dos campos

                Intent intent = new Intent(UsuarioCadastroDadosAcessoActivity.this, UsuarioCadastroDadosPessoaisActivity.class);
                startActivity(intent);


            }
        });

    }

    private boolean validate() {
        boolean isValid = true;
        if (txtEmail.getText().toString().trim().isEmpty()) {
            txtEmail.setError("Preencha o campo email");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
            txtEmail.setError("Preencha um email válido");
            isValid = false;
        } else {
            txtEmail.setError(null);
        }
        if (txtSenha.getText().toString().trim().isEmpty()) {
            txtSenha.setError("Preencha o campo senha");
            isValid = false;
        } else {
            txtSenha.setError(null);
        }
        return isValid;
    }

}
