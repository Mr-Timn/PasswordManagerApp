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
    public static final String RESULT_TAG_INDPASS = "passindex";

    Intent signup, login, menu;

    protected static BetterActivityResult<Intent, ActivityResult> activityLauncher;

    public void lauchActivity(Intent intent) {
        activityLauncher.launch(intent, result -> {
            Intent data = result.getData();
            int rescode = result.getResultCode();

            if (intent == menu) lauchActivity(login);

            if ((intent == login || intent == signup) && rescode == Activity.RESULT_OK) {
                menu = new Intent(getApplicationContext(), MainMenu.class);
                menu.putExtra(RESULT_TAG_USER, data.getStringExtra(RESULT_TAG_USER));
                menu.putExtra(RESULT_TAG_PASS, data.getStringExtra(RESULT_TAG_PASS));
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

        StoredData data = new StoredData(getApplicationContext(), null);
        if (!data.hasUsers()) {
            lauchActivity(signup);
        } else {
            lauchActivity(login);
        }
    }
}