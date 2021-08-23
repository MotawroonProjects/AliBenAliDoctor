package com.alibenalidoctor.activities_fragments.activity_add_report;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibenalidoctor.R;
import com.alibenalidoctor.adapters.FilesAdapter;
import com.alibenalidoctor.databinding.ActivityAddReportBinding;
import com.alibenalidoctor.databinding.ActivityContactUsBinding;
import com.alibenalidoctor.interfaces.Listeners;
import com.alibenalidoctor.language.Language;
import com.alibenalidoctor.models.ContactUsModel;
import com.alibenalidoctor.models.StatusResponse;
import com.alibenalidoctor.models.UserModel;
import com.alibenalidoctor.preferences.Preferences;
import com.alibenalidoctor.remote.Api;
import com.alibenalidoctor.share.Common;
import com.alibenalidoctor.tags.Tags;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReportActivity extends AppCompatActivity implements Listeners.DeleteDiseaseListener{
    private ActivityAddReportBinding binding;
    private Preferences preferences;
    private UserModel userModel;

    private String lang = "ar";
    private List<String> images;
    private FilesAdapter filesAdapter;
    private final String WRITE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String CAMERA_PERM = Manifest.permission.CAMERA;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private ActivityResultLauncher<Intent> launcher;
    private String id;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_report);
        getDataFromIntent();
        initView();

    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        id=  intent.getStringExtra("reservid");
    }
    private void initView() {
        images = new ArrayList<>();
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);


        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filesAdapter = new FilesAdapter(images, this,this);
        binding.recViewImages.setAdapter(filesAdapter);

        binding.btnSend.setOnClickListener(view -> {

            if (images.size()>0){

                addReportWithImage();
            }else {
                addReport();
            }
        });
        binding.llBack.setOnClickListener(view -> finish());


        binding.flSelectImage.setOnClickListener(v -> {
            checkPermissions();
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Uri uri = getResultImageUri(result.getData());
                String path = Common.getImagePath(this,uri);
                images.add(0, path);
                filesAdapter.notifyItemInserted(0);
                binding.recViewImages.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.recViewImages.scrollToPosition(0);
                    }
                });
            }
        });
    }

    private void addReport() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        Api.getService(Tags.base_url)
                .addReport(id,binding.edtMessege+"")
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                removeImagesFromStorage();
                                setResult(RESULT_OK);
                                finish();
                            }

                        } else {

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void addReportWithImage() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody id_part = Common.getRequestBodyText(id+"");
        RequestBody messege_part = Common.getRequestBodyText(binding.edtMessege+"");



        List<MultipartBody.Part> imagesParts = new ArrayList<>();
        for (String path:images){
            MultipartBody.Part part = Common.getMultiPartFromPath(path,"reports[]");
            imagesParts.add(part);
        }

        Api.getService(Tags.base_url)
                .addReport(id_part,messege_part,imagesParts)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                removeImagesFromStorage();
                                setResult(RESULT_OK);
                                finish();
                            }

                        } else {

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });


    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, CAMERA_PERM) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, READ_PERM) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, WRITE_PERM) == PackageManager.PERMISSION_GRANTED

        ) {
            launcher.launch(getImageChooserIntent());

        } else {
            String[] perms = {CAMERA_PERM, READ_PERM, WRITE_PERM};
            ActivityCompat.requestPermissions(this, perms, 100);

        }

    }

    private Uri getImageUri() {
        Uri uri = null;
        File mainFolder = Environment.getExternalStorageDirectory();
        File appFile = new File(mainFolder.getPath(),"AliHospital");
        if (!appFile.exists()){
            appFile.mkdir();

        }
        final String imageName = "img"+(images.size()+1)+".png";
        uri = Uri.fromFile(new File(appFile.getPath(), imageName));


        return uri;
    }

    private Uri getResultImageUri(Intent intent) {
        boolean isCamera = true;
        if (intent != null) {
            String action = intent.getAction();

            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);

        }

        return isCamera ? getImageUri() : intent.getData();
    }

    private Intent getImageChooserIntent() {
        Uri uriOutput = getImageUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(cameraIntent, 0);
        for (ResolveInfo info : resolveInfo) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);
            if (uriOutput != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriOutput);
            }

            allIntents.add(intent);
        }


        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo info : resolveInfos) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);
            if (uriOutput != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriOutput);
            }

            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().contains("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
            }
        }
        allIntents.remove(mainIntent);
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }

    private void removeImagesFromStorage(){
        File mainFolder = Environment.getExternalStorageDirectory();
        File appFile = new File(mainFolder.getPath(),"AliHospital");
        if (appFile.exists()){
            appFile.delete();
        }
    }

    @Override
    public void deleteImage(int pos) {
        images.remove(pos);
        filesAdapter.notifyItemRemoved(pos);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            launcher.launch(getImageChooserIntent());

        }
    }

}