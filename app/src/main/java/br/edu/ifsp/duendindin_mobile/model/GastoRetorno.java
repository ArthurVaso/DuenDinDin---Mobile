package br.edu.ifsp.duendindin_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class GastoRetorno {

    private String data;
    @SerializedName("itens")
    private List<Gasto> listGastos;

    public GastoRetorno() {
    }

    public GastoRetorno(String data, List<Gasto> listGastos) {
        this.data = data;
        this.listGastos = listGastos;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Gasto> getListGastos() {
        return listGastos;
    }

    public void setListGastos(List<Gasto> listGastos) {
        this.listGastos = listGastos;
    }
}

