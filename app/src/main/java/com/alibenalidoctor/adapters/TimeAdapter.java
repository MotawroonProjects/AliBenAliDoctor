package com.alibenalidoctor.adapters;

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

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;
    private int i = 0;

    //private Fragment_Main fragment_main;
    public TimeAdapter(List<Object> list, Context context) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
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

        } else {
            myHolder.binding.tv1.setTextColor(context.getResources().getColor(R.color.gray1));
            myHolder.binding.tv2.setTextColor(context.getResources().getColor(R.color.gray1));
            HomeActivity activity = (HomeActivity) context;
            //activity.show();
        }

    }

    @Override
    public int getItemCount() {
        return 16;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TimeRowBinding binding;

        public MyHolder(@NonNull TimeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
