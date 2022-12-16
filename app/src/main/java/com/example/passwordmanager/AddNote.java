package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNote extends AppCompatActivity {
    EditText addtitle, addtext;
    Button add, cancel;
    StoredData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        data = new StoredData(getApplicationContext(), getIntent());

        addtitle = findViewById(R.id.addnote_title);
        addtext = findViewById(R.id.addnote_text);
        add = findViewById(R.id.addnote_add);
        cancel = findViewById(R.id.addnote_cancel);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.saveData(addtitle.getText().toString() + StoredData.DATA_DELIM + addtext.getText().toString(), StoredData.ID_NOTE);
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