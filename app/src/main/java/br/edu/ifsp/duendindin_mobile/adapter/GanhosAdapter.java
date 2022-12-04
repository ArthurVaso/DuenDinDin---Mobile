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
import br.edu.ifsp.duendindin_mobile.model.Ganho;
import br.edu.ifsp.duendindin_mobile.model.GanhoRetorno;
import br.edu.ifsp.duendindin_mobile.service.GanhoService;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
import br.edu.ifsp.duendindin_mobile.utils.CustomProgressDialog;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import br.edu.ifsp.duendindin_mobile.utils.URLAPI;
import br.edu.ifsp.duendindin_mobile.view.CategoriaAlterarActivity;
import br.edu.ifsp.duendindin_mobile.view.GanhoAlterarActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GanhosAdapter extends RecyclerView.Adapter<GanhosAdapter.ViewHolder> {

    private final String URL_API = new URLAPI().baseUrl;

    private ArrayList<Ganho> ganhos;
    private LayoutInflater inflater;

    private Context context;
    private Retrofit retrofitAPI;
    SharedPreferences pref;
    String token = "";
    int usuarioId = 0;

    public GanhosAdapter(LayoutInflater inflater, ArrayList<Ganho> ganhos) {
        this.inflater = inflater;
        this.ganhos = ganhos;
    }

    @Override
    public GanhosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemGanho = inflater.inflate(R.layout.ganho_item, parent, false);
        GanhosAdapter.ViewHolder ganhosViewHolder = new GanhosAdapter.ViewHolder(itemGanho);

        ganhosViewHolder.view = itemGanho;

        ganhosViewHolder.nome = itemGanho.findViewById(R.id.textView);
        ganhosViewHolder.descricao = itemGanho.findViewById(R.id.textView2);
        ganhosViewHolder.data = itemGanho.findViewById(R.id.textView3);

        ganhosViewHolder.imgEdit = itemGanho.findViewById(R.id.imageButton3);
        ganhosViewHolder.imgDelete = itemGanho.findViewById(R.id.imageButton4);

        context = parent.getContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);

        return ganhosViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull GanhosAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Ganho ganho = ganhos.get(position);
        String valor = String.format("%.2f", ganho.getValor());
        holder.nome.setText(ganho.getNome() + " - R$ "+ valor);
        holder.descricao.setText(ganho.getDescricao());
        String ganhoData = ganho.getData();
        String data[] = ganhoData.split("-");
        ganhoData = data[2] + "/" + data[1] + "/" + data[0];
        holder.data.setText(ganhoData);
        holder.position = position;


        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GanhoAlterarActivity.class);
                intent.putExtra("ganho", ganho);
                context.startActivity(intent);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(holder.view.getContext()).create();
                alertDialog.setTitle("DuenDinDin");
                alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                alertDialog.setMessage("Deseja mesmo excluir esse ganho?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SIM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                int position = ganhos.indexOf(ganho);
                                removerGanho(position);
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

    private void removerGanho(int position) {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                context,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        int ganhoId = ganhos.get(position).getId();
        //instanciando a interface
        GanhoService ganhoService = retrofitAPI.create(GanhoService.class);

        //passando os dados para o serviço
        Call<Message> call = ganhoService.deletarGanhoUsuario(token, ganhoId);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful()) {

                    if (response.code() == 200) {

                        new CustomMessageDialog(response.body().getMensagem(), context);
                        ganhos.remove(position);
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
                    new CustomMessageDialog("Ocorreu um erro ao excluir esse ganho.  \n" + msg.getMensagem(), context);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(context.getString(R.string.msg_erro_comunicacao_servidor), context);
                Log.d("GanhoCadastroActivity", t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ganhos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView nome, descricao, data;
        ImageButton imgEdit, imgDelete;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

