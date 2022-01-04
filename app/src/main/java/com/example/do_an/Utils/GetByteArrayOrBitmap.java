package com.example.do_an.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GetByteArrayOrBitmap {
    public static List<byte[]> getListByteArray(List<ImageView> imageViewList){
        List<byte[]> list = new ArrayList<>();
        for (ImageView imageView:imageViewList){
            if(imageView.getDrawable()!=null){
                //get bitmap from imageView
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageInByte = baos.toByteArray();
                list.add(imageInByte);
            }
        }
        return list;
    }
    //convert byteArray to bitmap
    public static Bitmap getByArrayAsBitmap(byte[] byteArray){
        return  BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
    }
}
