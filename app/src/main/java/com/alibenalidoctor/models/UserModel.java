package com.alibenalidoctor.models;

import java.io.Serializable;
import java.util.List;

public class UserModel extends StatusResponse implements Serializable{

    private User data;

    public User getUser() {
        return data;
    }

    public int getStatus() {
        return code;
    }

    public static class User implements Serializable {
        private int id;
        private String email;
        private String phone;
        private String type;
        private String name_ar;
        private String name_en;
        private String category_ar;
        private String category_en;
        private String image;
        private int country_id;
        private int clinic_id;
        private String min_age;
        private String max_age;
        private String address;
        private double latitude;
        private double longitude;
        private String status;
        private double price;
        private double rate;
        private String is_advisory;
        private String advisory_languages;
        private String has_home_visit;
        private String created_at;
        private String updated_at;
        private String name;
        private String category;
        private String firebaseToken;
        private List<RateModel> rates;

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getType() {
            return type;
        }

        public String getName_ar() {
            return name_ar;
        }

        public String getName_en() {
            return name_en;
        }

        public String getCategory_ar() {
            return category_ar;
        }

        public String getCategory_en() {
            return category_en;
        }

        public String getImage() {
            return image;
        }

        public int getCountry_id() {
            return country_id;
        }

        public int getClinic_id() {
            return clinic_id;
        }

        public String getMin_age() {
            return min_age;
        }

        public String getMax_age() {
            return max_age;
        }

        public String getAddress() {
            return address;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getStatus() {
            return status;
        }

        public double getPrice() {
            return price;
        }

        public double getRate() {
            return rate;
        }

        public String getIs_advisory() {
            return is_advisory;
        }

        public String getAdvisory_languages() {
            return advisory_languages;
        }

        public String getHas_home_visit() {
            return has_home_visit;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public String getFirebaseToken() {
            return firebaseToken;
        }

        public void setFirebaseToken(String firebaseToken) {
            this.firebaseToken = firebaseToken;
        }

        public List<RateModel> getRates() {
            return rates;
        }
    }


}
