package br.edu.ifsp.duendindin_mobile.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.service.CEPService;
import br.edu.ifsp.duendindin_mobile.model.CEP;
import br.edu.ifsp.duendindin_mobile.utils.Mascara;
import br.edu.ifsp.duendindin_mobile.utils.URLAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioCadastroDadosPessoaisActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private Button btnContinuar;
    private ImageView imgSetaVoltar;
    private TextInputEditText edtNome;
    private TextInputEditText edtCep;
    private TextInputEditText edtEstado;
    private TextInputEditText edtCidade;
    private TextView txtDataNasc;
    private TextView txtEsqueciCEP;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText edtRendaFixa;

    private Retrofit retrofitAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_dados_pessoais);
        final Calendar c = Calendar.getInstance();
        int anoAtual = c.get(Calendar.YEAR);
        int mesAtual = c.get(Calendar.MONTH);
        int diaAtual = c.get(Calendar.DAY_OF_MONTH);
        edtNome = findViewById(R.id.edt_nome_usuario);
        edtRendaFixa = findViewById(R.id.edt_renda_usuario);
        txtDataNasc = findViewById(R.id.txt_data_nasc_usuario);
        txtDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(UsuarioCadastroDadosPessoaisActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int ano,
                                                  int mes, int dia) {
                                Calendar dataSelecionada = Calendar.getInstance();
                                dataSelecionada.set(ano, mes, dia);
                                calcularIdade(dia, mes, ano, diaAtual, mesAtual, anoAtual);
                            }
                        }, anoAtual, mesAtual, diaAtual);
                datePickerDialog.show();
            }
        });

        txtEsqueciCEP = findViewById(R.id.txt_esqueci_cep);
        txtEsqueciCEP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri parsedUrl = Uri.parse("https://buscacepinter.correios.com.br/app/endereco/index.php");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(parsedUrl);
                startActivity(intent);
            }
        });

        edtCep = findViewById(R.id.edt_cep);
        edtEstado = findViewById(R.id.edt_estado_usuario);
        edtEstado.setKeyListener(null);
        edtCidade = findViewById(R.id.edt_cidade_usuario);
        edtCidade.setKeyListener(null);

        //Aplicando a máscara para CEP
        edtCep.addTextChangedListener(Mascara.insert(Mascara.MASCARA_CEP, edtCep));

        //configura os recursos do retrofit
        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        edtCep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (validateCep()) {
                        consultarCEP();
                    }
                }
            }
        });

        btnContinuar = findViewById(R.id.btn_usuario_cadastro_continuar);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Intent intent = new Intent(UsuarioCadastroDadosPessoaisActivity.this, UsuarioCadastroDadosAcessoActivity.class);
                    startActivity(intent);
                }
            }
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

    }

    private void consultarCEP() {
        String sCep = edtCep.getText().toString().trim();

        //removendo o ponto e o traço do padrão CEP
        sCep = sCep.replaceAll("[.-]+", "");
        //instanciando a interface
        CEPService CEPService = retrofitAPI.create(CEPService.class);
        //passando os dados para consulta
        Call<CEP> call = CEPService.consultarCEP(sCep);

        //colocando a requisição na fila para execução
        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(@NonNull Call<CEP> call, @NonNull Response<CEP> response) {
                if (response.isSuccessful()) {
                    if (response.code()==200 && response.body().getUf() != null) { //OK - CEP EXISTE
                        CEP cep = response.body();
                        edtEstado.setText(cep.getUf());
                        edtCidade.setText(cep.getLocalidade());
                        Toast.makeText(getApplicationContext(), "CEP consultado com sucesso", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 200 && response.body().getUf() == null) { // OK - CEP NÃO EXISTE
                        Toast.makeText(getApplicationContext(), "CEP inválido! Tente novamente.", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 400) { //BAD REQUEST - CODE 400
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o CEP.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Ocorreu outro erro ao tentar consultar o CEP.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o CEP. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void calcularIdade(int diaS, int mesS, int anoS, int diaAtual, int mesAtual, int anoAtual) {
        int idade = 0;
        idade = anoAtual - anoS;
        if (mesS > mesAtual) {
            idade--;
        } else if (mesAtual == mesS) {
            if (diaS > diaAtual) {
                idade--;
            }
        }
        if (idade > 110 || idade < 10) {
            txtDataNasc.setText("");
            Toast.makeText(UsuarioCadastroDadosPessoaisActivity.this, R.string.msg_data_nasc_invalida, Toast.LENGTH_LONG).show();
            return;
        }
        txtDataNasc.setText(diaS + "/" + (mesS + 1) + "/" + anoS);

    }

    private boolean validate() {
        boolean isValid = true;

        isValid = validateCep(); // chamando a validação do cep

        if (edtNome.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioCadastroDadosPessoaisActivity.this, "O campo Nome deve ser preenchido!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtNome.getText().toString().trim().length() > 30) {
            Toast.makeText(UsuarioCadastroDadosPessoaisActivity.this, "O campo Nome não deve ter mais de 30 caracteres!", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    private boolean validateCep() {
        boolean isValid = true;

        if (edtCep.getText().toString().trim().isEmpty()) {
            edtCep.setError("");
            Toast.makeText(UsuarioCadastroDadosPessoaisActivity.this, "Digite um CEP válido.", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtCep.getText().toString().trim().length() != 10) {
            edtCep.setError("");
            Toast.makeText(UsuarioCadastroDadosPessoaisActivity.this, "O CEP deve possuir 8 dígitos!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            edtCep.setError(null);
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
