package com.example.spendit.network;

import com.example.spendit.data.Category;
import com.example.spendit.data.Expenses;
import com.example.spendit.data.Exphistory;
import com.example.spendit.data.Users;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("users")
    @Expose
    private Users users;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("expenses")
    @Expose
    private Expenses expenses;
    @SerializedName("exphistory")
    @Expose
    private List<Exphistory> exphistory = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Expenses getExpenses() {
        return expenses;
    }

    public void setExpenses(Expenses expenses) {
        this.expenses = expenses;
    }

    public List<Exphistory> getExphistory() {
        return exphistory;
    }

    public void setExphistory(List<Exphistory> exphistory) {
        this.exphistory = exphistory;
    }
}
