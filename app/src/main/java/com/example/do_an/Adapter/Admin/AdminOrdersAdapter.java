package com.example.do_an.Adapter.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an.GUI.Admin.AdminOrderDetailActivity;
import com.example.do_an.Models.OrderModel;
import com.example.do_an.R;
import com.example.do_an.Service.OrderDetailService;
import com.example.do_an.Service.OrderService;
import com.example.do_an.SystemConstant.SystemConstant;
import com.example.do_an.Utils.SessionUtil;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminOrdersAdapter extends BaseAdapter {
    private Context context;
    private List<OrderModel> list;

    public AdminOrdersAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View row = inflater.inflate(R.layout.list_row_admin_order, null);
        final OrderModel orderModel = list.get(i);
        TextView date = row.findViewById(R.id.tvOrderDate);
        TextView total = row.findViewById(R.id.tvTotalPrice);
        Button btnDelete = row.findViewById(R.id.btnDelete);
        Button btnUpdate = row.findViewById(R.id.btnUpdate);
        TextView lineNumber = row.findViewById(R.id.tvLineNumber);
        TextView status = row.findViewById(R.id.tvOrderStatus);
        TextView customerName = row.findViewById(R.id.tvCustomerName);
        TextView orderId = row.findViewById(R.id.tvOrderId);

        lineNumber.setText((i + 1) + ".");
        orderId.setText("DH" + orderModel.getId());
        customerName.setText(orderModel.getCustomerName());
        date.setText(orderModel.getCreatedDate());
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        total.setText(numberFormat.format(orderModel.getTotal()));
        if (orderModel.getStatus() == SystemConstant.STATUS_ORDER_PENDING) {
            status.setText(SystemConstant.STATUS_ORDER_PENDING_STR);
            status.setTextColor(Color.GREEN);
        } else if (orderModel.getStatus() == SystemConstant.STATUS_ORDER_DELIVERING) {
            status.setText(SystemConstant.STATUS_ORDER_DELIVERING_STR);
            status.setTextColor(Color.BLUE);
        } else if (orderModel.getStatus() == SystemConstant.STATUS_ORDER_DELIVERED) {
            status.setText(SystemConstant.STATUS_ORDER_DELIVERED_STR);
        } else {
            status.setText(SystemConstant.STATUS_ORDER_CANCEL_STR);
            status.setTextColor(Color.RED);
        }
        btnUpdate.setOnClickListener(view12 -> {
            Intent intent = new Intent(context, AdminOrderDetailActivity.class);
            intent.putExtra("id", orderModel.getId());
            context.startActivity(intent);
        });
        if (SessionUtil.getPermissionUserLogin().equals(SystemConstant.PERMISSION_ADMIN)) {
            btnDelete.setOnClickListener(view1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa đơn hàng này?");
                builder.setPositiveButton("Có", (dialog, which) -> {
                    if (OrderDetailService.getInstance().deleteAllByOrderId(orderModel.getId()) > 0) {
                        if (OrderService.getInstance().delete(orderModel.getId()) > 0) {
                            View layout = LayoutInflater.from(context).inflate(R.layout.toast,
                                    (ViewGroup) row.findViewById(R.id.toast_layout_root));
                            TextView text = (TextView) layout.findViewById(R.id.text);
                            ImageView imageView = (ImageView) layout.findViewById(R.id.image);
                            imageView.setImageResource(R.drawable.ic_baseline_check_24);
                            text.setText("Đã xóa");
                            Toast toast = new Toast(context);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                            list.remove(orderModel);
                            notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Không", (dialog, which) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }else {
            btnDelete.setEnabled(false);
        }

        return row;
    }
}
