package com.alibenalidoctor.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_home.HomeActivity;
import com.alibenalidoctor.activities_fragments.activity_patients.PatientsActivity;
import com.alibenalidoctor.activities_fragments.activity_patients_detials.PatientsDetialsActivity;
import com.alibenalidoctor.databinding.PatientDetialsRowBinding;
import com.alibenalidoctor.databinding.ReservisionRowBinding;
import com.alibenalidoctor.models.ReservationModel;

import java.util.List;

public class PatientDetialsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReservationModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int i = 0;

    //private Fragment_Main fragment_main;
    public PatientDetialsAdapter(List<ReservationModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        PatientDetialsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.patient_detials_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.tvDetials.setPaintFlags(myHolder.binding.tvDetials.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof PatientsDetialsActivity){
                    PatientsDetialsActivity activity = (PatientsDetialsActivity) context;
                activity.show(list.get(holder.getLayoutPosition()).getId());
            }}
        });
        myHolder.binding.tvDetials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof PatientsDetialsActivity){
                    PatientsDetialsActivity activity = (PatientsDetialsActivity) context;
                    activity.show(list.get(holder.getLayoutPosition()).getId());
                }}
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public PatientDetialsRowBinding binding;

        public MyHolder(@NonNull PatientDetialsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
