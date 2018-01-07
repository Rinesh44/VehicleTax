package com.example.android.vehicletax;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.android.vehicletax.Database.DatabaseHelper;

public class VehicleInformation extends AppCompatActivity {
TextView name, number, cc, year, type, fuel, category, date;
Toolbar toolbar;
public Vehicle vehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_information);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Vehicle Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.valueName);
        number = findViewById(R.id.valueNumber);
        cc = findViewById(R.id.valueCc);
        year = findViewById(R.id.valueYear);
        type = findViewById(R.id.valueType);
        fuel = findViewById(R.id.valueFuel);
        category = findViewById(R.id.valueCategory);
        date = findViewById(R.id.valueDate);

        Intent i  = getIntent();

        String getNumber = i.getStringExtra("number");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        vehicle = dbHelper.getVehicle(getNumber);

        name.setText(vehicle.getName());
        number.setText(vehicle.getNumber());
        cc.setText(vehicle.getCc());
        year.setText(vehicle.getYear());
        type.setText(vehicle.getType());
        fuel.setText(vehicle.getFuel());
        category.setText(vehicle.getCategory());
        date.setText(vehicle.getDate());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.action_edit:
                Intent edit = new Intent(VehicleInformation.this, EditInformation.class);
                edit.putExtra("number", vehicle.getNumber());
                startActivity(edit);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }
}
