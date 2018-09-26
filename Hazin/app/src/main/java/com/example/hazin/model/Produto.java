package com.example.hazin.model;

import com.example.hazin.config.ConfiguracaoFirebase;
import com.example.hazin.helper.Base64;
import com.example.hazin.helper.Date;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Produto {

    private String idProduto;
    private String data;
    private String nomeProduto;
    private String descricao;
    private String tipo;
    private int quantidade;
    private Double valorUnit = 0.00;


    public Produto() {
    }

    public void salvar( String data){

        FirebaseAuth autentica = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64.encodeEmail(autentica.getCurrentUser().getEmail());
        String mesAno = Date.retornaMesAno(data);

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("produto")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Double getValorUnit() {
        return valorUnit;
    }

    public void setValorUnit(Double valorUnit) {
        this.valorUnit = valorUnit;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
