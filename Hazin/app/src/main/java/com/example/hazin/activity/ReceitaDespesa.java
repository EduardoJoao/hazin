package com.example.hazin.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.hazin.R;
import com.example.hazin.adapter.AdapterMovimentacao;
import com.example.hazin.config.ConfiguracaoFirebase;
import com.example.hazin.helper.Base64;
import com.example.hazin.model.Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReceitaDespesa extends AppCompatActivity {

    private RecyclerView list;
    private AdapterMovimentacao movimentacao;
    private ValueEventListener valueList;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private List<Produto> listproduto = new ArrayList<>();
    private Produto produto;
    private String mesAno;

    private DatabaseReference movimentacaoProduto = ConfiguracaoFirebase.getFirebaseDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita_despesa);

        list = findViewById(R.id.list);

        deletar();

        movimentacao = new AdapterMovimentacao(listproduto,this);

        RecyclerView.LayoutManager layoutRecycle =  new LinearLayoutManager(this);
        list.setLayoutManager(layoutRecycle);
        list.setHasFixedSize(true);
        list.setAdapter(movimentacao);


    }

    public void deletar(){
        ItemTouchHelper.Callback exclui = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeflag = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags,swipeflag);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                movimentoExcluido(viewHolder);
            }
        };

        new ItemTouchHelper(exclui).attachToRecyclerView(list);
    }

    public void movimentoExcluido(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Excluir Receita e Despesa");
        alert.setMessage("Tem certeza que deseja deletar essa Informação?");
        alert.setCancelable(false);

        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int posicao = viewHolder.getAdapterPosition();
                produto =  listproduto.get(posicao);

                String emailUsu = autenticacao.getCurrentUser().getEmail();
                String idUsu = Base64.encodeEmail(emailUsu);

                movimentacaoProduto = movimentacaoProduto.child("produto")
                        .child(idUsu)
                        .child("092018");

                movimentacaoProduto.child( produto.getIdProduto() ).removeValue();

                movimentacao.notifyItemRemoved(posicao);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ReceitaDespesa.this, "Cancelado!!",Toast.LENGTH_LONG).show();
                movimentacao.notifyDataSetChanged();
            }
        });

        AlertDialog dialogo = alert.create();
        dialogo.show();
    }

    public void produto(){

        String emailUsu = autenticacao.getCurrentUser().getEmail();
        String idUsu = Base64.encodeEmail(emailUsu);

        movimentacaoProduto = movimentacaoProduto.child("produto")
                           .child(idUsu)
                           .child("092018");

        valueList = movimentacaoProduto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                listproduto.clear();
                for(DataSnapshot dados: dataSnapshot.getChildren()){

                   // Produto produto = dados.getValue( Produto.class );
                    Produto produto = dados.getValue(Produto.class);
                    produto.setIdProduto( dados.getKey());
                    listproduto.add(produto);
                }
                movimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        produto();
    }

    @Override
    protected void onStop() {
        super.onStop();
        movimentacaoProduto.removeEventListener(valueList);

    }
}
