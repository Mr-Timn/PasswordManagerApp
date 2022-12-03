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
    Button addpass, addnote, viewpass, viewnote, logout;
    StoredData data;

    public void lauchActivity(Class<?> c) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra(MainActivity.RESULT_TAG_USER, data.username);
        intent.putExtra(MainActivity.RESULT_TAG_PASS, data.password);
        MainActivity.activityLauncher.launch(intent, result -> {
            Intent data = result.getData();
            int rescode = result.getResultCode();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        data = new StoredData(getApplicationContext(), getIntent());

        title = findViewById(R.id.menu_title);
        addpass = findViewById(R.id.addpassword);
        addnote = findViewById(R.id.addnote);
        viewpass = findViewById(R.id.viewpasswords);
        viewnote = findViewById(R.id.viewnotes);
        logout = findViewById(R.id.logout);

        title.setText(data.username);
        addpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lauchActivity(AddPassword.class);
            }
        });
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lauchActivity(AddNote.class);
            }
        });
        viewpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lauchActivity(ViewPasswords.class);
            }
        });
        viewnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lauchActivity(ViewNotes.class);
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