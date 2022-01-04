package com.example.do_an.Adapter.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.do_an.Models.OrderDetailModel;
import com.example.do_an.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends BaseAdapter {
    private Context context;
    private List<OrderDetailModel> list;

    public OrderDetailAdapter(Context context, List<OrderDetailModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.list_row_orderdetail, null);
        final OrderDetailModel model = list.get(i);

        TextView name = row.findViewById(R.id.tvProductName);
        TextView size = row.findViewById(R.id.tvSize);
        TextView price = row.findViewById(R.id.tvPrice);
        TextView quantity = row.findViewById(R.id.tvQuantity);
        TextView lineNumber = row.findViewById(R.id.tvLineNumber);

        lineNumber.setText((i + 1)+".");
        name.setText(model.getProduct().getName());
        size.setText(model.getProductSize());
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        price.setText(numberFormat.format(model.getProduct().getPrice()));
        quantity.setText(String.valueOf(model.getQuantity()));

        return row;
    }
}
