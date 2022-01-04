package com.example.do_an.GUI.Admin;

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

import com.example.do_an.Adapter.Admin.AdminUserAdapter;
import com.example.do_an.GUI.Common.MainActivity;
import com.example.do_an.Models.UserModel;
import com.example.do_an.R;
import com.example.do_an.Service.UserService;
import com.example.do_an.SystemConstant.SystemConstant;

import java.util.List;
import java.util.stream.Collectors;

public class AdminUserActivity extends AppCompatActivity {
    private Button btnInsert;
    private AdminUserAdapter userAdapter;
    private List<UserModel> list, userModels;
    private ListView listView;
    private ImageView imgBack;
    private Spinner spinnerSort;
    private EditText edtSearch;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        initializeData();

        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(AdminUserActivity.this, MainActivity.class);
            intent.putExtra("fragmentKey", "manager");
            startActivity(intent);
        });
        btnInsert.setOnClickListener(view -> {
            Intent intent = new Intent(AdminUserActivity.this, InsertOrUpdateUserActivity.class);
            startActivity(intent);
        });

        ArrayAdapter<String> adapterSort = new ArrayAdapter<>(AdminUserActivity.this,
                android.R.layout.simple_spinner_item, SystemConstant.SORT_USERS);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapterSort);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (SystemConstant.SORT_USERS.get(i)){
                    case SystemConstant.SORT_ADMIN:{
                        list = userModels.stream().filter(
                                p->p.getPermission().equals(SystemConstant.PERMISSION_ADMIN))
                                .collect(Collectors.toList());
                        userAdapter = new AdminUserAdapter(AdminUserActivity.this, list);
                        listView.setAdapter(userAdapter);
                        break;
                    }
                    case SystemConstant.SORT_STAFF:{
                        list = userModels.stream().filter(
                                p->p.getPermission().equals(SystemConstant.PERMISSION_STAFF))
                                .collect(Collectors.toList());
                        userAdapter = new AdminUserAdapter(AdminUserActivity.this, list);
                        listView.setAdapter(userAdapter);
                        break;
                    }
                    case SystemConstant.SORT_USER:{
                        list = userModels.stream().filter(
                                p->p.getPermission().equals(SystemConstant.PERMISSION_USER))
                                .collect(Collectors.toList());
                        userAdapter = new AdminUserAdapter(AdminUserActivity.this, list);
                        listView.setAdapter(userAdapter);
                        break;
                    }
                    case SystemConstant.SORT_ACTIVE:{
                        list = userModels.stream().filter(
                                p->p.getStatus() == 1)
                                .collect(Collectors.toList());
                        userAdapter = new AdminUserAdapter(AdminUserActivity.this, list);
                        listView.setAdapter(userAdapter);
                        break;
                    }
                    case SystemConstant.SORT_INACTIVE:{
                        list = userModels.stream().filter(
                                p->p.getStatus() == 0)
                                .collect(Collectors.toList());
                        userAdapter = new AdminUserAdapter(AdminUserActivity.this, list);
                        listView.setAdapter(userAdapter);
                        break;
                    }
                    default:{
                        userAdapter = new AdminUserAdapter(AdminUserActivity.this, userModels);
                        listView.setAdapter(userAdapter);
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
                List<UserModel> listSearch;
                listSearch = list.stream().filter(p->p.getFullName().toLowerCase()
                        .contains(edtSearch.getText().toString().toLowerCase()))
                        .collect(Collectors.toList());
                userAdapter = new AdminUserAdapter(AdminUserActivity.this, listSearch);
                listView.setAdapter(userAdapter);
                return true;
            }
            return false;
        });
    }

    private void initializeData() {
        btnInsert = findViewById(R.id.btnInsert);
        userModels = UserService.getInstance().findALl();
        list = userModels;
        userAdapter = new AdminUserAdapter(AdminUserActivity.this, list);
        listView = findViewById(R.id.listViewUser);
        listView.setAdapter(userAdapter);
        imgBack = findViewById(R.id.imgBack);
        spinnerSort = findViewById(R.id.spinnerSort);
        edtSearch = findViewById(R.id.edtSearch);
    }
}