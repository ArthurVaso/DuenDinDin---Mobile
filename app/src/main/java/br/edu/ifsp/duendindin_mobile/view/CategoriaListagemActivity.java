package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.adapter.CategoriasAdapter;
import br.edu.ifsp.duendindin_mobile.model.Categoria;

public class CategoriaListagemActivity extends AppCompatActivity {

    RecyclerView rvCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_categoria);

        rvCategorias = findViewById(R.id.rv_categorias);
        ArrayList<Categoria> listCategorias = new ArrayList();
        Categoria c1 = new Categoria(1, 1, "Vaquinha", "Vou guardar para ajudar uma família no Natal.");
        Categoria c2 = new Categoria(2, 1, "Reserva de Emergência", "Essa é minha reserva de emergência :).");
        Categoria c3 = new Categoria(3, 1, "Viagem Final de Ano", "Conhecer alguma cidade nova no final de ano.");
        Categoria c4 = new Categoria(4, 1, "Meus Salários", "Onde deixo meu salário todo mês!");
        Categoria c5 = new Categoria(5, 1, "Meus Gastos", "Onde deixo meus gastos todo mês!");

        listCategorias.add(c1);
        listCategorias.add(c2);
        listCategorias.add(c3);
        listCategorias.add(c4);
        listCategorias.add(c5);

        rvCategorias.setLayoutManager(new LinearLayoutManager(this));
        CategoriasAdapter categoriasAdapter = new CategoriasAdapter(this.getLayoutInflater(), listCategorias);
        rvCategorias.setAdapter(categoriasAdapter);

        Button btnNovaCategoria = findViewById(R.id.btn_categoria_listagem_nova_categoria);
        btnNovaCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoriaListagemActivity.this, CategoriaCadastroActivity.class);
                startActivity(intent);
            }
        });
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
                } else if ("Vencimentos".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, GastoListagemActivity.class));
                } else if ("Recebimento".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, GanhoListagemActivity.class));
                } else if ("Perfil".equals(title)) {
                    startActivity(new Intent(CategoriaListagemActivity.this, UsuarioPerfilActivity.class));
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}