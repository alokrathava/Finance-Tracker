package com.example.spendit.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.spendit.R;
import com.example.spendit.auth.Login;
import com.example.spendit.data.Category;
import com.example.spendit.data.Expenses;
import com.example.spendit.databinding.ActivityExpenseBinding;
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

public class Expense extends AppCompatActivity {

    private static final String TAG = "Expense";
    private final Context context = this;
    private ActivityExpenseBinding binding;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private androidx.appcompat.widget.Toolbar mtoolbar;
    private final List<Category> categories = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private SharedPrefManager sharedPrefManager;
    private int UID, Category_id, Set_amount_id, amount;
    private String U_Name;
    private Expenses expenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        binding = ActivityExpenseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        clickListener();
        init();
    }

    private void clickListener() {
        binding.categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category_id = Integer.parseInt(categories.get(position).getCategoryId());
                Log.e(TAG, "onItemClick: " + Category_id);
                Expense.this.getCategoryVal(Category_id, UID);
            }
        });
    }

    private void getCategoryVal(int category_id, int uid) {
        Log.e(TAG, "getCategoryVal: " + category_id);
        Log.e(TAG, "getCategoryVal: " + uid);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.getCategoryVal(category_id, uid);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        expenses = serverResponse.getExpenses();
                        Log.e(TAG, "onResponse: " + expenses.getAmountSet());
                        binding.budgetval.setText("Budget: " + expenses.getAmountSet());
                        Set_amount_id = Integer.parseInt(expenses.getSetAmountId());
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


    private void init() {
        /*Shared Prefrence*/
        sharedPrefManager = new SharedPrefManager(context);
        Log.e(TAG, "init: " + sharedPrefManager.getInt("id"));
        Log.e(TAG, "init: " + sharedPrefManager.getString("name"));
        UID = sharedPrefManager.getInt("id");
        U_Name = sharedPrefManager.getString("name");
        getCategory(UID);

        /*Navigation Drawer Header*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.navmenu);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nameTitle);
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
                    startActivity(new Intent(context, MainActivity.class));
                    break;
                case R.id.category:
                    Config.showToast(context, "Category");
                    startActivity(new Intent(context, com.example.spendit.dashboard.Category.class));
                    break;
                case R.id.budget:
                    Config.showToast(context, "Budget");
                    startActivity(new Intent(context, Budget.class));
                    break;
                case R.id.expense:
                    Config.showToast(context, "Expense");
                    break;
                case R.id.logout:
                    Config.showToast(context, "Logout");
                    Logout();
                    break;
            }
            return true;
        });

        binding.addbtn.setOnClickListener(v -> {
            doExpense();
        });

    }

    private void doExpense() {
        Log.e(TAG, "doExpense: " + UID);
        Log.e(TAG, "doExpense: " + Category_id);
        Log.e(TAG, "doExpense: " + Set_amount_id);
        if (TextUtils.isEmpty(binding.expenseval.getText().toString())) {
            binding.expenseval.setError("Add Amount");
        } else {
            amount = Integer.parseInt(binding.expenseval.getText().toString());
            executeExpense(UID, Category_id, amount, Set_amount_id);
        }
    }

    private void executeExpense(int UID, int category_id, int amount, int set_amount_id) {
        Log.e(TAG, "executeExpense:User Id " + UID);
        Log.e(TAG, "executeExpense:Category Id " + category_id);
        Log.e(TAG, "executeExpense:Amount " + amount);
        Log.e(TAG, "executeExpense:Budget Id " + set_amount_id);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.setExpense(UID, category_id, amount, set_amount_id);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
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

    private void getCategory(int uid) {
        Log.e(TAG, "getCategory: " + uid);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.getCategory(uid);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        categories.clear();
                        categories.addAll(serverResponse.getCategories());
                        handleCategoriesList();
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

    private void handleCategoriesList() {
        if (!categories.isEmpty()) {
            List<String> strings = new ArrayList<>();

            for (Category category : categories) {
                strings.add(category.getCategoriesName());
            }

            arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, strings);
            binding.categorySpinner.setAdapter(arrayAdapter);

            Log.e(TAG, "handleCategoriesList: " + categories.get(0).getCategoryId());
            Category_id = Integer.parseInt(categories.get(0).getCategoryId());
        }
    }

    private void Logout() {
        sharedPrefManager.clear();
        MoveToActivity();
    }

    private void MoveToActivity() {
        startActivity(new Intent(context, Login.class));
    }
}