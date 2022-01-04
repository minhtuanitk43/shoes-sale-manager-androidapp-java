package com.example.do_an.GUI.Common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.do_an.Adapter.User.CartAdapter;
import com.example.do_an.GUI.Users.UserOrderActivity;
import com.example.do_an.Models.CartModel;
import com.example.do_an.R;
import com.example.do_an.Service.CartService;
import com.example.do_an.Utils.SessionUtil;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class CartFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static TextView totalPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button deleteAll = view.findViewById(R.id.btnDeleteAll);
        LinearLayout lnlTotal = view.findViewById(R.id.lnlTotal);
        int userId = SessionUtil.getUserId();
        if (userId > 0) {
            List<CartModel> list = CartService.getInstance().findALlByUserId(userId);
            CartAdapter adapter = new CartAdapter(view.getContext(), list);
            ListView listView = view.findViewById(R.id.listViewCart);
            listView.setAdapter(adapter);
            totalPrice = view.findViewById(R.id.tvTotalPrice);
            Locale locale = new Locale("vi", "VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            double total = 0;
            for (CartModel c : list) {
                total += c.getProduct().getPrice() * c.getQuantity();
            }
            totalPrice.setText(numberFormat.format(total));
            Button btnOrder = view.findViewById(R.id.btnOrder);

            deleteAll.setOnClickListener(view12 -> {
                if (!list.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view12.getContext());
                    builder.setIcon(view12.getContext().getResources().getDrawable(R.drawable.ic_baseline_clear_24));
                    builder.setTitle("Xóa tất cả sản phẩm trong giỏ hàng ?");
                    builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                        CartService.getInstance().deleteAllByUserId(userId);
                        list.clear();
                        totalPrice.setText(numberFormat.format(0));
                        adapter.notifyDataSetChanged();
                        MainActivity.setNotificationCart(0);
                    });
                    builder.setNegativeButton("Không", (dialog, which) -> {
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            btnOrder.setOnClickListener(view1 -> {
                List<CartModel> list1 = list.stream().filter(c -> c.getProduct().getStock() > 0
                        && c.getProduct().getStatus() == 1).collect(Collectors.toList());
                if (!list1.isEmpty()) {
                    if (list.stream().noneMatch(c -> c.getProduct().getStock() == 0 || c.getProduct().getStatus() == 0)) {
                        Intent intent = new Intent(view1.getContext(), UserOrderActivity.class);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view1.getContext());
                        builder.setIcon(getResources().getDrawable(R.drawable.ic_baseline_add_shopping_cart_24));
                        builder.setMessage("Tiếp tục mua, xóa các sản phẩm đã hết hàng khỏi giỏ hàng ?");
                        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                            Intent intent = new Intent(view1.getContext(), UserOrderActivity.class);
                            startActivity(intent);
                        });
                        builder.setNegativeButton("Không", (dialog, which) -> {
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }else{
                    AlertDialog.Builder dlgAlert =
                            new AlertDialog.Builder(view1.getContext());
                    dlgAlert.setMessage("Không có sản phẩm nào khả dụng trong giỏ hàng");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            });

        } else {
            TextView tvNotLogin = view.findViewById(R.id.tvNotLogin);
            tvNotLogin.setVisibility(TextView.VISIBLE);
            deleteAll.setVisibility(Button.GONE);
            lnlTotal.setVisibility(LinearLayout.GONE);
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
            builder.setTitle("Bạn chưa đăng nhập");
            builder.setPositiveButton("Đăng nhập ngay", (dialog, which) -> {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            });
            builder.setNegativeButton("Lúc khác", (dialog, which) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}
