package com.alibenalidoctor.activities_fragments.activity_patients_detials;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_patients.PatientsActivity;
import com.alibenalidoctor.activities_fragments.activity_reservdetials.ReservDetialsActivity;
import com.alibenalidoctor.adapters.PatientAdapter;
import com.alibenalidoctor.adapters.PatientDetialsAdapter;
import com.alibenalidoctor.databinding.ActivityPatientsBinding;
import com.alibenalidoctor.databinding.ActivityPatientsDetialsBinding;
import com.alibenalidoctor.language.Language;
import com.alibenalidoctor.models.ReservationDataModel;
import com.alibenalidoctor.models.ReservationDataModel;
import com.alibenalidoctor.models.ReservationModel;
import com.alibenalidoctor.models.UserModel;
import com.alibenalidoctor.preferences.Preferences;
import com.alibenalidoctor.remote.Api;
import com.alibenalidoctor.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientsDetialsActivity extends AppCompatActivity {
    private ActivityPatientsDetialsBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;


    private List<ReservationModel> list;
    private PatientDetialsAdapter patientDetialsAdapter;
    private String type;
    private String reservid;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patients_detials);
        getDataFromIntent();
        initView();
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            reservid = intent.getStringExtra("data");

        }
    }

    private void initView() {
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.progBar.setVisibility(View.GONE);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        patientDetialsAdapter = new PatientDetialsAdapter(list, this);
        binding.recView.setAdapter(patientDetialsAdapter);
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
    }


    public void show(int reservid) {
//        binding.recView.setVisibility(View.GONE);
//        binding.llNoData.setVisibility(View.VISIBLE);
        Intent intent = new Intent(PatientsDetialsActivity.this, ReservDetialsActivity.class);
        intent.putExtra("type", "patient");
        intent.putExtra("data", reservid + "");
        startActivity(intent);
    }

    public void getData() {

        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);
        list.clear();
        patientDetialsAdapter.notifyDataSetChanged();
        if (userModel == null) {
            binding.swipeRefresh.setRefreshing(false);
            binding.progBar.setVisibility(View.GONE);
            binding.tvNoData.setVisibility(View.VISIBLE);
            return;
        }
        Api.getService(Tags.base_url).patientDetails(lang, userModel.getUser().getId() + "",reservid).
                enqueue(new Callback<ReservationDataModel>() {
                    @Override
                    public void onResponse(Call<ReservationDataModel> call, Response<ReservationDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null && response.body().getStatus() == 200) {
                                if (response.body().getData().size() > 0) {
                                    binding.tvNoData.setVisibility(View.GONE);
                                    list.addAll(response.body().getData());
                                    patientDetialsAdapter.notifyDataSetChanged();
                                } else {
                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                            }


                        } else {
                            binding.tvNoData.setVisibility(View.VISIBLE);


                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ReservationDataModel> call, Throwable t) {
                        try {
                            binding.tvNoData.setVisibility(View.VISIBLE);

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


    @Override
    public void onBackPressed() {
        finish();
    }
}