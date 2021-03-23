package com.example.spendit.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spendit.R;
import com.example.spendit.databinding.ActivityEditExpenseBinding;
import com.example.spendit.network.Api;
import com.example.spendit.network.AppConfig;
import com.example.spendit.network.ServerResponse;
import com.example.spendit.utils.Config;
import com.example.spendit.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditExpense extends AppCompatActivity {

    private static final String TAG = "EditExpense";
    private final Context context = this;
    private ActivityEditExpenseBinding binding;
    private SharedPrefManager sharedPrefManager;
    private int UID, A_ID, A_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        binding = ActivityEditExpenseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPrefManager = new SharedPrefManager(context);

        init();
    }

    private void init() {
        Log.e(TAG, "init: " + sharedPrefManager.getInt("id"));
        UID = sharedPrefManager.getInt("id");
        A_ID = Integer.parseInt(getIntent().getStringExtra("category_id"));
        binding.categoryVal.setText(getIntent().getStringExtra("amount"));

        binding.addbtn.setOnClickListener(v -> doUpdate());
    }

    private void doUpdate() {
        A_val = Integer.parseInt(binding.categoryVal.getText().toString());
        if (TextUtils.isEmpty(String.valueOf(A_val))) {
            binding.categoryVal.setError("Insert Data");
        } else {
            executeUpdate(A_ID, UID, A_val);
        }
    }

    private void executeUpdate(int a_id, int uid, int a_val) {
        Log.e(TAG, "executeUpdate: " + a_id);
        Log.e(TAG, "executeUpdate: " + uid);
        Log.e(TAG, "executeUpdate: " + a_val);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.updateExpense(a_id, a_val);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }
                } else {
                    Config.showToast(context, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Config.showToast(context, t.getMessage());
            }
        });
    }
}