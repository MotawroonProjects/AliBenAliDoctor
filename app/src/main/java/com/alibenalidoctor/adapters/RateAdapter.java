package com.alibenalidoctor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_profile.ProfileActivity;
import com.alibenalidoctor.databinding.RateRowBinding;
import com.alibenalidoctor.models.RateModel;

import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RateModel> list;
    private Context context;
    private LayoutInflater inflater;
    private ProfileActivity activity;

    public RateAdapter(List<RateModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        // activity = (DoctorDetailsActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        RateRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.rate_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public RateRowBinding binding;

        public MyHolder(@NonNull RateRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
