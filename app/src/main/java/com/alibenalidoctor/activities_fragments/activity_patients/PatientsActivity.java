package com.alibenalidoctor.activities_fragments.activity_patients;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_reservdetials.ReservDetialsActivity;
import com.alibenalidoctor.adapters.PatientAdapter;
import com.alibenalidoctor.databinding.ActivityPatientsBinding;
import com.alibenalidoctor.language.Language;
import com.alibenalidoctor.models.UserModel;
import com.alibenalidoctor.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class PatientsActivity extends AppCompatActivity {
    private ActivityPatientsBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;


    private List<Object> list;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patients);
        initView();
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
        binding.recView.setAdapter(new PatientAdapter(list, this));
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void show() {
        Intent intent = new Intent(PatientsActivity.this, ReservDetialsActivity.class);
        intent.putExtra("type", "patient");
        startActivity(intent);
    }

    public void setItemData(Object o) {
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}