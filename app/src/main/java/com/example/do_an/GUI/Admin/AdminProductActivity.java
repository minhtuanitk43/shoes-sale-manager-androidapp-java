package com.example.do_an.GUI.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.Adapter.Admin.AdminProductAdapter;
import com.example.do_an.GUI.Common.MainActivity;
import com.example.do_an.Models.ProductModel;
import com.example.do_an.R;
import com.example.do_an.Service.ProductService;
import com.example.do_an.SystemConstant.SystemConstant;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AdminProductActivity extends AppCompatActivity {
    private AdminProductAdapter productAdapter;
    private List<ProductModel> productModels;
    private ListView listView;
    private Button btnInsert;
    private ImageView imgBack;
    private Spinner spinnerSort;
    List<ProductModel> list;
    private EditText edtSearch;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        initializeData();
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(AdminProductActivity.this, MainActivity.class);
            intent.putExtra("fragmentKey", "manager");
            startActivity(intent);
        });

        btnInsert.setOnClickListener(view -> {
            Intent intent = new Intent(AdminProductActivity.this, InsertOrUpdateProductActivity.class);
            startActivity(intent);
        });

        ArrayAdapter<String> adapterSort = new ArrayAdapter<>(AdminProductActivity.this,
                android.R.layout.simple_spinner_item, SystemConstant.SORT_PRODUCT);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapterSort);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (SystemConstant.SORT_PRODUCT.get(i)){
                    case SystemConstant.SORT_IN_STOCK:{
                        list = productModels.stream().filter(
                                p->p.getStock()>0).collect(Collectors.toList());
                        productAdapter = new AdminProductAdapter(AdminProductActivity.this, list);
                        listView.setAdapter(productAdapter);
                        break;
                    }
                    case SystemConstant.SORT_OUT_STOCK:{
                        list = productModels.stream().filter(
                                p->p.getStock()==0).collect(Collectors.toList());
                        productAdapter = new AdminProductAdapter(AdminProductActivity.this, list);
                        listView.setAdapter(productAdapter);
                        break;
                    }
                    case SystemConstant.SORT_VISIBLE:{
                        list = productModels.stream().filter(
                                p->p.getStatus()==1).collect(Collectors.toList());
                        productAdapter = new AdminProductAdapter(AdminProductActivity.this, list);
                        listView.setAdapter(productAdapter);
                        break;
                    }
                    case SystemConstant.SORT_HIDE:{
                        list = productModels.stream().filter(
                                p->p.getStatus()==0).collect(Collectors.toList());
                        productAdapter = new AdminProductAdapter(AdminProductActivity.this, list);
                        listView.setAdapter(productAdapter);
                        break;
                    }
                    default:{
                        productAdapter = new AdminProductAdapter(AdminProductActivity.this, productModels);
                        listView.setAdapter(productAdapter);
                        break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                List<ProductModel> listSearch;
                listSearch = list.stream().filter(p->p.getName().toLowerCase()
                        .contains(edtSearch.getText().toString().toLowerCase()))
                        .collect(Collectors.toList());
                productAdapter = new AdminProductAdapter(AdminProductActivity.this, listSearch);
                listView.setAdapter(productAdapter);
                return true;
            }
            return false;
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeData() {
        listView = findViewById(R.id.listViewProduct);
        productModels = ProductService.getInstance().findALl();
        Collections.sort(productModels, (p1, p2) -> p2.getLocalDateTime().compareTo(p1.getLocalDateTime()));
        productAdapter = new AdminProductAdapter(this, productModels);
        listView.setAdapter(productAdapter);
        btnInsert = findViewById(R.id.btnInsert);
        imgBack = findViewById(R.id.imgBack);
        spinnerSort = findViewById(R.id.spinnerSort);
        edtSearch = findViewById(R.id.edtSearch);
        list = productModels;
    }
}