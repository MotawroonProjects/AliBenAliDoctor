package com.alibenalidoctor.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalidoctor.R;
import com.alibenalidoctor.databinding.ImagesRowBinding;
import com.alibenalidoctor.databinding.PatientRowBinding;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;
    private int i = 0;

    //private Fragment_Main fragment_main;
    public ImagesAdapter(List<Object> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ImagesRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.images_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

    }

    @Override
    public int getItemCount() {
        return 16;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ImagesRowBinding binding;

        public MyHolder(@NonNull ImagesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
