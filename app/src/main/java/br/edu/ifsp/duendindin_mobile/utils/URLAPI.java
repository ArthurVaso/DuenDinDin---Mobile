package br.edu.ifsp.duendindin_mobile.utils;

public class URLAPI {
    //Atualizar URL baseada no IPv4 do IPCONFIG da máquina que estará rodando a API
    //A maquina que roda a API deve estar na mesma rede que o ceular/emulador
    public String baseUrl = "http://192.168.0.106:3000/";

    //Pode ser que este ip mude, se a cloud for encerrada e retomada
    //public String baseUrl = "http://3.20.222.45:3000/";

}
