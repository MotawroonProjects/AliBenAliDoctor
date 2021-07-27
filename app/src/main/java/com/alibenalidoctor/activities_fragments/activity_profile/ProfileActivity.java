package com.alibenalidoctor.activities_fragments.activity_profile;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.alibenalidoctor.R;
import com.alibenalidoctor.adapters.RateAdapter;
import com.alibenalidoctor.databinding.ActivityProfileBinding;
import com.alibenalidoctor.language.Language;
import com.alibenalidoctor.models.UserModel;
import com.alibenalidoctor.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private String lang;
    private RateAdapter adapter;
    private List<Object> list;
    private Preferences preferences;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
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
        binding.recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new RateAdapter(list, this);
        binding.recView.setAdapter(adapter);


        binding.llBack.setOnClickListener(view -> finish());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}