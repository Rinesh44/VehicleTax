package com.example.android.vehicletax;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class FineDetails extends AppCompatActivity {
private TextView actualAmount, finePeriod, finePercent, fineAmount, totalTaxAmount, valueFirst, valueSecond;
Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tax Details");

        actualAmount = findViewById(R.id.valueActualAmount);
        finePercent = findViewById(R.id.valueFinePercent);
        finePeriod = findViewById(R.id.valueFinePeriod);
        fineAmount = findViewById(R.id.valueFineAmount);
        totalTaxAmount = findViewById(R.id.valueTotalTaxAmount);
        valueFirst = findViewById(R.id.valueFirst);
        valueSecond = findViewById(R.id.valueSecond);

        Intent i = getIntent();
        String getActualAmount = i.getStringExtra("actualAmount");
        String getFinePeriod = i.getStringExtra("finePeriod");
        String getTotal = i.getStringExtra("total");

        actualAmount.setText(getActualAmount);
        if(getFinePeriod.matches("0")){
            finePeriod.setText("Null");
            finePercent.setText("0%");
            fineAmount.setText("0");
            totalTaxAmount.setText(getTotal);
            valueFirst.setText(getActualAmount);
            valueSecond.setText(fineAmount.getText());
        }
        if(getFinePeriod.matches("5")){
            finePeriod.setText("First 30 days after delay in payment");
            finePercent.setText("5%");
            Double fine = Integer.valueOf(getActualAmount) * 0.05;
            fineAmount.setText(String.valueOf(fine));
            totalTaxAmount.setText(getTotal);
            valueFirst.setText(getActualAmount);
            valueSecond.setText(fineAmount.getText());
        }
        if(getFinePeriod.matches("10")){
            finePeriod.setText("After 30 days of delay in payment");
            finePercent.setText("10%");
            Double fine = Integer.valueOf(getActualAmount) * 0.1;
            fineAmount.setText(String.valueOf(fine));
            totalTaxAmount.setText(getTotal);
            valueFirst.setText(getActualAmount);
            valueSecond.setText(fineAmount.getText());
        }
        if(getFinePeriod.matches("20")){
            finePeriod.setText("After 75 days of delay in payment");
            finePercent.setText("20%");
            Double fine = Integer.valueOf(getActualAmount) * 0.2;
            fineAmount.setText(String.valueOf(fine));
            totalTaxAmount.setText(getTotal);
            valueFirst.setText(getActualAmount);
            valueSecond.setText(fineAmount.getText());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
            finish();
        }
        return false;
    }
}
