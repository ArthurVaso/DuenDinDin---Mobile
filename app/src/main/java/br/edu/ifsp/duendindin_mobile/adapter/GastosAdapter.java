package br.edu.ifsp.duendindin_mobile.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifsp.duendindin_mobile.model.Gasto;

public class GastosAdapter extends RecyclerView.Adapter<GastosAdapter.ViewHolder> {

    private List<Gasto> gastos;

    public GastosAdapter(List<Gasto> gastos) {
        this.gastos = gastos;
    }

    @NonNull
    @Override
    public GastosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GastosAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

