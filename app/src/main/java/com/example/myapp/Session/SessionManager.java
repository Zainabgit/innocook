package com.example.myapp.Session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapp.HelperClass;
import com.google.gson.Gson;

public class SessionManager {

    private static final String PREF_NAME = "userSession";
    private static final String KEY_USER = "user";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(HelperClass user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(KEY_USER, json);
        editor.commit();
    }

    public HelperClass getUser() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_USER, null);
        return gson.fromJson(json, HelperClass.class);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}
