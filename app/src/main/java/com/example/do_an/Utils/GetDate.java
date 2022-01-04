package com.example.do_an.Utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetDate {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(date);
    }
}
