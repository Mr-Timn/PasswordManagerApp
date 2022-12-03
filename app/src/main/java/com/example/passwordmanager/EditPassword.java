package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditPassword extends AppCompatActivity {
    EditText editusername, editpassword;
    Button edit, cancel;

    StoredData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        data = new StoredData(getApplicationContext(), getIntent());
        final int index = getIntent().getIntExtra(MainActivity.RESULT_TAG_INDPASS, -1);
        if (index == -1) {
            finish();
        }

        Log.e(MainActivity.LOG_TAG, "index = " + index);
        String pdata = data.getData(StoredData.ID_PASS, index); Log.e(MainActivity.LOG_TAG, "pdata = " + pdata);
        String puser = pdata.substring(0, pdata.indexOf(StoredData.DATA_DELIM));
        String ppass = pdata.substring(pdata.indexOf(StoredData.DATA_DELIM) + StoredData.DATA_DELIM.length());

        editusername = findViewById(R.id.editpass_user);
        editpassword = findViewById(R.id.editpass_pass);
        edit = findViewById(R.id.editpass_add);
        cancel = findViewById(R.id.editpass_cancel);

        editusername.setText(puser);
        editpassword.setText(ppass);

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