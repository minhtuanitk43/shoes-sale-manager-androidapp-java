package com.example.do_an.Adapter.User;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.do_an.Adapter.Common.OrderDetailAdapter;
import com.example.do_an.Models.OrderDetailModel;
import com.example.do_an.Models.OrderModel;
import com.example.do_an.R;
import com.example.do_an.Service.OrderDetailService;
import com.example.do_an.Service.OrderService;
import com.example.do_an.Utils.SessionUtil;
import com.example.do_an.SystemConstant.SystemConstant;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class UserOrdersAdapter extends BaseAdapter {
    private Context context;
    private List<OrderModel> list;

    public UserOrdersAdapter(Context context, List<OrderModel> list) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.list_row_user_order, null);
        final OrderModel orderModel = list.get(i);
        TextView date = row.findViewById(R.id.tvOrderDate);
        TextView total = row.findViewById(R.id.tvTotalPrice);
        TextView detail = row.findViewById(R.id.tvOrderDetail);
        TextView lineNumber = row.findViewById(R.id.tvLineNumber);
        TextView status = row.findViewById(R.id.tvOrderStatus);
        TextView cancel = row.findViewById(R.id.tvCancel);

        if(orderModel.getStatus() == SystemConstant.STATUS_ORDER_PENDING){
            cancel.setEnabled(true);
            cancel.setTextColor(Color.RED);
            cancel.setOnClickListener(view1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(context.getResources().getDrawable(R.drawable.ic_baseline_clear_24));
                builder.setTitle("Bạn có chắc muốn hủy đơn hàng này ?");
                builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                    orderModel.setStatus(SystemConstant.STATUS_ORDER_CANCEL);
                    OrderService.getInstance().update(orderModel);
                    list.clear();
                    list = OrderService.getInstance().findAllByUserId(SessionUtil.getUserId());
                    notifyDataSetChanged();
                });
                builder.setNegativeButton("Không", (dialog, which) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }
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
        lineNumber.setText((i + 1) + ".");
        detail.setOnClickListener(view12 -> {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.user_orderdetail_layout);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
            }
            TextView tvNote = dialog.findViewById(R.id.tvNote);
            tvNote.setText(orderModel.getNote());
            ListView listView = dialog.findViewById(R.id.listViewOrderDetail);
            List<OrderDetailModel> list2 = OrderDetailService.getInstance()
                    .findAllByOrderId(orderModel.getId());
            OrderDetailAdapter adapter = new OrderDetailAdapter(context, list2);
            listView.setAdapter(adapter);
            dialog.show();
        });

        return row;
    }
}
