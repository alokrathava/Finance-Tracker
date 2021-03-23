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
import com.example.spendit.adapter.ExhistoryAdapter;
import com.example.spendit.auth.Login;
import com.example.spendit.data.Exphistory;
import com.example.spendit.databinding.ActivityMainBinding;
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

public class MainActivity extends AppCompatActivity implements ExhistoryAdapter.ExHistoryInterface {

    private static final String TAG = "MainActivity";
    private final Context context = this;
    private ActivityMainBinding binding;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private androidx.appcompat.widget.Toolbar mtoolbar;
    private SharedPrefManager sharedPrefManager;
    private int User_Id;
    private final List<Exphistory> exphistories = new ArrayList<>();
    private ExhistoryAdapter exhistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        init();
    }

    private void init() {
        /*Shared Prefrences Manager*/
        sharedPrefManager = new SharedPrefManager(context);
        User_Id = sharedPrefManager.getInt("id");
        sharedPrefManager.getString("name");

        getExpenseHistory(User_Id);

        /*===================Recycle Attendance Adapter========================*/
        exhistoryAdapter = new ExhistoryAdapter(exphistories, this);
        binding.recycleexpense.setHasFixedSize(true);
        binding.recycleexpense.setAdapter(exhistoryAdapter);

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
                    Config.showToast(context, "Home");
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
    }

    private void getExpenseHistory(int user_id) {
        Log.e(TAG, "getExpenseHistory: " + user_id);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.getExpenseHistory(user_id);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        exphistories.clear();
                        exphistories.addAll(serverResponse.getExphistory());
                        exhistoryAdapter.notifyDataSetChanged();
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

    private void Logout() {
        sharedPrefManager.clear();
        MoveToActivity();
    }

    private void MoveToActivity() {
        startActivity(new Intent(context, Login.class));
        finish();
    }

    @Override
    public void onClick(Exphistory exphistory) {
        Config.showToast(context, "Detail: " + exphistory.getCategoryId());
        Log.e(TAG, "onDelete: " + exphistory.getCategoryId());

        Intent intent = new Intent(context, DetailExpense.class);
        intent.putExtra("Category_id", exphistory.getCategoryId());
        startActivity(intent);
    }
}