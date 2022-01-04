package com.example.do_an.GUI.Users;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an.Models.UserModel;
import com.example.do_an.R;
import com.example.do_an.Service.UserService;
import com.example.do_an.Utils.SessionUtil;

import java.util.regex.Pattern;

public class ChangeUserInfoActivity extends AppCompatActivity {
    private EditText edtFullName;
    private EditText edtPhone;
    private EditText edtAddress;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        //
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);
        EditText edtUsername = findViewById(R.id.edtUserName);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        ImageView imgBack = findViewById(R.id.imgBack);

        UserModel userModel = UserService.getInstance().findOne(SessionUtil.getUserId());

        edtUsername.setText(userModel.getUserName());
        edtFullName.setText(userModel.getFullName());
        edtPhone.setText(userModel.getPhone());
        edtAddress.setText(userModel.getAddress());

        btnSave.setOnClickListener(view -> {
            try {
                int check = 1;
                String fullName = edtFullName.getText().toString().trim();
                if (fullName.isEmpty()) {
                    edtFullName.setError("Chưa nhập họ tên !");
                    check = 0;
                }
                Pattern patternPhone = Pattern.compile("^[0-9]{10,}$");
                String phone = edtPhone.getText().toString().trim();
                if (phone.isEmpty()) {
                    edtPhone.setError("Chưa nhập số điện thoại !");
                    check = 0;
                }
                if (!patternPhone.matcher(phone).matches()) {
                    edtPhone.setError("Số điện thoại không hợp lệ !");
                    check = 0;
                }
                String address = edtAddress.getText().toString().trim();
                if (address.isEmpty()) {
                    edtAddress.setError("Chưa nhập địa chỉ !");
                    check = 0;
                }
                if (check == 0) {
                    throw new Exception("Thông tin nhập vào không hợp lệ !");
                }
                userModel.setPassword("");
                userModel.setFullName(fullName);
                userModel.setPhone(phone);
                userModel.setAddress(address);
                if (UserService.getInstance().update(userModel) > 0) {
                    View layout = LayoutInflater.from(getBaseContext()).inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toast_layout_root));
                    TextView text = (TextView) layout.findViewById(R.id.text);
                    ImageView imageView = (ImageView) layout.findViewById(R.id.image);
                    imageView.setImageResource(R.drawable.ic_baseline_check_24);
                    text.setText("Cập nhật thành công !");
                    Toast toast = new Toast(getBaseContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else {
                    throw new Exception("Lỗi hệ thống !");
                }

            } catch (Exception e) {
                android.app.AlertDialog.Builder dlgAlert =
                        new android.app.AlertDialog.Builder(ChangeUserInfoActivity.this);
                dlgAlert.setMessage(e.getMessage());
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        });
        btnCancel.setOnClickListener(view -> finish());
        imgBack.setOnClickListener(view -> finish());

    }
}