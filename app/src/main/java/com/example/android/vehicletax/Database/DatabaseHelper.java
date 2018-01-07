package com.example.android.vehicletax.Database;


/**
 * Created by Shaakya on 8/31/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.vehicletax.Vehicle;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "VehicleManager.db";

    // User table name
    public static final String TABLE_VEHICLE = "vehicle";

    // User Table Columns names
    private static final String COLUMN_VEHICLE_ID = "vehicle_id";
    private static final String COLUMN_VEHICLE_NAME = "vehicle_name";
    public static final String COLUMN_VEHICLE_NUMBER = "vehicle_number";
    public static final String COLUMN_VEHICLE_CC = "vehicle_cc";
    public static final String COLUMN_VEHICLE_YEAR = "vehicle_year";
    public static final String COLUMN_VEHICLE_TYPE = "vehicle_type";
    public static final String COLUMN_VEHICLE_FUEL = "vehicle_fuel";
    public static final String COLUMN_VEHICLE_CATEGORY = "vehicle_category";
    public static final String COLUMN_VEHICLE_DATE = "vehicle_date";


    // create table sql query
    private String CREATE_VEHICLE_TABLE = "CREATE TABLE " + TABLE_VEHICLE + "("
            + COLUMN_VEHICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_VEHICLE_NAME + " TEXT,"
            + COLUMN_VEHICLE_NUMBER + " TEXT," + COLUMN_VEHICLE_CC + " TEXT," + COLUMN_VEHICLE_YEAR + " TEXT,"
            + COLUMN_VEHICLE_TYPE + " TEXT," + COLUMN_VEHICLE_FUEL + " TEXT," + COLUMN_VEHICLE_CATEGORY + " TEXT,"
            + COLUMN_VEHICLE_DATE + " TEXT" + ")";

    // drop table sql query
    private String DROP_VEHICLE_TABLE = "DROP TABLE IF EXISTS " + TABLE_VEHICLE;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_VEHICLE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop Vehicle Table if exist
        db.execSQL(DROP_VEHICLE_TABLE);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create vehicle record
     *
     * @param vehicle
     */
    public void addVehicle(Vehicle vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VEHICLE_NAME, vehicle.getName());
        values.put(COLUMN_VEHICLE_NUMBER, vehicle.getNumber());
        values.put(COLUMN_VEHICLE_CC, vehicle.getCc());
        values.put(COLUMN_VEHICLE_YEAR, vehicle.getYear());
        values.put(COLUMN_VEHICLE_TYPE, vehicle.getType());
        values.put(COLUMN_VEHICLE_FUEL, vehicle.getFuel());
        values.put(COLUMN_VEHICLE_CATEGORY, vehicle.getCategory());
        values.put(COLUMN_VEHICLE_DATE, vehicle.getDate());

        // Inserting Row
        db.insert(TABLE_VEHICLE, null, values);
        db.close();
    }

    /**
     * This method is to fetch all vehicle and return the list of vehicle records
     *
     * @return list
     */
    public List<Vehicle> getAllVehicle() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_VEHICLE_ID,
                COLUMN_VEHICLE_NAME,
                COLUMN_VEHICLE_NUMBER,
                COLUMN_VEHICLE_CC,
                COLUMN_VEHICLE_YEAR,
                COLUMN_VEHICLE_TYPE,
                COLUMN_VEHICLE_FUEL,
                COLUMN_VEHICLE_CATEGORY,
                COLUMN_VEHICLE_DATE
        };
        // sorting orders
        String sortOrder =
                COLUMN_VEHICLE_NAME + " ASC";
        List<Vehicle> vehicleList = new ArrayList<Vehicle>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the vehicle table
        /**
         * Here query function is used to fetch records from vehicle table this function works like we use sql query.
      */
        Cursor cursor = db.query(TABLE_VEHICLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_ID))));
                vehicle.setName(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_NAME)));
                vehicle.setNumber(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_NUMBER)));
                vehicle.setCc(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_CC)));
                vehicle.setYear(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_YEAR)));
                vehicle.setType(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_TYPE)));
                vehicle.setFuel(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_FUEL)));
                vehicle.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_CATEGORY)));
                vehicle.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_DATE)));
                // Adding vehicle record to list
                vehicleList.add(vehicle);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return vehicle list
        return vehicleList;
    }

    /**
     * This method to update vehicle record
     *
     * @param vehicle
     */
    public void updateVehicle(Vehicle vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VEHICLE_NAME, vehicle.getName());
        values.put(COLUMN_VEHICLE_NUMBER, vehicle.getNumber());
        values.put(COLUMN_VEHICLE_CC, vehicle.getCc());
        values.put(COLUMN_VEHICLE_YEAR, vehicle.getYear());
        values.put(COLUMN_VEHICLE_TYPE, vehicle.getType());
        values.put(COLUMN_VEHICLE_FUEL, vehicle.getFuel());
        values.put(COLUMN_VEHICLE_CATEGORY, vehicle.getCategory());
        values.put(COLUMN_VEHICLE_DATE, vehicle.getDate());
        // updating row
        db.update(TABLE_VEHICLE, values, COLUMN_VEHICLE_NUMBER + " = ?",
                new String[]{vehicle.getNumber()});
        db.close();
    }

    /**
     * This method is to delete vehicle record
     *
     * @param number
     */
    public void deleteVehicle(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_VEHICLE, COLUMN_VEHICLE_NUMBER + " = ?",
                new String[]{number});
        db.close();
    }

    /**
     * This method to check vehicle exist or not
     *
     * @param number
     * @return true/false
     */
    public boolean checkVehicle(String number) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_VEHICLE_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_VEHICLE_NUMBER + " = ?";

        // selection argument
        String[] selectionArgs = {number};

        // query vehicle table with condition
        /**
         * Here query function is used to fetch records from vehicle table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT vehicle_id FROM vehicle WHERE vehicle_number = 'something';
         */
        Cursor cursor = db.query(TABLE_VEHICLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    /**
     * This method to check vehicle exist or not
     * @return true/false

    public boolean checkUser(String username, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {username, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
    */

    public Vehicle getVehicle(String number){

        String[] columns = {
                COLUMN_VEHICLE_ID,
                COLUMN_VEHICLE_NAME,
                COLUMN_VEHICLE_NUMBER,
                COLUMN_VEHICLE_CC,
                COLUMN_VEHICLE_YEAR,
                COLUMN_VEHICLE_TYPE,
                COLUMN_VEHICLE_FUEL,
                COLUMN_VEHICLE_CATEGORY,
                COLUMN_VEHICLE_DATE
        };

        String selection = COLUMN_VEHICLE_NUMBER + " = ?";

        String[] selectionArgs = {number};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_VEHICLE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null)
            cursor.moveToFirst();

        Vehicle vehicle = new Vehicle();
        vehicle.setId(Integer.parseInt(cursor.getString(0)));
        vehicle.setName(cursor.getString(1));
        vehicle.setNumber(cursor.getString(2));
        vehicle.setCc(cursor.getString(3));
        vehicle.setYear(cursor.getString(4));
        vehicle.setType(cursor.getString(5));
        vehicle.setFuel(cursor.getString(6));
        vehicle.setCategory(cursor.getString(7));
        vehicle.setDate(cursor.getString(8));

        return vehicle;

    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

}

