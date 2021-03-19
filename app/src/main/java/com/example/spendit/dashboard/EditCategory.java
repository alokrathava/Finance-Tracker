package com.example.spendit.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spendit.R;
import com.example.spendit.databinding.ActivityEditCategoryBinding;
import com.example.spendit.network.Api;
import com.example.spendit.network.AppConfig;
import com.example.spendit.network.ServerResponse;
import com.example.spendit.utils.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditCategory extends AppCompatActivity {

    private static final String TAG = "EditCategory";
    private ActivityEditCategoryBinding binding;
    private String ReName;
    private int Category_Id;
    private final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        binding = ActivityEditCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        init();
    }

    private void init() {
        Category_Id = Integer.parseInt(getIntent().getStringExtra("category_id"));
        Log.e(TAG, "init: " + Category_Id);
        binding.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEdit(Category_Id);
            }
        });
    }

    private void doEdit(int category_id) {
        Log.e(TAG, "doEdit: " + category_id);
        ReName = binding.categoryVal.getText().toString();

        if (TextUtils.isEmpty(ReName)) {
            binding.categoryVal.setError("All Fields Are Required");
        } else {
            executeUpdate(ReName, category_id);
        }
    }

    private void executeUpdate(String reName, int category_id) {
        Log.e(TAG, "executeUpdate: " + reName);
        Log.e(TAG, "executeUpdate: " + category_id);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.updateCategory(category_id, reName);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        resendActivity();
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

    private void resendActivity() {
        startActivity(new Intent(context, Category.class));
    }
}