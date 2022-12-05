package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditPassword extends AppCompatActivity {
    EditText editusername, editpassword;
    TextView passstr;
    Button edit, cancel, tshow, randpass, remove;
    StoredData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        data = new StoredData(getApplicationContext(), getIntent());
        final int index = getIntent().getIntExtra(MainActivity.RESULT_TAG_INDPASS, -1);
        if (index == -1) {
            Log.e(MainActivity.LOG_TAG, "INVALID INDEX");
            finish();
        }

        String pdata = data.getData(StoredData.ID_PASS, index);
        String puser = pdata.substring(0, pdata.indexOf(StoredData.DATA_DELIM));
        String ppass = pdata.substring(pdata.indexOf(StoredData.DATA_DELIM) + StoredData.DATA_DELIM.length());

        editusername = findViewById(R.id.editpass_user);
        editpassword = findViewById(R.id.editpass_pass);
        tshow = findViewById(R.id.editpass_tshow);
        passstr = findViewById(R.id.editpass_str);
        randpass = findViewById(R.id.editpass_randpass);
        remove = findViewById(R.id.editpass_remove);
        edit = findViewById(R.id.editpass_add);
        cancel = findViewById(R.id.editpass_cancel);

        StoredData.trackPasswordStrength(editpassword, passstr);

        editusername.setText(puser);
        editpassword.setText(ppass);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.removeData(StoredData.ID_PASS, index);
            }
        });
        tshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tshow.getText().toString().equals("SHOW")) {
                    editpassword.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                    tshow.setText("HIDE");
                } else {
                    editpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    tshow.setText("SHOW");
                }
            }
        });
        randpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editpassword.setText(StoredData.randomPassword(16));
                StoredData.checkPasswordStrength(editpassword, passstr);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.editData(editusername.getText().toString() + StoredData.DATA_DELIM + editpassword.getText().toString(), StoredData.ID_PASS, index);
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