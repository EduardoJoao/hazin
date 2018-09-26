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

public class Receitas extends AppCompatActivity {

    private TextInputEditText produtoReceita, descricaoReceita, quantReceita, unitReceita, dataReceita;
    private android.support.design.widget.FloatingActionButton salvarReceita;
    private DatabaseReference dataReferencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autentica = ConfiguracaoFirebase.getFirebaseAutenticacao();

    private Double receitaTotal;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        produtoReceita = findViewById(R.id.produtoReceita);
        descricaoReceita = findViewById(R.id.descricaoReceia);
        quantReceita = findViewById(R.id.quantReceita);
        unitReceita = findViewById(R.id.unitReceiita);
        dataReceita = findViewById(R.id.dataReceita);
        salvarReceita = findViewById(R.id.salvarReceita);

        dataReceita.setText(Date.dataAtual());

        recuperarReceita();

        salvarReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()) {
                    produto = new Produto();

                    String data = dataReceita.getText().toString();
                    Double valorRecuperado = Double.parseDouble(unitReceita.getText().toString());
                    produto.setValorUnit(valorRecuperado);
                    produto.setNomeProduto(produtoReceita.getText().toString());
                    produto.setDescricao(descricaoReceita.getText().toString());
                    produto.setQuantidade(Integer.parseInt(quantReceita.getText().toString()));
                    produto.setData(data);
                    produto.setTipo("r");

                    Double receitaAtualizada = receitaTotal + valorRecuperado;
                    String teste = String.valueOf(receitaAtualizada);
                    Log.i("teste",teste);
                    atualizarReceita(receitaAtualizada);

                    produto.salvar(data);
                    Toast.makeText(Receitas.this, "Receita salva com Sucesso!!",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Receitas.this, "Problema ao validar os campos!!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public Boolean validarCampos(){

        String produtoNome = produtoReceita.getText().toString();
        String prodquantidade = quantReceita.getText().toString();
        String unitValor = unitReceita.getText().toString();
        String dataprod = dataReceita.getText().toString();

        if(!dataprod.isEmpty()){
            if(!produtoNome.isEmpty()){
                if(!prodquantidade.isEmpty()){
                    if(!unitValor.isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(Receitas.this, "Informar um Valor unitário",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else{
                    Toast.makeText(Receitas.this, "Informar uma quantidade",Toast.LENGTH_LONG).show();
                    return false;
                }
            }else{
                Toast.makeText(Receitas.this, "Informar um Nome",Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(Receitas.this, "Informar uma Data",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void recuperarReceita(){
        String emailUsu = autentica.getCurrentUser().getEmail();
        String idUsu = Base64.encodeEmail(emailUsu);
        DatabaseReference usuRef = dataReferencia.child("usuarios").child( idUsu );

        usuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceita( Double receita){

        String emailUsu = autentica.getCurrentUser().getEmail();
        String idUsu = Base64.encodeEmail(emailUsu);
        DatabaseReference usuRef = dataReferencia.child("usuarios").child( idUsu );

        usuRef.child("receitaTotal").setValue(receita);
    }
}
