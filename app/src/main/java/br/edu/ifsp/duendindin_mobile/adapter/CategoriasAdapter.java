package br.edu.ifsp.duendindin_mobile.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.edu.ifsp.duendindin_mobile.R;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private ArrayList<String> categorias;
    private LayoutInflater inflater;


    public CategoriasAdapter(LayoutInflater inflater, ArrayList<String> categorias) {
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
        categoriasViewHolder.periodoRecorrencia = itemCategoria.findViewById(R.id.textView3);

        categoriasViewHolder.imgEdit = itemCategoria.findViewById(R.id.imageButton);
        categoriasViewHolder.imgDelete = itemCategoria.findViewById(R.id.imageButton2);

        return categoriasViewHolder;

    }

    @Override
    public void onBindViewHolder(CategoriasAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String categoria = categorias.get(position);
        holder.nome.setText(categoria+" - Fixo ou Variável");
        holder.descricao.setText("Descriçãoooooo");
        holder.periodoRecorrencia.setText("Periodo de recorrênciaaaaa");
        holder.position = position;

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "EDIT", Toast.LENGTH_SHORT).show();
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "DELETE", Toast.LENGTH_SHORT).show();
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
}
