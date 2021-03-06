package com.apps.akkaber.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.akkaber.R;
import com.apps.akkaber.databinding.CartRowBinding;
import com.apps.akkaber.databinding.OrderProductRowBinding;
import com.apps.akkaber.model.ItemCartModel;
import com.apps.akkaber.uis.activity_cart.CartActivity;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemCartModel> list;
    private Context context;
    private LayoutInflater inflater;


    public OrderProductAdapter(List<ItemCartModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        OrderProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_product_row, parent, false);
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

    public class MyHolder extends RecyclerView.ViewHolder {
        public OrderProductRowBinding binding;

        public MyHolder(@NonNull OrderProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
