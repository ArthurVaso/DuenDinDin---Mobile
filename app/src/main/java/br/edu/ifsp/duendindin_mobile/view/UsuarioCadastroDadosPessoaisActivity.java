package br.edu.ifsp.duendindin_mobile.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.model.UsuarioComToken;
import br.edu.ifsp.duendindin_mobile.service.CEPService;
import br.edu.ifsp.duendindin_mobile.model.CEP;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
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
    private Usuario usuario = new Usuario();
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
        Calendar dataSelecionada = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                dataSelecionada.set(Calendar.YEAR, ano);
                dataSelecionada.set(Calendar.MONTH, mes);
                dataSelecionada.set(Calendar.DAY_OF_MONTH, dia);

                updateCalendar(dia, mes, ano);
            }

            private void updateCalendar(int diaS, int mesS, int anoS) {
                int idade = 0;
                idade = anoAtual - anoS;
                if (mesS > mesAtual) {
                    idade--;
                } else if (mesAtual == mesS) {
                    if (diaS > diaAtual) {
                        idade--;
                    }
                }
                if (idade < 18) {
                    txtDataNasc.setText("");
                    new CustomMessageDialog(getString(R.string.msg_data_nasc_invalida), UsuarioCadastroDadosPessoaisActivity.this);
                    return;
                }
                String Format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.getDefault());
                txtDataNasc.setText(sdf.format(dataSelecionada.getTime()));
            }
        };
        txtDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UsuarioCadastroDadosPessoaisActivity.this, date,
                        dataSelecionada.get(Calendar.YEAR),
                        dataSelecionada.get(Calendar.MONTH),
                        dataSelecionada.get(Calendar.DAY_OF_MONTH))
                        .show();
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
                    usuario.setNome(edtNome.getText().toString());
                    String data[] = txtDataNasc.getText().toString().split("/");
                    String dataNasc = data[2] + "-" + data[1] + "-" + data[0];
                    usuario.setDataNascimento(dataNasc);
                    usuario.setCep(edtCep.getText().toString().replaceAll("[.-]+", ""));
                    usuario.setEstado(edtEstado.getText().toString());
                    usuario.setCidade(edtCidade.getText().toString());
                    usuario.setRendaFixa(Double.parseDouble(edtRendaFixa.getText().toString().replaceAll(",", ".")));
                    Intent intent = new Intent(UsuarioCadastroDadosPessoaisActivity.this, UsuarioCadastroDadosAcessoActivity.class);
                    intent.putExtra("Usuario", usuario);
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
                    if (response.code() == 200 && response.body().getUf() != null) { //OK - CEP EXISTE
                        CEP cep = response.body();
                        edtEstado.setText(cep.getUf());
                        edtCidade.setText(cep.getLocalidade());
                        Toast.makeText(getApplicationContext(), "CEP consultado com sucesso!", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 200 && response.body().getUf() == null) { // OK - CEP NÃO EXISTE
                        Toast.makeText(getApplicationContext(), "CEP inválido! Digite um CEP existente!", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 400) { //BAD REQUEST - CODE 400
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro ao consultar o CEP informado.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro ao consultar o CEP.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                new CustomMessageDialog("Ocorreu um erro ao tentar consultar o CEP. \nErro: " + t.getMessage(), UsuarioCadastroDadosPessoaisActivity.this);
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;

        //isValid = validateCep();
        if (edtNome.getText().toString().trim().isEmpty()) {
            new CustomMessageDialog("O campo Nome deve ser preenchido!", UsuarioCadastroDadosPessoaisActivity.this);
            isValid = false;
        }  else if (edtNome.getText().toString().trim().length() > 30) {
            new CustomMessageDialog("O campo Nome não deve ter mais de 30 caracteres!", UsuarioCadastroDadosPessoaisActivity.this);
            isValid = false;
        } else if (txtDataNasc.getText().toString().trim().isEmpty() || txtDataNasc.getText().toString().trim().length() < 10) {
            new CustomMessageDialog("O campo Data de Nascimento deve ser preenchido!", UsuarioCadastroDadosPessoaisActivity.this);
            isValid = false;
        } else if (edtEstado.getText().toString().trim().isEmpty() || edtCidade.getText().toString().trim().isEmpty()) {
            new CustomMessageDialog("Digite um CEP existente para que o Estado e a Cidade sejam preenchidos!", UsuarioCadastroDadosPessoaisActivity.this);
            isValid = false;
        } else if (isValid) {
            isValid = validateCep(); // chamando a validação do cep
        }

        return isValid;
    }

    private boolean validateCep() {
        boolean isValid = true;

        if (edtCep.getText().toString().trim().isEmpty()) {
            edtCep.setError("");
            new CustomMessageDialog("Digite um CEP válido!", UsuarioCadastroDadosPessoaisActivity.this);
            isValid = false;
        } else if (edtCep.getText().toString().trim().length() != 10) {
            edtCep.setError("");
            new CustomMessageDialog("O CEP deve possuir 8 dígitos!", UsuarioCadastroDadosPessoaisActivity.this);
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
