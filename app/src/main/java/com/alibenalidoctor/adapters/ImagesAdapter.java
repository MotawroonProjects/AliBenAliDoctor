package com.alibenalidoctor.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalidoctor.R;
import com.alibenalidoctor.databinding.ImagesRowBinding;
import com.alibenalidoctor.databinding.PatientRowBinding;
import com.alibenalidoctor.models.FileModel;
import com.alibenalidoctor.models.ReservationModel;
import com.alibenalidoctor.tags.Tags;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FileModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int i = 0;

    //private Fragment_Main fragment_main;
    public ImagesAdapter(List<FileModel> list, Context context) {
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
        Log.e("dldll", Tags.IMAGE_URL+list.get(position).getFile());
        myHolder.binding.setModel(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ImagesRowBinding binding;

        public MyHolder(@NonNull ImagesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
