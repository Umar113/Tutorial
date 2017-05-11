package com.example.android.shopinggururegister.Preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.android.shopinggururegister.Activities.LoginActivity;
import com.example.android.shopinggururegister.Activities.MainActivityNew;
import com.example.android.shopinggururegister.Database.ProductsListDb;

import java.util.HashMap;

/**
 * Created by Android on 26-11-2016.
 */
public class SessionManager {
    SharedPreferences loginSharedPreferences;
    SharedPreferences.Editor loginEditor;
    Context context;
    int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "ShopingLoginPref";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "email";
    private ProductsListDb productsListDb;
//    public static final String KEY_USERNAME = "username";

    public static final String KEY_ID = "id";

    public SessionManager(Context context) {
        this.context = context;
        loginSharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        loginEditor = loginSharedPreferences.edit();
    }

    public void createLoginSession(int id) {
        loginEditor.putBoolean(IS_LOGIN, true);
//        loginEditor.putString(KEY_EMAIL, email);
        loginEditor.putInt(KEY_ID, id);
        productsListDb = new ProductsListDb(context);
        loginEditor.commit();
        Toast.makeText(context, "successful login", Toast.LENGTH_SHORT).show();
    }

    public void checkLogin() {
        if (this.isLoggedIn()) {
            Intent intent = new Intent(context, MainActivityNew.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_ID, String.valueOf(Integer.parseInt(String.valueOf(loginSharedPreferences.getInt(KEY_ID, 0)))));
        user.put(KEY_EMAIL, loginSharedPreferences.getString(KEY_EMAIL, null));
        return user;
    }

    public void logoutUser() {
        productsListDb = new ProductsListDb(context);

        loginEditor.clear();
        loginEditor.commit();
        productsListDb.deleteAllProductsFromDatabase();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return loginSharedPreferences.getBoolean(IS_LOGIN, false);
    }
}
