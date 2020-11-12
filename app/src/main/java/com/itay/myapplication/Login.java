package com.itay.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class Login extends AppCompatActivity {
    private Button btnLogin;
    private EditText etEmail, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLoginAccount);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword2);

        EventHandler();
    }

    private void EventHandler() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login(){
        String email=etEmail.getText().toString();
        String password=etPassword.getText().toString();

        DBref.Auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Intent i = new Intent();
                            Toast.makeText(Login.this, "Logged in", Toast.LENGTH_LONG).show();
                            i.setClass(getBaseContext(), MainMenu.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(Login.this, "Username or Passowrd are wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
