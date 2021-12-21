package com.apps.akkaber.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.akkaber.R;
import com.apps.akkaber.databinding.HomeCookingItemRowBinding;

import java.util.List;

public class HomeCookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;


    public HomeCookingAdapter(List<Object>list,Context context) {
        this.list=list;
        this.context=context;
        inflater=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeCookingItemRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.home_cooking_item_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
    }

    @Override
    public int getItemCount() {
        if (list!=null){
            return list.size();
        }else {
            return 4;
        }
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public HomeCookingItemRowBinding binding;

        public MyHolder(HomeCookingItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
