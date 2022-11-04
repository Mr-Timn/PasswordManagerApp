package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static final String LOG_TAG = "APP_DEBUG";
    static final String PREF_ID = "pref";

    static final String TAG_USER = "user";
    static final String TAG_PASS = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedprefs = getApplicationContext().getSharedPreferences(PREF_ID, 0);
        Set<String> users = sharedprefs.getStringSet(TAG_USER, null);

        if (users == null || users.size() == 0) {
            Intent signup = new Intent(getApplicationContext(), Signup.class);
            startActivity(signup);
        } else {
            Intent login = new Intent(getApplicationContext(), Login.class);
            startActivity(login);
        }
    }
}