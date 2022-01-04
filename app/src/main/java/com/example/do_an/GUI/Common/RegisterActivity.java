package com.example.do_an.GUI.Common;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.Models.UserModel;
import com.example.do_an.R;
import com.example.do_an.Service.UserService;
import com.example.do_an.SystemConstant.SystemConstant;
import com.example.do_an.Utils.SessionUtil;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegister, btnRefresh;
    private ImageView imgBack;
    private EditText edtUserName, edtPassword, edtPasswordAgain, edtFullName, edtPhone, edtAddress;
    private String userName, password, passwordAgain, fullName, phone, address;
    private Pattern patternPassword, patternUsername;
    UserModel userModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
        //set event imgBack
        imgBack.setOnClickListener(view -> finish());

        btnRegister.setOnClickListener(view -> register());

        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtUserName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                userName = edtUserName.getText().toString().trim();
                if (patternUsername.matcher(userName).matches()) {
                    edtUserName.setError(null);
                    edtUserName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24_green, 0);
                } else {
                    edtUserName.setError("Username tối thiểu 5 ký tự bao gồm chữ cái, số và các ký tự: '-', '.', '_'");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (userName.isEmpty())
                    edtUserName.setError("Chưa nhập Username !");
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                password = edtPassword.getText().toString().trim();
                if (patternPassword.matcher(password).matches()) {
                    edtPassword.setError(null);
                    edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24_green, 0);
                } else {
                    edtPassword.setError("Mật khẩu tối thiểu 8 ký tự, bao gồm chữ cái hoa, chữ cái thường, " +
                            "số và ít nhất 1 ký tự đặc biệt !");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (password.isEmpty()) {
                    edtPassword.setError("Chưa nhập mật khẩu !");
                }
            }
        });

        edtPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtPasswordAgain.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                passwordAgain = edtPasswordAgain.getText().toString().trim();
                if (!passwordAgain.equals(password)) {
                    edtPasswordAgain.setError("Nhập lại mật khẩu không khớp !");
                } else {
                    edtPasswordAgain.setError(null);
                    edtPasswordAgain.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_baseline_check_24_green, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (passwordAgain.isEmpty()) {
                    edtPasswordAgain.setError("Chưa xác nhận mật khẩu !");
                }
            }
        });
        btnRefresh.setOnClickListener(view -> {
            edtUserName.setText("");
            edtFullName.setText("");
            edtPassword.setText("");
            edtPhone.setText("");
            edtPasswordAgain.setText("");
            edtAddress.setText("");
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void register() {
        int check = 1;
        try {
            if (edtUserName.getError() != null) {
                check = 0;
            }
            userName = edtUserName.getText().toString().trim();
            if (UserService.getInstance().checkUsername(userName)) {
                edtUserName.setError("Username đã tồn tại !");
                check = 0;
            }
            if (edtPassword.getError() != null) {
                check = 0;
            }
            if (edtPasswordAgain.getError() != null) {
                check = 0;
            }
            password = edtPassword.getText().toString().trim();

            fullName = edtFullName.getText().toString().trim();
            if (fullName.isEmpty()) {
                edtFullName.setError("Chưa nhập họ tên !");
                check = 0;
            }
            Pattern patternPhone = Pattern.compile("^[0-9]{10,}$");
            phone = edtPhone.getText().toString().trim();
            if (phone.isEmpty()) {
                edtPhone.setError("Chưa nhập số điện thoại !");
                check = 0;
            }
            if (!patternPhone.matcher(phone).matches()) {
                edtPhone.setError("Số điện thoại không hợp lệ !");
                check = 0;
            }
            address = edtAddress.getText().toString().trim();
            if (address.isEmpty()) {
                edtAddress.setError("Chưa nhập địa chỉ !");
            }
            if (check == 0) {
                throw new Exception("Thông tin nhập vào không hợp lệ !");
            }
            long result = insert();
            if (result <= 0) {
                throw new Exception("Lỗi hệ thống !");
            }
            userModel.setId((int) result);
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
            builder.setTitle("Đăng ký thành công");
            builder.setPositiveButton("Đăng nhập ngay", (dialog, which) -> {
                SessionUtil sessionUtil = new SessionUtil(RegisterActivity.this);
                sessionUtil.setUserLogin(userModel);
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            });
            builder.setNegativeButton("Lúc khác", (dialog, which) -> {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            android.app.AlertDialog.Builder dlgAlert =
                    new android.app.AlertDialog.Builder(RegisterActivity.this);
            dlgAlert.setMessage(e.getMessage());
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private long insert() {
        userModel = new UserModel();
        userModel.setUserName(userName);
        userModel.setPassword(password);
        userModel.setFullName(fullName);
        userModel.setPhone(phone);
        userModel.setAddress(address);
        userModel.setStatus(SystemConstant.STATUS_ACTIVE);
        userModel.setPermission(SystemConstant.PERMISSION_USER);
        userModel.setCreatedBy(userName);
        return UserService.getInstance().insert(userModel);
    }

    private void initialize() {
        patternPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        patternUsername = Pattern.compile("^[a-zA-Z0-9-._]{5,}$");
        btnRegister = findViewById(R.id.btnRegister);
        btnRefresh = findViewById(R.id.btnRefresh);
        imgBack = findViewById(R.id.imgBack);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtPasswordAgain = findViewById(R.id.edtPasswordAgain);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtFullName = findViewById(R.id.edtFullName);
    }
}