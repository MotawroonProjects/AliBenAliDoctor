package com.alibenalidoctor.models;

import java.io.Serializable;
import java.util.List;

public class PatientModel implements Serializable {
    private int id;
    private String name;
    private String phone_code;
    private String phone;
    private String is_login;
    private String created_at;
    private String updated_at;
    private ReservationModel last_reservation;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getIs_login() {
        return is_login;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public ReservationModel getLast_reservation() {
        return last_reservation;
    }
}
