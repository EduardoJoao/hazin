package com.example.hazin.helper;

import java.text.SimpleDateFormat;

public class Date {

    public static String dataAtual(){

        long date = System.currentTimeMillis();
        SimpleDateFormat data =  new SimpleDateFormat("dd/MM/yyyy");
        String dateString = data.format(date);
        return dateString;
    }

    public  static String retornaMesAno(String data){

        String retornoData[] = data.split("/");

        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        String mesAno = mes + ano;

        return mesAno;
    }
}
