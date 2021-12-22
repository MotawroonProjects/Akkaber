package com.apps.akkaber.uis.activity_intro_slider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.apps.akkaber.R;
import com.apps.akkaber.adapter.IntroAdapter;
import com.apps.akkaber.databinding.ActivityIntroSliderBinding;
import com.apps.akkaber.language.Language;
import com.apps.akkaber.uis.activity_home.HomeActivity;

import io.paperdb.Paper;

public class IntroSliderActivity extends AppCompatActivity {
    private ActivityIntroSliderBinding binding;
    private IntroAdapter adapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_slider);
        initView();
    }

    private void initView() {
        binding.tab.setupWithViewPager(binding.pager);
        adapter = new IntroAdapter(this);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(4);
        for (int i = 0; i < binding.tab.getTabCount(); i++) {
            View tab = ((ViewGroup) binding.tab.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(10, 0, 10, 0);
            tab.requestLayout();


            binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == adapter.getCount() - 1) {
                        binding.btnStart.setVisibility(View.VISIBLE);
                        binding.btnSkip.setVisibility(View.INVISIBLE);
                        binding.btnNext.setVisibility(View.INVISIBLE);

                    } else {
                        binding.btnStart.setVisibility(View.INVISIBLE);
                        binding.btnSkip.setVisibility(View.VISIBLE);
                        binding.btnNext.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            binding.btnSkip.setOnClickListener(view -> {
                Intent intent = new Intent(IntroSliderActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            });

            binding.btnNext.setOnClickListener(view -> binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1));
            binding.btnStart.setOnClickListener(view -> {
                Intent intent = new Intent(IntroSliderActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            });

        }
    }
}