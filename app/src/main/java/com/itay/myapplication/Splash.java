package com.itay.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    private ImageView img1;
    private TextView txt1;
    private ProgressBar pb1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img1 = findViewById(R.id.imageView2);
        txt1 = findViewById(R.id.textView4);
        pb1 = findViewById(R.id.progressBar);

        Thread mSplashThread = new Thread(){
            @Override
            public void run(){
                try{
                    synchronized (this){
                        Animation fadeIn = AnimationUtils.loadAnimation(Splash.this, R.anim.fade);
                        img1.startAnimation(fadeIn);
                        txt1.startAnimation(fadeIn);
                        pb1.startAnimation(fadeIn);

                        wait(3000);
                    }
                }
                catch(InterruptedException ex){

                }
                finish();

                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
            }
        };

        mSplashThread.start();
    }
}
