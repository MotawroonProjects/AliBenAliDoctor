package com.alibenalidoctor.activities_fragments.activity_sign_up.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_sign_up.SignUpActivity;
import com.alibenalidoctor.databinding.DialogSelectImageBinding;
import com.alibenalidoctor.databinding.FragmentSignUp2Binding;
import com.alibenalidoctor.models.SignUpModel;
import com.alibenalidoctor.share.Common;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;

public class FragmentSignUp2 extends Fragment  {
    private SignUpActivity activity;
    private static final String TAG = "DATA";
    private FragmentSignUp2Binding binding;
    public SignUpModel signUpModel;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private ActivityResultLauncher<Intent> launcher;
    private int selectedReq = 0;
    private Uri uri = null;
    private AlertDialog dialog;

    private String lang;

    public static FragmentSignUp2 newInstance(SignUpModel signUpModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, signUpModel);
        FragmentSignUp2 fragmentSignUp2 = new FragmentSignUp2();
        fragmentSignUp2.setArguments(bundle);
        return fragmentSignUp2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up2, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.flSelectImage.setOnClickListener(view2 -> createImageDialogAlert());
        binding.setModel(signUpModel);
        //createImageDialogAlert();
    }

    private void initView() {
        activity = (SignUpActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            signUpModel = (SignUpModel) bundle.getSerializable(TAG);
        }
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                Log.e("lll",selectedReq+"");
                if (selectedReq == READ_REQ) {
                    binding.icon.setVisibility(View.GONE);

                    uri = result.getData().getData();
                    File file = new File(Common.getImagePath(activity, uri));
                    Picasso.get().load(file).fit().into(binding.imageLicense);

                } else if (selectedReq == CAMERA_REQ) {
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    binding.icon.setVisibility(View.GONE);
                    uri = getUriFromBitmap(bitmap);
                 //   Log.e("dldkk",uri.toString());
                    if (uri != null) {
                        String path = Common.getImagePath(activity, uri);

                        if (path != null) {
                            Picasso.get().load(new File(path)).fit().into(binding.imageLicense);

                        } else {
                            Picasso.get().load(uri).fit().into(binding.imageLicense);

                        }
                    }
                }
            }
        });
        binding.flSelectImage.setOnClickListener(view -> createImageDialogAlert());


    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "", ""));
    }

    public void createImageDialogAlert() {
        dialog = new AlertDialog.Builder(activity)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_select_image, null, false);
        binding.btnCamera.setOnClickListener(view -> {
            dialog.dismiss();
            checkCameraPermission();
        });
        binding.btnGallery.setOnClickListener(view -> {
            dialog.dismiss();
            checkReadPermission();
        });
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    public void checkReadPermission() {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {


        if (ContextCompat.checkSelfPermission(activity, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {
        selectedReq = req;

        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            launcher.launch(intent);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                launcher.launch(intent);
            } catch (SecurityException e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }



}
