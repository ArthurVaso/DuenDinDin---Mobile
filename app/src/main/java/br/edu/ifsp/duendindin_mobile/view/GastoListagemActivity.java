package br.edu.ifsp.duendindin_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.edu.ifsp.duendindin_mobile.R;

public class GastoListagemActivity extends AppCompatActivity {

    private Button btnNovoVencimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_gasto);

        btnNovoVencimento = findViewById(R.id.btn_gasto_listagem_novo_vencimento);
        btnNovoVencimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GastoListagemActivity.this, GastoCadastroActivity.class);
                startActivity(intent);
            }
        });
    }
}