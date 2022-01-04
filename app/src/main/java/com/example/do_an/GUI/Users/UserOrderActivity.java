package com.example.do_an.GUI.Users;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.Adapter.User.ListCartInOrderAdapter;
import com.example.do_an.GUI.Common.MainActivity;
import com.example.do_an.Models.CartModel;
import com.example.do_an.Models.OrderDetailModel;
import com.example.do_an.Models.OrderModel;
import com.example.do_an.Models.ProductModel;
import com.example.do_an.Models.UserModel;
import com.example.do_an.R;
import com.example.do_an.Service.CartService;
import com.example.do_an.Service.OrderDetailService;
import com.example.do_an.Service.OrderService;
import com.example.do_an.Service.ProductService;
import com.example.do_an.Service.UserService;
import com.example.do_an.SystemConstant.SystemConstant;
import com.example.do_an.Utils.SessionUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserOrderActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static TextView tvTotalPrice;
    private List<CartModel> list = new ArrayList<>();
    private Button btnOrder;
    private EditText edtFullName, edtPhone, edtAddress, edtNote;
    private Integer productId;
    private ListView listView;
    private String phone, address, note, customerName;
    private double total = 0;
    private ImageView imgBack;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        initialize();
        imgBack.setOnClickListener(view -> finish());
        if (getIntent().getExtras() != null) {
            productId = getIntent().getExtras().getInt("id");
            String size = getIntent().getExtras().getString("size");
            CartModel cartModel = new CartModel();
            ProductModel productModel = ProductService.getInstance().findOne(productId);
            cartModel.setProduct(productModel);
            cartModel.setProductSize(size);
            cartModel.setQuantity(1);
            list.add(cartModel);
        } else {
            list = CartService.getInstance().findALlByUserId(SessionUtil.getUserId());
            list = list.stream().filter(c -> c.getProduct().getStock() > 0 && c.getProduct().getStatus() == 1)
                    .collect(Collectors.toList());
        }
        ListCartInOrderAdapter adapter = new ListCartInOrderAdapter(UserOrderActivity.this, list);
        listView.setAdapter(adapter);
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        for (CartModel c : list) {
            total += c.getProduct().getPrice() * c.getQuantity();
        }
        tvTotalPrice.setText(numberFormat.format(total));

        btnOrder.setOnClickListener(view -> {
            try {
                customerName = edtFullName.getText().toString().trim();
                if (customerName.isEmpty()) {
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
                if (insert() <= 0) {
                    throw new Exception("Lỗi hệ thống !");
                }
                if (productId == null) {
                    CartService.getInstance().deleteAllByUserId(SessionUtil.getUserId());
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(UserOrderActivity.this);
                builder.setIcon(getResources().getDrawable(R.drawable.ic_baseline_add_shopping_cart_24));
                builder.setTitle("Đặt hàng thành công !");
                builder.setPositiveButton("Quản lý đơn hàng", (dialog, which) -> {
                    Intent intent = new Intent(UserOrderActivity.this, UserOrdersActivity.class);
                    startActivity(intent);
                });
                builder.setNegativeButton("Tiếp tục mua sắm", (dialog, which) -> {
                    Intent intent = new Intent(UserOrderActivity.this, MainActivity.class);
                    startActivity(intent);
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } catch (Exception e) {
                showDialog(e.getMessage());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private long insert() {
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(SessionUtil.getUserId());
        orderModel.setPhone(phone);
        orderModel.setAddress(address);
        orderModel.setTotal(total);
        orderModel.setNote(note);
        orderModel.setCustomerName(customerName);
        orderModel.setStatus(SystemConstant.STATUS_ORDER_PENDING);
        long orderId = OrderService.getInstance().insert(orderModel);
        if (orderId > 0) {
            for (CartModel c : list) {
                OrderDetailModel orderDetailModel = new OrderDetailModel();
                orderDetailModel.setOrderId((int) orderId);
                orderDetailModel.setProductId(c.getProduct().getId());
                orderDetailModel.setQuantity(c.getQuantity());
                orderDetailModel.setProductSize(c.getProductSize());
                OrderDetailService.getInstance().insert(orderDetailModel);
                ProductService.getInstance().updatePurchase(c.getProduct().getId(), c.getQuantity());
            }
        }
        return orderId;
    }

    private void initialize() {
        UserModel userModel = UserService.getInstance().findOne(SessionUtil.getUserId());
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnOrder = findViewById(R.id.btnOrder);
        edtFullName = findViewById(R.id.edtFullName);
        edtFullName.setText(userModel.getFullName());
        edtPhone = findViewById(R.id.edtPhone);
        edtPhone.setText(userModel.getPhone());
        edtAddress = findViewById(R.id.edtAddress);
        edtAddress.setText(userModel.getAddress());
        edtNote = findViewById(R.id.edtNote);
        imgBack = findViewById(R.id.imgBack);
        listView = findViewById(R.id.listViewCart);
    }

    private void showDialog(String message) {
        android.app.AlertDialog.Builder dlgAlert =
                new android.app.AlertDialog.Builder(UserOrderActivity.this);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}