package com.example.ractic.ChatNormal.Reg;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ractic.ChatNormal.User;
import com.google.gson.Gson;


public class Auth {
    private  Context context;
    private static String username = null;
    private static String key = null;
    private static User user = null;

    public Auth(Context context) {
        this.context = context;
    }

    public  String getUsername() {
        if (username == null) {
            SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
            username = sp.getString("username", null);
        }
        return username;
    }

    public  void setUsername(String username) {
        Auth.username = username;
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("username", username);
        edit.apply();
    }

    public  String getKey() {
        if (key == null) {
            SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
            key = sp.getString("key", null);
        }
        return key;
    }

    public  void setKey(String key) {
        Auth.key = key;
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("key", key);
        edit.apply();
    }

    public  User getUser() {
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("user", "");
        return gson.fromJson(json, User.class);
    }

    public  void setUser(User user) {
        Auth.user = user;
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        edit.putString("user", json);
        edit.apply();
    }
}
