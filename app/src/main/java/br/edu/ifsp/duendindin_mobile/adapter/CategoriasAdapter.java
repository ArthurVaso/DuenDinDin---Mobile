package br.edu.ifsp.duendindin_mobile.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Categoria;
import br.edu.ifsp.duendindin_mobile.service.CategoriaService;
import br.edu.ifsp.duendindin_mobile.utils.CustomMessageDialog;
import br.edu.ifsp.duendindin_mobile.utils.CustomProgressDialog;
import br.edu.ifsp.duendindin_mobile.utils.Message;
import br.edu.ifsp.duendindin_mobile.utils.URLAPI;
import br.edu.ifsp.duendindin_mobile.view.CategoriaAlterarActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private final String URL_API = new URLAPI().baseUrl;

    private ArrayList<Categoria> categorias;
    private LayoutInflater inflater;


    private Context context;
    private Retrofit retrofitAPI;
    SharedPreferences pref;
    String token = "";
    int usuarioId = 0;

    public CategoriasAdapter(LayoutInflater inflater, ArrayList<Categoria> categorias) {
        this.inflater = inflater;
        this.categorias = categorias;
    }

    @Override
    public CategoriasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemCategoria = inflater.inflate(R.layout.category_item, parent, false);
        CategoriasAdapter.ViewHolder categoriasViewHolder = new CategoriasAdapter.ViewHolder(itemCategoria);

        categoriasViewHolder.view = itemCategoria;

        categoriasViewHolder.nome = itemCategoria.findViewById(R.id.textView);
        categoriasViewHolder.descricao = itemCategoria.findViewById(R.id.textView2);

        categoriasViewHolder.imgEdit = itemCategoria.findViewById(R.id.imageButton);
        categoriasViewHolder.imgDelete = itemCategoria.findViewById(R.id.imageButton2);

        context = parent.getContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        retrofitAPI = new Retrofit.Builder()
                .baseUrl(URL_API)                                //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        token = pref.getString("token", "");
        usuarioId = pref.getInt("usuarioId", 0);

        return categoriasViewHolder;

    }

    @Override
    public void onBindViewHolder(CategoriasAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Categoria categoria = categorias.get(position);
        holder.nome.setText(categoria.getNome());
        holder.descricao.setText(categoria.getDescricao());
        holder.position = position;

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoriaAlterarActivity.class);
                intent.putExtra("categoria", categoria);
                context.startActivity(intent);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(holder.view.getContext()).create();
                alertDialog.setTitle("DuenDinDin");
                alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                alertDialog.setMessage("Deseja mesmo excluir essa categoria?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SIM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                int position = categorias.indexOf(categoria);
                                removerCategoria(position);
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
        return categorias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView nome, descricao, periodoRecorrencia;
        ImageButton imgEdit, imgDelete;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

    private void removerCategoria(int position) {
        CustomProgressDialog progressDialog = new CustomProgressDialog(
                context,
                "DuenDinDin",
                "Aguarde...",
                false
        );
        progressDialog.show();

        int categoriaId = categorias.get(position).getId();
        CategoriaService categoriaService = retrofitAPI.create(CategoriaService.class);

        Call<Message> call = categoriaService.deletarCategoriaUsuario(token, usuarioId, categoriaId);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful()) {

                    if (response.code() == 200) {

                        new CustomMessageDialog(response.body().getMensagem(), context);
                        categorias.remove(position);
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
                    new CustomMessageDialog("Ocorreu um erro ao excluir essa categoria.  \n" + msg.getMensagem(), context);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                new CustomMessageDialog(context.getString(R.string.msg_erro_comunicacao_servidor), context);
            }
        });
    }

}
