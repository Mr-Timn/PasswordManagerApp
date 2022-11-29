package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    TextView title = null;
    Button addpass = null, addnote = null, logout = null;
    Intent addpassintent = null;

    public void lauchActivity(Intent intent) {
        MainActivity.activityLauncher.launch(intent, result -> {
            Intent data = result.getData();
            int rescode = result.getResultCode();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //StoredData data = new StoredData(getApplicationContext());

        String username = getIntent().getStringExtra(MainActivity.RESULT_TAG_USER);
        String password = getIntent().getStringExtra(MainActivity.RESULT_TAG_PASS);

        Log.d(MainActivity.LOG_TAG, username);
        Log.d(MainActivity.LOG_TAG, password);

        title = findViewById(R.id.menu_title);
        title.setText(username);

        addpass = findViewById(R.id.addpassword);
        addnote = findViewById(R.id.addnote);
        logout = findViewById(R.id.logout);

        addpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addpassintent == null) {
                    addpassintent = new Intent(getApplicationContext(), AddPassword.class);
                    addpassintent.putExtra(MainActivity.RESULT_TAG_USER, username);
                    addpassintent.putExtra(MainActivity.RESULT_TAG_PASS, password);
                }
                lauchActivity(addpassintent);
            }
        });
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNote.class);
                lauchActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}