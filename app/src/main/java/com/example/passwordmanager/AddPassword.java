package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPassword extends AppCompatActivity {
    EditText addusername = null, addpassword = null;
    Button add = null, cancel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        StoredData data = new StoredData(getApplicationContext());

        String username = getIntent().getStringExtra(MainActivity.RESULT_TAG_USER);
        String password = getIntent().getStringExtra(MainActivity.RESULT_TAG_PASS);

        addusername = findViewById(R.id.addpass_user);
        addpassword = findViewById(R.id.addpass_pass);
        add = findViewById(R.id.addpass_add);
        cancel = findViewById(R.id.addpass_cancel);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.saveData(username, password, addpassword.getText().toString(), addusername.getText().toString());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}