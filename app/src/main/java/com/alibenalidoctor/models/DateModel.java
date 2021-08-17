package com.alibenalidoctor.models;

import java.io.Serializable;
import java.util.List;

public class DateModel implements Serializable {
    private String date;
    private String day_number;
    private String day_text;
    private String month;

    public String getDate() {
        return date;
    }

    public String getDay_number() {
        return day_number;
    }

    public String getDay_text() {
        return day_text;
    }

    public String getMonth() {
        return month;
    }
}
