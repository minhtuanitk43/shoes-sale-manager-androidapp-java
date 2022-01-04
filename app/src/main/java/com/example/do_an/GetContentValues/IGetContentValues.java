package com.example.do_an.GetContentValues;

import android.content.ContentValues;

public interface IGetContentValues <T>{
    ContentValues getContentValues(T model);
}
