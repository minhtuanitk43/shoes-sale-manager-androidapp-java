package com.example.do_an.GUI.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.Models.CategoryModel;
import com.example.do_an.R;
import com.example.do_an.Service.CategoryService;
import com.example.do_an.Service.ProductService;
import com.example.do_an.Utils.SessionUtil;
import com.example.do_an.SystemConstant.SystemConstant;

public class InsertOrUpdateCategoryActivity extends AppCompatActivity {
    Button btnInsertOrUpdate, btnUpdate, btnRefresh, btnDelete;
    TextView tvId, tvCreatedDate, tvCreatedBy, tvModifiedDate, tvModifiedBy;
    EditText edtName, edtKeySearch;
    Integer id;
    CategoryModel categoryModelUpdate;
    LinearLayout lnlId, lnlCreatedDate, lnlCreatedBy, lnlModifiedDate, lnlModifiedBy;
    ImageView imgBack;
    String name, keySearch;
    TextView titleBar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_or_update_category);
        initializeData();

        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(InsertOrUpdateCategoryActivity.this, AdminCategoryActivity.class);
            startActivity(intent);
        });
        if (getIntent().hasExtra("id")) {
            id = getIntent().getExtras().getInt("id");
            btnInsertOrUpdate.setText("Cập nhật");
            btnInsertOrUpdate.setEnabled(false);
            titleBar.setText("Cập nhật loại sản phẩm");
            getData();
            if (SessionUtil.getPermissionUserLogin().equals(SystemConstant.PERMISSION_ADMIN)) {
                btnDelete.setOnClickListener(view -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InsertOrUpdateCategoryActivity.this);
                    builder.setIcon(android.R.drawable.ic_delete);
                    builder.setTitle("Xác nhận xóa");
                    builder.setMessage("Bạn có chắc chắn muốn xóa loại sản phẩm này?");
                    builder.setPositiveButton("Có", (dialog, which) -> {
                        if (ProductService.getInstance().checkCategoryId(id)) {
                            AlertDialog.Builder dlgAlert =
                                    new AlertDialog.Builder(InsertOrUpdateCategoryActivity.this);
                            dlgAlert.setMessage("Không thể xóa loại sản phẩm đang có sản phẩm !");
                            dlgAlert.setPositiveButton("OK", null);
                            dlgAlert.create().show();
                        } else {
                            CategoryService.getInstance().delete(id);
                            Intent intent = new Intent(InsertOrUpdateCategoryActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Không", (dialog, which) -> {
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                });
            } else {
                btnDelete.setEnabled(false);
            }
        } else {
            btnDelete.setVisibility(Button.GONE);
            btnUpdate.setVisibility(Button.GONE);
            edtName.setFocusableInTouchMode(true);
            edtKeySearch.setFocusableInTouchMode(true);
            lnlId.setVisibility(LinearLayout.GONE);
            lnlModifiedBy.setVisibility(LinearLayout.GONE);
            lnlModifiedDate.setVisibility(LinearLayout.GONE);
            lnlCreatedBy.setVisibility(LinearLayout.GONE);
            lnlCreatedDate.setVisibility(LinearLayout.GONE);
        }
        btnUpdate.setOnClickListener(view -> {
            edtName.setFocusableInTouchMode(true);
            edtName.requestFocus();
            edtKeySearch.setFocusableInTouchMode(true);
            if (!btnInsertOrUpdate.isEnabled()) {
                btnInsertOrUpdate.setEnabled(true);
            }
        });

        btnInsertOrUpdate.setOnClickListener(view -> {
            try {
                name = edtName.getText().toString().trim();
                if (name.isEmpty()) {
                    edtName.requestFocus();
                    edtName.setError("Chưa nhập tên loại sản phẩm !");
                    throw new Exception(edtName.getError().toString());
                }
                keySearch = edtKeySearch.getText().toString().trim();
                save();
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
                e.printStackTrace();
            }
        });

        btnRefresh.setOnClickListener(view -> {
            edtName.setText("");
            edtKeySearch.setText("");
            if (id != null) {
                getData();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void save() {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName(name);
        categoryModel.setKeySearch(keySearch);
        if (id != null) {
            categoryModel.setCreatedDate(categoryModelUpdate.getCreatedDate());
            categoryModel.setCreatedBy(categoryModelUpdate.getCreatedBy());
            categoryModel.setId(id);
            CategoryService.getInstance().update(categoryModel);
        } else {
            CategoryService.getInstance().insert(categoryModel);
        }
    }

    private void getData() {
        categoryModelUpdate = CategoryService.getInstance().findOne(id);
        tvId.setText(String.valueOf(categoryModelUpdate.getId()));
        tvCreatedDate.setText(categoryModelUpdate.getCreatedDate());
        tvCreatedBy.setText(categoryModelUpdate.getCreatedBy());
        tvModifiedDate.setText(categoryModelUpdate.getModifiedDate());
        tvModifiedBy.setText(categoryModelUpdate.getModifiedBy());
        edtName.setText(categoryModelUpdate.getName());
        edtKeySearch.setText(categoryModelUpdate.getKeySearch());
    }

    private void initializeData() {
        btnInsertOrUpdate = findViewById(R.id.btn_insertOrUpdate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnDelete = findViewById(R.id.btnDelete);
        imgBack = findViewById(R.id.imgBack);
        titleBar = findViewById(R.id.tvTitle);

        tvId = findViewById(R.id.tvCategoryId);
        tvCreatedDate = findViewById(R.id.tvCategoryCreatedDate);
        tvCreatedBy = findViewById(R.id.tvCategoryCreatedBy);
        tvModifiedDate = findViewById(R.id.tvCategoryModifiedDate);
        tvModifiedBy = findViewById(R.id.tvCategoryMOdifiedBy);

        edtName = findViewById(R.id.edtCategoryName);
        edtKeySearch = findViewById(R.id.edtCategoryKeySearch);
        lnlId = findViewById(R.id.lnlId);
        lnlModifiedDate = findViewById(R.id.lnlModifiedDate);
        lnlCreatedBy = findViewById(R.id.lnlCreatedBy);
        lnlCreatedDate = findViewById(R.id.lnlCreatedDate);
        lnlModifiedBy = findViewById(R.id.lnlModifiedBy);
    }
}