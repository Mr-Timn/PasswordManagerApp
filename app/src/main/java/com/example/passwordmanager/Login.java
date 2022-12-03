package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class Login extends AppCompatActivity {
    EditText username = null, password = null;
    Button login = null;
    TextView gotosignup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.loginusername);
        password = findViewById(R.id.loginpassword);
        login = findViewById(R.id.login);
        gotosignup = findViewById(R.id.goto_signup);

        StoredData data = new StoredData(getApplicationContext(), null);
        if (!data.hasUsers()) {
            Toast.makeText(getApplicationContext(), "No accounts", (int)3).show();
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
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

                if (data.verifyUser(newuser, newpass)) {
                    Intent data = new Intent();
                    data.putExtra(MainActivity.RESULT_TAG_USER, newuser);
                    data.putExtra(MainActivity.RESULT_TAG_PASS, newpass);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid credentials", (int)3).show();
                }
            }
        });
    }
}