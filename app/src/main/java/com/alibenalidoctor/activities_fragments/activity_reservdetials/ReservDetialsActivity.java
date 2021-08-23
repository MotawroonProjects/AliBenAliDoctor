package com.alibenalidoctor.activities_fragments.activity_reservdetials;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_about_us.AboutUsActivity;
import com.alibenalidoctor.activities_fragments.activity_add_report.AddReportActivity;
import com.alibenalidoctor.adapters.DiseaseAdapter;
import com.alibenalidoctor.adapters.ImagesAdapter;
import com.alibenalidoctor.databinding.ActivityReservdetialsBinding;
import com.alibenalidoctor.language.Language;
import com.alibenalidoctor.models.DiseasesModel;
import com.alibenalidoctor.models.FileModel;
import com.alibenalidoctor.models.ReservationDiseasesModel;
import com.alibenalidoctor.models.ReservationModel;
import com.alibenalidoctor.models.SingleReservationDataModel;
import com.alibenalidoctor.models.StatusResponse;
import com.alibenalidoctor.models.UserModel;
import com.alibenalidoctor.preferences.Preferences;
import com.alibenalidoctor.remote.Api;
import com.alibenalidoctor.share.Common;
import com.alibenalidoctor.tags.Tags;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservDetialsActivity extends AppCompatActivity {
    private ActivityReservdetialsBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;


    private List<FileModel> list;
    private List<ReservationDiseasesModel> diseasesModelList;
    private ImagesAdapter imagesAdapter;
    private DiseaseAdapter diseaseAdapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservdetials);
        getDataFromIntent();
        initView();
    }


    private void initView() {
        list = new ArrayList<>();
        diseasesModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.flCall.setOnClickListener(v -> {
            boolean video = true;
            if (type.equals("audio")) {
                video = false;
            }

            startChat(video);
        });

        binding.flAddReport.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddReportActivity.class);
            intent.putExtra("reservid", reservid);
            startActivity(intent);
        });
        imagesAdapter = new ImagesAdapter(list, this);
        diseaseAdapter = new DiseaseAdapter(diseasesModelList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(imagesAdapter);
        binding.recViewdisease.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewdisease.setAdapter(diseaseAdapter);
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSinglereservison();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            reservid = intent.getStringExtra("data");

        }
    }

    private void startChat(boolean video) {
        try {
            String roomName = "ali_ben_ali_app" + reservid;
            JitsiMeetUserInfo info = new JitsiMeetUserInfo();
            info.setAvatar(new URL(Tags.IMAGE_URL + userModel.getUser().getImage()));
            info.setDisplayName(userModel.getUser().getName());

            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom(roomName)
                    .setUserInfo(info)
                    .setAudioMuted(false)
                    .setVideoMuted(false)
                    .setAudioOnly(video)
                    .setWelcomePageEnabled(false)
                    .build();


            JitsiMeetActivity.launch(this, options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void getSinglereservison() {
        binding.scroll.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url)
                .getSingleReservison(lang, reservid)
                .enqueue(new Callback<SingleReservationDataModel>() {
                    @Override
                    public void onResponse(Call<SingleReservationDataModel> call, Response<SingleReservationDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    binding.scroll.setVisibility(View.VISIBLE);
                                    updatedata(response.body().getData());
                                }
                            } else {

                                //  Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {


                            switch (response.code()) {
                                case 500:
                                    //   Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    //   Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SingleReservationDataModel> call, Throwable t) {
                        try {

//                            binding.arrow.setVisibility(View.VISIBLE);
//
//                            binding.progBar.setVisibility(View.GONE);
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

    private void updatedata(ReservationModel data) {
        Log.e("kdkdj", data.getId() + "");
        binding.setModel(data);
        if (data.getFiles().size() > 0) {
            list.clear();
            list.addAll(data.getFiles());
            imagesAdapter.notifyDataSetChanged();
        } else {

        }
        if (data.getReservation_diseases().size() > 0) {
            diseasesModelList.clear();
            diseasesModelList.addAll(data.getReservation_diseases());
            diseaseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    private void endReservation() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        Api.getService(Tags.base_url)
                .endReservation(reservid)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
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

}