package com.example.do_an.GUI.Users;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.do_an.Adapter.Common.ProductImagesAdapter;
import com.example.do_an.GUI.Admin.InsertOrUpdateProductActivity;
import com.example.do_an.GUI.Common.LoginActivity;
import com.example.do_an.GUI.Common.MainActivity;
import com.example.do_an.Models.CartModel;
import com.example.do_an.Models.FavouriteModel;
import com.example.do_an.Models.ImageModel;
import com.example.do_an.Models.ProductModel;
import com.example.do_an.Models.UserModel;
import com.example.do_an.R;
import com.example.do_an.Service.CartService;
import com.example.do_an.Service.FavouriteService;
import com.example.do_an.Service.ProductService;
import com.example.do_an.SystemConstant.SystemConstant;
import com.example.do_an.Utils.SessionUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class UserProductDetailActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ProductModel productModel;
    private TextView tvName, tvPrice, tvShortDes, tvDes, tvAddToCart, tvBuyNow;
    private ImageView imgBack, imgFavourite, imgUpdate;
    private LinearLayout lnlSize;
    ColorStateList oldColors;
    private String size = null;

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_detail);
        initialize();
        //get Product id
        if (getIntent().hasExtra("id")) {
            int id = getIntent().getExtras().getInt("id");
            productModel = ProductService.getInstance().findOne(id);
        }

        //set imgBack
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(UserProductDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });

        //set adapter for slide image
        ImageModel imageModel = new ImageModel();
        imageModel.setImage(productModel.getThumbnail());
        List<ImageModel> imageModels = new ArrayList<>();
        imageModels.add(imageModel);
        imageModels.addAll(productModel.getImages());
        ProductImagesAdapter adapter = new ProductImagesAdapter(getBaseContext(), imageModels);
        viewPager.setAdapter(adapter);

        declare();

        //set event ocClick
        tvAddToCart.setOnClickListener(view -> {
            String userNameLogin = SessionUtil.getUserNameUserLogin();
            if (userNameLogin.isEmpty()) {
                showDiaLogLogin();
            } else if (productModel.getStock() > 0) {
                if (size != null) {
                    UserModel user = new UserModel();
                    user.setId(SessionUtil.getUserId());
                    CartModel cartModel = new CartModel();
                    cartModel.setProduct(productModel);
                    cartModel.setUser(user);
                    cartModel.setQuantity(1);
                    cartModel.setProductSize(size);
                    if (CartService.getInstance().findByProductIdAndUserIdAndSize(cartModel) != null) {
                        AlertDialog.Builder dlgAlert =
                                new AlertDialog.Builder(UserProductDetailActivity.this);
                        dlgAlert.setMessage("Sản phẩm đã có trong giỏ hàng");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    } else {
                        long result = CartService.getInstance().insert(cartModel);
                        if (result > 0) {
                            MainActivity.setNotificationCart(CartService.getInstance()
                                    .findALlByUserId(SessionUtil.getUserId()).size());
                            toastResult();
                        }
                    }
                } else {
                    AlertDialog.Builder dlgAlert =
                            new AlertDialog.Builder(UserProductDetailActivity.this);
                    dlgAlert.setMessage("Bạn chưa chọn size sản phẩm");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            }
        });

        if (SessionUtil.getUserId() > 0) {
            imgFavourite.setOnClickListener(view -> {
                int userId = SessionUtil.getUserId();
                if (userId > 0) {
                    FavouriteModel favouriteModel = FavouriteService.getInstance()
                            .checkFavouriteByUserIdAndProductId(userId, productModel.getId());
                    if (favouriteModel != null) {
                        FavouriteService.getInstance().delete(favouriteModel.getId());
                        imgFavourite.setBackgroundTintList(getResources().getColorStateList(R.color.nav_color));
                        toastFavourite("unFavourite");
                    } else {
                        favouriteModel = new FavouriteModel();
                        favouriteModel.setUserId(userId);
                        favouriteModel.setProductId(productModel.getId());
                        FavouriteService.getInstance().insert(favouriteModel);
                        imgFavourite.setBackgroundTintList(getResources().getColorStateList(R.color.orange));
                        toastFavourite("favourite");
                    }
                }
            });
        }
    }

    private void toastFavourite(String type) {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        TextView text = (TextView) layout.findViewById(R.id.text);
        if (type.equals("favourite")) {
            image.setImageResource(R.drawable.ic_baseline_check_24);
            text.setText("Đã thích");
        } else {
            image.setImageResource(R.drawable.ic_baseline_clear_24);
            text.setText("Bỏ thích");
        }

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void toastResult() {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_baseline_check_circle_24);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Đã thêm vào giỏ hàng");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showDiaLogLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProductDetailActivity.this);
        builder.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
        builder.setTitle("Bạn chưa đăng nhập");
        builder.setPositiveButton("Đăng nhập ngay", (dialog, which) -> {
            Intent intent = new Intent(UserProductDetailActivity.this, LoginActivity.class);
            intent.putExtra("productDetailId2", productModel.getId());
            startActivity(intent);
        });
        builder.setNegativeButton("Lúc khác", (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint({"UseCompatLoadingForColorStateLists", "UseCompatLoadingForDrawables"})
    private void declare() {
        if (SessionUtil.getUserId() <= 0) {
            imgFavourite.setVisibility(ImageView.GONE);
        } else {
            if (SessionUtil.getPermissionUserLogin().equals(SystemConstant.PERMISSION_ADMIN)
                    || SessionUtil.getPermissionUserLogin().equals(SystemConstant.PERMISSION_STAFF)) {
                imgUpdate.setVisibility(ImageView.VISIBLE);
                imgUpdate.setOnClickListener(view -> {
                    Intent intent = new Intent(UserProductDetailActivity.this, InsertOrUpdateProductActivity.class);
                    intent.putExtra("productDetailId2", productModel.getId());
                    startActivity(intent);
                });
            }
            if (FavouriteService.getInstance().checkFavouriteByUserIdAndProductId(
                    SessionUtil.getUserId(), productModel.getId()) != null) {
                imgFavourite.setBackgroundTintList(getResources().getColorStateList(R.color.orange));
            }
        }
        if (productModel.getStock() <= 0) {
            tvBuyNow.setText(SystemConstant.PRODUCT_SOLD_OUT);
            tvAddToCart.setVisibility(TextView.GONE);
        } else {
            tvBuyNow.setOnClickListener(view -> {
                String userNameLogin = SessionUtil.getUserNameUserLogin();
                if (userNameLogin.isEmpty()) {
                    showDiaLogLogin();
                } else if (size != null) {
                    Intent intent = new Intent(UserProductDetailActivity.this, UserOrderActivity.class);
                    intent.putExtra("id", productModel.getId());
                    intent.putExtra("size", size);
                    startActivity(intent);
                }else {
                    AlertDialog.Builder dlgAlert =
                            new AlertDialog.Builder(UserProductDetailActivity.this);
                    dlgAlert.setMessage("Bạn chưa chọn size sản phẩm");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            });
        }

        tvName.setText(productModel.getName());
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        tvPrice.setText(numberFormat.format(productModel.getPrice()));
        tvShortDes.setText(productModel.getShortDescription());
        tvDes.setText(productModel.getDescription());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 10, 0);
        List<String> sizes = getSizes(productModel.getSizes());
        for (String str : sizes) {
            str = str.trim();
            while (str.length() > 0 && (str.charAt(0) == '.' || str.charAt(0) == ',' || str.charAt(0) == '0')) {
                str = str.substring(1);
            }
            while (str.length() > 0 && (str.charAt(str.length() - 1) == '.' || str.charAt(str.length() - 1) == ',')) {
                str = str.substring(1);
            }
            if (str.length() > 0) {
                TextView textView = new TextView(getBaseContext());
                textView.setText(str.trim());
                /*textView.setId(listCatgory.get(index).getId());*/
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setLayoutParams(params);
                textView.setBackgroundResource(R.drawable.cusstomborder);
                textView.setOnClickListener(view -> {
                    size = textView.getText().toString();
                    int cout = lnlSize.getChildCount();
                    for (int i = 0; i < cout; i++) {
                        TextView tv = (TextView) lnlSize.getChildAt(i);
                        tv.setBackground(getResources().getDrawable(R.drawable.cusstomborder));
                        tv.setTextColor(oldColors);
                    }
                    textView.setBackground(getResources().getDrawable(R.drawable.cusstomborder_orange));
                    textView.setTextColor(Color.parseColor("#FD511B"));
                });
                lnlSize.addView(textView);
            }
        }

    }

    private void initialize() {
        viewPager = findViewById(R.id.viewpager);
        productModel = new ProductModel();
        imgBack = findViewById(R.id.imgBack);
        imgFavourite = findViewById(R.id.imgFavourite);
        tvName = findViewById(R.id.tvProductName);
        tvShortDes = findViewById(R.id.tvShortDes);
        tvPrice = findViewById(R.id.tvProductPrice);
        tvDes = findViewById(R.id.tvDescription);
        tvAddToCart = findViewById(R.id.tvAddToCart);
        tvBuyNow = findViewById(R.id.tvBuyNow);
        lnlSize = findViewById(R.id.lnlProductSize);
        oldColors = tvName.getTextColors();
        imgUpdate = findViewById(R.id.imgUpdate);
    }

    private List<String> getSizes(String size) {
        size = size.trim();
        size = size.replaceAll(",+", ",");
        size = size.replaceAll("\\.+", ".");
        String[] result = size.split(",");
        return Arrays.asList(result);
    }
}