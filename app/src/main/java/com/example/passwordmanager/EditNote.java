package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNote extends AppCompatActivity {
    EditText edittitle, edittext;
    Button edit, cancel, remove;
    StoredData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        data = new StoredData(getApplicationContext(), getIntent());
        final int index = getIntent().getIntExtra(MainActivity.RESULT_TAG_INDPASS, -1);
        if (index == -1) {
            Log.e(MainActivity.LOG_TAG, "INVALID INDEX");
            finish();
        }

        String pdata = data.getData(StoredData.ID_NOTE, index);
        String ptitle = pdata.substring(0, pdata.indexOf(StoredData.DATA_DELIM));
        String pnote  = pdata.substring(pdata.indexOf(StoredData.DATA_DELIM) + StoredData.DATA_DELIM.length());

        edittitle = findViewById(R.id.editnote_title);
        edittext = findViewById(R.id.editnote_text);
        edit = findViewById(R.id.editnote_add);
        cancel = findViewById(R.id.editnote_cancel);
        remove = findViewById(R.id.editnote_remove);

        edittitle.setText(ptitle);
        edittext.setText(pnote);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.removeData(StoredData.ID_NOTE, index);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.editData(edittitle.getText().toString() + StoredData.DATA_DELIM + edittext.getText().toString(), StoredData.ID_NOTE, index);
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