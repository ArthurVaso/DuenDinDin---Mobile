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

public class RecuperarSenhaNovaSenhaActivity extends AppCompatActivity {

    private Button btnFinalizar;
    private ImageView imgSetaVoltar;
    private TextInputEditText txtNovaSenha;
    private TextInputEditText txtConfirmaSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha_nova_senha);

        txtNovaSenha = findViewById(R.id.txt_edit_recuperar_senha_nova_senha);
        txtConfirmaSenha = findViewById(R.id.txt_edit_recuperar_senha_confirmar_senha);

        btnFinalizar = findViewById(R.id.btn_recuperar_senha_finalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (validate()) {
                    Toast.makeText(RecuperarSenhaNovaSenhaActivity.this, "Senha Redefinida com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecuperarSenhaNovaSenhaActivity.this, UsuarioEntrarActivity.class);
                    startActivity(intent);
                }

            }
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(RecuperarSenhaNovaSenhaActivity.this, RecuperarSenhaInformaCodigoActivity.class);
                startActivity(intent);


            }
        });

    }

    private boolean validate() {
        boolean isValid = true;
        if (txtNovaSenha.getText().toString().trim().isEmpty()) {
            txtNovaSenha.setError("Preencha o campo email");
            isValid = false;
        } else if (!txtNovaSenha.getText().toString().equals(txtConfirmaSenha.getText().toString())) {
            txtNovaSenha.setError("As senhas não correspondem");
            isValid = false;
        } else {
            txtNovaSenha.setError(null);
        }
        return isValid;
    }

}
