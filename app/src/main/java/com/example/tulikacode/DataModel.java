package com.example.tulikacode;

public class DataModel {
    public String getTitle() {
        return title;
    }

    public DataModel(String title, String c_date) {
        this.title = title;
        this.c_date = c_date;
    }

    public String getC_date() {
        return c_date;
    }

    String title ,c_date;
}
