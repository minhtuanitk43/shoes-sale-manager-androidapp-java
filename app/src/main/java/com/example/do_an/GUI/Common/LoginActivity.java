package com.example.do_an.GUI.Common;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.Database.Database;
import com.example.do_an.GUI.Users.UserProductDetailActivity;
import com.example.do_an.Models.UserModel;
import com.example.do_an.R;
import com.example.do_an.Service.UserService;
import com.example.do_an.SystemConstant.SystemConstant;
import com.example.do_an.Utils.SessionUtil;

public class LoginActivity extends AppCompatActivity {

    private UserModel userLogin;
    private EditText edtUserName, edtPassword;
    private String userName, password;
    private SessionUtil sessionUtil;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Set context
        Database.context = this.getApplicationContext();
        //declare
        edtUserName = findViewById(R.id.userName);
        edtPassword = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(view -> {
            Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {
            int check = 1;
            try {
                userName = edtUserName.getText().toString();
                if(userName.isEmpty()){
                    edtUserName.setError("Chưa nhập tên đăng nhập");
                    check = 0;
                }
                password = edtPassword.getText().toString();
                if(password.isEmpty()){
                    edtPassword.setError("Chưa nhập mật khẩu");
                    check=0;
                }
                if(check==0){
                    throw new Exception("Vui lòng nhập đủ thông tin !");
                }
                userLogin = UserService.getInstance().findOneByUserNameAndPasswordAndStatus(
                        userName, password, SystemConstant.STATUS_ACTIVE);
                if(userLogin == null){
                    throw new Exception("Thông tin sai hoặc tài khoản không hợp lệ !");
                }
                sessionUtil = new SessionUtil(getBaseContext());
                sessionUtil.setUserLogin(userLogin);
                if(getIntent().hasExtra("id")){
                    intent = new Intent(LoginActivity.this, UserProductDetailActivity.class);
                    int id = getIntent().getExtras().getInt("id");
                    intent.putExtra("id", id);
                    startActivity(intent);
                }else {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }catch (Exception e){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                alertDialog.setMessage(e.getMessage());
                alertDialog.setTitle("Lỗi !");
                alertDialog.setPositiveButton("Ok",null);
                alertDialog.setCancelable(true);
                alertDialog.create().show();
            }

        });

        TextView tvNotLogin = findViewById(R.id.tvNotLogin);
        tvNotLogin.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}