package com.example.android.vehicletax;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;

import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vehicletax.Database.DatabaseHelper;
import com.hornet.dateconverter.DateConverter;
import com.hornet.dateconverter.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Interface, VehicleAdapter.VehicleAdapterListener {
    private TextInputEditText number, cc, year;
    private AppCompatSpinner fuel, type;
    public static RecyclerView recyclerView;
    public static VehicleAdapter adapter;
    private List<Vehicle> vehicleList;
    private List<Vehicle> actualList;
    private Boolean exit = false;
    private Toolbar toolbar;
    private SearchView searchView;
    SwipeRefreshLayout refresh;
    int amount;
    String finePeriod;
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MainActivity.this, InputInformation.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);

        refresh = findViewById(R.id.swipe_refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actualList.clear();
                adapter = new VehicleAdapter(MainActivity.this, actualList, MainActivity.this);
                recyclerView.setAdapter(adapter);
                prepareAlbums();
                refresh.setRefreshing(false);
            }
        });

        mEmptyStateTextView = findViewById(R.id.empty_view);

        vehicleList = new ArrayList<>();
        actualList = new ArrayList<>();
        adapter = new VehicleAdapter(this, actualList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        if (actualList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.GONE);
        }
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        /*
        int[] cover = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};
                */

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        vehicleList = databaseHelper.getAllVehicle();
        Log.e("VehicleListSize:", String.valueOf(vehicleList.size()));

        for (int i = 0; i < vehicleList.size(); i++) {
            String mName = vehicleList.get(i).getName();
            String mNumber = vehicleList.get(i).getNumber();
            String mCc = vehicleList.get(i).getCc();
            String mYear = vehicleList.get(i).getYear();
            String mType = vehicleList.get(i).getType();
            String mFuel = vehicleList.get(i).getFuel();
            String mCategory = vehicleList.get(i).getCategory();
            String mDate = vehicleList.get(i).getDate();

            Vehicle v = new Vehicle(mNumber, mCc, mYear, mType, mFuel, mName, mCategory, mDate);
            actualList.add(v);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tax_calculation);
        dialog.show();

        int getCC = Integer.valueOf(VehicleAdapter.getCC);
        String getType = VehicleAdapter.getType;
        String getCategory = VehicleAdapter.getCategory;
        String getDate = VehicleAdapter.getDate;

        TextView textView = dialog.findViewById(R.id.taxText);
        final TextView value = dialog.findViewById(R.id.value);
        textView.setText(R.string.tax_text);

        final String a = "Motorcycle, Scooter";
        final String b = "Car, Jeep, Van, Micro-Bus";
        final String c = "Dojer, Excavator, Loader";
        final String d = "Roller, Tipper, Crane";
        final String e = "Mini-Tipper";
        final String f = "Three-Wheeler";
        final String g = "Tractor";
        final String h = "PowerTiller";
        final String i = "Mini-Truck, Mini-Bus";
        final String j = "Truck, Bus";

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


        String[] separated = getDate.split("-");
        String separatedDay = separated[0];
        String separatedMonth = separated[1];
        String separatedYear = separated[2];
        int intYear = Integer.valueOf(separatedYear);
        int intMonth = Integer.valueOf(separatedMonth);
        int intDay = Integer.valueOf(separatedDay);

        DateConverter dc = new DateConverter();
        Model outputDate = dc.getNepaliDate(currentYear, currentMonth + 1, currentDay);
        int getDay = outputDate.getDay();
        int getMonth = outputDate.getMonth();
        int getYear = outputDate.getYear();

        if ((getYear - intYear) <= 1) {
            finePeriod = "0";
            if (getType.matches("Private")) {
                switch (getCategory) {
                    case a:
                        if (getCC <= 125) {
                            amount = 2500;
                            value.setText("Rs. " + "2500");
                        } else if (getCC > 125 && getCC < 251) {
                            amount = 4000;
                            value.setText("Rs. " + "4000");
                        } else if (getCC > 250 && getCC < 401) {
                            amount = 8000;
                            value.setText("Rs. " + "8000");
                        } else {
                            amount = 15000;
                            value.setText("Rs. " + "15000");
                        }
                        break;

                    case b:
                        if (getCC <= 1000) {
                            amount = 19000;
                            value.setText("Rs. " + "19000");
                        } else if (getCC > 1000 && getCC < 1501) {
                            amount = 21000;
                            value.setText("Rs. " + "21000");
                        } else if (getCC > 1500 && getCC < 2001) {
                            amount = 23000;
                            value.setText("Rs. " + "23000");
                        } else if (getCC > 2000 && getCC < 2501) {
                            amount = 32000;
                            value.setText("Rs. " + "32000");
                        } else if (getCC > 2500 && getCC < 2901) {
                            amount = 37000;
                            value.setText("Rs. " + "37000");
                        } else {
                            amount = 53000;
                            value.setText("Rs. " + "53000");
                        }
                        break;

                    case c:
                        amount = 35000;
                        value.setText("Rs. " + "35000");
                        break;

                    case d:
                        amount = 35000;
                        value.setText("Rs. " + "35000");
                        break;

                    case e:
                        amount = 25000;
                        value.setText("Rs. " + "25000");
                        break;

                    case f:
                        amount = 5000;
                        value.setText("Rs. " + "5000");
                        break;

                    case g:
                        amount = 4000;
                        value.setText("Rs. " + "4000");
                        break;

                    case h:
                        amount = 3000;
                        value.setText("Rs. " + "3000");
                        break;

                    case i:
                        amount = 22000;
                        value.setText("Rs. " + "22000");
                        break;

                    case j:
                        amount = 30000;
                        value.setText("Rs. " + "30000");
                        break;

                    default:
                        value.setText("null");
                        break;
                }

            } else {
                switch (getCategory) {
                    case b:
                        if (getCC <= 1300) {
                            amount = 8000;
                            value.setText("Rs. " + "8000");
                        } else if (getCC > 1300 && getCC < 2001) {
                            amount = 9000;
                            value.setText("Rs. " + "9000");
                        } else if (getCC > 2000 && getCC < 2901) {
                            amount = 11000;
                            value.setText("Rs. " + "11000");
                        } else if (getCC > 2900 && getCC < 4001) {
                            amount = 13000;
                            value.setText("Rs. " + "13000");
                        } else {
                            amount = 15000;
                            value.setText("Rs. " + "15000");
                        }
                        break;

                    case c:
                        amount = 17000;
                        value.setText("Rs. " + "17000");
                        break;

                    case d:
                        amount = 17000;
                        value.setText("Rs. " + "17000");
                        break;

                    case e:
                        amount = 14000;
                        value.setText("Rs. " + "14000");
                        break;

                    case f:
                        amount = 4000;
                        value.setText("Rs. " + "4000");
                        break;

                    case g:
                        amount = 2500;
                        value.setText("Rs. " + "2500");
                        break;

                    case h:
                        amount = 2000;
                        value.setText("Rs. " + "2000");
                        break;

                    case i:
                        amount = 12000;
                        value.setText("Rs. " + "12000");
                        break;

                    case j:
                        amount = 16000;
                        value.setText("Rs. " + "16000");
                        break;

                    default:
                        value.setText("null");
                        break;
                }
            }
        } else if ((getYear - intYear) > 1) {
            if (getMonth == 0) {
                if (getDay < 31) {
                    finePeriod = "5";
                    if (getType.matches("Private")) {
                        switch (getCategory) {
                            case a:
                                if (getCC <= 125) {
                                    amount = 2500;
                                    double fineAmount = 2500 + 0.05 * 2500;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 125 && getCC < 251) {
                                    amount = 4000;
                                    double fineAmount = 4000 + 0.05 * 4000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));

                                } else if (getCC > 250 && getCC < 401) {
                                    amount = 8000;
                                    double fineAmount = 8000 + 0.05 * 8000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else {
                                    amount = 15000;
                                    double fineAmount = 15000 + 0.05 * 15000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                }
                                break;

                            case b:
                                if (getCC <= 1000) {
                                    amount = 19000;
                                    double fineAmount = 19000 + 0.05 * 19000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 1000 && getCC < 1501) {
                                    amount = 21000;
                                    double fineAmount = 21000 + 0.05 * 21000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 1500 && getCC < 2001) {
                                    amount = 23000;
                                    double fineAmount = 23000 + 0.05 * 23000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 2000 && getCC < 2501) {
                                    amount = 32000;
                                    double fineAmount = 32000 + 0.05 * 32000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 2500 && getCC < 2901) {
                                    amount = 37000;
                                    double fineAmount = 37000 + 0.05 * 37000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else {
                                    amount = 53000;
                                    double fineAmount = 53000 + 0.05 * 53000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                }
                                break;

                            case c:
                                amount = 35000;
                                double cFineAmount = 35000 + 0.05 * 35000;
                                value.setText("Rs. " + String.valueOf(cFineAmount));
                                break;

                            case d:
                                amount = 35000;
                                double dFineAmount = 35000 + 0.05 * 35000;
                                value.setText("Rs. " + String.valueOf(dFineAmount));
                                break;

                            case e:
                                amount = 25000;
                                double eFineAmount = 25000 + 0.05 * 25000;
                                value.setText("Rs. " + String.valueOf(eFineAmount));
                                break;

                            case f:
                                amount = 5000;
                                double fFineAmount = 5000 + 0.05 * 5000;
                                value.setText("Rs. " + String.valueOf(fFineAmount));
                                break;

                            case g:
                                amount = 4000;
                                double gFineAmount = 4000 + 0.05 * 4000;
                                value.setText("Rs. " + String.valueOf(gFineAmount));
                                break;

                            case h:
                                amount = 3000;
                                double hFineAmount = 3000 + 0.05 * 3000;
                                value.setText("Rs. " + String.valueOf(hFineAmount));
                                break;

                            case i:
                                amount = 22000;
                                double iFineAmount = 22000 + 0.05 * 22000;
                                value.setText("Rs. " + String.valueOf(iFineAmount));
                                break;

                            case j:
                                amount = 30000;
                                double jFineAmount = 30000 + 0.05 * 30000;
                                value.setText("Rs. " + String.valueOf(jFineAmount));
                                break;

                            default:
                                value.setText("null");
                                break;
                        }

                    } else {
                        switch (getCategory) {
                            case b:
                                if (getCC <= 1300) {
                                    amount = 8000;
                                    double bFineAmount = 8000 + 0.05 * 8000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 1300 && getCC < 2001) {
                                    amount = 8000;
                                    double bFineAmount = 8000 + 0.05 * 8000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 2000 && getCC < 2901) {
                                    amount = 11000;
                                    double bFineAmount = 11000 + 0.05 * 11000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 2900 && getCC < 4001) {
                                    amount = 13000;
                                    double bFineAmount = 13000 + 0.05 * 13000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else {
                                    amount = 15000;
                                    double bFineAmount = 15000 + 0.05 * 15000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                }
                                break;

                            case c:
                                amount = 17000;
                                double cFineAmount = 17000 + 0.05 * 17000;
                                value.setText("Rs. " + String.valueOf(cFineAmount));
                                break;

                            case d:
                                amount = 17000;
                                double dFineAmount = 17000 + 0.05 * 17000;
                                value.setText("Rs. " + String.valueOf(dFineAmount));
                                break;

                            case e:
                                amount = 14000;
                                double eFineAmount = 14000 + 0.05 * 14000;
                                value.setText("Rs. " + String.valueOf(eFineAmount));
                                break;

                            case f:
                                amount = 4000;
                                double fFineAmount = 4000 + 0.05 * 4000;
                                value.setText("Rs. " + String.valueOf(fFineAmount));
                                break;

                            case g:
                                amount = 2500;
                                double gFineAmount = 2500 + 0.05 * 2500;
                                value.setText("Rs. " + String.valueOf(gFineAmount));
                                break;

                            case h:
                                amount = 2000;
                                double hFineAmount = 2000 + 0.05 * 2000;
                                value.setText("Rs. " + String.valueOf(hFineAmount));
                                break;

                            case i:
                                amount = 12000;
                                double iFineAmount = 12000 + 0.05 * 12000;
                                value.setText("Rs. " + String.valueOf(iFineAmount));
                                break;

                            case j:
                                amount = 16000;
                                double jFineAmount = 16000 + 0.05 * 16000;
                                value.setText("Rs. " + String.valueOf(jFineAmount));
                                break;

                            default:
                                value.setText("null");
                                break;
                        }
                    }
                } else {
                    finePeriod = "10";
                    if (getType.matches("Private")) {
                        switch (getCategory) {
                            case a:
                                if (getCC <= 125) {
                                    amount = 2500;
                                    double fineAmount = 2500 + 0.1 * 2500;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 125 && getCC < 251) {
                                    amount = 4000;
                                    double fineAmount = 4000 + 0.1 * 4000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));

                                } else if (getCC > 250 && getCC < 401) {
                                    amount = 8000;
                                    double fineAmount = 8000 + 0.1 * 8000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else {
                                    amount = 15000;
                                    double fineAmount = 15000 + 0.1 * 15000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                }
                                break;

                            case b:
                                if (getCC <= 1000) {
                                    amount = 19000;
                                    double fineAmount = 19000 + 0.1 * 19000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 1000 && getCC < 1501) {
                                    amount = 21000;
                                    double fineAmount = 21000 + 0.1 * 21000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 1500 && getCC < 2001) {
                                    amount = 23000;
                                    double fineAmount = 23000 + 0.1 * 23000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 2000 && getCC < 2501) {
                                    amount = 32000;
                                    double fineAmount = 32000 + 0.1 * 32000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 2500 && getCC < 2901) {
                                    amount = 37000;
                                    double fineAmount = 37000 + 0.1 * 37000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else {
                                    amount = 53000;
                                    double fineAmount = 53000 + 0.1 * 53000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                }
                                break;

                            case c:
                                amount = 35000;
                                double cFineAmount = 35000 + 0.1 * 35000;
                                value.setText("Rs. " + String.valueOf(cFineAmount));
                                break;

                            case d:
                                amount = 35000;
                                double dFineAmount = 35000 + 0.1 * 35000;
                                value.setText("Rs. " + String.valueOf(dFineAmount));
                                break;

                            case e:
                                amount = 25000;
                                double eFineAmount = 25000 + 0.1 * 25000;
                                value.setText("Rs. " + String.valueOf(eFineAmount));
                                break;

                            case f:
                                amount = 5000;
                                double fFineAmount = 5000 + 0.1 * 5000;
                                value.setText("Rs. " + String.valueOf(fFineAmount));
                                break;

                            case g:
                                amount = 4000;
                                double gFineAmount = 4000 + 0.1 * 4000;
                                value.setText("Rs. " + String.valueOf(gFineAmount));
                                break;

                            case h:
                                amount = 3000;
                                double hFineAmount = 3000 + 0.1 * 3000;
                                value.setText("Rs. " + String.valueOf(hFineAmount));
                                break;

                            case i:
                                amount = 22000;
                                double iFineAmount = 22000 + 0.1 * 22000;
                                value.setText("Rs. " + String.valueOf(iFineAmount));
                                break;

                            case j:
                                amount = 30000;
                                double jFineAmount = 30000 + 0.1 * 30000;
                                value.setText("Rs. " + String.valueOf(jFineAmount));
                                break;

                            default:
                                value.setText("null");
                                break;
                        }

                    } else {
                        switch (getCategory) {
                            case b:
                                if (getCC <= 1300) {
                                    amount = 8000;
                                    double bFineAmount = 8000 + 0.1 * 8000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 1300 && getCC < 2001) {
                                    amount = 8000;
                                    double bFineAmount = 8000 + 0.1 * 8000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 2000 && getCC < 2901) {
                                    amount = 11000;
                                    double bFineAmount = 11000 + 0.1 * 11000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 2900 && getCC < 4001) {
                                    amount = 13000;
                                    double bFineAmount = 13000 + 0.1 * 13000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else {
                                    amount = 15000;
                                    double bFineAmount = 15000 + 0.1 * 15000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                }
                                break;

                            case c:
                                amount = 17000;
                                double cFineAmount = 17000 + 0.1 * 17000;
                                value.setText("Rs. " + String.valueOf(cFineAmount));
                                break;

                            case d:
                                amount = 17000;
                                double dFineAmount = 17000 + 0.1 * 17000;
                                value.setText("Rs. " + String.valueOf(dFineAmount));
                                break;

                            case e:
                                amount = 14000;
                                double eFineAmount = 14000 + 0.1 * 14000;
                                value.setText("Rs. " + String.valueOf(eFineAmount));
                                break;

                            case f:
                                amount = 4000;
                                double fFineAmount = 4000 + 0.1 * 4000;
                                value.setText("Rs. " + String.valueOf(fFineAmount));
                                break;

                            case g:
                                amount = 2500;
                                double gFineAmount = 2500 + 0.1 * 2500;
                                value.setText("Rs. " + String.valueOf(gFineAmount));
                                break;

                            case h:
                                amount = 2000;
                                double hFineAmount = 2000 + 0.1 * 2000;
                                value.setText("Rs. " + String.valueOf(hFineAmount));
                                break;

                            case i:
                                amount = 12000;
                                double iFineAmount = 12000 + 0.1 * 12000;
                                value.setText("Rs. " + String.valueOf(iFineAmount));
                                break;

                            case j:
                                amount = 16000;
                                double jFineAmount = 16000 + 0.1 * 16000;
                                value.setText("Rs. " + String.valueOf(jFineAmount));
                                break;

                            default:
                                value.setText("null");
                                break;
                        }
                    }
                }
            } else if (getMonth == 1) {
                finePeriod = "10";
                if (getType.matches("Private")) {
                    switch (getCategory) {
                        case a:
                            if (getCC <= 125) {
                                amount = 2500;
                                double fineAmount = 2500 + 0.1 * 2500;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else if (getCC > 125 && getCC < 251) {
                                amount = 4000;
                                double fineAmount = 4000 + 0.1 * 4000;
                                value.setText("Rs. " + String.valueOf(fineAmount));

                            } else if (getCC > 250 && getCC < 401) {
                                amount = 8000;
                                double fineAmount = 8000 + 0.1 * 8000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else {
                                amount = 15000;
                                double fineAmount = 15000 + 0.1 * 15000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            }
                            break;

                        case b:
                            if (getCC <= 1000) {
                                amount = 19000;
                                double fineAmount = 19000 + 0.1 * 19000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else if (getCC > 1000 && getCC < 1501) {
                                amount = 21000;
                                double fineAmount = 21000 + 0.1 * 21000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else if (getCC > 1500 && getCC < 2001) {
                                amount = 23000;
                                double fineAmount = 23000 + 0.1 * 23000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else if (getCC > 2000 && getCC < 2501) {
                                amount = 32000;
                                double fineAmount = 32000 + 0.1 * 32000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else if (getCC > 2500 && getCC < 2901) {
                                amount = 37000;
                                double fineAmount = 37000 + 0.1 * 37000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else {
                                amount = 53000;
                                double fineAmount = 53000 + 0.1 * 53000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            }
                            break;

                        case c:
                            amount = 35000;
                            double cFineAmount = 35000 + 0.1 * 35000;
                            value.setText("Rs. " + String.valueOf(cFineAmount));
                            break;

                        case d:
                            amount = 35000;
                            double dFineAmount = 35000 + 0.1 * 35000;
                            value.setText("Rs. " + String.valueOf(dFineAmount));
                            break;

                        case e:
                            amount = 25000;
                            double eFineAmount = 25000 + 0.1 * 25000;
                            value.setText("Rs. " + String.valueOf(eFineAmount));
                            break;

                        case f:
                            amount = 5000;
                            double fFineAmount = 5000 + 0.1 * 5000;
                            value.setText("Rs. " + String.valueOf(fFineAmount));
                            break;

                        case g:
                            amount = 4000;
                            double gFineAmount = 4000 + 0.1 * 4000;
                            value.setText("Rs. " + String.valueOf(gFineAmount));
                            break;

                        case h:
                            amount = 3000;
                            double hFineAmount = 3000 + 0.1 * 3000;
                            value.setText("Rs. " + String.valueOf(hFineAmount));
                            break;

                        case i:
                            amount = 22000;
                            double iFineAmount = 22000 + 0.1 * 22000;
                            value.setText("Rs. " + String.valueOf(iFineAmount));
                            break;

                        case j:
                            amount = 30000;
                            double jFineAmount = 30000 + 0.1 * 30000;
                            value.setText("Rs. " + String.valueOf(jFineAmount));
                            break;

                        default:
                            value.setText("null");
                            break;
                    }

                } else {
                    switch (getCategory) {
                        case b:
                            if (getCC <= 1300) {
                                amount = 8000;
                                double bFineAmount = 8000 + 0.1 * 8000;
                                value.setText("Rs. " + String.valueOf(bFineAmount));
                            } else if (getCC > 1300 && getCC < 2001) {
                                amount = 8000;
                                double bFineAmount = 8000 + 0.1 * 8000;
                                value.setText("Rs. " + String.valueOf(bFineAmount));
                            } else if (getCC > 2000 && getCC < 2901) {
                                amount = 11000;
                                double bFineAmount = 11000 + 0.1 * 11000;
                                value.setText("Rs. " + String.valueOf(bFineAmount));
                            } else if (getCC > 2900 && getCC < 4001) {
                                amount = 13000;
                                double bFineAmount = 13000 + 0.1 * 13000;
                                value.setText("Rs. " + String.valueOf(bFineAmount));
                            } else {
                                amount = 15000;
                                double bFineAmount = 15000 + 0.1 * 15000;
                                value.setText("Rs. " + String.valueOf(bFineAmount));
                            }
                            break;

                        case c:
                            amount = 17000;
                            double cFineAmount = 17000 + 0.1 * 17000;
                            value.setText("Rs. " + String.valueOf(cFineAmount));
                            break;

                        case d:
                            amount = 17000;
                            double dFineAmount = 17000 + 0.1 * 17000;
                            value.setText("Rs. " + String.valueOf(dFineAmount));
                            break;

                        case e:
                            amount = 14000;
                            double eFineAmount = 14000 + 0.1 * 14000;
                            value.setText("Rs. " + String.valueOf(eFineAmount));
                            break;

                        case f:
                            amount = 4000;
                            double fFineAmount = 4000 + 0.1 * 4000;
                            value.setText("Rs. " + String.valueOf(fFineAmount));
                            break;

                        case g:
                            amount = 2500;
                            double gFineAmount = 2500 + 0.1 * 2500;
                            value.setText("Rs. " + String.valueOf(gFineAmount));
                            break;

                        case h:
                            amount = 2000;
                            double hFineAmount = 2000 + 0.1 * 2000;
                            value.setText("Rs. " + String.valueOf(hFineAmount));
                            break;

                        case i:
                            amount = 12000;
                            double iFineAmount = 12000 + 0.1 * 12000;
                            value.setText("Rs. " + String.valueOf(iFineAmount));
                            break;

                        case j:
                            amount = 16000;
                            double jFineAmount = 16000 + 0.1 * 16000;
                            value.setText("Rs. " + String.valueOf(jFineAmount));
                            break;

                        default:
                            value.setText("null");
                            break;
                    }
                }
            } else if (getMonth == 2) {
                if (getDay <= 15) {
                    finePeriod = "10";
                    if (getType.matches("Private")) {
                        switch (getCategory) {
                            case a:
                                if (getCC <= 125) {
                                    amount = 2500;
                                    double fineAmount = 2500 + 0.1 * 2500;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 125 && getCC < 251) {
                                    amount = 4000;
                                    double fineAmount = 4000 + 0.1 * 4000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));

                                } else if (getCC > 250 && getCC < 401) {
                                    amount = 8000;
                                    double fineAmount = 8000 + 0.1 * 8000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else {
                                    amount = 15000;
                                    double fineAmount = 15000 + 0.1 * 15000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                }
                                break;

                            case b:
                                if (getCC <= 1000) {
                                    amount = 19000;
                                    double fineAmount = 19000 + 0.1 * 19000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 1000 && getCC < 1501) {
                                    amount = 21000;
                                    double fineAmount = 21000 + 0.1 * 21000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 1500 && getCC < 2001) {
                                    amount = 23000;
                                    double fineAmount = 23000 + 0.1 * 23000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 2000 && getCC < 2501) {
                                    amount = 32000;
                                    double fineAmount = 32000 + 0.1 * 32000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 2500 && getCC < 2901) {
                                    amount = 37000;
                                    double fineAmount = 37000 + 0.1 * 37000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else {
                                    amount = 53000;
                                    double fineAmount = 53000 + 0.1 * 53000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                }
                                break;

                            case c:
                                amount = 35000;
                                double cFineAmount = 35000 + 0.1 * 35000;
                                value.setText("Rs. " + String.valueOf(cFineAmount));
                                break;

                            case d:
                                amount = 35000;
                                double dFineAmount = 35000 + 0.1 * 35000;
                                value.setText("Rs. " + String.valueOf(dFineAmount));
                                break;

                            case e:
                                amount = 25000;
                                double eFineAmount = 25000 + 0.1 * 25000;
                                value.setText("Rs. " + String.valueOf(eFineAmount));
                                break;

                            case f:
                                double fFineAmount = 5000 + 0.1 * 5000;
                                value.setText("Rs. " + String.valueOf(fFineAmount));
                                break;

                            case g:
                                amount = 4000;
                                double gFineAmount = 4000 + 0.1 * 4000;
                                value.setText("Rs. " + String.valueOf(gFineAmount));
                                break;

                            case h:
                                amount = 3000;
                                double hFineAmount = 3000 + 0.1 * 3000;
                                value.setText("Rs. " + String.valueOf(hFineAmount));
                                break;

                            case i:
                                amount = 22000;
                                double iFineAmount = 22000 + 0.1 * 22000;
                                value.setText("Rs. " + String.valueOf(iFineAmount));
                                break;

                            case j:
                                amount = 30000;
                                double jFineAmount = 30000 + 0.1 * 30000;
                                value.setText("Rs. " + String.valueOf(jFineAmount));
                                break;

                            default:
                                value.setText("null");
                                break;
                        }

                    } else {
                        switch (getCategory) {
                            case b:
                                if (getCC <= 1300) {
                                    amount = 8000;
                                    double bFineAmount = 8000 + 0.1 * 8000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 1300 && getCC < 2001) {
                                    amount = 8000;
                                    double bFineAmount = 8000 + 0.1 * 8000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 2000 && getCC < 2901) {
                                    amount = 11000;
                                    double bFineAmount = 11000 + 0.1 * 11000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 2900 && getCC < 4001) {
                                    amount = 13000;
                                    double bFineAmount = 13000 + 0.1 * 13000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else {
                                    amount = 15000;
                                    double bFineAmount = 15000 + 0.1 * 15000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                }
                                break;

                            case c:
                                amount = 17000;
                                double cFineAmount = 17000 + 0.1 * 17000;
                                value.setText("Rs. " + String.valueOf(cFineAmount));
                                break;

                            case d:
                                amount = 17000;
                                double dFineAmount = 17000 + 0.1 * 17000;
                                value.setText("Rs. " + String.valueOf(dFineAmount));
                                break;

                            case e:
                                amount = 14000;
                                double eFineAmount = 14000 + 0.1 * 14000;
                                value.setText("Rs. " + String.valueOf(eFineAmount));
                                break;

                            case f:
                                amount = 4000;
                                double fFineAmount = 4000 + 0.1 * 4000;
                                value.setText("Rs. " + String.valueOf(fFineAmount));
                                break;

                            case g:
                                amount = 2500;
                                double gFineAmount = 2500 + 0.1 * 2500;
                                value.setText("Rs. " + String.valueOf(gFineAmount));
                                break;

                            case h:
                                amount = 2000;
                                double hFineAmount = 2000 + 0.1 * 2000;
                                value.setText("Rs. " + String.valueOf(hFineAmount));
                                break;

                            case i:
                                amount = 12000;
                                double iFineAmount = 12000 + 0.1 * 12000;
                                value.setText("Rs. " + String.valueOf(iFineAmount));
                                break;

                            case j:
                                amount = 16000;
                                double jFineAmount = 16000 + 0.1 * 16000;
                                value.setText("Rs. " + String.valueOf(jFineAmount));
                                break;

                            default:
                                value.setText("null");
                                break;
                        }
                    }
                } else {
                    finePeriod = "20";
                    if (getType.matches("Private")) {
                        switch (getCategory) {
                            case a:
                                if (getCC <= 125) {
                                    amount = 2500;
                                    double fineAmount = 2500 + 0.2 * 2500;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 125 && getCC < 251) {
                                    amount = 4000;
                                    double fineAmount = 4000 + 0.2 * 4000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));

                                } else if (getCC > 250 && getCC < 401) {
                                    amount = 8000;
                                    double fineAmount = 8000 + 0.2 * 8000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else {
                                    amount = 15000;
                                    double fineAmount = 15000 + 0.2 * 15000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                }
                                break;

                            case b:
                                if (getCC <= 1000) {
                                    amount = 19000;
                                    double fineAmount = 19000 + 0.2 * 19000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 1000 && getCC < 1501) {
                                    amount = 21000;
                                    double fineAmount = 21000 + 0.2 * 21000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 1500 && getCC < 2001) {
                                    amount = 23000;
                                    double fineAmount = 23000 + 0.2 * 23000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 2000 && getCC < 2501) {
                                    amount = 32000;
                                    double fineAmount = 32000 + 0.2 * 32000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else if (getCC > 2500 && getCC < 2901) {
                                    amount = 37000;
                                    double fineAmount = 37000 + 0.2 * 37000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                } else {
                                    amount = 53000;
                                    double fineAmount = 53000 + 0.2 * 53000;
                                    value.setText("Rs. " + String.valueOf(fineAmount));
                                }
                                break;

                            case c:
                                amount = 35000;
                                double cFineAmount = 35000 + 0.2 * 35000;
                                value.setText("Rs. " + String.valueOf(cFineAmount));
                                break;

                            case d:
                                amount = 35000;
                                double dFineAmount = 35000 + 0.2 * 35000;
                                value.setText("Rs. " + String.valueOf(dFineAmount));
                                break;

                            case e:
                                amount = 25000;
                                double eFineAmount = 25000 + 0.2 * 25000;
                                value.setText("Rs. " + String.valueOf(eFineAmount));
                                break;

                            case f:
                                amount = 5000;
                                double fFineAmount = 5000 + 0.2 * 5000;
                                value.setText("Rs. " + String.valueOf(fFineAmount));
                                break;

                            case g:
                                amount = 4000;
                                double gFineAmount = 4000 + 0.2 * 4000;
                                value.setText("Rs. " + String.valueOf(gFineAmount));
                                break;

                            case h:
                                amount = 3000;
                                double hFineAmount = 3000 + 0.2 * 3000;
                                value.setText("Rs. " + String.valueOf(hFineAmount));
                                break;

                            case i:
                                amount = 22000;
                                double iFineAmount = 22000 + 0.2 * 22000;
                                value.setText("Rs. " + String.valueOf(iFineAmount));
                                break;

                            case j:
                                amount = 30000;
                                double jFineAmount = 30000 + 0.2 * 30000;
                                value.setText("Rs. " + String.valueOf(jFineAmount));
                                break;

                            default:
                                value.setText("null");
                                break;
                        }

                    } else {
                        switch (getCategory) {
                            case b:
                                if (getCC <= 1300) {
                                    amount = 8000;
                                    double bFineAmount = 8000 + 0.2 * 8000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 1300 && getCC < 2001) {
                                    amount = 8000;
                                    double bFineAmount = 8000 + 0.2 * 8000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 2000 && getCC < 2901) {
                                    amount = 11000;
                                    double bFineAmount = 11000 + 0.2 * 11000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else if (getCC > 2900 && getCC < 4001) {
                                    amount = 13000;
                                    double bFineAmount = 13000 + 0.2 * 13000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                } else {
                                    amount = 15000;
                                    double bFineAmount = 15000 + 0.2 * 15000;
                                    value.setText("Rs. " + String.valueOf(bFineAmount));
                                }
                                break;

                            case c:
                                amount = 17000;
                                double cFineAmount = 17000 + 0.2 * 17000;
                                value.setText("Rs. " + String.valueOf(cFineAmount));
                                break;

                            case d:
                                amount = 17000;
                                double dFineAmount = 17000 + 0.2 * 17000;
                                value.setText("Rs. " + String.valueOf(dFineAmount));
                                break;

                            case e:
                                amount = 14000;
                                double eFineAmount = 14000 + 0.2 * 14000;
                                value.setText("Rs. " + String.valueOf(eFineAmount));
                                break;

                            case f:
                                amount = 4000;
                                double fFineAmount = 4000 + 0.2 * 4000;
                                value.setText("Rs. " + String.valueOf(fFineAmount));
                                break;

                            case g:
                                amount = 2500;
                                double gFineAmount = 2500 + 0.2 * 2500;
                                value.setText("Rs. " + String.valueOf(gFineAmount));
                                break;

                            case h:
                                amount = 2000;
                                double hFineAmount = 2000 + 0.2 * 2000;
                                value.setText("Rs. " + String.valueOf(hFineAmount));
                                break;

                            case i:
                                amount = 12000;
                                double iFineAmount = 12000 + 0.2 * 12000;
                                value.setText("Rs. " + String.valueOf(iFineAmount));
                                break;

                            case j:
                                amount = 16000;
                                double jFineAmount = 16000 + 0.2 * 16000;
                                value.setText("Rs. " + String.valueOf(jFineAmount));
                                break;

                            default:
                                value.setText("null");
                                break;
                        }
                    }
                }
            } else {
                finePeriod = "20";
                if (getType.matches("Private")) {
                    switch (getCategory) {
                        case a:
                            if (getCC <= 125) {
                                amount = 2500;
                                double fineAmount = 2500 + 0.2 * 2500;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else if (getCC > 125 && getCC < 251) {
                                amount = 4000;
                                double fineAmount = 4000 + 0.2 * 4000;
                                value.setText("Rs. " + String.valueOf(fineAmount));

                            } else if (getCC > 250 && getCC < 401) {
                                amount = 8000;
                                double fineAmount = 8000 + 0.2 * 8000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else {
                                amount = 15000;
                                double fineAmount = 15000 + 0.2 * 15000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            }
                            break;

                        case b:
                            if (getCC <= 1000) {
                                amount = 19000;
                                double fineAmount = 19000 + 0.2 * 19000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else if (getCC > 1000 && getCC < 1501) {
                                amount = 21000;
                                double fineAmount = 21000 + 0.2 * 21000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else if (getCC > 1500 && getCC < 2001) {
                                amount = 23000;
                                double fineAmount = 23000 + 0.2 * 23000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else if (getCC > 2000 && getCC < 2501) {
                                amount = 32000;
                                double fineAmount = 32000 + 0.2 * 32000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else if (getCC > 2500 && getCC < 2901) {
                                amount = 37000;
                                double fineAmount = 37000 + 0.2 * 37000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            } else {
                                amount = 53000;
                                double fineAmount = 53000 + 0.2 * 53000;
                                value.setText("Rs. " + String.valueOf(fineAmount));
                            }
                            break;

                        case c:
                            amount = 35000;
                            double cFineAmount = 35000 + 0.2 * 35000;
                            value.setText("Rs. " + String.valueOf(cFineAmount));
                            break;

                        case d:
                            amount = 35000;
                            double dFineAmount = 35000 + 0.2 * 35000;
                            value.setText("Rs. " + String.valueOf(dFineAmount));
                            break;

                        case e:
                            amount = 25000;
                            double eFineAmount = 25000 + 0.2 * 25000;
                            value.setText("Rs. " + String.valueOf(eFineAmount));
                            break;

                        case f:
                            amount = 5000;
                            double fFineAmount = 5000 + 0.2 * 5000;
                            value.setText("Rs. " + String.valueOf(fFineAmount));
                            break;

                        case g:
                            amount = 4000;
                            double gFineAmount = 4000 + 0.2 * 4000;
                            value.setText("Rs. " + String.valueOf(gFineAmount));
                            break;

                        case h:
                            amount = 3000;
                            double hFineAmount = 3000 + 0.2 * 3000;
                            value.setText("Rs. " + String.valueOf(hFineAmount));
                            break;

                        case i:
                            amount = 22000;
                            double iFineAmount = 22000 + 0.2 * 22000;
                            value.setText("Rs. " + String.valueOf(iFineAmount));
                            break;

                        case j:
                            amount = 30000;
                            double jFineAmount = 30000 + 0.2 * 30000;
                            value.setText("Rs. " + String.valueOf(jFineAmount));
                            break;

                        default:
                            value.setText("null");
                            break;
                    }

                } else {
                    switch (getCategory) {
                        case b:
                            if (getCC <= 1300) {
                                amount = 8000;
                                double bFineAmount = 8000 + 0.2 * 8000;
                                value.setText("Rs. " + String.valueOf(bFineAmount));
                            } else if (getCC > 1300 && getCC < 2001) {
                                amount = 8000;
                                double bFineAmount = 8000 + 0.2 * 8000;
                                value.setText("Rs. " + String.valueOf(bFineAmount));
                            } else if (getCC > 2000 && getCC < 2901) {
                                amount = 11000;
                                double bFineAmount = 11000 + 0.2 * 11000;
                                value.setText("Rs. " + String.valueOf(bFineAmount));
                            } else if (getCC > 2900 && getCC < 4001) {
                                amount = 13000;
                                double bFineAmount = 13000 + 0.2 * 13000;
                                value.setText("Rs. " + String.valueOf(bFineAmount));
                            } else {
                                amount = 15000;
                                double bFineAmount = 15000 + 0.2 * 15000;
                                value.setText("Rs. " + String.valueOf(bFineAmount));
                            }
                            break;

                        case c:
                            amount = 17000;
                            double cFineAmount = 17000 + 0.2 * 17000;
                            value.setText("Rs. " + String.valueOf(cFineAmount));
                            break;

                        case d:
                            amount = 17000;
                            double dFineAmount = 17000 + 0.2 * 17000;
                            value.setText("Rs. " + String.valueOf(dFineAmount));
                            break;

                        case e:
                            amount = 14000;
                            double eFineAmount = 14000 + 0.2 * 14000;
                            value.setText("Rs. " + String.valueOf(eFineAmount));
                            break;

                        case f:
                            amount = 4000;
                            double fFineAmount = 4000 + 0.2 * 4000;
                            value.setText("Rs. " + String.valueOf(fFineAmount));
                            break;

                        case g:
                            amount = 2500;
                            double gFineAmount = 2500 + 0.2 * 2500;
                            value.setText("Rs. " + String.valueOf(gFineAmount));
                            break;

                        case h:
                            amount = 2000;
                            double hFineAmount = 2000 + 0.2 * 2000;
                            value.setText("Rs. " + String.valueOf(hFineAmount));
                            break;

                        case i:
                            amount = 12000;
                            double iFineAmount = 12000 + 0.2 * 12000;
                            value.setText("Rs. " + String.valueOf(iFineAmount));
                            break;

                        case j:
                            amount = 16000;
                            double jFineAmount = 16000 + 0.2 * 16000;
                            value.setText("Rs. " + String.valueOf(jFineAmount));
                            break;

                        default:
                            value.setText("null");
                            break;
                    }
                }
            }
        }


        AppCompatButton btn = dialog.findViewById(R.id.ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        AppCompatButton details = dialog.findViewById(R.id.view_details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDetails = new Intent(MainActivity.this, FineDetails.class);
                toDetails.putExtra("actualAmount", String.valueOf(amount));
                toDetails.putExtra("finePeriod", finePeriod);
                toDetails.putExtra("total", value.getText().toString());
                Log.e("amount:", String.valueOf(amount));
                startActivity(toDetails);
            }
        });
    }

    @Override
    public void getDeleteCheckDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_confirmation);
        dialog.show();

        TextView text = dialog.findViewById(R.id.taxText);
        text.setText(R.string.delete_check);

        AppCompatButton ok = dialog.findViewById(R.id.ok);
        AppCompatButton cancel = dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getNumber = VehicleAdapter.getNumber;

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                databaseHelper.deleteVehicle(getNumber);
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Vehicle Deleted, swipe down to refresh", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onContactSelected(Vehicle vehicle) {
        Toast.makeText(getApplicationContext(), "Selected: " + vehicle.getName() + ", " + vehicle.getNumber(), Toast.LENGTH_LONG).show();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }

                outRect.bottom = spacing; // item bottom

            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check:
                Intent viewDb = new Intent(MainActivity.this, AndroidDatabaseManager.class);
                startActivity(viewDb);
                break;

            case R.id.action_search:
                return true;

            case R.id.action_reminder:
                Intent reminderActivity = new Intent(MainActivity.this, Reminder.class);
                startActivity(reminderActivity);
                break;

            case android.R.id.home:
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

}
