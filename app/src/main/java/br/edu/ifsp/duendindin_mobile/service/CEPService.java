package br.edu.ifsp.duendindin_mobile.service;

import br.edu.ifsp.duendindin_mobile.model.CEP;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService {

    //consultar CEP no webservice do ViaCEP
    @GET("https://viacep.com.br/ws/{cep}/json/")
    Call<CEP> consultarCEP(@Path("cep") String cep);


}
