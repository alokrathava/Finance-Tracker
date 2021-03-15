package com.example.spendit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spendit.auth.Login;

public class SplashScreen extends AppCompatActivity {

    private final Context context = this;
    private final static int SPLASH_SCREEN_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> startActivity(new Intent(context, Login.class)), SPLASH_SCREEN_TIME_OUT);
    }
}