package br.edu.ifsp.duendindin_mobile.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.adapter.CategoriasAdapter;

public class CategoriaListagemActivity extends AppCompatActivity {

    RecyclerView rvCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_categoria);

        rvCategorias = findViewById(R.id.rv_categorias);
        ArrayList<String> listCategorias = new ArrayList();
        listCategorias.add("Pessoal");
        listCategorias.add("Salário");
        listCategorias.add("Viagem");

        rvCategorias.setLayoutManager(new LinearLayoutManager(this));
        CategoriasAdapter categoriasAdapter = new CategoriasAdapter(this.getLayoutInflater(), listCategorias);
        rvCategorias.setAdapter(categoriasAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bnvCategoria = findViewById(R.id.bnv_categoria);
        bnvCategoria.setSelectedItemId(R.id.bottom_nav_menu_category);
        bnvCategoria.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                if ("Home".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, HomeActivity.class));
                } else if ("Calendário".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, GastoListagemActivity.class));
                } else if ("Recebimentos".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, GanhoListagemActivity.class));
                } else if ("Perfil".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, UsuarioPerfilActivity.class));
                } else {
                    throw new IllegalStateException("Unexpected value: " + bnvCategoria.getSelectedItemId());
                }
                return false;
            }
        });
    }
}