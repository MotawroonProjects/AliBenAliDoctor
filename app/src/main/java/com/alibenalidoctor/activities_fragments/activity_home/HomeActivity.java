package com.alibenalidoctor.activities_fragments.activity_home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_login.LoginActivity;
import com.alibenalidoctor.databinding.ActivityHomeBinding;
import com.alibenalidoctor.interfaces.Listeners;
import com.alibenalidoctor.language.Language;
import com.alibenalidoctor.models.UserModel;
import com.alibenalidoctor.preferences.Preferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements Listeners.HomeListener {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private UserModel userModel;
    private String lang;
    private ActionBarDrawerToggle toggle;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();

    }


    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        if (userModel != null) {
            //   binding.setUsermodel(userModel);
        }
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setAction(this);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close);
        toggle.setDrawerIndicatorEnabled(false);

        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.drawer.isDrawerVisible(GravityCompat.START)) {
                    binding.drawer.closeDrawer(GravityCompat.START);
                } else {
                    binding.drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();
        binding.toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new MainCategoryAdapter(mainDepartmentsList, this);
//        binding.recView.setAdapter(adapter);


        if (userModel != null) {
//            EventBus.getDefault().register(this);
//            getNotificationCount();
//            updateTokenFireBase();
//            updateLocation();
        }


    }


//    private void getNotificationCount() {
//        Api.getService(Tags.base_url)
//                .getUnreadNotificationCount("Bearer " + userModel.getData().getToken(), userModel.getData().getId())
//                .enqueue(new Callback<NotificationCount>() {
//                    @Override
//                    public void onResponse(Call<NotificationCount> call, Response<NotificationCount> response) {
//                        if (response.isSuccessful()) {
//                            Log.e("count", response.body().getCount() + "_");
//                            binding.setNotCount(response.body().getCount());
//                        } else {
//                            try {
//                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            if (response.code() == 500) {
//                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<NotificationCount> call, Throwable t) {
//                        try {
//                            if (t.getMessage() != null) {
//                                Log.e("error_not_code", t.getMessage() + "__");
//
//                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                    Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } catch (Exception e) {
//                            Log.e("Error", e.getMessage() + "__");
//                        }
//                    }
//                });
//    }
//
//    private void readNotificationCount() {
//        Api.getService(Tags.base_url)
//                .readNotification("Bearer " + userModel.getData().getToken(), userModel.getData().getId())
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            binding.setNotCount(0);
//                            Log.e("bb", "bb");
//                        } else {
//                            try {
//                                Log.e("error", response.code() + "__" + response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            if (response.code() == 500) {
//                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        try {
//                            if (t.getMessage() != null) {
//                                Log.e("error", t.getMessage() + "__");
//
//                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                    Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } catch (Exception e) {
//                            Log.e("Error", e.getMessage() + "__");
//                        }
//                    }
//                });
//    }


//    private void updateTokenFireBase() {
//
//
//        FirebaseInstanceId.getInstance()
//                .getInstanceId().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                String token = task.getResult().getToken();
//
//                try {
//
//                    try {
//
//                        Api.getService(Tags.base_url)
//                                .updatePhoneToken("Bearer " + userModel.getData().getToken(), token, userModel.getData().getId(), 1)
//                                .enqueue(new Callback<ResponseBody>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                        if (response.isSuccessful() && response.body() != null) {
//                                            Log.e("token", "updated successfully");
//                                        } else {
//                                            try {
//
//                                                Log.e("error", response.code() + "_" + response.errorBody().string());
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                        try {
//
//                                            if (t.getMessage() != null) {
//                                                Log.e("error", t.getMessage());
//                                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                                    Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//
//                                        } catch (Exception e) {
//                                        }
//                                    }
//                                });
//                    } catch (Exception e) {
//
//
//                    }
//                } catch (Exception e) {
//
//
//                }
//
//            }
//        });
//    }


    public void logout() {
//        if (userModel != null) {
//
//
//            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
//            dialog.show();
//
//
//            FirebaseInstanceId.getInstance()
//                    .getInstanceId().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    String token = task.getResult().getToken();
//
//                    Api.getService(Tags.base_url)
//                            .logout("Bearer " + userModel.getData().getToken(), token)
//                            .enqueue(new Callback<ResponseBody>() {
//                                @Override
//                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                    dialog.dismiss();
//                                    if (response.isSuccessful()) {
//                                        preferences.clear(HomeActivity.this);
//                                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                                        if (manager != null) {
//                                            manager.cancel(Tags.not_tag, Tags.not_id);
//                                        }
//                                        navigateToSignInActivity();
//
//
//                                    } else {
//                                        dialog.dismiss();
//                                        try {
//                                            Log.e("error", response.code() + "__" + response.errorBody().string());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        if (response.code() == 500) {
//                                            Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                    try {
//                                        dialog.dismiss();
//                                        if (t.getMessage() != null) {
//                                            Log.e("error", t.getMessage() + "__");
//
//                                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                                Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        Log.e("Error", e.getMessage() + "__");
//                                    }
//                                }
//                            });
//
//                }
//            });
//
//
//        } else {
//            navigateToSignInActivity();
//        }

    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 1050);


    }


    @Override
    public void onBackPressed() {
        finish();
    }


    private void navigateToSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    public void status() {

    }

    @Override
    public void profile() {

    }

    @Override
    public void patient() {

    }

    @Override
    public void setting() {

    }

    @Override
    public void notification() {

    }

    @Override
    public void langChange() {

    }
}
