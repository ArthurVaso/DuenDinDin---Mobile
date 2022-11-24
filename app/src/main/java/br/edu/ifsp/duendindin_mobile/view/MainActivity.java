package br.edu.ifsp.duendindin_mobile.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifsp.duendindin_mobile.R;

public class MainActivity extends AppCompatActivity {

    private Button btnEntrar;
    private Button btnCadastrar;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        pref.edit().clear().commit();
        btnCadastrar = findViewById(R.id.btn_main_cadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UsuarioCadastroDadosPessoaisActivity.class);
                startActivity(intent);
            }
        });

        btnEntrar = findViewById(R.id.btn_main_entrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UsuarioEntrarActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //TODO tem certeza que deseja finalizar o sistema?
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("DuenDinDin");
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setMessage("Deseja mesmo encerrar a aplicação?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SIM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pref.edit().clear().commit();
                        System.exit(0);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NÃO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}