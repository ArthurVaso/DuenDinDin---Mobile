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
import br.edu.ifsp.duendindin_mobile.adapter.GastosAdapter;
import br.edu.ifsp.duendindin_mobile.model.Gasto;

public class GastoListagemActivity extends AppCompatActivity {

    RecyclerView rvGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_gasto);

        rvGastos = findViewById(R.id.rv_gasto);
        ArrayList<Gasto> listGastos = new ArrayList();

        Gasto g1 = new Gasto(1, 5, "Academia Outubro", true, new Date(2022, 10, 30), 100.00, "Paguei a academia de outubro.", false, true);
        Gasto g2 = new Gasto(2, 5, "Presente Cunhado", false, new Date(2022, 11, 19), 120.00, "Comprei o presente do meu cunhado.", true, false);
        Gasto g3 = new Gasto(3, 5, "Uber", false, new Date(2022, 11, 27), 30.00, "Uber ida e volta pra academia.", false, true);
        listGastos.add(g1);
        listGastos.add(g2);
        listGastos.add(g3);

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