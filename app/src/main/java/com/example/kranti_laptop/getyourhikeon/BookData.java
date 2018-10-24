package com.example.kranti_laptop.getyourhikeon;

import android.widget.TextView;

public class BookData {
    String location,date,time;

    public BookData(String location, String date, String time) {
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
