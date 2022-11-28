package br.edu.ifsp.duendindin_mobile.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Categoria;
import br.edu.ifsp.duendindin_mobile.view.CategoriaAlterarActivity;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private ArrayList<Categoria> categorias;
    private LayoutInflater inflater;


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
                Intent intent = new Intent(holder.view.getContext(), CategoriaAlterarActivity.class);
                intent.putExtra("categoria", categoria);
                holder.view.getContext().startActivity(intent);
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
                                categorias.remove(position);
                                notifyItemRemoved(position);
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
}
