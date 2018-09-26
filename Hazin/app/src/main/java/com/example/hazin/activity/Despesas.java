package com.example.hazin.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hazin.R;
import com.example.hazin.config.ConfiguracaoFirebase;
import com.example.hazin.helper.Base64;
import com.example.hazin.helper.Date;
import com.example.hazin.model.Produto;
import com.example.hazin.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Despesas extends AppCompatActivity {

    private TextInputEditText nomeProduto, descricacaoProduto, quantidadeProd, valorUnit, dataProduto;
    private android.support.design.widget.FloatingActionButton btnSalvarDespesa;
    private DatabaseReference dataReferencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autentica = ConfiguracaoFirebase.getFirebaseAutenticacao();

    private Double despesasTotal;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        nomeProduto = findViewById(R.id.nomeProduto);
        descricacaoProduto = findViewById(R.id.descricaoProduto);
        quantidadeProd = findViewById(R.id.quantidadeProd);
        valorUnit = findViewById(R.id.produtoValor);
        dataProduto = findViewById(R.id.dataProduto);

        dataProduto.setText( Date.dataAtual() );
        recuperarDespesa();

        btnSalvarDespesa = findViewById(R.id.btnSalvarDespesa);


        btnSalvarDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validarCampos()) {
                    produto = new Produto();

                    String data = dataProduto.getText().toString();
                    Double valorRecuperado = Double.parseDouble(valorUnit.getText().toString());
                    produto.setValorUnit(valorRecuperado);
                    produto.setNomeProduto(nomeProduto.getText().toString());
                    produto.setDescricao(descricacaoProduto.getText().toString());
                    produto.setQuantidade(Integer.parseInt(quantidadeProd.getText().toString()));
                    produto.setData(data);
                    produto.setTipo("d");

                    Double despesaAtualizada = despesasTotal + valorRecuperado;
                    String teste = String.valueOf(despesaAtualizada);
                    Log.i("teste",teste);
                    atualizarDespesa(despesaAtualizada);

                    produto.salvar(data);
                }else{
                    Toast.makeText(Despesas.this, "",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public Boolean validarCampos(){

        String produtoNome = nomeProduto.getText().toString();
        String prodquantidade = quantidadeProd.getText().toString();
        String unitValor = valorUnit.getText().toString();
        String dataprod = dataProduto.getText().toString();

        if(!dataprod.isEmpty()){
            if(!produtoNome.isEmpty()){
                if(!prodquantidade.isEmpty()){
                    if(!unitValor.isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(Despesas.this, "Informar um Valor unitário",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else{
                    Toast.makeText(Despesas.this, "Informar uma quantidade",Toast.LENGTH_LONG).show();
                    return false;
                }
            }else{
                Toast.makeText(Despesas.this, "Informar um Nome",Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(Despesas.this, "Informar uma Data",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void recuperarDespesa(){
        String emailUsu = autentica.getCurrentUser().getEmail();
        String idUsu = Base64.encodeEmail(emailUsu);
        DatabaseReference usuRef = dataReferencia.child("usuarios").child( idUsu );

        usuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesasTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDespesa( Double despesa){

        String emailUsu = autentica.getCurrentUser().getEmail();
        String idUsu = Base64.encodeEmail(emailUsu);
        DatabaseReference usuRef = dataReferencia.child("usuarios").child( idUsu );

        usuRef.child("despesaTotal").setValue(despesa);
    }
}
