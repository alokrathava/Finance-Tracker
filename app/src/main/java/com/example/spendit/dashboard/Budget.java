package com.example.spendit.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.spendit.R;
import com.example.spendit.adapter.BudgetAdapter;
import com.example.spendit.auth.Login;
import com.example.spendit.data.BudgetData;
import com.example.spendit.data.Category;
import com.example.spendit.databinding.ActivityBudgetBinding;
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

public class Budget extends AppCompatActivity implements BudgetAdapter.BudgetInterface {

    private static final String TAG = "Budget";
    private final Context context = this;
    private final List<Category> categories = new ArrayList<>();
    private ActivityBudgetBinding binding;
    private SharedPrefManager sharedPrefManager;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private androidx.appcompat.widget.Toolbar mtoolbar;
    private int UID, Category_id, Budget;
    private ArrayAdapter<String> arrayAdapter;
    private final List<BudgetData> budgetDataList = new ArrayList<>();
    private BudgetAdapter budgetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        binding = ActivityBudgetBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        clickListener();
        init();
    }

    private void clickListener() {
        binding.categorySpinner.setOnItemClickListener((parent, view, position, id) -> {
            Category_id = Integer.parseInt(categories.get(position).getCategoryId());
            Log.e(TAG, "onItemClick: " + Category_id);
        });
    }

    private void init() {
        /*Shared Prefrences Manager*/
        sharedPrefManager = new SharedPrefManager(context);
        sharedPrefManager.getInt("id");
        sharedPrefManager.getString("name");
        UID = sharedPrefManager.getInt("id");
        getCategory(UID);
        getBudgetValues(UID);
        /*Navigation Drawer Header*/
        NavigationView navigationView = findViewById(R.id.navmenu);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nameTitle);
        navUsername.setText(sharedPrefManager.getString("name"));

        /*===================Recycle Attendance Adapter========================*/
        budgetAdapter = new BudgetAdapter(budgetDataList, this);
        binding.recyclebudget.setHasFixedSize(true);
        binding.recyclebudget.setAdapter(budgetAdapter);


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

        binding.addbtn.setOnClickListener(v -> {
            Budget = Integer.parseInt(binding.budgetval.getText().toString());
            addBudget(Category_id, Budget, UID);
        });

    }

    private void getBudgetValues(int uid) {
        Log.e(TAG, "getBudgetValues: " + uid);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.getBudget(uid);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        budgetDataList.clear();
                        budgetDataList.addAll(serverResponse.getBudget());
                        budgetAdapter.notifyDataSetChanged();
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

    private void addBudget(int category_id, int budget, int UID) {

        Log.e(TAG, "addBudget: Category id: " + category_id);
        Log.e(TAG, "addBudget: Budget: " + budget);
        Log.e(TAG, "addBudget: User id: " + UID);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.addbudget(category_id, budget, UID);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    Log.e(TAG, "onResponse: " + response.body());
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        binding.budgetval.setText("");
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
        finish();
    }

    private void MoveToActivity() {
        startActivity(new Intent(context, Login.class));
    }

    @Override
    public void onDelete(BudgetData budgetData) {
        Config.showToast(context, "Update: " + budgetData.getSetAmountId());
        Config.showToast(context, "Update: " + budgetData.getAmountSet());
        Log.e(TAG, "onDelete: " + budgetData.getSetAmountId());


        Intent intent = new Intent(context, EditBudget.class);
        intent.putExtra("budget_id", budgetData.getSetAmountId());
        intent.putExtra("budget_val", budgetData.getAmountSet());
        startActivity(intent);
    }
}