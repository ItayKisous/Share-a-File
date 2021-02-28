package com.itay.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class Login extends AppCompatActivity {
    private Button btnLogin;
    private EditText etEmail, etPassword;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLoginAccount);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword2);
        checkBox=findViewById(R.id.cbStay);

        EventHandler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), Settings.class);

        switch(item.getItemId()){
            case R.id.Settings:
                i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
                break;
        }
        return true;
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
                            SharedPreferences settings = getSharedPreferences("PREFS_NAME",0 );
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("isChecked", checkBox.isChecked());
                            editor.commit();
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
