package com.alibenalidoctor.activities_fragments.activity_sign_up;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_login.LoginActivity;
import com.alibenalidoctor.activities_fragments.activity_sign_up.fragments.FragmentSignUp1;
import com.alibenalidoctor.activities_fragments.activity_sign_up.fragments.FragmentSignUp2;
import com.alibenalidoctor.activities_fragments.activity_sign_up.fragments.FragmentSignUp3;
import com.alibenalidoctor.databinding.ActivitySignUpBinding;
import com.alibenalidoctor.language.Language;
import com.alibenalidoctor.models.SignUpModel;
import com.alibenalidoctor.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private FragmentManager fragmentManager;

    private SignUpModel signUpModel;

    private Preferences preferences;
    private FragmentSignUp1 fragmentSignUp1;
    private FragmentSignUp2 fragmentSignUp2;
    private FragmentSignUp3 fragmentSignUp3;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();

    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        preferences = Preferences.getInstance();
        fragmentManager = getSupportFragmentManager();
        signUpModel = new SignUpModel();
        displayFragment1();
        binding.btnNext.setOnClickListener(view -> {
            manageData(false);
        });

        binding.btnPrevious.setOnClickListener(view -> {
            manageData(true);
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onBackPressed() {
        manageData(true);
    }

    public void manageData(boolean isBack) {
        if (fragmentSignUp1 != null && fragmentSignUp1.isAdded() && fragmentSignUp1.isVisible()) {
            this.signUpModel = fragmentSignUp1.signUpModel;
            if (isBack) {
                Back();
            } else {
                // if (signUpModel.isStep1Valid(this)) {
                displayFragment2();
                //}
            }
        } else if (fragmentSignUp2 != null && fragmentSignUp2.isAdded() && fragmentSignUp2.isVisible()) {
            this.signUpModel = fragmentSignUp2.signUpModel;

            if (isBack) {
                displayFragment1();
            } else {

                //  if (this.signUpModel.isStep2Valid(this)) {
                displayFragment3();
                //}
            }
        } else if (fragmentSignUp3 != null && fragmentSignUp3.isAdded() && fragmentSignUp3.isVisible()) {
            this.signUpModel = fragmentSignUp3.signUpModel;

            if (isBack) {
                displayFragment2();
            } else {
                if (this.signUpModel.isStep3Valid(this)) {
                    // signUp(signUpModel);
                }
            }
        }
    }

    private void displayFragment1() {
        if (fragmentSignUp1 == null) {
            fragmentSignUp1 = FragmentSignUp1.newInstance(signUpModel);
        }

        if (fragmentSignUp2 != null && fragmentSignUp2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp2).commit();
        }

        if (fragmentSignUp3 != null && fragmentSignUp3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp3).commit();
        }

        if (fragmentSignUp1.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentSignUp1).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentSignUp1, "fragmentSignUp1").commit();
        }
        FragmentSignUp1Displayed();

    }

    private void displayFragment2() {
        if (fragmentSignUp2 == null) {
            fragmentSignUp2 = FragmentSignUp2.newInstance(signUpModel);
        }

        if (fragmentSignUp1 != null && fragmentSignUp1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp1).commit();
        }

        if (fragmentSignUp3 != null && fragmentSignUp3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp3).commit();
        }

        if (fragmentSignUp2.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentSignUp2).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentSignUp2, "fragmentSignUp2").commit();
        }
        FragmentSignUp2Displayed();

    }

    private void displayFragment3() {
        if (fragmentSignUp3 == null) {
            fragmentSignUp3 = FragmentSignUp3.newInstance(signUpModel);
        }

        if (fragmentSignUp1 != null && fragmentSignUp1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp1).commit();
        }

        if (fragmentSignUp2 != null && fragmentSignUp2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentSignUp2).commit();
        }

        if (fragmentSignUp3.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentSignUp3).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentSignUp3, "fragmentSignUp3").commit();
        }
        FragmentSignUp3Displayed();
    }

    public void Back() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void FragmentSignUp1Displayed() {
        binding.tv1.setBackgroundResource(R.drawable.circle_primary);
        binding.tv1.setTextColor(ContextCompat.getColor(this, R.color.white));

        binding.tv2.setBackgroundResource(R.drawable.circle_gray1_stroke);
        binding.tv2.setTextColor(ContextCompat.getColor(this, R.color.gray1));

        binding.tv3.setBackgroundResource(R.drawable.circle_gray1_stroke);
        binding.tv3.setTextColor(ContextCompat.getColor(this, R.color.gray1));

        binding.btnPrevious.setVisibility(View.GONE);
    }


    public void FragmentSignUp2Displayed() {
        binding.tv1.setBackgroundResource(R.drawable.circle_gray1_stroke);
        binding.tv1.setTextColor(ContextCompat.getColor(this, R.color.gray1));

        binding.tv2.setBackgroundResource(R.drawable.circle_primary);
        binding.tv2.setTextColor(ContextCompat.getColor(this, R.color.white));

        binding.tv3.setBackgroundResource(R.drawable.circle_gray1_stroke);
        binding.tv3.setTextColor(ContextCompat.getColor(this, R.color.gray1));

        binding.btnPrevious.setVisibility(View.VISIBLE);
    }


    public void FragmentSignUp3Displayed() {
        binding.tv1.setBackgroundResource(R.drawable.circle_gray1_stroke);
        binding.tv1.setTextColor(ContextCompat.getColor(this, R.color.gray1));

        binding.tv2.setBackgroundResource(R.drawable.circle_gray1_stroke);
        binding.tv2.setTextColor(ContextCompat.getColor(this, R.color.gray1));

        binding.tv3.setBackgroundResource(R.drawable.circle_primary);
        binding.tv3.setTextColor(ContextCompat.getColor(this, R.color.white));

        binding.btnPrevious.setVisibility(View.VISIBLE);
    }

}