package com.alibenalidoctor.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
   private int id;
   private String title_ar;
   private String message_ar;
   private String title_en;
   private String message_en;
   private String user_id;
   private int doctor_id;
   private String date;
   private int reservation_id;
   private String created_at;
   private String updated_at;
   private String title;
   private String message;

    public int getId() {
        return id;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public String getMessage_ar() {
        return message_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getMessage_en() {
        return message_en;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public String getDate() {
        return date;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
