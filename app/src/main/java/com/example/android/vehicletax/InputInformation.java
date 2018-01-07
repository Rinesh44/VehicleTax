package com.example.android.vehicletax;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.android.vehicletax.Database.DatabaseHelper;
import com.example.android.vehicletax.InputValidation.CheckValidity;

import com.hornet.dateconverter.DatePicker.DatePickerDialog;

import java.util.Calendar;


public class InputInformation extends AppCompatActivity {
    private TextInputLayout layoutName, layoutNumber, layoutCc, layoutYear, layoutDate;
    private TextInputEditText name, number, cc, year, date;
    private AppCompatSpinner fuel, type, category;
    private AppCompatButton add;
    private Toolbar toolbar;
    private CheckValidity checkValidity;
    private Calendar calendar;
    private DatabaseHelper databaseHelper;
    private Vehicle vehicle;
    private int calendarYear, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_information);

        toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.vehicle_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutName = findViewById(R.id.nameLayout);
        layoutNumber = findViewById(R.id.numberLayout);
        layoutCc = findViewById(R.id.ccLayout);
        layoutYear = findViewById(R.id.yearLayout);
        layoutDate = findViewById(R.id.dateLayout);

        name = findViewById(R.id.textName);
        number = findViewById(R.id.textNumber);
        cc =  findViewById(R.id.textCC);
        year = findViewById(R.id.textYear);
        date = findViewById(R.id.textDate);

        fuel = findViewById(R.id.fuelSpinner);
        type = findViewById(R.id.typeSpinner);
        category = findViewById(R.id.categorySpinner);

        vehicle = new Vehicle();
        databaseHelper = new DatabaseHelper(InputInformation.this);
        checkValidity = new CheckValidity(InputInformation.this);

        calendar = Calendar.getInstance();
        calendarYear = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String[] fuelItems = new String[]{"Diesel", "Petrol"};
        ArrayAdapter<String> fuelAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, fuelItems);
        fuel.setAdapter(fuelAdapter);

        String[] typeItems = new String[]{"Private", "Rented"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, typeItems);
        type.setAdapter(itemAdapter);

        String[] categoryItems = new String[]{getString(R.string.a), getString(R.string.b), getString(R.string.c),
                getString(R.string.d), getString(R.string.e), getString(R.string.f),getString(R.string.g),getString(R.string.h),
                getString(R.string.i),getString(R.string.j)};

        String[] categoryItemsAlternate = new String[]{getString(R.string.b), getString(R.string.c),
                getString(R.string.d), getString(R.string.e), getString(R.string.f),getString(R.string.g),getString(R.string.h),
                getString(R.string.i),getString(R.string.j)};

        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categoryItems);
        final ArrayAdapter<String> categoryAdapterAlternate = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categoryItemsAlternate);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(type.getSelectedItem().toString().matches("Private"))
                    category.setAdapter(categoryAdapter);
                else
                    category.setAdapter(categoryAdapterAlternate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    category.setAdapter(categoryAdapter);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });

        add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkValidity.isInputEditTextFilled(name, layoutName, getString(R.string.error_message))) {
                    return;
                }
                if (!checkValidity.isInputEditTextFilled(number, layoutNumber, getString(R.string.error_message))) {
                    return;
                }
                if (!checkValidity.isInputEditTextFilled(cc, layoutCc, getString(R.string.error_message))) {
                    return;
                }
                if (!checkValidity.isInputEditTextFilled(year, layoutYear, getString(R.string.error_message))) {
                    return;
                }
                if (!checkValidity.isInputEditTextFilled(date, layoutDate, getString(R.string.error_message))) {
                    return;
                }

                dismissKeyboard(InputInformation.this);

                vehicle.setName(name.getText().toString().trim());
                vehicle.setNumber(number.getText().toString().trim());
                vehicle.setCc(cc.getText().toString().trim());
                vehicle.setYear(year.getText().toString().trim());
                vehicle.setType(type.getSelectedItem().toString().trim());
                vehicle.setFuel(fuel.getSelectedItem().toString().trim());
                vehicle.setCategory(category.getSelectedItem().toString());
                vehicle.setDate(date.getText().toString().trim());

                databaseHelper.addVehicle(vehicle);
                Toast.makeText(InputInformation.this, "Vehicle Added, Swipe down to refresh", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
            return false;
    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
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

        date.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }
}
