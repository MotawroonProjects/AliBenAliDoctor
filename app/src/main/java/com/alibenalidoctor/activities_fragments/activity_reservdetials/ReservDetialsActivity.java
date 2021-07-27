package com.alibenalidoctor.activities_fragments.activity_reservdetials;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalidoctor.R;
import com.alibenalidoctor.adapters.ImagesAdapter;
import com.alibenalidoctor.adapters.PatientAdapter;
import com.alibenalidoctor.databinding.ActivityPatientsBinding;
import com.alibenalidoctor.databinding.ActivityReservdetialsBinding;
import com.alibenalidoctor.language.Language;
import com.alibenalidoctor.models.UserModel;
import com.alibenalidoctor.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ReservDetialsActivity extends AppCompatActivity {
    private ActivityReservdetialsBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;


    private List<Object> list;
    private String type;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservdetials);
        getDataFromIntent();
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
        if (type.equals("patient")) {
            binding.btnCall.setVisibility(View.GONE);
        }
        binding.recView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(new ImagesAdapter(list, this));
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");

        }
    }

    public void show() {

    }

    public void setItemData(Object o) {
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}