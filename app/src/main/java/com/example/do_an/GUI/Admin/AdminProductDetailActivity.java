package com.example.do_an.GUI.Admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.Models.ProductModel;
import com.example.do_an.R;
import com.example.do_an.Service.CartService;
import com.example.do_an.Service.ProductService;
import com.example.do_an.Utils.GetByteArrayOrBitmap;
import com.example.do_an.Utils.SessionUtil;
import com.example.do_an.SystemConstant.SystemConstant;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminProductDetailActivity extends AppCompatActivity {
    private Button btnUpdate, btnDelete;
    private ImageView imgBack;
    private Integer productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);
        initializeData();
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(AdminProductDetailActivity.this, AdminProductActivity.class);
            startActivity(intent);
        });
        btnUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(AdminProductDetailActivity.this, InsertOrUpdateProductActivity.class);
            intent.putExtra("productDetailId", productId);
            startActivity(intent);
        });
        if (SessionUtil.getPermissionUserLogin().equals(SystemConstant.PERMISSION_ADMIN)) {
            btnDelete.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminProductDetailActivity.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
                builder.setPositiveButton("Có", (dialog, which) -> {
                    ProductService.getInstance().delete(productId);
                    CartService.getInstance().deleteAllByProductId(productId);

                });
                builder.setNegativeButton("Không", (dialog, which) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            });
        } else {
            btnDelete.setEnabled(false);
        }
    }


    private void initializeData() {
        productId = getIntent().getExtras().getInt("id");
        ProductModel productModel = ProductService.getInstance().findOne(productId);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        imgBack = findViewById(R.id.imgBack);

        TextView tvProductId = findViewById(R.id.tv_productId);
        tvProductId.setText(String.valueOf(productModel.getId()));
        TextView tvProductName = findViewById(R.id.tv_productName);
        tvProductName.setText(productModel.getName());
        TextView tvProductCategory = findViewById(R.id.tv_productCategory);
        tvProductCategory.setText(productModel.getCategory().getName());
        TextView tvProductShortDes = findViewById(R.id.tv_productShortDes);
        tvProductShortDes.setText(productModel.getShortDescription());
        TextView tvProductDescription = findViewById(R.id.tv_productDesciption);
        tvProductDescription.setText(productModel.getDescription());
        TextView tvProductPrice = findViewById(R.id.tv_productPrice);
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        tvProductPrice.setText(numberFormat.format(productModel.getPrice()));
        TextView tvProductPurchase = findViewById(R.id.tv_productPurchase);
        tvProductPurchase.setText(String.valueOf(productModel.getPurchases()));
        TextView tvProductStock = findViewById(R.id.tv_productStock);
        tvProductStock.setText(String.valueOf(productModel.getStock()));
        TextView tvProductCreatedDate = findViewById(R.id.tv_productCreatedDate);
        tvProductCreatedDate.setText(productModel.getCreatedDate());
        TextView tvProductCreatedBy = findViewById(R.id.tv_productCreatedBy);
        tvProductCreatedBy.setText(productModel.getCreatedBy());
        TextView tvProductModifiedDate = findViewById(R.id.tv_productModifiedDate);
        tvProductModifiedDate.setText(productModel.getModifiedDate());
        TextView tvProductModifiedBy = findViewById(R.id.tv_productModiifedBy);
        tvProductModifiedBy.setText(productModel.getModifiedBy());
        TextView tvProductSize = findViewById(R.id.tv_productSize);
        tvProductSize.setText(productModel.getSizes());
        TextView tvProductStatus = findViewById(R.id.tv_productStatus);
        tvProductStatus.setText(productModel.getStatus() == 1 ? "Hiện" : "Ẩn");

        ImageView imageView1 = findViewById(R.id.imgImage1);
        ImageView imageView2 = findViewById(R.id.imgImage2);
        ImageView imageView3 = findViewById(R.id.imgImage3);
        ImageView imageView4 = findViewById(R.id.imgImage4);
        ImageView imageView5 = findViewById(R.id.imgImage5);
        ImageView imageView6 = findViewById(R.id.imgImage6);

        List<ImageView> imageViews = new ArrayList<>();
        imageViews.add(imageView2);
        imageViews.add(imageView3);
        imageViews.add(imageView4);
        imageViews.add(imageView5);
        imageViews.add(imageView6);

        imageView1.setImageBitmap(GetByteArrayOrBitmap.getByArrayAsBitmap(productModel.getThumbnail()));
        for (int i = 0; i < productModel.getImages().size(); i++) {
            imageViews.get(i).setImageBitmap(GetByteArrayOrBitmap.getByArrayAsBitmap(
                    productModel.getImages().get(i).getImage()));
        }
    }
}