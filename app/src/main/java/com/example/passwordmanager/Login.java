package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.Set;

public class Login extends AppCompatActivity {
    EditText username = null, password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedprefs = getApplicationContext().getSharedPreferences(MainActivity.PREF_ID, 0);
        Set<String> users = sharedprefs.getStringSet(MainActivity.TAG_USER, null);
        Set<String> pass = sharedprefs.getStringSet(MainActivity.TAG_PASS, null);

        username = findViewById(R.id.loginusername);
        password = findViewById(R.id.loginpassword);


    }
}