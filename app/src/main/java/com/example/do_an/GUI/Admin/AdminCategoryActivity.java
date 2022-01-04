package com.example.do_an.GUI.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.Adapter.Admin.AdminCategoryAdapter;
import com.example.do_an.GUI.Common.MainActivity;
import com.example.do_an.Models.CategoryModel;
import com.example.do_an.R;
import com.example.do_an.Service.CategoryService;

import java.util.List;

public class AdminCategoryActivity extends AppCompatActivity {
    private ListView listView;
    private Button btnInsert;
    private ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        initializeData();

        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
            intent.putExtra("fragmentKey", "manager");
            startActivity(intent);
        });
        btnInsert.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, InsertOrUpdateCategoryActivity.class);
            startActivity(intent);
        });
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            int id = (int) adapterView.getItemIdAtPosition(i);
            Intent intent = new Intent(AdminCategoryActivity.this,InsertOrUpdateCategoryActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
    }

    private void initializeData() {
        listView = findViewById(R.id.listViewCategory);
        List<CategoryModel> categoryModels = CategoryService.getInstance().findALl();
        AdminCategoryAdapter categoryAdapter = new AdminCategoryAdapter(AdminCategoryActivity.this, categoryModels);
        listView.setAdapter(categoryAdapter);
        imgBack = findViewById(R.id.imgBack);
        btnInsert = findViewById(R.id.btnInsert);
    }


}