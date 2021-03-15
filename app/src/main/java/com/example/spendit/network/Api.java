package com.example.spendit.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    String my_url = "Api.php?apicall=";

    @FormUrlEncoded
    @POST(my_url + "login")
    Call<ServerResponse> login(
            @Field("u_email") String email,
            @Field("u_password") String password
    );

    @FormUrlEncoded
    @POST(my_url + "register")
    Call<ServerResponse> register(
            @Field("u_name") String name,
            @Field("u_email") String email,
            @Field("u_password") String pwd,
            @Field("u_mob") String mobile
    );

    @FormUrlEncoded
    @POST(my_url + "login_data")
    Call<ServerResponse> userdata(
            @Field("u_email") String email,
            @Field("u_password") String pwd
    );

    @FormUrlEncoded
    @POST(my_url + "add_category")
    Call<ServerResponse> category(
            @Field("category_name") String category,
            @Field("user_id") int u_id
    );

    @FormUrlEncoded
    @POST(my_url + "get_category")
    Call<ServerResponse> getCategory(
            @Field("user_id") int u_id
    );

    @FormUrlEncoded
    @POST(my_url + "delete_category")
    Call<ServerResponse> deleteCategory(
            @Field("category_id") int cat_id
    );

    @FormUrlEncoded
    @POST(my_url + "setamount")
    Call<ServerResponse> addbudget(
            @Field("category_id") int cat_id,
            @Field("amount_set") int budget,
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST(my_url + "get_categoryval")
    Call<ServerResponse> getCategoryVal(
            @Field("category_id") int cat_id,
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST(my_url + "expenseamount")
    Call<ServerResponse> setExpense(
            @Field("u_id") int user_id,
            @Field("category_id") int cat_id,
            @Field("amount") int expensval,
            @Field("set_amount_id") int amount_id
    );

    @FormUrlEncoded
    @POST(my_url + "getExpenseHistory")
    Call<ServerResponse> getExpenseHistory(
            @Field("user_id") int user_id
    );

}
