package br.edu.ifsp.duendindin_mobile.adapter;

import android.annotation.SuppressLint;
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
import java.util.List;

import br.edu.ifsp.duendindin_mobile.R;
import br.edu.ifsp.duendindin_mobile.model.Gasto;

public class GastosAdapter extends RecyclerView.Adapter<GastosAdapter.ViewHolder> {

    private ArrayList<Gasto> gastos;
    private LayoutInflater inflater;

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

        gastosViewHolder.cbxPago = itemGasto.findViewById(R.id.checkBox);

        return gastosViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull GastosAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Gasto gasto = gastos.get(position);
        String valor = String.format( "%.2f", gasto.getValor());
        holder.nome.setText(gasto.getNome()+" - R$ "+ valor);
        holder.descricao.setText(gasto.getDescricao());
        holder.position = position;
        holder.cbxPago.setChecked(gasto.getPago());

        holder.cbxPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cbxPago.isChecked()){
                    gasto.setPago(true);
                    Toast.makeText(view.getContext(), "Esse gasto foi pago!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Esse gasto ainda não foi pago!", Toast.LENGTH_SHORT).show();
                    gasto.setPago(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView nome, descricao;
        CheckBox cbxPago;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

