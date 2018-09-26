package com.example.hazin.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference dataBase;

    // retornando a instancia do Database;
    public static DatabaseReference getFirebaseDatabase(){

        if(dataBase == null){
            dataBase = FirebaseDatabase.getInstance().getReference();
        }
        return dataBase;
    }

    // retornando a instancia do firebaseauth
    public static FirebaseAuth getFirebaseAutenticacao(){

        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
