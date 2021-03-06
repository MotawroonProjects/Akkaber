package com.apps.akkaber.uis.activity_cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.apps.akkaber.R;
import com.apps.akkaber.adapter.CartAdapter;
import com.apps.akkaber.databinding.ActivityCartBinding;
import com.apps.akkaber.model.CartDataModel;
import com.apps.akkaber.model.ItemCartModel;
import com.apps.akkaber.model.UserModel;
import com.apps.akkaber.preferences.Preferences;
import com.apps.akkaber.uis.activity_base.BaseActivity;
import com.apps.akkaber.uis.activity_home.HomeActivity;
import com.apps.akkaber.uis.activity_login.LoginActivity;
import com.apps.akkaber.uis.activity_payment.PaymentActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private String lang;
    private LinearLayoutManager manager;
    private UserModel userModel;
    private Preferences preferences;
    private List<ItemCartModel> itemCartModelList;
    private CartDataModel cartDataModel;
    private CartAdapter cartAdapter;
    private boolean isDataChanged = false;
    private double total;
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        initView();

    }


    private void initView() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                userModel = getUserModel();
                isDataChanged = true;
            } else if (req == 2 && result.getResultCode() == Activity.RESULT_OK) {
                checkdata();
            }
        });
        itemCartModelList = new ArrayList<>();
        binding.setLang(getLang());

        preferences = Preferences.getInstance();
        userModel = getUserModel();
        manager = new GridLayoutManager(this, 1);
        binding.recView.setLayoutManager(manager);

        cartAdapter = new CartAdapter(itemCartModelList, this);
        binding.recView.setAdapter(cartAdapter);
        checkdata();
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        binding.btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {
                    req = 2;
                    Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                    launcher.launch(intent);
                } else {
                    navigationToLoginActivity();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        back();
    }


    public void back() {
        if (isDataChanged) {
            setResult(RESULT_OK);
        }
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        checkdata();

    }

    private void checkdata() {
        if (preferences != null) {
            cartDataModel = preferences.getCartData(this);
            itemCartModelList.clear();
            cartAdapter.notifyDataSetChanged();
            if (cartDataModel != null) {
                itemCartModelList.addAll(cartDataModel.getDetails());
                cartAdapter.notifyDataSetChanged();
                if (itemCartModelList.size() > 0) {
                    binding.llEmptyCart.setVisibility(View.GONE);
                    binding.flTotal.setVisibility(View.VISIBLE);
                } else {
                    binding.llEmptyCart.setVisibility(View.VISIBLE);
                    binding.flTotal.setVisibility(View.GONE);
                }
                calculateTotal();

            } else {
                if (itemCartModelList.size() == 0) {
                    binding.llEmptyCart.setVisibility(View.VISIBLE);
                    binding.flTotal.setVisibility(View.GONE);
                }
            }
        }
    }


    public void increase_decrease(ItemCartModel model, int adapterPosition) {
        itemCartModelList.set(adapterPosition, model);
        cartAdapter.notifyItemChanged(adapterPosition);

        cartDataModel.setDetails(itemCartModelList);
        calculateTotal();
        preferences.createUpdateCartData(this, cartDataModel);


    }

    private void calculateTotal() {
        total = 0;
        for (ItemCartModel model : itemCartModelList) {

            total += model.getTotal_price();

        }
        cartDataModel.setSub_total(total);
        cartDataModel.setTotal(cartDataModel.getSub_total() + cartDataModel.getShipping());
        binding.tvTotal.setText(total + "");
        //   binding.tvtotal.setText(String.valueOf(total));
    }

    public void deleteItem(int adapterPosition) {
        itemCartModelList.remove(adapterPosition);
        cartAdapter.notifyItemRemoved(adapterPosition);
        cartDataModel.setDetails(itemCartModelList);
        preferences.createUpdateCartData(this, cartDataModel);
        isDataChanged = true;
        calculateTotal();
        if (itemCartModelList.size() == 0) {
            binding.llEmptyCart.setVisibility(View.VISIBLE);
            binding.flTotal.setVisibility(View.GONE);
            preferences.clearCart(this);
        }
    }

    private void navigationToLoginActivity() {
        req = 1;
        Intent intent = new Intent(CartActivity.this, LoginActivity.class);
        launcher.launch(intent);
    }

}
