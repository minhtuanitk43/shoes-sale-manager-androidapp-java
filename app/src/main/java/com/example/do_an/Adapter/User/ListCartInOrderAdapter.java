package com.example.do_an.Adapter.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.do_an.Models.CartModel;
import com.example.do_an.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListCartInOrderAdapter extends BaseAdapter {
    private Context context;
    private List<CartModel> list;

    public ListCartInOrderAdapter(Context context, List<CartModel> list) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.list_row_orderdetail, null);
        final CartModel cartModel = list.get(i);

        TextView name = row.findViewById(R.id.tvProductName);
        TextView size = row.findViewById(R.id.tvSize);
        TextView price = row.findViewById(R.id.tvPrice);
        TextView quantity = row.findViewById(R.id.tvQuantity);
        TextView lineNumber = row.findViewById(R.id.tvLineNumber);

        lineNumber.setText((i + 1) + ".");
        name.setText(cartModel.getProduct().getName());
        size.setText(cartModel.getProductSize());
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        price.setText(numberFormat.format(cartModel.getProduct().getPrice()));
        quantity.setText(String.valueOf(cartModel.getQuantity()));

        return row;
    }
}
