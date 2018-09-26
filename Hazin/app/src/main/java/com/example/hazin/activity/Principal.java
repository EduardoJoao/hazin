package com.example.hazin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hazin.R;
import com.example.hazin.config.ConfiguracaoFirebase;
import com.example.hazin.helper.Base64;
import com.example.hazin.model.Usuario;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.graphics.Color.RED;

public class Principal extends AppCompatActivity {

    private TextView nomePrincipal, somatorioPrincipal;
    private Double despesaTotal = 0.00 ;
    private Double receitaTotal = 0.00 ;
    private Double somatorioTotal = 0.00 ;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference dataReferencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuRef;
    private ValueEventListener valueEventListenerUsuario;

    private PieChart pieChart;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        nomePrincipal = findViewById(R.id.nomePrincipal);
        somatorioPrincipal = findViewById(R.id.somatorioPrincipal);

        pieChart = findViewById(R.id.pieChart);
        lineChart = findViewById(R.id.lineChart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.15f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        yValues.add(new PieEntry(25f, "Entrada"));
        yValues.add(new PieEntry(25f, "Saída"));

        pieChart.animateY(1000, Easing.EasingOption.EaseInCubic);

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(Color.rgb(22,140,22), RED);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaDados();
    }

    public void adicionarDespesa(View view){

        startActivity(new Intent(Principal.this,Despesas.class));

    }

    public void adicionarReceita(View view){

        startActivity(new Intent(Principal.this,Receitas.class));

    }

    public void lista(View view){
        startActivity(new Intent(Principal.this,ReceitaDespesa.class));
    }

    public void recuperaDados(){

        String emailUsu = autenticacao.getCurrentUser().getEmail();
        String idUsu = Base64.encodeEmail(emailUsu);
        usuRef = dataReferencia.child("usuarios").child( idUsu );

        valueEventListenerUsuario = usuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                somatorioTotal = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String resultado = decimalFormat.format(somatorioTotal);

                nomePrincipal.setText(usuario.getNome());
                somatorioPrincipal.setText("R$ " + resultado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuRef.removeEventListener(valueEventListenerUsuario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:

               break;
            case R.id.item2:
               autenticacao.signOut();
               startActivity(new Intent(this, Login.class));
               finish();
               break;
        }
        return super.onOptionsItemSelected(item);
    }

}
