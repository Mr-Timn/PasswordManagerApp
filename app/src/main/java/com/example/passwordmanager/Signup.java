package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class Signup extends AppCompatActivity {
    EditText username = null, password = null;
    Button signup = null;
    TextView gotologin = null;

    static {
        System.loadLibrary("main");
    }

    private native String createSecureHash(String Password);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Log.d(MainActivity.LOG_TAG, "Created hash: " + createSecureHash("pass123"));

        SharedPreferences sharedprefs = getApplicationContext().getSharedPreferences(MainActivity.PREF_ID, 0);
        Set<String> user_pref = sharedprefs.getStringSet(MainActivity.TAG_USER, null);
        Set<String> pass_pref = sharedprefs.getStringSet(MainActivity.TAG_PASS, null);

        username = findViewById(R.id.loginusername);
        password = findViewById(R.id.loginpassword);
        signup = findViewById(R.id.signup);
        gotologin = findViewById(R.id.goto_login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString() == "") {
                    Toast.makeText(getApplicationContext(), "No username", 3).show();
                    return;
                }
                if (password.getText().toString() == "") {
                    Toast.makeText(getApplicationContext(), "No password", 3).show();
                    return;
                }

                user_pref.add(username.getText().toString());
                pass_pref.add(createSecureHash(password.getText().toString()));
            }
        });
    }
}