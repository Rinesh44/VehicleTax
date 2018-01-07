package com.example.android.vehicletax;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class Tax extends AppCompatActivity {
Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Tax Info");
        setSupportActionBar(toolbar);
    }
}
