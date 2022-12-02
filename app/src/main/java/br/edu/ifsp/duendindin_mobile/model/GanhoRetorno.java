package br.edu.ifsp.duendindin_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class GanhoRetorno {

    private String data;
    @SerializedName("itens")
    private List<Ganho> listGanhos;

    public GanhoRetorno() {

    }

    public GanhoRetorno(String data, List<Ganho> listGanhos) {
        this.data = data;
        this.listGanhos = listGanhos;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Ganho> getListGanhos() {
        return listGanhos;
    }

    public void setListGanhos(List<Ganho> listGanhos) {
        this.listGanhos = listGanhos;
    }
}
