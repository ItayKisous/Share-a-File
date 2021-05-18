package com.itay.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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
                            NotificationManager mNotificationManager = (NotificationManager)
                                    getSystemService(Context.NOTIFICATION_SERVICE);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){ //בודק האם הגרסא של האנדרואיד תומכת בהודעות
                                NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                                        "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
                                channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
                                mNotificationManager.createNotificationChannel(channel);
                            }
                            NotificationCompat.Builder mBuilder = new
                                    NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                                    .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Welcome to Share a File!")
                                    .setContentText("Registered Successfully!").setAutoCancel(true);
                            mNotificationManager.notify(0, mBuilder.build());
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
