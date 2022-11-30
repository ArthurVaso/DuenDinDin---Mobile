package br.edu.ifsp.duendindin_mobile.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Ganho;
import br.edu.ifsp.duendindin_mobile.model.GanhoRetorno;

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
    }

    @Override
    public int getItemCount() {
        return ganhos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView nome, descricao, data;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

