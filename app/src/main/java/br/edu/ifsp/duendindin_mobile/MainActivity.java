package br.edu.ifsp.duendindin_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnEntrar;
    private Button btnCadastrar;

    private Button btnCategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCadastrar = findViewById(R.id.btn_main_cadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, UsuarioCadastroDadosPessoaisActivity.class);
                startActivity(intent);
            }
        });

        btnEntrar = findViewById(R.id.btn_main_entrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UsuarioCadastroDadosPessoaisActivity.class);
                startActivity(intent);
            }
        });

        btnCategoria = findViewById(R.id.btn_main_categoria);
        btnCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoriaCadastroActivity.class);
                startActivity(intent);
            }
        });

    }
}