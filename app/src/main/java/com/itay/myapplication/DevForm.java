package com.itay.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

public class DevForm extends AppCompatActivity {
    private Spinner yearSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_form);

        yearSelect=findViewById(R.id.yearSelect);
    }
}
