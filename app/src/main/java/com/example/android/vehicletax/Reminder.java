package com.example.android.vehicletax;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.android.vehicletax.Database.DatabaseHelper;
import com.hornet.dateconverter.DatePicker.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Reminder extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText pickDate;
    AppCompatSpinner spinner;
    List<Vehicle> vehicles;
    private Calendar calendar;
    ArrayList<String> vehicleNames;
    private int calendarYear, month, day;
    static String selectedVehicle;
    HashMap<String, PendingIntent> intentCollection;
    public static final String SHARED_PREFS = "sharedPrefs";
    AppCompatButton ok, cancel;
    ArrayList<PendingIntent> intentArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Set Reminder");

        pickDate = findViewById(R.id.date);
        spinner = findViewById(R.id.vehicleSpinner);
        vehicleNames = new ArrayList<>();
        ok = findViewById(R.id.ok);
        cancel = findViewById(R.id.cancel);

        calendar = Calendar.getInstance();
        calendarYear = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        vehicles = databaseHelper.getAllVehicle();

        for (int i = 0; i < vehicles.size(); i++) {
            String name = vehicles.get(i).getName();
            vehicleNames.add(name);
        }

        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, vehicleNames);
        spinner.setAdapter(nameAdapter);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

                cal.setTimeInMillis(System.currentTimeMillis());
                cal.clear();
                cal.set(calendarYear, month, day, 14, 32);

                selectedVehicle = spinner.getSelectedItem().toString();

                AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(Reminder.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(Reminder.this, 0, intent, 0);
                intentArray.add(pendingIntent);

                SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
                editor.putString(selectedVehicle, String.valueOf(cal.getTimeInMillis()));
                editor.apply();

                alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                Toast.makeText(Reminder.this, "Reminder set for " + calendarYear + "/" + month + 1 + "/" + day, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(myDateListener, calendarYear, month, day);
            datePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                    showDate(i2, i1 + 1, i);
                }

            };

    private void showDate(int day, int month, int year) {

        pickDate.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }
}
