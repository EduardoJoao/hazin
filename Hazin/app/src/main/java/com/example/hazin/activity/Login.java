package com.example.hazin.activity;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hazin.R;
import com.example.hazin.config.ConfiguracaoFirebase;
import com.example.hazin.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Login extends AppCompatActivity {

    private Button loginCadadatro;
    private Button login;
    private EditText entradaEmail;
    private EditText entradaPassword;
    private Usuario usuario;
    private FirebaseAuth autenticcao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginCadadatro = findViewById(R.id.loginCdastro);
        login = findViewById(R.id.login);
        entradaEmail = (EditText) findViewById(R.id.entradaEmail);
        entradaPassword = (EditText) findViewById(R.id.entradaPassword);


        // Verificação no Botão de Login.
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = entradaEmail.getText().toString();
                    String senha = entradaPassword.getText().toString();

                    if(!email.isEmpty()){
                        if (!senha.isEmpty()) {

                            usuario = new Usuario();
                            usuario.setEmail(email);
                            usuario.setPassword(senha);
                            validarLogin();

                        }else{
                            Toast.makeText(Login.this, "Informar um Password",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Login.this, "Informar um Email",Toast.LENGTH_LONG).show();
                    }
                }
            });

        loginCadadatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Cadastro.class));
            }
        });
    }

    // Mensagem de alerta!!
    private void alert(String s ){
        Toast.makeText(this,s, Toast.LENGTH_LONG).show();
    }

    // Validando o Usuário para se logar!!
    public void validarLogin(){
        autenticcao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticcao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    telaHome();

                    Toast.makeText(Login.this, "Sucesso!!",Toast.LENGTH_LONG).show();
                }else{

                    String excecao = "";
                    try{
                        throw  task.getException();
                    }catch ( FirebaseAuthInvalidCredentialsException e ){
                        excecao = "Email e Senha não Cadastrados.";
                    }catch ( FirebaseAuthInvalidUserException e ){
                        excecao = "Usuário não cadastrado!!";
                    }catch (Exception e ){
                        excecao = "Erro ao cadastrar usuário " + e.getMessage();
                    }

                    Toast.makeText(Login.this, excecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verificando se o Usuário está logado.
        verificarLogado();

    }

    // Indo para a tela Home
    public void telaHome(){
        startActivity(new Intent(Login.this, Principal.class));
        finish();
    }

    public void telaHomeLogado(){
       startActivity(new Intent(Login.this, Principal.class));
    }

    // Verificando se o Usuário está logado!!
    public void verificarLogado(){
        autenticcao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticcao.signOut();
        if (autenticcao.getCurrentUser() != null) {
            telaHomeLogado();
        }
    }
}
