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
import java.util.Date;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.adapter.GanhosAdapter;
import br.edu.ifsp.duendindin_mobile.model.Ganho;

public class GanhoListagemActivity extends AppCompatActivity {

    RecyclerView rvGanhos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_ganho);

        rvGanhos = findViewById(R.id.rv_ganhos);
        ArrayList<Ganho> listGanhos = new ArrayList();
        Ganho g1 = new Ganho(1, 4, "Salário Outubro", new Date(2022, 10, 5), 1000.00, "Esse é meu salário de outubro", true, true);
        Ganho g2 = new Ganho(2, 1, "Presente do meu avô", new Date(2022, 11, 15), 50.00, "Meu avô me ajudou na vaquinha", false, false);
        Ganho g3 = new Ganho(3, 2, "Freela Tio", new Date(2022, 10, 27), 500.00, "Fiz um Freela pra empresa do meu tio", false, true);
        listGanhos.add(g1);
        listGanhos.add(g2);
        listGanhos.add(g3);

        rvGanhos.setLayoutManager(new LinearLayoutManager(this));
        GanhosAdapter ganhosAdapter = new GanhosAdapter(this.getLayoutInflater(), listGanhos);
        rvGanhos.setAdapter(ganhosAdapter);

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
        bnvGanho.setSelectedItemId(R.id.bottom_nav_menu_ganhos);
        bnvGanho.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                if ("Home".equals(title)) {
                    startActivity(new Intent(GanhoListagemActivity.this, HomeActivity.class));
                } else if ("Vencimentos".equals(title)) {
                    startActivity(new Intent(GanhoListagemActivity.this, GastoListagemActivity.class));
                } else if("Categorias".equals(title)){
                    startActivity(new Intent(GanhoListagemActivity.this, CategoriaListagemActivity.class));
                } else if ("Perfil".equals(title)) {
                    startActivity(new Intent(GanhoListagemActivity.this, UsuarioPerfilActivity.class));
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