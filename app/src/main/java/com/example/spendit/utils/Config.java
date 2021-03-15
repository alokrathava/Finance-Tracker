package com.example.spendit.utils;

import android.content.Context;
import android.widget.Toast;

public class Config {

    public static int user_id = 1;
    public static String url = "http://192.168.0.133/SpendIt/api/";
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


}
