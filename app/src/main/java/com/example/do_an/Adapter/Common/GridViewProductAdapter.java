package com.example.do_an.Adapter.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.do_an.Models.FavouriteModel;
import com.example.do_an.Models.ProductModel;
import com.example.do_an.R;
import com.example.do_an.Service.FavouriteService;
import com.example.do_an.Utils.GetByteArrayOrBitmap;
import com.example.do_an.Utils.SessionUtil;
import com.example.do_an.SystemConstant.SystemConstant;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class GridViewProductAdapter extends ArrayAdapter<ProductModel> {
    ImageView imgDeal;
    public GridViewProductAdapter(@NonNull Context context, List<ProductModel> productModels) {
        super(context, 0, productModels);
    }

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForColorStateLists"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);

        }
        ProductModel productModel = getItem(position);
        ImageView img = view.findViewById(R.id.imgProduct);
        TextView name = view.findViewById(R.id.tvProductName);
        TextView price = view.findViewById(R.id.tvProductPrice);
        TextView soldOut = view.findViewById(R.id.tvSoldOut);

        if(productModel.getThumbnail()!=null) {
            img.setImageBitmap(GetByteArrayOrBitmap.getByArrayAsBitmap(productModel.getThumbnail()));
        }
        name.setText(productModel.getName());
        imgDeal = view.findViewById(R.id.imgDeal);
        if (productModel.getStock() == 0) {
            soldOut.setText(SystemConstant.PRODUCT_SOLD_OUT);
            imgDeal.setImageResource(R.drawable.sold_out);
            imgDeal.getLayoutParams().width = 100;
            imgDeal.getLayoutParams().height = 100;
        }else {
            soldOut.setText("");
            imgDeal.setImageResource(R.drawable.deal);
            imgDeal.getLayoutParams().width = 50;
            imgDeal.getLayoutParams().height = 50;
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        price.setText(numberFormat.format(productModel.getPrice()));

        ImageView imgFavourite = view.findViewById(R.id.imgFavourite);
        int userId = SessionUtil.getUserId();
        if(userId>0) {
            if (FavouriteService.getInstance().checkFavouriteByUserIdAndProductId(
                    userId, productModel.getId()) != null) {
                imgFavourite.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.orange));
            }else {
                imgFavourite.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.nav_color));
            }
        }else {
            imgFavourite.setVisibility(ImageView.GONE);
        }
        imgFavourite.setOnClickListener(view1 -> {
            View layout = LayoutInflater.from(getContext()).inflate(R.layout.toast, (ViewGroup) view1.findViewById(R.id.toast_layout_root));
            TextView text = (TextView) layout.findViewById(R.id.text);
            ImageView imageView = (ImageView) layout.findViewById(R.id.image);

            int userId1 = SessionUtil.getUserId();
            if(userId1 >0) {
                FavouriteModel favouriteModel = FavouriteService.getInstance()
                        .checkFavouriteByUserIdAndProductId(userId1, productModel.getId());
                if (favouriteModel != null) {
                    FavouriteService.getInstance().delete(favouriteModel.getId());
                    imgFavourite.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.nav_color));

                    imageView.setImageResource(R.drawable.ic_baseline_clear_24);
                    text.setText("Bỏ thích");
                    Toast toast = new Toast(getContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                } else {
                    favouriteModel = new FavouriteModel();
                    favouriteModel.setUserId(userId1);
                    favouriteModel.setProductId(productModel.getId());
                    FavouriteService.getInstance().insert(favouriteModel);
                    imgFavourite.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.orange));

                    imageView.setImageResource(R.drawable.ic_baseline_check_24);
                    text.setText("Đã thích");
                    Toast toast = new Toast(getContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
            }
        });
        return view;
    }
}
