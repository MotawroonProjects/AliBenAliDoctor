package com.alibenalidoctor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalidoctor.R;
import com.alibenalidoctor.databinding.DiseaseRowBinding;
import com.alibenalidoctor.databinding.ImagesRowBinding;
import com.alibenalidoctor.models.DiseasesModel;
import com.alibenalidoctor.models.FileModel;
import com.alibenalidoctor.models.ReservationDiseasesModel;

import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReservationDiseasesModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int i = 0;

    //private Fragment_Main fragment_main;
    public DiseaseAdapter(List<ReservationDiseasesModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DiseaseRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.disease_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position).getDiseases());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public DiseaseRowBinding binding;

        public MyHolder(@NonNull DiseaseRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
