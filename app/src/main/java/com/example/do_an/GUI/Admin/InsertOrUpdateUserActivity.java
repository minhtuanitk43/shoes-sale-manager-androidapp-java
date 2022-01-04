package com.example.do_an.GUI.Admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.Models.UserModel;
import com.example.do_an.R;
import com.example.do_an.Service.CartService;
import com.example.do_an.Service.UserService;
import com.example.do_an.SystemConstant.SystemConstant;
import com.example.do_an.Utils.SessionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InsertOrUpdateUserActivity extends AppCompatActivity {
    Button btnInsertOrUpdate, btnUpdate, btnRefresh, btnDelete;
    TextView tvTitle, tvId, tvCreatedDate, tvCreatedBy, tvModifiedDate, tvModifiedBy;
    EditText edtUserName, edtPassword, edtFullName, edtPhone, edtAddress;
    Spinner spinnerPermission, spinnerStatus;
    Integer id, status;
    List<String> listStatus = new ArrayList<>();
    UserModel userModelUpdate;
    LinearLayout lnlId, lnlCreatedDate, lnlCreatedBy, lnlModifiedDate, lnlModifiedBy;
    ImageView imgBack;
    String userName, password, fullName, phone, address, permission;
    ArrayAdapter<String> adapterPermission, adapterStatus;
    Pattern patternUsername, patternPassword;

    static {
        SystemConstant.PERMISSIONS.add(0, "--Chọn quyền--");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_or_update_user);
        initializeData();
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(InsertOrUpdateUserActivity.this, AdminUserActivity.class);
            startActivity(intent);
        });

        //set dataSource for spinnerPermission
        adapterPermission = new ArrayAdapter<>(InsertOrUpdateUserActivity.this,
                android.R.layout.simple_spinner_item, SystemConstant.PERMISSIONS);
        adapterPermission.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPermission.setAdapter(adapterPermission);
        spinnerPermission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                permission = SystemConstant.PERMISSIONS.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //set dataSource for spinnerStatus
        listStatus.add("Hoạt động");
        listStatus.add("Không hoạt động");
        adapterStatus = new ArrayAdapter<>(InsertOrUpdateUserActivity.this,
                android.R.layout.simple_spinner_item, listStatus);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listStatus.get(i).equals("Hoạt động")) {
                    status = SystemConstant.STATUS_ACTIVE;
                } else status = SystemConstant.STATUS_INACTIVE;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //check Insert or Update, getdata
        if (getIntent().hasExtra("id")) {
            id = getIntent().getExtras().getInt("id");
            btnInsertOrUpdate.setText("Cập nhật");
            btnInsertOrUpdate.setEnabled(false);
            tvTitle.setText("Cập nhật thành viên");
            getData();
            spinnerPermission.setEnabled(false);
            spinnerStatus.setEnabled(false);
            btnDelete.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(InsertOrUpdateUserActivity.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa user này?");
                builder.setPositiveButton("Có", (dialog, which) -> {
                    if (SessionUtil.getUserNameUserLogin().equals(userModelUpdate.getUserName())) {
                        AlertDialog.Builder dlgAlert =
                                new AlertDialog.Builder(InsertOrUpdateUserActivity.this);
                        dlgAlert.setMessage("Không thể xóa tài khoản đang đăng nhập !");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    } else {
                        UserService.getInstance().delete(id);
                        CartService.getInstance().deleteAllByUserId(id);
                        Intent intent = new Intent(InsertOrUpdateUserActivity.this, AdminUserActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Không", (dialog, which) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        } else {
            btnUpdate.setVisibility(Button.GONE);
            btnDelete.setVisibility(Button.GONE);
            lnlId.setVisibility(LinearLayout.GONE);
            lnlCreatedBy.setVisibility(LinearLayout.GONE);
            lnlCreatedDate.setVisibility(LinearLayout.GONE);
            lnlModifiedBy.setVisibility(LinearLayout.GONE);
            lnlModifiedDate.setVisibility(LinearLayout.GONE);
            enableEdit();
        }

        btnUpdate.setOnClickListener(view -> enableEdit());
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
                if (id == null) {
                    if (userName.isEmpty())
                        edtUserName.setError("Chưa nhập Username !");
                }
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
                if (id == null) {
                    if (password.isEmpty()) {
                        edtPassword.setError("Chưa nhập mật khẩu !");
                    }
                }else {
                    if(password.isEmpty()){
                        edtPassword.setError(null);
                    }
                }
            }
        });
        btnInsertOrUpdate.setOnClickListener(view -> {
            int check = 1;
            try {
                if (edtUserName.getError() != null) {
                    check = 0;
                }
                userName = edtUserName.getText().toString().trim();
                if (id == null) {
                    if (UserService.getInstance().checkUsername(userName)) {
                        edtUserName.setError("Username đã tồn tại !");
                        check = 0;
                    }
                }
                if (edtPassword.getError() != null) {
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
                if (spinnerPermission.getSelectedItemPosition() == 0) {
                    spinnerPermission.requestFocus();
                    throw new Exception("Chưa chọn quyền người dùng !");
                }
                if (check == 0) {
                    throw new Exception("Thông tin nhập vào không hợp lệ !");
                }
                long result = save();
                if (result <= 0) {
                    throw new Exception("Lỗi hệ thống !");
                }
                if (id != null) {
                    getData();
                    updateComplited();
                }
                View layout = LayoutInflater.from(getBaseContext()).inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toast_layout_root));
                TextView text = (TextView) layout.findViewById(R.id.text);
                ImageView imageView = (ImageView) layout.findViewById(R.id.image);
                imageView.setImageResource(R.drawable.ic_baseline_check_24);
                if (id == null) {
                    text.setText("Thêm thành công");
                } else {
                    text.setText("Cập nhật thành công");
                }
                Toast toast = new Toast(getBaseContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            } catch (Exception e) {
                AlertDialog.Builder dlgAlert =
                        new AlertDialog.Builder(InsertOrUpdateUserActivity.this);
                dlgAlert.setMessage(e.getMessage());
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        });

        btnRefresh.setOnClickListener(view -> {
            edtUserName.setText("");
            edtPassword.setText("");
            edtPhone.setText("");
            edtFullName.setText("");
            edtAddress.setText("");
            if (id != null) {
                getData();
            }
        });
    }

    private void updateComplited() {
        btnInsertOrUpdate.setEnabled(false);
        edtFullName.setFocusableInTouchMode(false);
        edtPhone.setFocusableInTouchMode(false);
        edtUserName.setFocusableInTouchMode(false);
        edtPassword.setFocusableInTouchMode(false);
        edtAddress.setFocusableInTouchMode(false);
        spinnerPermission.setEnabled(false);
        spinnerStatus.setEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private long save() {
        UserModel userModel = new UserModel();
        if(id!=null) {
            userModel.setUserName(userModelUpdate.getUserName());
        }else{
            userModel.setUserName(userName);
        }
        userModel.setPassword(password);
        userModel.setFullName(fullName);
        userModel.setPhone(phone);
        userModel.setAddress(address);
        userModel.setPermission(permission);
        userModel.setStatus(status);
        if (id != null) {
            userModel.setId(userModelUpdate.getId());
            userModel.setCreatedDate(userModelUpdate.getCreatedDate());
            userModel.setCreatedBy(userModelUpdate.getCreatedBy());
            return UserService.getInstance().update(userModel);
        } else {
            return UserService.getInstance().insert(userModel);
        }
    }

    private void enableEdit() {
        if (id != null) {
            edtUserName.setOnClickListener(view -> {
                AlertDialog.Builder dlgAlert =
                        new AlertDialog.Builder(InsertOrUpdateUserActivity.this);
                dlgAlert.setMessage("Không thể thay đổi Username !");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            });
        } else {
            edtUserName.setFocusableInTouchMode(true);
        }
        edtPassword.setFocusableInTouchMode(true);
        edtFullName.setFocusableInTouchMode(true);
        edtPhone.setFocusableInTouchMode(true);
        edtAddress.setFocusableInTouchMode(true);
        if (!btnInsertOrUpdate.isEnabled()) {
            btnInsertOrUpdate.setEnabled(true);
        }
        if (!spinnerPermission.isEnabled()) {
            spinnerPermission.setEnabled(true);
        }
        if (!spinnerStatus.isEnabled()) {
            spinnerStatus.setEnabled(true);
        }
    }

    private void getData() {
        userModelUpdate = UserService.getInstance().findOne(id);
        tvId.setText(String.valueOf(userModelUpdate.getId()));
        edtUserName.setText(userModelUpdate.getUserName());
        edtPassword.setText("");
        edtFullName.setText(userModelUpdate.getFullName());
        edtPhone.setText(userModelUpdate.getPhone());
        edtAddress.setText(userModelUpdate.getAddress());
        tvCreatedDate.setText(userModelUpdate.getCreatedDate());
        tvCreatedBy.setText(userModelUpdate.getCreatedBy());
        tvModifiedDate.setText(userModelUpdate.getModifiedDate());
        tvModifiedBy.setText(userModelUpdate.getModifiedBy());
        spinnerPermission.setSelection(adapterPermission.getPosition(userModelUpdate.getPermission()));
        spinnerStatus.setSelection(adapterStatus.getPosition(userModelUpdate.getStatus() == 1 ? "Hoạt động" : "Không hoạt động"));
    }

    private void initializeData() {
        patternUsername = Pattern.compile("^[a-zA-Z0-9-._]{5,}$");
        patternPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        btnInsertOrUpdate = findViewById(R.id.btnInsertOrUpdate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnDelete = findViewById(R.id.btnDelete);
        imgBack = findViewById(R.id.imgBack);
        tvTitle = findViewById(R.id.tvTitle);

        tvId = findViewById(R.id.tvUserId);
        tvCreatedDate = findViewById(R.id.tvCreatedDate);
        tvCreatedBy = findViewById(R.id.tvCreatedBy);
        tvModifiedBy = findViewById(R.id.tvModifiedBy);
        tvModifiedDate = findViewById(R.id.tvModifiedDate);

        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);

        spinnerPermission = findViewById(R.id.spinnerPermission);
        spinnerStatus = findViewById(R.id.spinner_status);

        lnlId = findViewById(R.id.lnlId);
        lnlModifiedDate = findViewById(R.id.lnlModifiedDate);
        lnlCreatedBy = findViewById(R.id.lnlCreatedBy);
        lnlCreatedDate = findViewById(R.id.lnlCreatedDate);
        lnlModifiedBy = findViewById(R.id.lnlModifiedBy);
    }
}