package com.example.do_an.Adapter.User;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.do_an.GUI.Common.CartFragment;
import com.example.do_an.GUI.Common.MainActivity;
import com.example.do_an.Models.CartModel;
import com.example.do_an.R;
import com.example.do_an.Service.CartService;
import com.example.do_an.SystemConstant.SystemConstant;
import com.example.do_an.Utils.GetByteArrayOrBitmap;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<CartModel> list;

    public CartAdapter(Context context, List<CartModel> list) {
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.list_row_user_cart, null);
        final CartModel cartModel = list.get(i);
        ImageView thumbnail = row.findViewById(R.id.imgProduct);
        TextView name = row.findViewById(R.id.tvProductName);
        TextView size = row.findViewById(R.id.tvSize);
        TextView price = row.findViewById(R.id.tvPrice);
        TextView quantity = row.findViewById(R.id.tvQuantity);

        thumbnail.setImageBitmap(GetByteArrayOrBitmap.getByArrayAsBitmap(cartModel.getProduct().getThumbnail()));
        name.setText(cartModel.getProduct().getName());
        size.setText(cartModel.getProductSize());
        quantity.setText(String.valueOf(cartModel.getQuantity()));

        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        TextView imgDelete = row.findViewById(R.id.tvDelete);
        imgDelete.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(context.getResources().getDrawable(R.drawable.ic_baseline_clear_24));
            builder.setTitle("Xóa sản phẩm này khỏi giỏ hàng ?");
            builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                CartService.getInstance().delete(cartModel.getId());
                list.remove(cartModel);
                MainActivity.setNotificationCart(list.size());
                double total = 0;
                for (CartModel c : list) {
                    total += c.getProduct().getPrice();
                }
                CartFragment.totalPrice.setText(numberFormat.format(total));
                notifyDataSetChanged();
            });
            builder.setNegativeButton("Không", (dialog, which) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        if (cartModel.getProduct().getStock() > 0 && cartModel.getProduct().getStatus() == 1) {
            price.setText(numberFormat.format(cartModel.getProduct().getPrice()));
            ImageView imgQuantityUp = row.findViewById(R.id.imgQuantityUp);
            imgQuantityUp.setOnClickListener(view12 -> {
                int qty = Integer.parseInt(quantity.getText().toString());
                if (qty >= cartModel.getProduct().getStock()) {
                    AlertDialog.Builder dlgAlert =
                            new AlertDialog.Builder(context);
                    dlgAlert.setMessage("Số lượng đã đạt giới hạn tồn kho của sản phẩm");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                } else if (qty >= 10) {
                    AlertDialog.Builder dlgAlert =
                            new AlertDialog.Builder(context);
                    dlgAlert.setMessage("Một sản phẩm chỉ được mua tối đa 10 sản phẩm");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                } else {
                    qty++;
                    cartModel.setQuantity(qty);
                    CartService.getInstance().update(cartModel);
                    list.set(i, cartModel);
                    double total = 0;
                    for (CartModel c : list) {
                        total += c.getProduct().getPrice() * c.getQuantity();
                    }
                    CartFragment.totalPrice.setText(numberFormat.format(total));
                    notifyDataSetChanged();

                }
            });

            ImageView imgQuantityDown = row.findViewById(R.id.imgQuantityDown);
            imgQuantityDown.setOnClickListener(view13 -> {
                int qty = Integer.parseInt(quantity.getText().toString());
                if (qty == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(context.getResources().getDrawable(R.drawable.ic_baseline_clear_24));
                    builder.setTitle("Xóa sản phẩm này khỏi giỏ hàng ?");
                    builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                        CartService.getInstance().delete(cartModel.getId());
                        list.remove(cartModel);
                        double total = 0;
                        for (CartModel c : list) {
                            total += c.getProduct().getPrice() * c.getQuantity();
                        }
                        CartFragment.totalPrice.setText(numberFormat.format(total));
                        MainActivity.setNotificationCart(list.size());
                        notifyDataSetChanged();
                    });
                    builder.setNegativeButton("Không", (dialog, which) -> {
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    qty--;
                    cartModel.setQuantity(qty);
                    CartService.getInstance().update(cartModel);
                    list.set(i, cartModel);
                    double total = 0;
                    for (CartModel c : list) {
                        total += c.getProduct().getPrice() * c.getQuantity();
                    }
                    CartFragment.totalPrice.setText(numberFormat.format(total));
                    notifyDataSetChanged();
                }
            });
        } else {
            price.setText(SystemConstant.PRODUCT_SOLD_OUT);
            row.setBackgroundResource(R.color.gray);
        }
        return row;
    }
}
