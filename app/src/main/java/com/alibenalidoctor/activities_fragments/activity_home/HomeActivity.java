package com.alibenalidoctor.activities_fragments.activity_home;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_language.LanguageActivity;
import com.alibenalidoctor.activities_fragments.activity_login.LoginActivity;
import com.alibenalidoctor.activities_fragments.activity_notification.NotificationActivity;
import com.alibenalidoctor.activities_fragments.activity_patients.PatientsActivity;
import com.alibenalidoctor.activities_fragments.activity_profile.ProfileActivity;
import com.alibenalidoctor.activities_fragments.activity_reservdetials.ReservDetialsActivity;
import com.alibenalidoctor.adapters.ReservisionAdapter;
import com.alibenalidoctor.adapters.TimeAdapter;
import com.alibenalidoctor.databinding.ActivityHomeBinding;
import com.alibenalidoctor.interfaces.Listeners;
import com.alibenalidoctor.language.Language;
import com.alibenalidoctor.models.DateDataModel;
import com.alibenalidoctor.models.DateModel;
import com.alibenalidoctor.models.ReservationDataModel;
import com.alibenalidoctor.models.ReservationModel;
import com.alibenalidoctor.models.StatusResponse;
import com.alibenalidoctor.models.UserModel;
import com.alibenalidoctor.preferences.Preferences;
import com.alibenalidoctor.remote.Api;
import com.alibenalidoctor.share.Common;
import com.alibenalidoctor.tags.Tags;
import com.google.firebase.iid.FirebaseInstanceId;


import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.result.contract.ActivityResultContracts;

public class HomeActivity extends AppCompatActivity implements Listeners.HomeListener {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private ActionBarDrawerToggle toggle;
    private List<ReservationModel> list;
    private ReservisionAdapter reservisionAdapter;
    private TimeAdapter adapter;
    private List<DateModel> dateModelList;
    private ActivityResultLauncher<Intent> launcher;

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
        list = new ArrayList<>();
        dateModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        if (userModel != null) {
            binding.setModel(userModel);
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
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapter = new TimeAdapter(dateModelList, this);
        binding.recViewCategory.setAdapter(adapter);
        binding.progBarCategory.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.GONE);
        reservisionAdapter = new ReservisionAdapter(list, this);
        binding.recView.setAdapter(reservisionAdapter);
        binding.swipeRefresh.setOnRefreshListener(this::getDates);
//        adapter = new MainCategoryAdapter(mainDepartmentsList, this);
//        binding.recView.setAdapter(adapter);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                lang = result.getData().getStringExtra("lang");
                refreshActivity(lang);
            }

        });

        if (userModel != null) {
//            EventBus.getDefault().register(this);
//            getNotificationCount();
            updateTokenFireBase();
//            updateLocation();
        }
        getDates();

    }

    private void getDates() {
//        Log.e("dlkkdkdk",userModel.getUser().getId()+"");
        binding.swipeRefresh.setRefreshing(false);
        try {
            binding.progBarCategory.setVisibility(View.VISIBLE);

            if (userModel == null) {
                binding.progBarCategory.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);

                return;
            }
            Api.getService(Tags.base_url)
                    .getDates(lang)
                    .enqueue(new Callback<DateDataModel>() {
                        @Override
                        public void onResponse(Call<DateDataModel> call, Response<DateDataModel> response) {
                            binding.progBarCategory.setVisibility(View.GONE);
                            binding.swipeRefresh.setRefreshing(false);
                            if (response.isSuccessful() && response.body() != null) {
                                // Log.e("suuuu",response.body().getStatus()+"");
                                if (response.body().getStatus() == 200) {
                                    //Log.e("llll",response.body().getData().size()+"");
                                    if (response.body().getData().size() > 0) {
                                        dateModelList.clear();
                                        dateModelList.addAll(response.body().getData());
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        dateModelList.clear();
                                        adapter.notifyDataSetChanged();
                                        binding.llNoData.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                }
                            } else {
                                binding.progBarCategory.setVisibility(View.GONE);
                                binding.swipeRefresh.setRefreshing(false);

                                if (response.code() == 500) {


                                } else {

                                    try {

                                        Log.e("errorsss", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DateDataModel> call, Throwable t) {
                            try {
                                binding.progBarCategory.setVisibility(View.GONE);
                                binding.swipeRefresh.setRefreshing(false);

                                if (t.getMessage() != null) {
                                    Log.e("errorsss", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    } else {
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    public void getData(String date) {

        binding.progBar.setVisibility(View.VISIBLE);
        binding.llNoData.setVisibility(View.GONE);
        list.clear();
        reservisionAdapter.notifyDataSetChanged();
        if (userModel == null) {
            binding.swipeRefresh.setRefreshing(false);
            binding.progBar.setVisibility(View.GONE);
            binding.llNoData.setVisibility(View.VISIBLE);
            return;
        }
        Log.e("ldkldkkd", userModel.getUser().getId() + " " + date + " " + lang);
        Api.getService(Tags.base_url).myReservation(lang, userModel.getUser().getId() + "", date).
                enqueue(new Callback<ReservationDataModel>() {
                    @Override
                    public void onResponse(Call<ReservationDataModel> call, Response<ReservationDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null && response.body().getStatus() == 200) {
                                if (response.body().getData().size() > 0) {
                                    binding.llNoData.setVisibility(View.GONE);
                                    list.addAll(response.body().getData());
                                    reservisionAdapter.notifyDataSetChanged();
                                } else {
                                    binding.llNoData.setVisibility(View.VISIBLE);

                                }
                            }


                        } else {
                            binding.llNoData.setVisibility(View.VISIBLE);


                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ReservationDataModel> call, Throwable t) {
                        try {
                            binding.llNoData.setVisibility(View.VISIBLE);

                            binding.swipeRefresh.setRefreshing(false);
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //     Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //  Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
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


    private void updateTokenFireBase() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        try {
                            Api.getService(Tags.base_url)
                                    .updatePhoneToken(token, userModel.getUser().getId(), "android")
                                    .enqueue(new Callback<StatusResponse>() {
                                        @Override
                                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                            if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                                                userModel.getUser().setFirebaseToken(token);
                                                preferences.create_update_userdata(HomeActivity.this, userModel);

                                                Log.e("token", "updated successfully");
                                            } else {
                                                try {

                                                    Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<StatusResponse> call, Throwable t) {
                                            try {

                                                if (t.getMessage() != null) {
                                                    Log.e("errorToken2", t.getMessage());

                                                }

                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        } catch (Exception e) {

                        }
                    }
                });
    }

    @Override
    public void logout() {

        if (userModel == null) {
            finish();
            return;
        }
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .logout(userModel.getUser().getId() + "", userModel.getUser().getFirebaseToken())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                manager.cancel(Tags.not_tag, Tags.not_id);
                                navigateToSignInActivity();
                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else {
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

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
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void patient() {
        Intent intent = new Intent(this, PatientsActivity.class);
        startActivity(intent);
    }

    @Override
    public void setting() {

    }

    @Override
    public void notification() {

        Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void langChange() {

        Intent intent = new Intent(this, LanguageActivity.class);
        launcher.launch(intent);
    }

    public void show(int reservid) {
//        binding.recView.setVisibility(View.GONE);
//        binding.llNoData.setVisibility(View.VISIBLE);
        Intent intent = new Intent(HomeActivity.this, ReservDetialsActivity.class);
        intent.putExtra("type", "reserv");
        intent.putExtra("data", reservid + "");
        startActivity(intent);
    }
}
