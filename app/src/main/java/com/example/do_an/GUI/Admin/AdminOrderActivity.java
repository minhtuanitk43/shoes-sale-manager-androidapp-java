package com.example.do_an.GUI.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.Adapter.Admin.AdminOrdersAdapter;
import com.example.do_an.GUI.Common.MainActivity;
import com.example.do_an.Models.OrderModel;
import com.example.do_an.R;
import com.example.do_an.Service.OrderService;
import com.example.do_an.SystemConstant.SystemConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdminOrderActivity extends AppCompatActivity {
    private Spinner spinnerSort, spinnerDate;
    private List<OrderModel> orderModels, list;
    private AdminOrdersAdapter adapter;
    private ListView listView;
    private List<String> status, dates;
    private TextView edtSearch;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        initialize();

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(AdminOrderActivity.this, MainActivity.class);
            intent.putExtra("fragmentKey", "manager");
            startActivity(intent);
        });

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(AdminOrderActivity.this,
                android.R.layout.simple_spinner_item, status);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapterStatus);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (status.get(i)) {
                    case SystemConstant.STATUS_ORDER_PENDING_STR: {
                        list = orderModels.stream().filter(o -> o.getStatus() == SystemConstant.STATUS_ORDER_PENDING)
                                .collect(Collectors.toList());
                        adapter = new AdminOrdersAdapter(AdminOrderActivity.this, list);
                        listView.setAdapter(adapter);
                        break;
                    }
                    case SystemConstant.STATUS_ORDER_DELIVERING_STR: {
                        list = orderModels.stream().filter(o -> o.getStatus() == SystemConstant.STATUS_ORDER_DELIVERING)
                                .collect(Collectors.toList());
                        adapter = new AdminOrdersAdapter(AdminOrderActivity.this, list);
                        listView.setAdapter(adapter);
                        break;
                    }
                    case SystemConstant.STATUS_ORDER_DELIVERED_STR: {
                        list = orderModels.stream().filter(o -> o.getStatus() == SystemConstant.STATUS_ORDER_DELIVERED)
                                .collect(Collectors.toList());
                        adapter = new AdminOrdersAdapter(AdminOrderActivity.this, list);
                        listView.setAdapter(adapter);
                        break;
                    }
                    case SystemConstant.STATUS_ORDER_CANCEL_STR: {
                        list = orderModels.stream().filter(o -> o.getStatus() == SystemConstant.STATUS_ORDER_CANCEL)
                                .collect(Collectors.toList());
                        adapter = new AdminOrdersAdapter(AdminOrderActivity.this, list);
                        listView.setAdapter(adapter);
                        break;
                    }
                    default: {
                        list = orderModels;
                        adapter = new AdminOrdersAdapter(AdminOrderActivity.this, list);
                        listView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> adapterDate = new ArrayAdapter<>(AdminOrderActivity.this,
                android.R.layout.simple_spinner_item, dates);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(adapterDate);
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Collections.sort(list, (o1, o2) -> o2.getLocalDateTime().compareTo(o1.getLocalDateTime()));
                } else {
                    Collections.sort(list, Comparator.comparing(OrderModel::getLocalDateTime));
                }
                adapter = new AdminOrdersAdapter(AdminOrderActivity.this, list);
                listView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                List<OrderModel> listSearch;
                String key = edtSearch.getText().toString().trim();
                Pattern pattern = Pattern.compile("^([1-9][0-9]*)$");
                if (pattern.matcher(key).matches()) {
                    listSearch = list.stream().filter(p -> p.getId() == Integer.parseInt(key))
                            .collect(Collectors.toList());
                }else{
                    listSearch = list.stream().filter(p -> p.getCustomerName().toLowerCase()
                            .contains(key.toLowerCase())).collect(Collectors.toList());
                }
                adapter = new AdminOrdersAdapter(AdminOrderActivity.this, listSearch);
                listView.setAdapter(adapter);
                return true;
            }
            return false;
        });
    }

    private void initialize() {
        orderModels = OrderService.getInstance().findALl();
        adapter = new AdminOrdersAdapter(AdminOrderActivity.this, orderModels);
        listView = findViewById(R.id.listViewOrder);
        listView.setAdapter(adapter);
        spinnerSort = findViewById(R.id.spinnerSort);
        spinnerDate = findViewById(R.id.spinnerSortDate);
        list = orderModels;
        edtSearch = findViewById(R.id.edtSearch);

        status = new ArrayList<>();
        status.add(SystemConstant.SORT_ALL);
        status.add(SystemConstant.STATUS_ORDER_PENDING_STR);
        status.add(SystemConstant.STATUS_ORDER_DELIVERING_STR);
        status.add(SystemConstant.STATUS_ORDER_DELIVERED_STR);
        status.add(SystemConstant.STATUS_ORDER_CANCEL_STR);

        dates = new ArrayList<>();
        dates.add("Mới nhất");
        dates.add("Cũ nhất");
    }
}