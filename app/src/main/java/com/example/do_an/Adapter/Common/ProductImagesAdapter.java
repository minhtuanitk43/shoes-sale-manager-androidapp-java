package com.example.do_an.Adapter.Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.do_an.Models.ImageModel;
import com.example.do_an.R;
import com.example.do_an.Utils.GetByteArrayOrBitmap;

import java.util.List;

public class ProductImagesAdapter extends PagerAdapter {
    private Context context;
    LayoutInflater inflater;
    private List<ImageModel> listImage;

    public ProductImagesAdapter(Context context, List<ImageModel> images) {
        this.context = context;
        listImage = images;
    }

    @Override
    public int getCount() {
        return listImage.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_product_layout, container, false);

        ImageView img = (ImageView) view.findViewById(R.id.imageView_id);
        TextView page = (TextView) view.findViewById(R.id.tvPage);
        TextView totalPage =(TextView) view.findViewById(R.id.tvTotalPage);

        img.setImageBitmap(GetByteArrayOrBitmap.getByArrayAsBitmap(listImage.get(position).getImage()));
        page.setText(String.valueOf(position + 1));
        totalPage.setText(String.valueOf(listImage.size()));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (FrameLayout) object;
    }

}
