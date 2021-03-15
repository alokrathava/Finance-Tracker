package com.example.spendit.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spendit.R;
import com.example.spendit.dashboard.MainActivity;
import com.example.spendit.data.Users;
import com.example.spendit.databinding.ActivityLoginBinding;
import com.example.spendit.network.Api;
import com.example.spendit.network.AppConfig;
import com.example.spendit.network.ServerResponse;
import com.example.spendit.utils.Config;
import com.example.spendit.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private final Context context = this;
    private ActivityLoginBinding binding;
    private String Email, Password;
    private Users users;
    private int Uid;
    private String Uname, Uemail, Uphone;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPrefManager = new SharedPrefManager(context);
        init();
    }

    private void init() {
        /*Button*/
        binding.loginbtn.setOnClickListener(v -> {
            doLogin();
        });

    }

    private void doLogin() {
        Email = binding.email.getText().toString();
        Password = binding.password.getText().toString();
        Log.e(TAG, "doLogin: " + Email);
        Log.e(TAG, "doLogin: " + Password);

        if (TextUtils.isEmpty(Email) && TextUtils.isEmpty(Password)) {
            binding.email.setError("All Fields Are Required");
            binding.password.setError("All Fields Are Required");
        } else if (Password.length() < 8) {
            binding.password.setError("Password must be greater than 8 digits");
        } else {
            executeLogin(Email, Password);
        }
    }

    private void executeLogin(String email, String password) {
        Log.e(TAG, "executeLogin: " + email);
        Log.e(TAG, "executeLogin: " + password);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.login(email, password);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        getUserData(email, password);
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

    private void getUserData(String email, String password) {
        Log.e(TAG, "getUserData: " + email);
        Log.e(TAG, "getUserData: " + password);

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.userdata(email, password);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        users = serverResponse.getUsers();
                        /*Data Fetch*/
                        Uname = users.getUName();
                        Uemail = users.getUEmail();
                        Uid = Integer.parseInt(users.getUId());
                        Uphone = users.getUMob();
                        makeSession(Uid, Uname, Uphone, Uemail);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Config.showToast(context, t.getMessage());
            }
        });

    }

    private void makeSession(int uid, String uname, String uphone, String uemail) {
        Log.e(TAG, "makeSession: " + uid);
        Log.e(TAG, "makeSession: " + uname);
        Log.e(TAG, "makeSession: " + uemail);
        Log.e(TAG, "makeSession: " + uphone);

        sharedPrefManager.setInt("id", uid);
        sharedPrefManager.setString("name", uname);
        sharedPrefManager.setString("phone", uphone);
        sharedPrefManager.setString("email", uemail);

        checkSession();
    }

    private void checkSession() {
        sharedPrefManager = new SharedPrefManager(context);

        int User_Id = sharedPrefManager.getInt("id");

        if (User_Id != -1) {
            Uid = sharedPrefManager.getInt("id");
            Uname = sharedPrefManager.getString("name");
            Uemail = sharedPrefManager.getString("email");
            Uphone = sharedPrefManager.getString("phone");

            MoveActivity();
        }

    }

    private void MoveActivity() {
        startActivity(new Intent(context, MainActivity.class));
    }


    public void register(View view) {
        startActivity(new Intent(context, Register.class));
    }


}