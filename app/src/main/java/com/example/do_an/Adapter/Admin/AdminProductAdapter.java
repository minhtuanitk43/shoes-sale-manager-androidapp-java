package com.example.do_an.Adapter.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.do_an.GUI.Admin.AdminProductDetailActivity;
import com.example.do_an.GUI.Admin.InsertOrUpdateProductActivity;
import com.example.do_an.Models.ProductModel;
import com.example.do_an.R;
import com.example.do_an.Service.CartService;
import com.example.do_an.Service.ProductService;
import com.example.do_an.SystemConstant.SystemConstant;
import com.example.do_an.Utils.SessionUtil;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminProductAdapter extends BaseAdapter {
    Context context;
    List<ProductModel> products;

    public AdminProductAdapter(Context context, List<ProductModel> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View row = inflater.inflate(R.layout.list_row_admin_product, null);
        ImageView imgProduct = row.findViewById(R.id.imgProduct);
        TextView name = row.findViewById(R.id.tvProductName);
        TextView category = row.findViewById(R.id.tvProductCategory);
        TextView price = row.findViewById(R.id.tvProductPrice);
        TextView purchases = row.findViewById(R.id.tvProductPurchases);
        TextView stock = row.findViewById(R.id.tvProductStock);
        TextView sizes = row.findViewById(R.id.tvProductSizes);
        TextView status = row.findViewById(R.id.tvProductStatus);

        final ProductModel product = products.get(i);
        if (product.getThumbnail() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getThumbnail(), 0, product.getThumbnail().length);
            imgProduct.setImageBitmap(bitmap);
        }
        name.setText(product.getName());
        category.setText(product.getCategory().getName());
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        price.setText(numberFormat.format(product.getPrice()));
        purchases.setText(String.valueOf(product.getPurchases()));
        stock.setText(String.valueOf(product.getStock()));
        if (product.getStock() == 0) {
            stock.setTextColor(Color.RED);
        }
        sizes.setText(product.getSizes());
        /*status.setText(product.getStatus() == 1 ? "Hiện" : "Ẩn");*/
        if (product.getStatus() == 1) {
            status.setText("Hiện");
            status.setTextColor(Color.parseColor("#007700"));
        } else {
            status.setText("Ẩn");
            status.setTextColor(Color.parseColor("#FF3333"));
        }

        Button btnUpdate = row.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(view12 -> {
            Intent intent = new Intent(context, InsertOrUpdateProductActivity.class);
            intent.putExtra("id", product.getId());
            context.startActivity(intent);
        });
        Button btnDelete = row.findViewById(R.id.btnDelete);
        if (SessionUtil.getPermissionUserLogin().equals(SystemConstant.PERMISSION_ADMIN)) {
            btnDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
                builder.setPositiveButton("Có", (dialog, which) -> {
                    ProductService.getInstance().delete(product.getId());
                    CartService.getInstance().deleteAllByProductId(product.getId());
                    products.clear();
                    products = ProductService.getInstance().findALl();

                    View layout = LayoutInflater.from(context).inflate(R.layout.toast, (ViewGroup) row.findViewById(R.id.toast_layout_root));
                    TextView text = (TextView) layout.findViewById(R.id.text);
                    ImageView imageView = (ImageView) layout.findViewById(R.id.image);
                    imageView.setImageResource(R.drawable.ic_baseline_check_24);
                    text.setText("Đã xóa");
                    Toast toast = new Toast(context);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                    notifyDataSetChanged();
                });
                builder.setNegativeButton("Không", (dialog, which) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        } else {
            btnDelete.setEnabled(false);
        }
        Button btnDetail = row.findViewById(R.id.btnDetail);
        btnDetail.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, AdminProductDetailActivity.class);
            intent.putExtra("id", product.getId());
            context.startActivity(intent);
        });

        return row;
    }
}
