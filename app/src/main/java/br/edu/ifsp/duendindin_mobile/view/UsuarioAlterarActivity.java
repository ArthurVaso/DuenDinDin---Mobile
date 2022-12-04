package br.edu.ifsp.duendindin_mobile.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.CEP;
import br.edu.ifsp.duendindin_mobile.model.Usuario;
import br.edu.ifsp.duendindin_mobile.model.UsuarioComConfiguracao;
import br.edu.ifsp.duendindin_mobile.model.AtualizarUsuario;
import br.edu.ifsp.duendindin_mobile.service.CEPService;
import br.edu.ifsp.duendindin_mobile.service.UsuarioService;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
import br.edu.ifsp.duendindin_mobile.utils.CustomProgressDialog;
import br.edu.ifsp.duendindin_mobile.utils.Mascara;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import br.edu.ifsp.duendindin_mobile.utils.URLAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioAlterarActivity extends AppCompatActivity {

    private final String URL_API = new URLAPI().baseUrl;

    private UsuarioService _usuarioService;

    private Retrofit retrofitAPI;
    private SharedPreferences pref;
    private String token = "";
    private int usuarioId = 0;

    private Button btnContinuar;
    private ImageView imgSetaVoltar;
    private TextInputEditText edtNome;
    private TextView txtDataNasc;
    private TextView txtEsqueciCEP;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText edtCep;
    private TextInputEditText edtEstado;
    private TextInputEditText edtCidade;
    private TextInputEditText edtRendaFixa;

    private Usuario usuarioPerfil;
    private UsuarioComConfiguracao usuarioComConfiguracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_usuario);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        final Calendar c = Calendar.getInstance();
        int anoAtual = c.get(Calendar.YEAR);
        int mesAtual = c.get(Calendar.MONTH);
        int diaAtual = c.get(Calendar.DAY_OF_MONTH);
        edtNome = findViewById(R.id.edt_nome_usuario_alterar);
        edtCep = findViewById(R.id.edt_cep_usuario_alterar);
        edtEstado = findViewById(R.id.edt_estado_usuario_alterar);
        edtCidade = findViewById(R.id.edt_cidade_usuario_alterar);
        edtRendaFixa = findViewById(R.id.edt_renda_usuario_alterar);
        edtCep = findViewById(R.id.edt_cep_usuario_alterar);
        edtEstado = findViewById(R.id.edt_estado_usuario_alterar);
        edtCidade = findViewById(R.id.edt_cidade_usuario_alterar);

        txtDataNasc = findViewById(R.id.txt_data_nasc_usuario_alterar);
        txtDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(UsuarioAlterarActivity.this,
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

        txtEsqueciCEP = findViewById(R.id.txt_esqueci_cep_alterar);
        txtEsqueciCEP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri parsedUrl = Uri.parse("https://buscacepinter.correios.com.br/app/endereco/index.php");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(parsedUrl);
                startActivity(intent);
            }
        });

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

        btnContinuar = findViewById(R.id.btn_usuario_alterar_atualizar);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    atualizarUsuario();
                }
            }
        });

        imgSetaVoltar = findViewById(R.id.seta_voltar_alterar);
        imgSetaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);

        //instanciando a interface
        _usuarioService = retrofitAPI.create(UsuarioService.class);

        buscarUsuarioComConfiguracao();
    }

    private void buscarUsuarioComConfiguracao() {

        CustomProgressDialog progressDialog = new CustomProgressDialog(
                UsuarioAlterarActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        Call<UsuarioComConfiguracao> call = _usuarioService.consultarUsuarioComConfiguracao(token, usuarioId);
        call.enqueue(new Callback<UsuarioComConfiguracao>() {
            @Override
            public void onResponse(Call<UsuarioComConfiguracao> call, Response<UsuarioComConfiguracao> response) {
                if (response.isSuccessful()) {
                    usuarioComConfiguracao = response.body();

                    edtNome.setText(usuarioComConfiguracao.getNome());
                    String data[] = usuarioComConfiguracao.getDataNascimento().split("-");
                    String dataNasc = data[2] + "/" + data[1] + "/" + data[0];
                    txtDataNasc.setText(dataNasc);
                    String cep = usuarioComConfiguracao.getCep();
                    edtCep.setText(cep);
                    edtEstado.setText(usuarioComConfiguracao.getEstado());
                    edtCidade.setText(usuarioComConfiguracao.getCidade());
                    BigDecimal formatRendaFixa = new BigDecimal(usuarioComConfiguracao.getConfiguracao().getRendaFixa());
                    edtRendaFixa.setText(formatRendaFixa.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString().replace('.', ','));

                    progressDialog.dismiss();

                }else {
                    String errorBody = null;
                    Message msg = new Message();
                    try {
                        errorBody = response.errorBody().string();
                        Gson gson = new Gson(); // conversor
                        msg = gson.fromJson(errorBody, Message.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();

                    Log.d("UsuarioComConfiguracao", "Por favor, tente novamente mais tarde.");
                    new CustomMessageDialog("Ocorreu um erro ao consultar os dados do Usuário.  \n" + msg.getMensagem(), UsuarioAlterarActivity.this);
                }
            }

            @Override
            public void onFailure(Call<UsuarioComConfiguracao> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), UsuarioAlterarActivity.this);

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

    private void atualizarUsuario() {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                UsuarioAlterarActivity.this,
                "DuenDinDin",
                "Aguarde...",
                false
        );

        AtualizarUsuario atualizarUsuario = new AtualizarUsuario();

        atualizarUsuario.setNome(edtNome.getText().toString());

        String data[] = txtDataNasc.getText().toString().split("/");
        String dataNasc = data[2] + "-" + data[1] + "-" + data[0];

        atualizarUsuario.setDataNascimento(dataNasc);

        atualizarUsuario.setCep(edtCep.getText().toString().replaceAll("[.-]+", ""));

        atualizarUsuario.setEstado(edtEstado.getText().toString());

        atualizarUsuario.setCidade(edtCidade.getText().toString());

        atualizarUsuario.setRendaFixa(Double.parseDouble(edtRendaFixa.getText().toString()));


        Call<Message> call = _usuarioService.atualizarUsuario(token, usuarioId, atualizarUsuario);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful()) {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.body().getMensagem(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(UsuarioAlterarActivity.this, UsuarioPerfilActivity.class);
                    startActivity(intent);

                } else {

                    String errorBody = null;
                    Message msg = new Message();
                    try {
                        errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    new CustomMessageDialog("Ocorreu um erro ao realizar a atualização.  \n" + errorBody, UsuarioAlterarActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(getString(R.string.msg_erro_comunicacao_servidor), UsuarioAlterarActivity.this);

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
        if (idade < 10) {
            txtDataNasc.setText("");
            Toast.makeText(UsuarioAlterarActivity.this, R.string.msg_data_nasc_invalida, Toast.LENGTH_LONG).show();
            return;
        }
        txtDataNasc.setText(diaS + "/" + (mesS + 1) + "/" + anoS);

    }

    private boolean validate() {
        boolean isValid = true;

        isValid = validateCep(); // chamando a validação do cep

        if (edtNome.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioAlterarActivity.this, "O campo Nome deve ser preenchido!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtNome.getText().toString().trim().length() > 30) {
            Toast.makeText(UsuarioAlterarActivity.this, "O campo Nome não deve ter mais de 30 caracteres!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtEstado.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioAlterarActivity.this, "O campo Estado deve ser preenchido!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtCidade.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioAlterarActivity.this, "O campo Cidade deve ser preenchido!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtRendaFixa.getText().toString().trim().isEmpty()) {
            Toast.makeText(UsuarioAlterarActivity.this, "O campo Renda Fixa deve ser preenchido!", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    private boolean validateCep() {
        boolean isValid = true;

        if (edtCep.getText().toString().trim().isEmpty()) {
            edtCep.setError("");
            Toast.makeText(UsuarioAlterarActivity.this, "Digite um CEP válido.", Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (edtCep.getText().toString().trim().length() != 10) {
            edtCep.setError("");
            Toast.makeText(UsuarioAlterarActivity.this, "O CEP deve possuir 8 dígitos!", Toast.LENGTH_LONG).show();
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
