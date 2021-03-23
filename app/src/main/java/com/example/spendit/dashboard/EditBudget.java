package com.example.spendit.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spendit.R;
import com.example.spendit.databinding.ActivityEditBudgetBinding;
import com.example.spendit.network.Api;
import com.example.spendit.network.AppConfig;
import com.example.spendit.network.ServerResponse;
import com.example.spendit.utils.Config;
import com.example.spendit.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditBudget extends AppCompatActivity {

    private static final String TAG = "EditBudget";
    private final Context context = this;
    private ActivityEditBudgetBinding budgetBinding;
    private int Budget_id, UID, Budget_Val, budget_val;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);

        budgetBinding = ActivityEditBudgetBinding.inflate(getLayoutInflater());
        View view = budgetBinding.getRoot();
        setContentView(view);

        sharedPrefManager = new SharedPrefManager(context);

        init();
    }

    private void init() {
        Budget_id = Integer.parseInt(getIntent().getStringExtra("budget_id"));
        budget_val = Integer.parseInt(getIntent().getStringExtra("budget_val"));

        budgetBinding.categoryVal.setText(String.valueOf(budget_val));

        UID = sharedPrefManager.getInt("id");
        Log.e(TAG, "init: " + Budget_id);
        budgetBinding.addbtn.setOnClickListener(v -> {
            Budget_Val = Integer.parseInt(budgetBinding.categoryVal.getText().toString());
            doEdit(Budget_id, Budget_Val);
        });
    }

    private void doEdit(int budget_id, int budget_Val) {
        Log.e(TAG, "doEdit: " + budget_id);
        Log.e(TAG, "doEdit: " + budget_Val);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.updateBudget(budget_id, budget_Val);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        startActivity(new Intent(context, Budget.class));
                        finish();
                    } else {
                        Config.showToast(context, serverResponse.getMessage());
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