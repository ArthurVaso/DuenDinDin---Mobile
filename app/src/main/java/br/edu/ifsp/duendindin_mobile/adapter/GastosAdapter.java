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
import br.edu.ifsp.duendindin_mobile.model.Gasto;
import br.edu.ifsp.duendindin_mobile.view.CategoriaAlterarActivity;
import br.edu.ifsp.duendindin_mobile.view.GastoAlterarActivity;

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
        gastosViewHolder.data = itemGasto.findViewById(R.id.textView3);

        gastosViewHolder.cbxPago = itemGasto.findViewById(R.id.checkBox);

        gastosViewHolder.imgEdit = itemGasto.findViewById(R.id.imageButton5);
        gastosViewHolder.imgDelete = itemGasto.findViewById(R.id.imageButton6);

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
                    gasto.setPago(true);
                    Toast.makeText(view.getContext(), "Esse gasto foi pago!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Esse gasto ainda não foi pago!", Toast.LENGTH_SHORT).show();
                    gasto.setPago(false);
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
                                gastos.remove(position);
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
}

