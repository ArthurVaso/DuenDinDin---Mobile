package br.edu.ifsp.duendindin_mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class UsuarioPerfilActivity extends AppCompatActivity {

    private ImageButton btnEditUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        btnEditUsuario = findViewById(R.id.img_usuario_perfil_edit);
        btnEditUsuario.setOnClickListener(view -> {
            //todo ir para a tela de atualizar perfil do usuário
        });
    }
}