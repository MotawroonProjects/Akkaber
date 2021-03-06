package com.apps.akkaber.uis.activity_product_detials;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.akkaber.R;
import com.apps.akkaber.adapter.SizeAdapter;
import com.apps.akkaber.adapter.TypeAdapter;
import com.apps.akkaber.adapter.WayAdapter;
import com.apps.akkaber.adapter.WrapAdapter;
import com.apps.akkaber.databinding.ActivityProductDetialsBinding;
import com.apps.akkaber.model.ProductModel;
import com.apps.akkaber.model.SingleProductDataModel;
import com.apps.akkaber.model.SizeModel;
import com.apps.akkaber.model.TypeModel;
import com.apps.akkaber.model.UserModel;
import com.apps.akkaber.model.WayModel;
import com.apps.akkaber.model.WrapModel;
import com.apps.akkaber.mvvm.ActivityProductDetialsMvvm;
import com.apps.akkaber.preferences.Preferences;
import com.apps.akkaber.uis.activity_base.BaseActivity;
import com.esotericsoftware.minlog.Log;

public class ProductDetialsActivity extends BaseActivity {
    private ActivityProductDetialsBinding binding;
    private ActivityProductDetialsMvvm activityProductDetialsMvvm;
    private UserModel userModel;
    private Preferences preferences;
    private String proid;
    private TypeAdapter typeAdapter;
    private String user_id = null;
    private SizeAdapter sizeAdapter;
    private WayAdapter wayAdapter;
    private WrapAdapter wrapAdapter;
    private double wayprice, wrapprice, price;
    private ProductModel productmodel;
    private int amount = 1;
    private TypeModel typeModel;
    private String sizeid = "";
    private String desc = "", typedesc = "", sizedesc = "", waydesc = "", wrapdesc = "";
    private boolean isDataChanged = false,isfav=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detials);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        proid = intent.getStringExtra("proid");

    }

    private void initView() {
        binding.tvTotal.setText("0");
        binding.priceOld.setPaintFlags(binding.priceOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.amountOld.setPaintFlags(binding.amountOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.priceOldtype.setPaintFlags(binding.priceOldtype.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.amountOldtype.setPaintFlags(binding.amountOldtype.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        if (userModel != null) {
            user_id = userModel.getData().getId() + "";
        }
        binding.setUserModel(userModel);
        activityProductDetialsMvvm = ViewModelProviders.of(this).get(ActivityProductDetialsMvvm.class);
        activityProductDetialsMvvm.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                // binding.cardNoData.setVisibility(View.GONE);
                binding.progBar.setVisibility(View.VISIBLE);
                binding.nested.setVisibility(View.GONE);
                binding.flTotal.setVisibility(View.GONE);

            }
            // binding.swipeRefresh.setRefreshing(isLoading);
        });
        activityProductDetialsMvvm.getFav().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    isfav=true;
                    if (productmodel != null) {
                        if (productmodel.getIs_favorite() == null) {
                            productmodel.setIs_favorite("yes");

                        } else {
                            if (productmodel.getIs_favorite().equals("yes")) {
                                productmodel.setIs_favorite("no");
                            } else {
                                productmodel.setIs_favorite("yes");
                            }
                        }
                        binding.setModel(productmodel);

                    }
                }
            }
        });
        activityProductDetialsMvvm.getAmount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > 0) {
                    isDataChanged = true;

                    Toast.makeText(ProductDetialsActivity.this, getResources().getString(R.string.suc_add_to_cart), Toast.LENGTH_LONG).show();
                }
            }
        });
        activityProductDetialsMvvm.getProductData().observe(this, new Observer<SingleProductDataModel>() {

            @Override
            public void onChanged(SingleProductDataModel singleProductDataModel) {
                binding.progBar.setVisibility(View.GONE);
                binding.nested.setVisibility(View.VISIBLE);
                binding.flTotal.setVisibility(View.VISIBLE);

                if (singleProductDataModel.getData() != null) {
                    ProductDetialsActivity.this.productmodel = singleProductDataModel.getData();
                    binding.setModel(singleProductDataModel.getData());
                    if (singleProductDataModel.getData().getTypes() != null && singleProductDataModel.getData().getTypes().size() > 0) {

                        TypeModel typeModel = singleProductDataModel.getData().getTypes().get(0);
                        ProductDetialsActivity.this.typeModel = typeModel;
                        typeModel.setIsselected(true);
                        singleProductDataModel.getData().getTypes().set(0, typeModel);
                        binding.setTypeModel(typeModel);
                        typeAdapter.updateData(singleProductDataModel.getData().getTypes());
                        if (typeModel.getPrice().equals("0")) {
                            sizeAdapter.updateData(typeModel.getSizes());

                        } else {
                            if (typeModel.getOffer() == null) {
                                price = Double.parseDouble(typeModel.getPrice());
                                binding.tvTotal.setText(price + "");
                            } else {
                                price = Double.parseDouble(typeModel.getOffer().getPrice_after());
                                binding.tvTotal.setText(price + "");

                            }

                        }
                        //binding.cardNoData.setVisibility(View.GONE);
                    } else {
                        if (singleProductDataModel.getData().getOffer() == null) {
                            price = Double.parseDouble(singleProductDataModel.getData().getPrice());
                            binding.tvTotal.setText(price + "");
                        } else {
                            price = Double.parseDouble(singleProductDataModel.getData().getOffer().getPrice_after());
                            binding.tvTotal.setText(price + "");

                        }
                    }
                    if (singleProductDataModel.getData().getWays() != null && singleProductDataModel.getData().getWays().size() > 0) {
                        wayAdapter.updateData(singleProductDataModel.getData().getWays());
                    }
                    if (singleProductDataModel.getData().getWrapping() != null && singleProductDataModel.getData().getWrapping().size() > 0) {
                        wrapAdapter.updateData(singleProductDataModel.getData().getWrapping());
                    }
                }
            }
        });
        //  setUpToolbar(binding.toolbar, getString(R.string.contact_us), R.color.white, R.color.black);
        binding.setLang(getLang());

        typeAdapter = new TypeAdapter(this);
        binding.recViewAges.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewAges.setAdapter(typeAdapter);
        sizeAdapter = new SizeAdapter(this);
        binding.recViewSizes.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewSizes.setAdapter(sizeAdapter);
        wayAdapter = new WayAdapter(this);
        binding.recViewWays.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewWays.setAdapter(wayAdapter);
        wrapAdapter = new WrapAdapter(this);
        binding.recViewWrap.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewWrap.setAdapter(wrapAdapter);
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        binding.imageIncrease.setOnClickListener(view -> {
            amount++;
            binding.tvAmount.setText(String.valueOf(amount));
            binding.tvTotal.setText(((price * amount) + wayprice + wrapprice) + "");

        });

        binding.imageDecrease.setOnClickListener(view -> {
            if (amount > 1) {
                amount--;
                binding.tvAmount.setText(String.valueOf(amount));
                binding.tvTotal.setText(((price * amount) + wayprice + wrapprice) + "");

            }
        });
        binding.btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!productmodel.getPrice().equals("0")) {
                    addTocart();

                } else {
                    if (typeModel != null) {
                        if (typeModel.getPrice().equals("0")) {
                            if (!sizeid.equals("")) {
                                addTocart();

                            } else {
                                Toast.makeText(ProductDetialsActivity.this, getResources().getString(R.string.ch_size), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            addTocart();
                        }
                    }
                }
            }
        });
        binding.flFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {
                    activityProductDetialsMvvm.addRemoveFavourite(productmodel.getId(), user_id);
                }
            }
        });
        activityProductDetialsMvvm.getProductDetials(getLang(), proid, user_id);
    }

    private void back() {
        if (isDataChanged||isfav) {
            Log.error("ldldll", String.valueOf(isDataChanged));
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void addTocart() {
        double total = ((price * amount) + wayprice + wrapprice);
        ;
        desc = "";
        desc += typedesc + " " + sizedesc + " " + waydesc + " " + wrapdesc;
        activityProductDetialsMvvm.add_to_cart(productmodel, desc, amount, wayprice, wrapprice, total, price, this);
    }

    public void showSizes(TypeModel currentModel) {
        desc = "";
        typedesc = "";
        sizedesc = "";
        waydesc = "";
        wrapdesc = "";
        sizeid = "";
        typedesc = currentModel.getTitle();
        amount = 1;
        this.typeModel = currentModel;
        binding.tvAmount.setText(String.valueOf(amount));
        if (productmodel.getWays() != null && productmodel.getWays().size() > 0) {
            wayAdapter.updateslection();
            wayprice = 0;
        }
        if (productmodel.getWrapping() != null && productmodel.getWrapping().size() > 0) {
            wrapAdapter.updateslection();
            wrapprice = 0;
        }
        binding.setTypeModel(currentModel);
        if (currentModel.getPrice().equals("0")) {
            sizeAdapter.updateslection();
            sizeAdapter.updateData(currentModel.getSizes());
            price = 0;
            binding.tvTotal.setText(((price * amount) + wayprice + wrapprice) + "");

        } else {
            if (currentModel.getOffer() != null) {
                binding.tvTotal.setText(((Double.parseDouble(currentModel.getOffer().getPrice_after()) * amount) + wayprice + wrapprice) + "");
                price = Double.parseDouble(currentModel.getOffer().getPrice_after());

            } else {
                binding.tvTotal.setText(((Double.parseDouble(currentModel.getPrice()) * amount) + wayprice + wrapprice) + "");
                price = Double.parseDouble(currentModel.getPrice());

            }
        }
    }

    public void choosesize(SizeModel currentModel) {
        desc = "";
        sizedesc = currentModel.getTitle();
        sizeid = currentModel.getId();
        if (currentModel.getOffer() != null) {
            binding.tvTotal.setText(((Double.parseDouble(currentModel.getOffer().getPrice_after()) * amount) + wayprice + wrapprice) + "");
            price = Double.parseDouble(currentModel.getOffer().getPrice_after());

        } else {
            binding.tvTotal.setText(((Double.parseDouble(currentModel.getPrice()) * amount) + wayprice + wrapprice) + "");
            price = Double.parseDouble(currentModel.getPrice());

        }
    }

    public void chooseway(WayModel currentModel) {
        desc = "";
        waydesc = currentModel.getTitle();
        wayprice = Double.parseDouble(currentModel.getPrice());
        binding.tvTotal.setText(((price * amount) + wayprice + wrapprice) + "");

    }

    public void choosewrap(WrapModel currentModel) {
        desc = "";
        wrapdesc = currentModel.getTitle();
        wrapprice = Double.parseDouble(currentModel.getPrice());
        binding.tvTotal.setText(((price * amount) + wayprice + wrapprice) + "");
    }
}