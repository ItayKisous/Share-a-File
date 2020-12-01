package com.itay.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Register extends AppCompatActivity {
    private Button btnRegister;
    private EditText etEmail, etPassword, etUsername, etFirstname, etSurname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnSubmitRegister);
        etEmail=findViewById(R.id.etEmailRegister);
        etPassword=findViewById(R.id.etPasswordRegister);
        etUsername = findViewById(R.id.etUsernameRegister);
        etFirstname=findViewById(R.id.etName);
        etSurname=findViewById(R.id.etSurname);

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
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reg();
            }
        });
    }

    private void Reg(){
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String username = etUsername.getText().toString();
        final String firstname = etFirstname.getText().toString();
        final String surname = etSurname.getText().toString();

        DBref.Auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User u = new User(email, password, username, firstname, surname);
                            DBref.refUsers.child(email.replace('.', ' ')).setValue(u);
                            Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                        } else {
                           if (task.getException() instanceof FirebaseAuthUserCollisionException)
                               Toast.makeText(Register.this, "Email already in use", Toast.LENGTH_LONG).show();
                           else
                               Toast.makeText(Register.this, "An error has occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
