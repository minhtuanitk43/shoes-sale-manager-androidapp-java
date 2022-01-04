package com.example.do_an.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.do_an.Models.UserModel;

public class SessionUtil {
    private static SharedPreferences prefs;
    public SessionUtil(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void setUserLogin(UserModel user) {
        prefs.edit().putString("userName", user.getUserName()).apply();
        prefs.edit().putString("fullName", user.getFullName()).apply();
        prefs.edit().putString("permission", user.getPermission()).apply();
        prefs.edit().putInt("userId", user.getId()).apply();
    }
    public static void unSetUserLogin() {
        prefs.edit().clear().apply();
    }

    public static String getUserNameUserLogin() {
        return prefs.getString("userName","");
    }
    public static String getFullNameUserLogin() {
        return prefs.getString("fullName","");
    }
    public static String getPermissionUserLogin() {
        return prefs.getString("permission","");
    }
    public static int getUserId(){
        return prefs.getInt("userId",0);
    }
}