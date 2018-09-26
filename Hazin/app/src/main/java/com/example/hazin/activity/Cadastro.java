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
import com.example.hazin.helper.Base64;
import com.example.hazin.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Cadastro extends AppCompatActivity {

    private EditText nomeCadastro, emailCadastro, passwordCadastro;
    private Button btnCadastrar;

    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nomeCadastro = findViewById(R.id.nomeCadastro);
        emailCadastro = findViewById(R.id.emailCadastro);
        passwordCadastro = findViewById(R.id.passwordCadastro);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = nomeCadastro.getText().toString();
                String email = emailCadastro.getText().toString();
                String password = passwordCadastro.getText().toString();

                // Validando se os campos foram preenchidos

                if(!nome.isEmpty()){
                    if(!email.isEmpty()){
                        if (!password.isEmpty()) {

                            usuario = new Usuario();
                            usuario.setNome(nome);
                            usuario.setEmail(email);
                            usuario.setPassword(password);
                            Log.i("Teste", usuario.getEmail());
                            Log.i("Teste", usuario.getPassword());
                            cadastrarUsuario();
                        }else{
                            Toast.makeText(Cadastro.this, "Informar um Password",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Cadastro.this, "Informar um Email",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Cadastro.this, "Informar um Nome",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getPassword()

        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String idUsuario = Base64.encodeEmail(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
                    Toast.makeText(Cadastro.this, "Sucesso ao Cadastrar ",Toast.LENGTH_LONG).show();
                    telaLogin();

                }else{

                    String excecao = "";
                    try{
                        throw  task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte ou com 6 digitos ou mais!!";
                    }catch ( FirebaseAuthInvalidCredentialsException e ){
                        excecao = "Digite um email válido";
                    }catch ( FirebaseAuthUserCollisionException e ){
                        excecao = "Está conta já foi cadastrada";
                    }catch (Exception e ){
                        excecao = "Erro ao cadastrar usuário " + e.getMessage();
                    }

                    Toast.makeText(Cadastro.this, excecao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void telaLogin(){
        startActivity(new Intent(Cadastro.this, Principal.class));
        finish();
    }
}
