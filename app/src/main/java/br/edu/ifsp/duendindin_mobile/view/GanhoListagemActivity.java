package br.edu.ifsp.duendindin_mobile.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.edu.ifsp.duendindin_mobile.R;

public class GanhoListagemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_ganho);

        Button btnNovoRecebimento = findViewById(R.id.btn_ganho_listagem_novo_recebimento);
        btnNovoRecebimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GanhoListagemActivity.this, GanhoCadastroActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bnvGanho = findViewById(R.id.bnv_ganho);
        bnvGanho.setSelectedItemId(R.id.bottom_nav_menu_recebimentos);
        bnvGanho.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                if ("Home".equals(title)) {
                    startActivity(new Intent(GanhoListagemActivity.this, HomeActivity.class));
                } else if ("Calendário".equals(title)) {
                    startActivity(new Intent(GanhoListagemActivity.this, GastoListagemActivity.class));
                } else if ("Categorias".equals(title)) {
                    startActivity(new Intent(GanhoListagemActivity.this, CategoriaListagemActivity.class));
                } else if ("Perfil".equals(title)) {
                    startActivity(new Intent(GanhoListagemActivity.this, UsuarioPerfilActivity.class));
                } else {
                    throw new IllegalStateException("Unexpected value: " + bnvGanho.getSelectedItemId());
                }
                return false;
            }
        });
    }

}