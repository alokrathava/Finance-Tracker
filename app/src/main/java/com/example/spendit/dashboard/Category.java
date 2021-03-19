package com.example.spendit.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.spendit.R;
import com.example.spendit.adapter.CategoryAdapter;
import com.example.spendit.auth.Login;
import com.example.spendit.databinding.ActivityCategoryBinding;
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

public class Category extends AppCompatActivity implements CategoryAdapter.CategoryInterface {

    private static final String TAG = "Category";
    private ActivityCategoryBinding binding;
    private final Context context = this;
    private SharedPrefManager sharedPrefManager;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private androidx.appcompat.widget.Toolbar mtoolbar;
    private String Category;
    private int UID;
    private List<com.example.spendit.data.Category> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        init();
    }


    private void init() {
        /*Shared Prefrences Manager*/
        sharedPrefManager = new SharedPrefManager(context);
        sharedPrefManager.getInt("id");
        sharedPrefManager.getString("name");
        UID = sharedPrefManager.getInt("id");

        getCategory(UID);

        /*Navigation Drawer Header*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.navmenu);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nameTitle);
        navUsername.setText(sharedPrefManager.getString("name"));

        /*Add Category*/
        binding.addbtn.setOnClickListener(v -> {
            doCatrgory();
        });

        /*===================Recycle Attendance Adapter========================*/
        categoryAdapter = new CategoryAdapter(categories, this);
        binding.recyclecategory.setHasFixedSize(true);
        binding.recyclecategory.setAdapter(categoryAdapter);

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

    private void doCatrgory() {
        Category = binding.categoryVal.getText().toString();
        if (TextUtils.isEmpty(Category)) {
            binding.categoryVal.setError("Field is Empty");
        } else {
            executeCategory(Category);
        }
    }

    private void executeCategory(String category) {
        Log.e(TAG, "executeCategory: " + category);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.category(category, UID);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        binding.categoryVal.setText("");
                        getCategory(UID);
                    }
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
                        categoryAdapter.notifyDataSetChanged();
                    }
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
    }


    @Override
    public void onDelete(com.example.spendit.data.Category category) {
        Config.showToast(context, "Delete: " + category.getCategoryId());
        Log.e(TAG, "onDelete: " + category.getCategoryId());

        Intent intent = new Intent(context, EditCategory.class);
        intent.putExtra("category_id", category.getCategoryId());
        startActivity(intent);
    }
}