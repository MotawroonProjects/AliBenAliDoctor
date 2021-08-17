package com.alibenalidoctor.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.alibenalidoctor.R;
import com.alibenalidoctor.activities_fragments.activity_home.HomeActivity;
import com.alibenalidoctor.databinding.TimeRowBinding;
import com.alibenalidoctor.models.DateModel;
import com.alibenalidoctor.models.HourModel;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DateModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int i = 0;

    //private Fragment_Main fragment_main;
    public TimeAdapter(List<DateModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        TimeRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.time_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = position;
                notifyDataSetChanged();
            }
        });
        if (i == position) {
            myHolder.binding.tv1.setTextColor(context.getResources().getColor(R.color.colorAccent));
            myHolder.binding.tv2.setTextColor(context.getResources().getColor(R.color.colorAccent));
             if(context instanceof HomeActivity){
            HomeActivity activity = (HomeActivity) context;
            activity.getData(list.get(i).getDate());}


        } else {
            myHolder.binding.tv1.setTextColor(context.getResources().getColor(R.color.gray1));
            myHolder.binding.tv2.setTextColor(context.getResources().getColor(R.color.gray1));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TimeRowBinding binding;

        public MyHolder(@NonNull TimeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
