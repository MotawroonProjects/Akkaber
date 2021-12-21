package com.apps.akkaber.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.akkaber.R;
import com.apps.akkaber.databinding.DepartmentItemRowBinding;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public DepartmentAdapter(Context context,Fragment fragment) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DepartmentItemRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.department_item_row, parent, false);
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
        public DepartmentItemRowBinding binding;

        public MyHolder(DepartmentItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
