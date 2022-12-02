package br.edu.ifsp.duendindin_mobile.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Gasto;
import br.edu.ifsp.duendindin_mobile.service.GastoService;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
import br.edu.ifsp.duendindin_mobile.utils.CustomProgressDialog;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import br.edu.ifsp.duendindin_mobile.utils.URLAPI;
import br.edu.ifsp.duendindin_mobile.view.GastoAlterarActivity;
import br.edu.ifsp.duendindin_mobile.view.GastoListagemActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GastosAdapter extends RecyclerView.Adapter<GastosAdapter.ViewHolder> {

    private final String URL_API = new URLAPI().baseUrl;

    private ArrayList<Gasto> gastos;
    private LayoutInflater inflater;

    private Context context;
    private Retrofit retrofitAPI;
    SharedPreferences pref;
    String token = "";
    int usuarioId = 0;

    public GastosAdapter(LayoutInflater inflater, ArrayList<Gasto> gastos) {
        this.inflater = inflater;
        this.gastos = gastos;
    }

    @Override
    public GastosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemGasto = inflater.inflate(R.layout.gasto_item, parent, false);
        GastosAdapter.ViewHolder gastosViewHolder = new GastosAdapter.ViewHolder(itemGasto);

        gastosViewHolder.view = itemGasto;

        gastosViewHolder.nome = itemGasto.findViewById(R.id.textView);
        gastosViewHolder.descricao = itemGasto.findViewById(R.id.textView2);
        gastosViewHolder.data = itemGasto.findViewById(R.id.textView3);

        gastosViewHolder.cbxPago = itemGasto.findViewById(R.id.checkBox);

        gastosViewHolder.imgEdit = itemGasto.findViewById(R.id.imageButton5);
        gastosViewHolder.imgDelete = itemGasto.findViewById(R.id.imageButton6);

        context = parent.getContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);

        return gastosViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull GastosAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Gasto gasto = gastos.get(position);

        String valor = String.format( "%.2f", gasto.getValor());
        holder.nome.setText(gasto.getNome()+" - R$ "+ valor);
        holder.descricao.setText(gasto.getDescricao());
        holder.cbxPago.setChecked(gasto.getPago());

        holder.cbxPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cbxPago.isChecked()){
                    pagarGasto(position, true);
                } else {
                    pagarGasto(position, false);
                }
            }
        });

        String gastoVencimento = gasto.getVencimento();
        String data[] = gastoVencimento.split("-");
        gastoVencimento = data[2] + "/" + data[1] + "/" + data[0];
        holder.data.setText(gastoVencimento);
        holder.position = position;

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.view.getContext(), GastoAlterarActivity.class);
                intent.putExtra("gasto", gasto);
                holder.view.getContext().startActivity(intent);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(holder.view.getContext()).create();
                alertDialog.setTitle("DuenDinDin");
                alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                alertDialog.setMessage("Deseja mesmo excluir esse gasto?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SIM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                int position = gastos.indexOf(gasto);
                                removerGasto(position);
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
        });
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView nome, descricao, data;
        CheckBox cbxPago;
        ImageButton imgEdit, imgDelete;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void removerGasto(int position) {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                context,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        int gastoId = gastos.get(position).getId();
        //instanciando a interface
        GastoService gastoService = retrofitAPI.create(GastoService.class);

        //passando os dados para o serviço
        Call<Message> call = gastoService.deletarGastoUsuario(token, gastoId);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful()) {

                    if (response.code() == 200) {

                        new CustomMessageDialog(response.body().getMensagem(), context);
                        gastos.remove(position);
                        notifyItemRemoved(position);
                        //Toast.makeText(getApplicationContext(), "Categorias retornadas com sucesso!", Toast.LENGTH_LONG).show();

                    } else {
                        new CustomMessageDialog(response.body().getMensagem(), context);
                    }

                    progressDialog.dismiss();
                } else {

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
                    new CustomMessageDialog("Ocorreu um erro ao excluir esse gasto.  \n" + msg.getMensagem(), context);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(context.getString(R.string.msg_erro_comunicacao_servidor), context);
                Log.d("GastoCadastroActivity", t.getMessage());
            }
        });
    }

    private void pagarGasto(int position, boolean pago) {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                context,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        Gasto gasto = gastos.get(position);
        gasto.setPago(pago);
        //instanciando a interface
        GastoService gastoService = retrofitAPI.create(GastoService.class);

        //passando os dados para o serviço
        Call<Message> call = gastoService.atualizarGastoUsuario(token, gasto.getId(), gasto);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful()) {

                    if (response.code() == 200) {
                        new CustomMessageDialog(response.body().getMensagem(), context);
                        //Toast.makeText(context, response.body().getMensagem(), Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();


                    } else {
                        new CustomMessageDialog(response.body().getMensagem(), context);
                    }

                    progressDialog.dismiss();
                } else {

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
                    new CustomMessageDialog("Ocorreu um erro ao excluir esse gasto.  \n" + msg.getMensagem(), context);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(context.getString(R.string.msg_erro_comunicacao_servidor), context);
                Log.d("GastoCadastroActivity", t.getMessage());
            }
        });
    }
}

