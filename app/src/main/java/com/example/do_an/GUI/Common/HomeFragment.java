package com.example.do_an.GUI.Common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.do_an.Adapter.Common.GridViewProductAdapter;
import com.example.do_an.Adapter.Common.SlideAdapter;
import com.example.do_an.GUI.Users.UserProductDetailActivity;
import com.example.do_an.Models.CategoryModel;
import com.example.do_an.Models.ProductModel;
import com.example.do_an.R;
import com.example.do_an.Service.CategoryService;
import com.example.do_an.Service.ProductService;
import com.example.do_an.Utils.SessionUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    ViewPager viewPager;
    SlideAdapter adapterViewPage;
    GridView gridView;
    int currentPage = 0;
    Timer timer;
    private int sortPrice = 0;
    private List<ProductModel> list;
    final long DELAY_MS = 5000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;
    private GridViewProductAdapter adapterGridview;
    TextView tvSortPrice, tvAllProduct, tvNewEst, tvAdidas, tvNike, tvPuma, tvReebok, tvAsics;
    ColorStateList oldColors;
    LinearLayout lnlCategory1, lnlCategory2;
    View v;

    public HomeFragment(List<ProductModel> list) {
        this.list = list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@Nullable View view, Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView imvNotLogin = view.findViewById(R.id.tvNotLogin);
        if(SessionUtil.getUserNameUserLogin().isEmpty()){
            imvNotLogin.setVisibility(TextView.VISIBLE);
        }
        viewPager = view.findViewById(R.id.viewpager);
        adapterViewPage = new SlideAdapter(getContext());
        viewPager.setAdapter(adapterViewPage);
        v = view;

        //Set auto next slide
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == 3) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        EditText edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                list.clear();
                list = ProductService.getInstance().findALlByKeySearch(edtSearch.getText().toString());
                list.addAll(ProductService.getInstance().findAllByKey(edtSearch.getText().toString()));
                adapterGridview = new GridViewProductAdapter(v.getContext(), list);
                gridView.setAdapter(adapterGridview);
                return true;
            }
            return false;
        });

        gridView = view.findViewById(R.id.gridViewProduct);
        adapterGridview = new GridViewProductAdapter(view.getContext(), list);
        gridView.setAdapter(adapterGridview);
        gridView.setOnItemClickListener((adapterView, view13, i, l) -> {
            Intent intent = new Intent(view13.getContext(), UserProductDetailActivity.class);
            ProductModel p = (ProductModel) gridView.getItemAtPosition(i);
            intent.putExtra("id", p.getId());
            startActivity(intent);
        });

        //Visible slide
        ImageView imgVisibleSlide = view.findViewById(R.id.imgVisibleSlide);
        imgVisibleSlide.setOnClickListener(view14 -> {
            if (viewPager.getVisibility() == ViewPager.GONE) {
                viewPager.setVisibility(ViewPager.VISIBLE);
                imgVisibleSlide.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            } else {
                viewPager.setVisibility(ViewPager.GONE);
                imgVisibleSlide.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            }
        });
        //initialize TextView
        tvSortPrice = view.findViewById(R.id.tvPriceUpDown);
        oldColors = tvSortPrice.getTextColors();
        tvAllProduct = view.findViewById(R.id.tvAllProduct);
        tvAllProduct.setBackground(getResources().getDrawable(R.drawable.cusstomborder_orange));
        tvAllProduct.setTextColor(Color.parseColor("#FD511B"));
        tvAdidas = view.findViewById(R.id.tvAdidas);
        tvNewEst = view.findViewById(R.id.tvNewEst);
        tvNike = view.findViewById(R.id.tvNike);
        tvPuma = view.findViewById(R.id.tvPuma);
        tvReebok = view.findViewById(R.id.tvReebok);
        tvAsics = view.findViewById(R.id.tvAsics);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 5, 0);
        params.weight = 1;

        //Render categories
        List<CategoryModel> listCatgory = CategoryService.getInstance().findALl();
        lnlCategory1 = view.findViewById(R.id.lnlCategory1);
        lnlCategory2 = view.findViewById(R.id.lnlCategory2);
        int index = 0;
        for (int i = 0; i < 3; i++) {
            if (index < listCatgory.size()) {
                TextView textView = new TextView(view.getContext());
                textView.setText(listCatgory.get(index).getName());
                textView.setId(listCatgory.get(index).getId());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setLayoutParams(params);
                textView.setBackgroundResource(R.drawable.cusstomborder);
                int index2 = index;
                textView.setOnClickListener(view1 -> {
                    list.clear();
                    list = ProductService.getInstance().findALlByCategoryId(listCatgory.get(index2).getId());
                    refresh();
                    textView.setBackground(getResources().getDrawable(R.drawable.cusstomborder_orange));
                    textView.setTextColor(Color.parseColor("#FD511B"));
                });
                lnlCategory1.addView(textView);
                index++;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (index < listCatgory.size()) {
                TextView textView = new TextView(view.getContext());
                textView.setText(listCatgory.get(index).getName());
                textView.setId(listCatgory.get(index).getId());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setLayoutParams(params);
                textView.setBackgroundResource(R.drawable.cusstomborder);
                int index2 = index;
                textView.setOnClickListener(view12 -> {
                    list.clear();
                    list = ProductService.getInstance().findALlByCategoryId(listCatgory.get(index2).getId());
                    refresh();
                    textView.setBackground(getResources().getDrawable(R.drawable.cusstomborder_orange));
                    textView.setTextColor(Color.parseColor("#FD511B"));
                });
                lnlCategory2.addView(textView);
                index++;
            }
        }

        if (lnlCategory1.getChildCount() < 3) {
            lnlCategory1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        if (lnlCategory2.getChildCount() < 3) {
            lnlCategory2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        //set event onClick for textViews Menu
        tvAllProduct.setOnClickListener(this::onClick);
        tvSortPrice.setOnClickListener(this::onClick);
        tvNewEst.setOnClickListener(this::onClick);
        tvNike.setOnClickListener(this::onClick);
        tvAdidas.setOnClickListener(this::onClick);
        tvPuma.setOnClickListener(this::onClick);
        tvReebok.setOnClickListener(this::onClick);
        tvAsics.setOnClickListener(this::onClick);

    }

    @SuppressLint({"NewApi", "NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAllProduct: {
                list.clear();
                list = ProductService.getInstance().findALl();
                refresh();
                tvAllProduct.setBackground(getResources().getDrawable(R.drawable.cusstomborder_orange));
                tvAllProduct.setTextColor(Color.parseColor("#FD511B"));
                break;
            }
            case R.id.tvPriceUpDown: {
                if (sortPrice == 0) {
                    Collections.sort(list, Comparator.comparingDouble(ProductModel::getPrice));
                    adapterGridview = new GridViewProductAdapter(v.getContext(), list);
                    gridView.setAdapter(adapterGridview);
                    tvSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.price_up_24, 0);
                    tvSortPrice.setBackground(getResources().getDrawable(R.drawable.cusstomborder_orange));
                    tvSortPrice.setTextColor(Color.parseColor("#FD511B"));
                    sortPrice = 1;
                } else {
                    Collections.sort(list, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                    adapterGridview = new GridViewProductAdapter(v.getContext(), list);
                    gridView.setAdapter(adapterGridview);
                    tvSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.price_down_24, 0);
                    sortPrice = 0;
                }
                break;
            }
            case R.id.tvNewEst: {
                list.clear();
                list = ProductService.getInstance().findALl();
                Collections.sort(list, (p1, p2) -> p2.getLocalDateTime().compareTo(p1.getLocalDateTime()));
                refresh();
                tvNewEst.setTextColor(Color.parseColor("#FD511B"));
                break;
            }
            case R.id.tvNike: {
                list.clear();
                String key = tvNike.getText().toString();
                list = ProductService.getInstance().findAllByKey(key);
                refresh();
                tvNike.setTextColor(Color.parseColor("#FD511B"));
                break;
            }
            case R.id.tvAdidas: {
                list.clear();
                String key = tvAdidas.getText().toString();
                list = ProductService.getInstance().findAllByKey(key);
                refresh();
                tvAdidas.setTextColor(Color.parseColor("#FD511B"));
                break;
            }
            case R.id.tvPuma: {
                list.clear();
                String key = tvPuma.getText().toString();
                list = ProductService.getInstance().findAllByKey(key);
                refresh();
                tvPuma.setTextColor(Color.parseColor("#FD511B"));
                break;
            }
            case R.id.tvReebok: {
                list.clear();
                String key = tvReebok.getText().toString();
                list = ProductService.getInstance().findAllByKey(key);
                refresh();
                tvReebok.setTextColor(Color.parseColor("#FD511B"));
                break;
            }
            case R.id.tvAsics: {
                list.clear();
                String key = tvAsics.getText().toString();
                list = ProductService.getInstance().findAllByKey(key);
                refresh();
                tvAsics.setTextColor(Color.parseColor("#FD511B"));
                break;
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void refresh() {
        int childCount = lnlCategory1.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView v = (TextView) lnlCategory1.getChildAt(i);
            v.setBackground(getResources().getDrawable(R.drawable.cusstomborder));
            v.setTextColor(oldColors);
        }
        int childCount2 = lnlCategory2.getChildCount();
        for (int i = 0; i < childCount2; i++) {
            TextView v = (TextView) lnlCategory2.getChildAt(i);
            v.setBackground(getResources().getDrawable(R.drawable.cusstomborder));
            v.setTextColor(oldColors);
        }
        tvAllProduct.setBackground(getResources().getDrawable(R.drawable.cusstomborder));
        tvAllProduct.setTextColor(oldColors);
        tvNewEst.setTextColor(oldColors);
        tvNike.setTextColor(oldColors);
        tvAdidas.setTextColor(oldColors);
        tvPuma.setTextColor(oldColors);
        tvReebok.setTextColor(oldColors);
        tvAsics.setTextColor(oldColors);

        tvSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.price_up_down, 0);
        tvSortPrice.setBackground(getResources().getDrawable(R.drawable.cusstomborder));
        tvSortPrice.setTextColor(oldColors);
        sortPrice = 0;

        if (list == null || list.isEmpty()) {
            list = ProductService.getInstance().findALl();
        }
        adapterGridview = new GridViewProductAdapter(v.getContext(), list);
        gridView.setAdapter(adapterGridview);
    }
}
