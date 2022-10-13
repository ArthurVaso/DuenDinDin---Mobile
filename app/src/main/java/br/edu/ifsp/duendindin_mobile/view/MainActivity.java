package br.edu.ifsp.duendindin_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.edu.ifsp.duendindin_mobile.R;

public class MainActivity extends AppCompatActivity {

    private Button btnEntrar;
    private Button btnCadastrar;

    private Button btnCategoria;
    private Button btnGasto;
    private Button btnGanho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCadastrar = findViewById(R.id.btn_main_cadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                aaa
                Intent intent = new Intent(MainActivity.this, UsuarioCadastroDadosPessoaisActivity.class);
                startActivity(intent);
            }
        });

        btnEntrar = findViewById(R.id.btn_main_entrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GastoCadastroActivity.class);
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

        btnGasto = findViewById(R.id.btn_main_gasto);
        btnGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GastoCadastroActivity.class);
                startActivity(intent);
            }
        });

        btnGanho = findViewById(R.id.btn_main_ganho);
        btnGanho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GanhoCadastroActivity.class);
                startActivity(intent);
            }
        });

    }
}