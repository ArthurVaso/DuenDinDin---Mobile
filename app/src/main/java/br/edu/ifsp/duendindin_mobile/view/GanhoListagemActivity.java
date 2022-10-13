package br.edu.ifsp.duendindin_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.edu.ifsp.duendindin_mobile.R;

public class GanhoListagemActivity extends AppCompatActivity {

    private Button btnNovoRecebimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_ganho);

        btnNovoRecebimento = findViewById(R.id.btn_ganho_listagem_novo_recebimento);
        btnNovoRecebimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GanhoListagemActivity.this, GanhoCadastroActivity.class);
                startActivity(intent);
            }
        });
    }
}