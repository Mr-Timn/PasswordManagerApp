package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;
import java.util.SortedMap;

public class Signup extends AppCompatActivity {
    EditText username, password;
    Button signup;
    TextView gotologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.signupusername);
        password = findViewById(R.id.signuppassword);
        signup = findViewById(R.id.signup);
        gotologin = findViewById(R.id.goto_login);

        StoredData data = new StoredData(getApplicationContext(), null);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newuser = username.getText().toString();
                String newpass = password.getText().toString();

                if (newuser.length() == 0) {
                    Toast.makeText(getApplicationContext(), "No username", (int)3).show();
                    return;
                }
                if (newpass.length() == 0) {
                    Toast.makeText(getApplicationContext(), "No password", (int)3).show();
                    return;
                }

                if (data.addUser(newuser, newpass)) {
                    Intent data = new Intent();
                    data.putExtra(MainActivity.RESULT_TAG_USER, newuser);
                    data.putExtra(MainActivity.RESULT_TAG_PASS, newpass);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }
}