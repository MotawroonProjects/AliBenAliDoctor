package com.alibenalidoctor.activities_fragments.activity_changepassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_home.HomeActivity;
import com.alibenalidoctor.databinding.ActivityChangePasswordBinding;
import com.alibenalidoctor.language.Language;
import com.alibenalidoctor.models.ChangePasswordModel;
import com.alibenalidoctor.preferences.Preferences;
import com.alibenalidoctor.share.Common;

import io.paperdb.Paper;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    private String lang;
    private ChangePasswordModel changePasswordModel;
    private Preferences preferences;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        changePasswordModel = new ChangePasswordModel();
        binding.setModel(changePasswordModel);


    }


    private void login() {

       /* ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .login(loginModel.getPhone_code(),loginModel.getPhone(),loginModel.getPassword())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                    preferences.create_update_userdata(LoginActivity.this, response.body());
                                    preferences.create_update_session(LoginActivity.this, Tags.session_login);
                                    navigateToHomeActivity();

                            } else if (response.body().getStatus() == 401) {
                                Toast.makeText(LoginActivity.this,getResources().getString(R.string.incorrect_phone_pass),Toast.LENGTH_LONG).show();
                            }
                            else if (response.body().getStatus() == 409) {
                                Toast.makeText(LoginActivity.this,getResources().getString(R.string.user_blocked),Toast.LENGTH_LONG).show();
                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("mmmmmmmmmm", response.code() + "");

                                Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.toString() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });*/

    }


}