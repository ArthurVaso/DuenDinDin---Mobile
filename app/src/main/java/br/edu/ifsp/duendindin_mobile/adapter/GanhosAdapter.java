package br.edu.ifsp.duendindin_mobile.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Ganho;
import br.edu.ifsp.duendindin_mobile.model.GanhoRetorno;
import br.edu.ifsp.duendindin_mobile.view.CategoriaAlterarActivity;
import br.edu.ifsp.duendindin_mobile.view.GanhoAlterarActivity;

public class GanhosAdapter extends RecyclerView.Adapter<GanhosAdapter.ViewHolder> {

    private ArrayList<Ganho> ganhos;
    private LayoutInflater inflater;

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

        return ganhosViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull GanhosAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Ganho ganho = ganhos.get(position);
        String valor = String.format( "%.2f", ganho.getValor());
        holder.nome.setText(ganho.getNome()+" - R$ "+ valor);
        holder.descricao.setText(ganho.getDescricao());
        String ganhoVencimento = ganho.getData();
        String data[] = ganhoVencimento.split("-");
        ganhoVencimento = data[2] + "/" + data[1] + "/" + data[0];
        holder.data.setText(ganhoVencimento);
        holder.position = position;


        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.view.getContext(), GanhoAlterarActivity.class);
                intent.putExtra("ganho", ganho);
                holder.view.getContext().startActivity(intent);
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
                                ganhos.remove(position);
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

