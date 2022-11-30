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
import br.edu.ifsp.duendindin_mobile.adapter.GastosAdapter;

public class GastoListagemActivity extends AppCompatActivity {

    RecyclerView rvGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_gasto);

        rvGastos = findViewById(R.id.rv_gasto);
        ArrayList<String> listGastos = new ArrayList();
        listGastos.add("Academia");
        listGastos.add("Presente");
        listGastos.add("Uber");

        rvGastos.setLayoutManager(new LinearLayoutManager(this));
        GastosAdapter gastosAdapter = new GastosAdapter(this.getLayoutInflater(), listGastos);
        rvGastos.setAdapter(gastosAdapter);

        Button btnNovoVencimento = findViewById(R.id.btn_gasto_listagem_novo_vencimento);
        btnNovoVencimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GastoListagemActivity.this, GastoCadastroActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bnvGasto = findViewById(R.id.bnv_gasto);
        bnvGasto.setSelectedItemId(R.id.bottom_nav_menu_gastos);
        bnvGasto.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                if ("Home".equals(title)) {
                    startActivity(new Intent(GastoListagemActivity.this, HomeActivity.class));
                } else if ("Recebimento".equals(title)) {
                    startActivity(new Intent(GastoListagemActivity.this, GanhoListagemActivity.class));
                } else if("Categorias".equals(title)){
                    startActivity(new Intent(GastoListagemActivity.this, CategoriaListagemActivity.class));
                } else if ("Perfil".equals(title)) {
                    startActivity(new Intent(GastoListagemActivity.this, UsuarioPerfilActivity.class));
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