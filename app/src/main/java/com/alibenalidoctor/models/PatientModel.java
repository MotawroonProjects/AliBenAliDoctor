package com.alibenalidoctor.models;

import java.io.Serializable;
import java.util.List;

public class PatientModel implements Serializable {
    public int id;
    public String name;
    public String phone_code;
    public String phone;
    public String is_login;
    public String created_at;
    public String updated_at;
    public List<LastReservation> last_reservation;
    public LastDate last_date;


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


    public List<LastReservation> getLast_reservation() {
        return last_reservation;
    }

    public LastDate getLast_date() {
        return last_date;
    }

    public class LastReservation{
        public int id;
        public int doctor_id;
        public int offer_id;
        public int date_id;
        public int hour_id;
        public int disease_id;
        public int user_id;
        public String name;
        public String phone;
        public String report_text;
        public String type;
        public String call_type;
        public String call_start;
        public String gender;
        public int age;
        public String image;
        public String status;
        public String is_deleted;
        public String created_at;
        public String updated_at;
        public String can_deleted;
        public String can_canceled;
        public String can_updated;
        public Doctor doctor;
        public User user;
        public Date date;
        public Hour hour;
        public List<ReservationDiseas> reservation_diseases;
        public List<File> files;
        public List<Report> reports;

        public class Doctor{
            public int id;
            public String email;
            public String phone;
            public String type;
            public String name_ar;
            public String name_en;
            public String category_ar;
            public String category_en;
            public String image;
            public int country_id;
            public int clinic_id;
            public String min_age;
            public String max_age;
            public String audio;
            public String video;
            public String cv_image;
            public String address;
            public String latitude;
            public String longitugde;
            public String status;
            public int price;
            public int rate;
            public String is_advisory;
            public String advisory_languages;
            public String has_home_visit;
            public String created_at;
            public String updated_at;
            public String name;
            public String category;
        }

        public class User{
            public int id;
            public String name;
            public String phone_code;
            public String phone;
            public String is_login;
            public String created_at;
            public String updated_at;
        }

        public class Date{
            public int id;
            public int doctor_id;
            public Object offer_id;
            public String date;
            public String is_reserved;
            public String created_at;
            public String updated_at;
            public String day_number;
            public String day_text;
            public String month;
        }

        public class Hour{
            public int id;
            public int date_id;
            public String hour;
            public String is_reserved;
            public String created_at;
            public String updated_at;
            public String phone_hour;
            public String period;
        }



        public class ReservationDiseas{
            public int id;
            public int disease_id;
            public int reservation_id;
            public String created_at;
            public String updated_at;
            public Diseases diseases;
            public class Diseases{
                public int id;
                public String title_ar;
                public String title_en;
                public String created_at;
                public String updated_at;
                public String title;
            }
        }

        public class File{
            public int id;
            public int reservation_id;
            public String file;
            public String created_at;
            public String updated_at;
        }

        public class Report{
            public int id;
            public int reservation_id;
            public String report;
            public String created_at;
            public String updated_at;
        }
    }
    public class LastDate{
        public int id;
        public int doctor_id;
        public Object offer_id;
        public String date;
        public String is_reserved;
        public String created_at;
        public String updated_at;
        public String day_number;
        public String day_text;
        public String month;

        public int getId() {
            return id;
        }

        public int getDoctor_id() {
            return doctor_id;
        }

        public Object getOffer_id() {
            return offer_id;
        }

        public String getDate() {
            return date;
        }

        public String getIs_reserved() {
            return is_reserved;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
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
}
