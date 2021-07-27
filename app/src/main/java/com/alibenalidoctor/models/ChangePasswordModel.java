package com.alibenalidoctor.models;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.alibenalidoctor.BR;
import com.alibenalidoctor.R;

import java.io.Serializable;


public class ChangePasswordModel extends BaseObservable implements Serializable {


    private String password;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();


    public ChangePasswordModel() {

        this.password = "";


    }


    public boolean isStepValid(Context context) {
        if (
                !password.isEmpty() && password.length() >= 6
        ) {
            error_email.set(null);
            error_password.set(null);
            error_phone.set(null);
            return true;
        } else {

            if (password.isEmpty()) {
                error_password.set(context.getString(R.string.field_req));
            } else if (password.length() < 6) {
                error_password.set(context.getString(R.string.password_short));
            } else {
                error_password.set(null);
            }

            return false;
        }
    }


    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }


}
