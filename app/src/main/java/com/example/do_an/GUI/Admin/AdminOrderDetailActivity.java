package com.example.do_an.GUI.Admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.Adapter.Common.OrderDetailAdapter;
import com.example.do_an.Models.OrderDetailModel;
import com.example.do_an.Models.OrderModel;
import com.example.do_an.R;
import com.example.do_an.Service.OrderDetailService;
import com.example.do_an.Service.OrderService;
import com.example.do_an.SystemConstant.SystemConstant;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class AdminOrderDetailActivity extends AppCompatActivity {
    private OrderModel orderModel;
    private Spinner spinnerOrderStatus;
    private EditText edtFullName, edtPhone, edtAddress, edtNote;
    private ImageView imgBack;
    private Integer orderStatus = 0;
    private Button btnSave;
    private String customerName, phone, address, note;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);
        initialize();
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(AdminOrderDetailActivity.this, AdminOrderActivity.class);
            startActivity(intent);
        });

        final List<String> status = new ArrayList<>();
        status.add(SystemConstant.STATUS_ORDER_CANCEL_STR);
        status.add(SystemConstant.STATUS_ORDER_PENDING_STR);
        status.add(SystemConstant.STATUS_ORDER_DELIVERING_STR);
        status.add(SystemConstant.STATUS_ORDER_DELIVERED_STR);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(AdminOrderDetailActivity.this,
                android.R.layout.simple_spinner_item, status);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderStatus.setAdapter(adapterStatus);
        spinnerOrderStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                orderStatus = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerOrderStatus.setSelection(orderModel.getStatus());
        btnSave.setOnClickListener(view -> {
            try {
                customerName = edtFullName.getText().toString().trim();
                if(customerName.isEmpty()){
                    edtFullName.requestFocus();
                    throw new Exception("Chưa nhập tên người nhận !");
                }
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9-._]{5,}$");
                phone = edtPhone.getText().toString().trim();
                if (!pattern.matcher(phone).matches()) {
                    edtPhone.requestFocus();
                    throw new Exception("Số điện thoại không hợp lệ !");
                }
                address = edtAddress.getText().toString().trim();
                if (address.isEmpty()) {
                    edtAddress.requestFocus();
                    throw new Exception("Chưa nhập địa chỉ !");
                }
                note = edtNote.getText().toString().trim();
                if (update() <= 0) {
                    throw new Exception("Lỗi hệ thống !");
                }
                View layout = LayoutInflater.from(AdminOrderDetailActivity.this).inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_root));
                TextView text = (TextView) layout.findViewById(R.id.text);
                ImageView imageView = (ImageView) layout.findViewById(R.id.image);
                imageView.setImageResource(R.drawable.ic_baseline_check_24);
                text.setText("Đã lưu");
                Toast toast = new Toast(AdminOrderDetailActivity.this);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            } catch (Exception e) {
                android.app.AlertDialog.Builder dlgAlert =
                        new android.app.AlertDialog.Builder(AdminOrderDetailActivity.this);
                dlgAlert.setMessage(e.getMessage());
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int update() {
        orderModel.setStatus(orderStatus);
        orderModel.setCustomerName(customerName);
        orderModel.setPhone(phone);
        orderModel.setAddress(address);
        orderModel.setNote(note);
        return OrderService.getInstance().update(orderModel);
    }

    private void initialize() {
        int id = getIntent().getExtras().getInt("id");
        orderModel = OrderService.getInstance().findOne(id);
        List<OrderDetailModel> orderDetailModels = OrderDetailService.getInstance().findAllByOrderId(id);
        OrderDetailAdapter adapter = new OrderDetailAdapter(AdminOrderDetailActivity.this, orderDetailModels);
        ListView listView = findViewById(R.id.listViewOrderDetail);
        listView.setAdapter(adapter);

        TextView createdDate = findViewById(R.id.tvCreatedDate);
        createdDate.setText(orderModel.getCreatedDate());
        TextView createdBy = findViewById(R.id.tvCreatedBy);
        createdBy.setText(orderModel.getCreatedBy());
        TextView modifiedDate = findViewById(R.id.tvModifiedDate);
        modifiedDate.setText(orderModel.getModifiedDate());
        TextView modifiedBy = findViewById(R.id.tvModifiedBy);
        modifiedBy.setText(orderModel.getModifiedBy());
        edtFullName = findViewById(R.id.edtFullName);
        edtFullName.setText(orderModel.getCustomerName());
        edtPhone = findViewById(R.id.edtPhone);
        edtPhone.setText(orderModel.getPhone());
        edtAddress = findViewById(R.id.edtAddress);
        edtAddress.setText(orderModel.getAddress());
        edtNote = findViewById(R.id.edtNote);
        edtNote.setText(orderModel.getNote());
        TextView totalPrice = findViewById(R.id.tvTotalPrice);
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        totalPrice.setText(numberFormat.format(orderModel.getTotal()));

        imgBack = findViewById(R.id.imgBack);
        spinnerOrderStatus =findViewById(R.id.spinnerStatus);
        btnSave = findViewById(R.id.btnSave);
    }
}