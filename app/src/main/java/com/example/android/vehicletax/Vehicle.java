package com.example.android.vehicletax;

/**
 * Created by Shaakya on 12/5/2017.
 */

public class Vehicle {
    private int id;
    String name, number, cc, year, type, fuel, category, date;
    //int thumbnail;
    public Vehicle() {

    }

    public Vehicle(String number, String cc, String year, String type, String fuel, String name, int id) {
        this.number = number;
        this.cc = cc;
        this.type = type;
        this.year = year;
        this.fuel = fuel;
        this.name = name;
        this.id = id;
        //this.thumbnail = thumbnail;
    }

    public Vehicle(String number, String cc, String year, String type, String fuel, String name, String category, String date) {
        this.number = number;
        this.cc = cc;
        this.type = type;
        this.year = year;
        this.fuel = fuel;
        this.name = name;
        this.category = category;
        this.date = date;
        //this.thumbnail = thumbnail;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCc() {
        return cc;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /*
    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    */


}
