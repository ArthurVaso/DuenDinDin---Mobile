package br.edu.ifsp.duendindin_mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.edu.ifsp.duendindin_mobile.R;

public class UsuarioPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        ImageButton btnEditUsuario = findViewById(R.id.img_usuario_perfil_edit);
        btnEditUsuario.setOnClickListener(view -> {
            startActivity(new Intent(UsuarioPerfilActivity.this, UsuarioCadastroDadosPessoaisActivity.class));
        });

        TextView txtRedefinir = findViewById(R.id.msg_usuario_perfil_esqueceu_sua_senha);
        txtRedefinir.setOnClickListener(view -> {
            startActivity(new Intent(UsuarioPerfilActivity.this, RecuperarSenhaEnviaEmailActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bnvPerfil = findViewById(R.id.bnv_perfil);
        bnvPerfil.setSelectedItemId(R.id.bottom_nav_menu_profile);
        bnvPerfil.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                if ("Home".equals(title)) {
                    startActivity(new Intent(UsuarioPerfilActivity.this, HomeActivity.class));
                } else if ("Calendário".equals(title)) {
                    startActivity(new Intent(UsuarioPerfilActivity.this, GastoListagemActivity.class));
                } else if ("Recebimentos".equals(title)) {
                    startActivity(new Intent(UsuarioPerfilActivity.this, GanhoListagemActivity.class));
                } else if ("Categorias".equals(title)) {
                    startActivity(new Intent(UsuarioPerfilActivity.this, CategoriaListagemActivity.class));
                } else {
                    throw new IllegalStateException("Unexpected value: " + bnvPerfil.getSelectedItemId());
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