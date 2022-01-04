package com.example.do_an.GUI.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.do_an.Adapter.User.UserOrdersAdapter;
import com.example.do_an.GUI.Common.MainActivity;
import com.example.do_an.Models.OrderModel;
import com.example.do_an.R;
import com.example.do_an.Service.OrderService;
import com.example.do_an.Utils.SessionUtil;

import java.util.List;

public class UserOrdersActivity extends AppCompatActivity {
    UserOrdersAdapter adapter;
    ListView listView;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(view -> {
            if(getIntent().hasExtra("fromUser")){
                finish();
            }else {
                Intent intent = new Intent(UserOrdersActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        List<OrderModel> list = OrderService.getInstance().findAllByUserId(SessionUtil.getUserId());
        adapter = new UserOrdersAdapter(UserOrdersActivity.this,list);
        listView = findViewById(R.id.listViewOrder);
        listView.setAdapter(adapter);

    }
}