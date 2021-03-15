package com.example.spendit.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spendit.R;
import com.example.spendit.databinding.ActivityRegisterBinding;
import com.example.spendit.network.Api;
import com.example.spendit.network.AppConfig;
import com.example.spendit.network.ServerResponse;
import com.example.spendit.utils.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {

    private final Context context = this;
    private static final String TAG = "Register";
    private ActivityRegisterBinding binding;
    private String Name, Email, Password, Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        init();

    }

    private void init() {
        binding.registerbtn.setOnClickListener(v -> {
            doRegister();
        });
    }

    private void doRegister() {
        Name = binding.name.getText().toString();
        Email = binding.email.getText().toString();
        Password = binding.password.getText().toString();
        Phone = binding.phone.getText().toString();

        if (TextUtils.isEmpty(Name) && TextUtils.isEmpty(Email) && TextUtils.isEmpty(Password) && TextUtils.isEmpty(Phone)) {
            binding.name.setError("All Fields Are Required");
            binding.email.setError("");
            binding.password.setError("");
            binding.phone.setError("");
        } else if (Password.length() < 8) {
            binding.password.setError("Password should be greater than 8 digits");
        } else {
            executeRegister(Name, Email, Password, Phone);
        }
    }

    private void executeRegister(String name, String email, String password, String phone) {
        Log.e(TAG, "executeRegister: " + name);
        Log.e(TAG, "executeRegister: " + email);
        Log.e(TAG, "executeRegister: " + password);
        Log.e(TAG, "executeRegister: " + phone);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.register(name, email, password, phone);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        moveToActivity();
                    } else {
                        Config.showToast(context, serverResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Config.showToast(context, t.getMessage());
            }
        });

    }

    private void moveToActivity() {
        startActivity(new Intent(context, Login.class));
    }
}