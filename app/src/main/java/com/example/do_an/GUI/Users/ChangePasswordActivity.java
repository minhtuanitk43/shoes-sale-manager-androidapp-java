package com.example.do_an.GUI.Users;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.do_an.Utils.GetPasswordInMD5;
import com.example.do_an.Utils.SessionUtil;

import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edtOldPW, edtNewPW, edtPWAgain;
    private String oldPw, newPw, newPwAgain;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);
        edtNewPW = findViewById(R.id.edtNewPassword);
        edtOldPW = findViewById(R.id.edtOldPassword);
        edtPWAgain = findViewById(R.id.edtPasswordAgain);
        ImageView imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(view -> finish());

        Pattern patternPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        edtNewPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtNewPW.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                newPw = edtNewPW.getText().toString().trim();
                if (patternPassword.matcher(newPw).matches()) {
                    edtNewPW.setError(null);
                    edtNewPW.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24_green, 0);
                } else {
                    edtNewPW.setError("Mật khẩu tối thiểu 8 ký tự, bao gồm chữ cái hoa, chữ cái thường, " +
                            "số và ít nhất 1 ký tự đặc biệt !");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (newPw.isEmpty()) {
                    edtNewPW.setError("Chưa nhập mật khẩu !");
                }
            }
        });
        edtPWAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtPWAgain.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                newPwAgain = edtPWAgain.getText().toString().trim();
                if (!newPwAgain.equals(newPw)) {
                    edtPWAgain.setError("Nhập lại mật khẩu không khớp !");
                } else {
                    edtPWAgain.setError(null);
                    edtPWAgain.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_baseline_check_24_green, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (newPwAgain.isEmpty()) {
                    edtPWAgain.setError("Chưa xác nhận mật khẩu !");
                }
            }
        });

        btnSave.setOnClickListener(view -> {
            int check = 1;
            try {
                UserModel userModel = UserService.getInstance().findOne(SessionUtil.getUserId());
                oldPw = edtOldPW.getText().toString().trim();
                if (oldPw.isEmpty()) {
                    edtOldPW.setError("Nhập mật khẩu cũ !");
                    check = 0;
                }
                String oldPwMD5 = GetPasswordInMD5.getPasswordInMD5(oldPw);
                if (!oldPwMD5.equals(userModel.getPassword())) {
                    edtOldPW.setError("Mật khẩu cũ không đúng !");
                    check = 0;
                }

                if (edtNewPW.getError() != null) {
                    check = 0;
                }
                if (edtPWAgain.getError() != null) {
                    check = 0;
                }
                if(check==0){
                    throw new Exception("Thông tin không hợp lệ !");
                }
                newPw = edtNewPW.getText().toString().trim();
                userModel.setPassword(newPw);
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
                        new android.app.AlertDialog.Builder(ChangePasswordActivity.this);
                dlgAlert.setMessage(e.getMessage());
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }

        });
        btnCancel.setOnClickListener(view -> finish());
    }
}