package com.example.hazin.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hazin.R;
import com.example.hazin.model.Produto;

import java.util.List;

    public class AdapterMovimentacao extends RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder> {

        List<Produto> produtos;
        Context context;

        public AdapterMovimentacao(List<Produto> produtos, Context context) {
            this.produtos = produtos;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movimentacao, parent, false);
            return new MyViewHolder(itemLista);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Produto produto = produtos.get(position);

            holder.titulo.setText(produto.getNomeProduto());
            holder.valor.setText(String.valueOf(produto.getValorUnit()));
            holder.categoria.setText(produto.getDescricao());

            if (produto.getTipo() == "d" || produto.getTipo().equals("d")) {
                holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccent));
                holder.valor.setText("-" + produto.getValorUnit());
            }
        }


        @Override
        public int getItemCount() {
            return produtos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView titulo, valor, categoria;

            public MyViewHolder(View itemView) {
                super(itemView);

                titulo = itemView.findViewById(R.id.textAdapterTitulo);
                valor = itemView.findViewById(R.id.textAdapterValor);
                categoria = itemView.findViewById(R.id.textAdapterCategoria);
            }

        }

    }
