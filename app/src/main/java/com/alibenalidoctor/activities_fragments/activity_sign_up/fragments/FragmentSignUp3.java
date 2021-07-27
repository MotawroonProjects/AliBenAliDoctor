package com.alibenalidoctor.activities_fragments.activity_sign_up.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alibenalidoctor.R;
import com.alibenalidoctor.databinding.FragmentSignUp3Binding;
import com.alibenalidoctor.models.SignUpModel;


public class FragmentSignUp3 extends Fragment {
    private static final String TAG = "DATA";
    private FragmentSignUp3Binding binding;
    public SignUpModel signUpModel;

    public static FragmentSignUp3 newInstance(SignUpModel signUpModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, signUpModel);
        FragmentSignUp3 fragmentSignUp3 = new FragmentSignUp3();
        fragmentSignUp3.setArguments(bundle);
        return fragmentSignUp3;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up3, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            signUpModel = (SignUpModel) bundle.getSerializable(TAG);
        }
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith("0")) {
                    binding.edtPhone.setText("");
                }
            }
        });
        binding.setModel(signUpModel);
    }


}
