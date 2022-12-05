package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddPassword extends AppCompatActivity {
    EditText addusername, addpassword;
    TextView passstr;
    Button add, cancel, tshow, randpass;
    StoredData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        data = new StoredData(getApplicationContext(), getIntent());

        addusername = findViewById(R.id.addpass_user);
        addpassword = findViewById(R.id.addpass_pass);
        tshow = findViewById(R.id.addpass_tshow);
        passstr = findViewById(R.id.addpass_str);
        randpass = findViewById(R.id.addpass_randpass);
        add = findViewById(R.id.addpass_add);
        cancel = findViewById(R.id.addpass_cancel);

        StoredData.trackPasswordStrength(addpassword, passstr);

        tshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tshow.getText().toString().equals("SHOW")) {
                    addpassword.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                    tshow.setText("HIDE");
                } else {
                    addpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    tshow.setText("SHOW");
                }
            }
        });
        randpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addpassword.setText(StoredData.randomPassword(16));
                StoredData.checkPasswordStrength(addpassword, passstr);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.saveData(addusername.getText().toString() + StoredData.DATA_DELIM + addpassword.getText().toString(), StoredData.ID_PASS);
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