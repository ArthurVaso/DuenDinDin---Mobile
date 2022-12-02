package br.edu.ifsp.duendindin_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Categoria;
import br.edu.ifsp.duendindin_mobile.model.Usuario;

public class CategoriaAlterarActivity extends AppCompatActivity {

    private Button btnAlterar;
    private ImageView imgSetaVoltar;
    private TextInputEditText edtNome;
    private TextInputEditText edtDescricao;
    private Categoria categoriaListagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_categoria);
        edtNome = findViewById(R.id.edt_nome_categoria_alterar);
        edtDescricao = findViewById(R.id.edt_descricao_categoria_alterar);

        btnAlterar = findViewById(R.id.btn_categoria_cadastro_alterar);
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Intent intent = new Intent(CategoriaAlterarActivity.this, CategoriaListagemActivity.class);
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

        categoriaListagem = (Categoria) getIntent().getSerializableExtra("categoria");
        edtNome.setText(categoriaListagem.getNome());
        edtDescricao.setText(categoriaListagem.getDescricao());
    }

    private boolean validate() {
        boolean isValid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            Toast.makeText(CategoriaAlterarActivity.this, "Preencha o campo Nome", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}