package com.example.spendit.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.spendit.R;
import com.example.spendit.adapter.ExDetailAdapter;
import com.example.spendit.auth.Login;
import com.example.spendit.data.Exdetail;
import com.example.spendit.databinding.ActivityDetailExpenseBinding;
import com.example.spendit.network.Api;
import com.example.spendit.network.AppConfig;
import com.example.spendit.network.ServerResponse;
import com.example.spendit.utils.Config;
import com.example.spendit.utils.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailExpense extends AppCompatActivity implements ExDetailAdapter.ExDetailInterface {

    private static final String TAG = "DetailExpense";
    private final Context context = this;
    private ActivityDetailExpenseBinding binding;
    private String Cat_Val;
    private int UID;
    private SharedPrefManager sharedPrefManager;
    private List<Exdetail> exdetailList = new ArrayList<>();
    private ExDetailAdapter exDetailAdapter;

    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private androidx.appcompat.widget.Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_expense);

        binding = ActivityDetailExpenseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPrefManager = new SharedPrefManager(context);

        init();
    }

    private void init() {
        Cat_Val = getIntent().getStringExtra("Category_id");
        Log.e(TAG, "init: Category ID" + Cat_Val);
        UID = sharedPrefManager.getInt("id");
        Log.e(TAG, "init: User ID" + UID);

        /*===================Recycle Attendance Adapter========================*/
        exDetailAdapter = new ExDetailAdapter(exdetailList, this);
        binding.recycleexpense.setHasFixedSize(true);
        binding.recycleexpense.setAdapter(exDetailAdapter);

        sharedPrefManager.getString("name");

        /*Navigation Drawer Header*/
        NavigationView navigationView = findViewById(R.id.navmenu);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nameTitle);
        navUsername.setText(sharedPrefManager.getString("name"));

        /*Navigational Drawer*/
        mtoolbar = binding.toolbar;
        setSupportActionBar(mtoolbar);
        navigationView = binding.navmenu;
        drawerLayout = binding.drawer;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mtoolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Home:
                    startActivity(new Intent(context, MainActivity.class));
                    break;
                case R.id.category:
                    Config.showToast(context, "Category");
                    startActivity(new Intent(context, Category.class));
                    break;
                case R.id.budget:
                    Config.showToast(context, "Budget");
                    startActivity(new Intent(context, Budget.class));
                    break;
                case R.id.expense:
                    Config.showToast(context, "Expense");
                    startActivity(new Intent(context, Expense.class));
                    break;
                case R.id.logout:
                    Config.showToast(context, "Logout");
                    Logout();
                    break;
            }
            return true;
        });

        getExpenseDetail(Cat_Val, UID);
    }

    private void Logout() {
        sharedPrefManager.clear();
        MoveToActivity();
    }

    private void MoveToActivity() {
        startActivity(new Intent(context, Login.class));
        finish();
    }

    private void getExpenseDetail(String cat_val, int uid) {
        Log.e(TAG, "getExpenseDetail: " + cat_val);
        Log.e(TAG, "getExpenseDetail: " + uid);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.getExpenseDetail(cat_val, uid);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        exdetailList.clear();
                        exdetailList.addAll(serverResponse.getExdetails());
                        exDetailAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(Exdetail exdetail) {
        Log.e(TAG, "onClick: " + exdetail.getAmountId());
        Log.e(TAG, "onClick: " + exdetail.getAmount());

        Intent intent = new Intent(context, EditExpense.class);
        intent.putExtra("category_id", exdetail.getAmountId());
        intent.putExtra("amount", exdetail.getAmount());
        startActivity(intent);
    }
}