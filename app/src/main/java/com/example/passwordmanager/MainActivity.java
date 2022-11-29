package com.example.passwordmanager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements DefaultLifecycleObserver {
    public static final String LOG_TAG = "APP_DEBUG";
    public static final String RESULT_TAG_USER = "infouser";
    public static final String RESULT_TAG_PASS = "passuser";

    Intent signup, login, menu;

    protected static BetterActivityResult<Intent, ActivityResult> activityLauncher;

    public void lauchActivity(Intent intent) {
        activityLauncher.launch(intent, result -> {
            Intent data = result.getData();
            int rescode = result.getResultCode();

            if (intent == menu) {
                lauchActivity(login);
            }

            if (intent == login && rescode == Activity.RESULT_OK) {
                String username = data.getStringExtra(RESULT_TAG_USER);
                String password = data.getStringExtra(RESULT_TAG_PASS);

                menu = new Intent(getApplicationContext(), MainMenu.class);
                menu.putExtra(RESULT_TAG_USER, username);
                menu.putExtra(RESULT_TAG_PASS, password);
                lauchActivity(menu);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityLauncher = BetterActivityResult.registerActivityForResult(this);

        signup = new Intent(getApplicationContext(), Signup.class);
        login = new Intent(getApplicationContext(), Login.class);

        StoredData data = new StoredData(getApplicationContext());
        if (!data.hasUsers()) {
            lauchActivity(signup);
        } else {
            lauchActivity(login);
        }
    }
}