package com.itay.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

public class DevForm extends AppCompatActivity {
    private Spinner yearSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_form);

        yearSelect=findViewById(R.id.yearSelect);
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
}
