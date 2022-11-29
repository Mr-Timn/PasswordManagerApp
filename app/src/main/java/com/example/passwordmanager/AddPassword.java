package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPassword extends AppCompatActivity {
    static final String ID_PASS = "/pass/";
    static final String DATA_DELIM = "\\//\\\\//\\";

    EditText addusername = null, addpassword = null;
    Button add = null, cancel = null;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        StoredData data = new StoredData(getApplicationContext());

        username = getIntent().getStringExtra(MainActivity.RESULT_TAG_USER);
        password = getIntent().getStringExtra(MainActivity.RESULT_TAG_PASS);

        addusername = findViewById(R.id.addpass_user);
        addpassword = findViewById(R.id.addpass_pass);
        add = findViewById(R.id.addpass_add);
        cancel = findViewById(R.id.addpass_cancel);

        data.getAllData(username, password, ID_PASS);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.saveData(username, password, addusername.getText().toString() + DATA_DELIM + addpassword.getText().toString(), ID_PASS);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}