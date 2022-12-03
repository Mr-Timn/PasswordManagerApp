package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewPasswords extends AppCompatActivity {
    StoredData data;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_passwords);

        ll = findViewById(R.id.passwordlayout);
        data = new StoredData(getApplicationContext(), getIntent());

        ArrayList<String> passwords = data.getAllPasswords();
        for (int i = 0; i < passwords.size(); i++) {
            Log.e("APP_DEBUG", "-- " + passwords.get(i));
            String pass = passwords.get(i);
            String tu = pass.substring(0, pass.indexOf(StoredData.DATA_DELIM));
            String tp = pass.substring(pass.indexOf(StoredData.DATA_DELIM) + StoredData.DATA_DELIM.length());

            Log.e("APP_DEBUG", "[LOADED] " + tu + " " + tp);

            LinearLayout nll = new LinearLayout(getApplicationContext());
            nll.setOrientation(LinearLayout.HORIZONTAL);

            TextView tv = new TextView(getApplicationContext());
            tv.setText(tu);
            tv.setTextSize(30);
            nll.addView(tv);
            Button b = new Button(getApplicationContext());
            b.setText("Edit");
            int finalI = i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editpass(finalI);
                }
            });
            nll.addView(b);
            Button rem = new Button(getApplicationContext());
            rem.setText("Remove");
            nll.addView(rem);
            ll.addView(nll);

            TextView sp = new TextView(getApplicationContext());
            sp.setText(" ");
            sp.setTextSize(15);
            ll.addView(sp);
        }
        Log.e(MainActivity.LOG_TAG, "-------------------");
    }

    private void editpass(int i) {
        Intent vp = new Intent(getApplicationContext(), EditPassword.class);
        vp.putExtra(MainActivity.RESULT_TAG_USER, data.username);
        vp.putExtra(MainActivity.RESULT_TAG_PASS, data.password);
        vp.putExtra(MainActivity.RESULT_TAG_INDPASS, i);
        startActivity(vp);
    }
}