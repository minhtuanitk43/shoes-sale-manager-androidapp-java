package com.example.do_an.Adapter.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.do_an.Models.CategoryModel;
import com.example.do_an.R;

import java.util.List;

public class AdminCategoryAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryModel> list;
    public AdminCategoryAdapter(Context context, List<CategoryModel> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.list_row_admin_category, null);
        final CategoryModel categoryModel = list.get(i);
        TextView catId = row.findViewById(R.id.tvCategoryId);
        catId.setText(String.valueOf(categoryModel.getId()));
        TextView catNam = row.findViewById(R.id.tvCategoryName);
        catNam.setText(categoryModel.getName());
        TextView catKeySearch = row.findViewById(R.id.tvCategoryKeySearch);
        catKeySearch.setText(categoryModel.getKeySearch());
        return row;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CategoryModel getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

}
